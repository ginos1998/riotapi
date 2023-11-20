package riot.riotapi.services.implementations;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;
import riot.riotapi.dtos.ChampionDTO;
import riot.riotapi.dtos.SummonerDTO;
import riot.riotapi.dtos.match.*;
import riot.riotapi.entities.Summoner;
import riot.riotapi.exceptions.ServiceException;
import riot.riotapi.filters.MatchFilter;
import riot.riotapi.services.interfaces.*;
import riot.riotapi.utils.*;

import java.util.*;

@Service
public class ImpMatchApiService implements IntMatchApiService {
  private final WebClient webClient;

  @Value("${riot.apikey}")
  private String apiKey;
  private final ModelMapper mapper;
  private final ImpChampionApiService championApiService;
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  private final IntSummonerApiService summonerApiService;

  @Autowired
  public ImpMatchApiService(ImpChampionApiService championApiService, IntSummonerApiService summonerApiService) {
    mapper = new ModelMapper();
    this.championApiService = championApiService;
    this.summonerApiService = summonerApiService;
    webClient = WebClient.create();
  }

  @Override
  public MatchesDTO getSummonerMatchesByPuuid(Summoner summoner, MatchFilter filter) {
    try {

      validatesFilter(filter);

      String[] matchesList = webClient.get()
          .uri(buildDynamicURL(summoner.getPuuid(), filter))
          .header(URIs.HEADER_RIOT_API_TOKEN, this.apiKey)
          .retrieve()
          .bodyToMono(String[].class).block();

      return new MatchesDTO(matchesList, summoner.getPuuid(), summoner.getName());

    } catch (WebClientResponseException ex) {
      throw new ServiceException(String.format(ConstantsExceptions.ERROR_GETTING_SUMMONER_MATCHES_BY_PUUID, summoner.getPuuid()), ex);
    }

  }

  @Override
  public MatchRootDTO getMatchById(String matchId) {
    MatchRootDTO matchRootDTO;
    try {
      if (!CommonFunctions.isNotNullOrEmpty(matchId)) {
        throw new ServiceException("El valor de matchId no puede ser null o vacío.");
      }

      matchRootDTO = webClient.get()
              .uri(URIs.URI_LOL_MATCHES_BY_MATCH_ID.concat(matchId))
              .header(URIs.HEADER_RIOT_API_TOKEN, this.apiKey)
              .retrieve()
              .bodyToMono(MatchRootDTO.class)
              .block();

      if (matchRootDTO == null) {
        throw new ServiceException("Ha ocurrido un error al obtener la partida con matchId: ".concat(matchId));
      }
    } catch (Exception ex) {
      throw new ServiceException("Ha ocurrido un error inesperado al buscar la partida solicitada.\n".concat(ex.getMessage()));
    }

    return matchRootDTO;
  }

  @Override
  public LiveMatchRootDTO getCurrentMatchInfo(String summonerId) {
    LiveMatchRootDTO liveMatchDTO = null;

    try{
      if (!CommonFunctions.isNotNullOrEmpty(summonerId)) {
        throw new ServiceException("El valor de summonerId no puede ser null o vacío.");
      }

      liveMatchDTO = webClient.get()
              .uri(URIs.URI_LOL_LIVE_MATCH.concat(summonerId))
              .header(URIs.HEADER_RIOT_API_TOKEN, this.apiKey)
              .retrieve()
              .bodyToMono(LiveMatchRootDTO.class)
              .block();

    } catch (WebClientResponseException re) {
      if (re.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404))) {
        throw new ServiceException("No se ha encontrado partida en juego.\n");
      }
    } catch (Exception ex) {
      throw new ServiceException("Ha ocurrido un error inesperado al buscar la partida en juego solicitada.\n".concat(ex.getMessage()));
    }
    return liveMatchDTO;
  }

    /**
     * Searches a live match given a summoner name.
     * Fot that, it's necessary the summoner id, so first do this calling riot's API.
     * Then, calls riot's API to get the live match info. But this endpoint returns poor data,
     * so values like champion, summoner level, etc. needs to be fill calling other endpoints.
     * In this case, all champions are stored in the database, so it's not necessary to call
     * the respective API.
     * @param sumName summoner name.
     * @return no blocking data.
     */
  @Override
  public Mono<MatchDTO> getSummonerLiveMatch(String sumName) {
    return summonerApiService.getSummonerByNameMono(sumName)
            .flatMap(summonerDTO -> {
                logger.info("Start live match api request with ".concat(sumName));
                return getLiveMatchBySummonerIdMono(summonerDTO.getSummonerId())
                        .flatMap(this::mapLiveMatchToMatchDTOMono)
                        .onErrorResume(error -> {
                            logger.error("An error has occurred getting summonerLiveMatch: " + error.getMessage());
                            return Mono.empty();
                        })
                        .switchIfEmpty(Mono.empty());
            })
            .onErrorResume(error -> {
                logger.error("An error has occurred getting summonerDTO: " +error.getMessage());
                return Mono.empty();
            })
            .switchIfEmpty(Mono.empty());
  }

    /**
     * Searches a live match given summoner id. It produces a reactive result (no blocking).
     * @param summonerId summonerId
     * @return Mono
     */
  public Mono<LiveMatchRootDTO> getLiveMatchBySummonerIdMono(String summonerId) {
      String liveMatchUrl = URIs.URI_LOL_LIVE_MATCH.concat(summonerId);
      return webClient.get()
              .uri(liveMatchUrl)
              .header(URIs.HEADER_RIOT_API_TOKEN, this.apiKey)
              .retrieve()
              .bodyToMono(LiveMatchRootDTO.class)
              .onErrorResume(err -> {
                  logger.error("An error has occurred getting liveMatchBySummonerIdMono: " + err.getMessage());
                  return Mono.just(new LiveMatchRootDTO());
              });

  }

    /**
     * Maps liveMatchDTO object into MatchDTO, and returns non-blocking data
     * @param liveMatchDTO liveMatch
     * @return Mono data.
     */
  private Mono<MatchDTO> mapLiveMatchToMatchDTOMono(LiveMatchRootDTO liveMatchDTO) {
      if (liveMatchDTO.getMatchId() == null) {
        return Mono.empty();
      }

      Mono<List<ParticipantInfoDTO>> participantsInfoDTOMono = getListParticipantsInfo(liveMatchDTO.getParticipants());
      MatchDTO matchDTO = new MatchDTO();
      matchDTO.setMode(liveMatchDTO.getMode());
      matchDTO.setStartTime(CommonFunctions.getDateAsString(liveMatchDTO.getStartTime()));
      matchDTO.setDuration(CommonFunctions.getDurationMMssAsString(liveMatchDTO.getDuration()));

      return participantsInfoDTOMono.map(participantsInfoDTOList -> {
          matchDTO.setParticipants(participantsInfoDTOList);
          logger.info("Live match (id="+liveMatchDTO.getMatchId()+") has been mapped");
          return matchDTO;
      });
  }

    /**
     * Given the fact that liveMatch endpoint provides just the ids of champions, spells, and poor info about summoners,
     * it's necessary to complete values like summoner level, spells names, etc. making queries
     * o calling riot's API.
     * The result is non-blocking
     * @param participants list of participants in the liveMatch
     * @return Mono
     */
  private Mono<List<ParticipantInfoDTO>> getListParticipantsInfo(ArrayList<ParticipantDTO> participants) {
    Flux<ChampionDTO> championDTOFlux = championApiService.getChampionByMatchParticipants(participants);

    Flux<SummonerDTO> summoners = summonerApiService.findMatchSummonersByName(participants.stream()
            .map(ParticipantDTO::getSummonerName)
            .toList());

    Flux<ParticipantDTO> participantDTOFlux = Flux.fromIterable(participants);

    logger.info("campeones: {}", participants.stream().map(ParticipantDTO::getChampionId).toList());

    Flux<ParticipantInfoDTO> resultFlux = Flux.zip(participantDTOFlux, summoners, championDTOFlux) // here the magic appears
            .map(this::completeParticipantInfoDTO);

    return resultFlux.collectList()
        .onErrorResume(err -> {
          logger.error("An error has occurred while getting ListParticipantsInfo. Error: {}", err.getMessage());
          return Mono.empty();
        });

  }

  private ParticipantInfoDTO completeParticipantInfoDTO(Tuple3<ParticipantDTO, SummonerDTO, ChampionDTO> res) {
      ParticipantDTO participantDTO = res.getT1();
      SummonerDTO summonerDTO = res.getT2();
      ChampionDTO championDTO = res.getT3();

      ParticipantInfoDTO participantInfoDTO = mapper.map(participantDTO, ParticipantInfoDTO.class);

      if (participantInfoDTO.getChampionName() == null) {
          participantInfoDTO.setChampionName(championDTO.getName());
      }

      if (participantDTO.getSpell1Id() != null && participantDTO.getSpell2Id() != null) {
          participantInfoDTO.setSpell1Emoji(championDTO.getSpell1Emoji());
          participantInfoDTO.setSpell2Emoji(championDTO.getSpell2Emoji());
      }

      participantInfoDTO.setSummonerLevel(summonerDTO.getSummonerLevel());
      participantInfoDTO.setChampionEmoji(championDTO.getDsEmoji());
      return participantInfoDTO;
  }

  public String buildDynamicURL(String puuid, MatchFilter filter) {
    Map<String, String> queryParams = new LinkedHashMap<>();

    String uri = URIs.URI_LOL_MATCHES_BY_PUUID.replace("#", puuid);

    if (filter.getStartTime() != null) {
      queryParams.put("startTime", filter.getStartTime().toString());
    }
    if (filter.getEndTime() != null) {
      queryParams.put("endTime", filter.getEndTime().toString());
    }
    if (CommonFunctions.isNotNullAndPositive(filter.getQueue())) {
      queryParams.put("queue", Integer.toString(filter.getQueue()));
    }
    if (CommonFunctions.isNotNullOrEmpty(filter.getType())) {
      queryParams.put("type", filter.getType());
    }
    if (filter.getStart() > 0) {
      queryParams.put("start", Integer.toString(filter.getStart()));
    }
    if (filter.getCount() > 0) {
      queryParams.put("count", Integer.toString(filter.getCount()));
    }

    return CommonFunctions.buildURIWithQueryParams(uri, queryParams);
  }

  private void validatesFilter(MatchFilter filter) {
    if(CommonFunctions.isNullOrNegative(filter.getStartTime())) {
      filter.setStartTime(CommonFunctions.substractDaysToCurrentDate(5));
    }

    if(CommonFunctions.isNullOrNegative(filter.getEndTime())) {
      filter.setEndTime(CommonFunctions.getCurrentTimeEpochSeconds());
    }

    if (filter.getEndTime() - filter.getStartTime() < 0) {
      throw new ServiceException("El valor de endTime debe ser mayor al valor de startTime.");
    }

    if (CommonFunctions.isNullOrNegative(filter.getStart())) {
      filter.setStart(0);
    }

    if (filter.getCount() == null) {
      filter.setCount(filter.getStart() < 10 ? 10 : filter.getStart() + 1);
    }

    if (filter.getCount() <= filter.getStart()) {
      throw new ServiceException("El valor de count debe ser mayor al valor de start.");
    }

    if (filter.getCount() > 100) {
      throw new ServiceException("El valor de count debe ser menor a 100.");
    }

    if (CommonFunctions.isNotNullOrEmpty(filter.getType())
            && !Constants.LIST_TYPE_OF_MATCHES.contains(filter.getType())) {
      throw new ServiceException(String.format("El type %s no está permitido", filter.getType()));
    }
  }
}
