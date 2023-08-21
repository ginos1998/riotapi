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
import reactor.core.publisher.Mono;
import riot.riotapi.dtos.SummonerDTO;
import riot.riotapi.dtos.match.*;
import riot.riotapi.entities.Champion;
import riot.riotapi.entities.RiotApi;
import riot.riotapi.entities.Spell;
import riot.riotapi.entities.Summoner;
import riot.riotapi.exceptions.ServiceException;
import riot.riotapi.filters.MatchFilter;
import riot.riotapi.repositories.interfaces.IntRiotApi;
import riot.riotapi.services.interfaces.IntMatchApiService;
import riot.riotapi.services.interfaces.IntSpellService;
import riot.riotapi.services.interfaces.IntSummonerApiService;
import riot.riotapi.utils.CommonFunctions;
import riot.riotapi.utils.Constants;
import riot.riotapi.utils.ConstantsExceptions;
import riot.riotapi.utils.URIs;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ImpMatchApiService implements IntMatchApiService {
  private final IntRiotApi intRiotApi;
  private WebClient webClient;

  @Value("${riot.apikey}")
  private String apiKey;
  private final ModelMapper mapper;
  private final ImpChampionService championService;
  private final IntSpellService spellService;
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  private final IntSummonerApiService summonerApiService;
  @Autowired
  public ImpMatchApiService(IntRiotApi intRiotApi, ImpChampionService championService, IntSpellService spellService, IntSummonerApiService summonerApiService) {
    this.intRiotApi = intRiotApi;
    mapper = new ModelMapper();
    this.championService = championService;
    this.spellService = spellService;
    this.summonerApiService = summonerApiService;
    webClient = WebClient.create();
  }

  @Override
  public MatchesDTO getSummonerMatchesByPuuid(Summoner summoner, MatchFilter filter) {
    try {
      if (!CommonFunctions.isNotNullOrEmpty(apiKey)) {
        initApiKey();
      }

      validatesFilter(filter);

      String[] matchesList = webClient.get()
          .uri(buildDynamicURL(summoner.getPuuid(), filter))
          .header("X-Riot-Token", this.apiKey)
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

      if (!CommonFunctions.isNotNullOrEmpty(apiKey)) {
        initApiKey();
      }

      matchRootDTO = webClient.get()
              .uri(URIs.URI_LOL_MATCHES_BY_MATCH_ID.concat(matchId))
              .header("X-Riot-Token", this.apiKey)
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

      if (!CommonFunctions.isNotNullOrEmpty(apiKey)) {
        initApiKey();
      }

      liveMatchDTO = webClient.get()
              .uri(URIs.URI_LOL_LIVE_MATCH.concat(summonerId))
              .header("X-Riot-Token", this.apiKey)
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

  @Override
  public Mono<MatchDTO> getSummonerLiveMatch(String sumName) {

    if (!CommonFunctions.isNotNullOrEmpty(apiKey) || webClient == null) {
      initApiKey();
      logger.info("apikey and webClient initialized.".concat(sumName));
    }

    String url = URIs.URI_SUMMONER_ACCOUNT_NAME.concat(sumName);
    logger.info("Start champion api request with ".concat(sumName));
    return webClient.get()
            .uri(url)
            .header("X-Riot-Token", this.apiKey)
            .retrieve()
            .bodyToMono(SummonerDTO.class)
            .flatMap(summonerDTO -> {
              String liveMatchUrl = URIs.URI_LOL_LIVE_MATCH.concat(summonerDTO.getSummonerId());
              logger.info("Start live match api request with ".concat(sumName));
              return webClient.get()
                      .uri(liveMatchUrl)
                      .header("X-Riot-Token", this.apiKey)
                      .retrieve()
                      .bodyToMono(LiveMatchRootDTO.class)
                      .map(liveMatchDTO -> {
                        MatchDTO matchDTO = new MatchDTO();
                        matchDTO.setMode(liveMatchDTO.getMode());
                        matchDTO.setStartTime(CommonFunctions.getDateAsString(liveMatchDTO.getStartTime()));
                        matchDTO.setDuration(CommonFunctions.getDurationMMssAsString(liveMatchDTO.getDuration()));
                        matchDTO.setParticipants(getListParticipantsInfo(liveMatchDTO.getParticipants()));
                        logger.info("Live match mapped and returning for ".concat(sumName));
                        return matchDTO;
                      })
                      .onErrorResume(WebClientResponseException.NotFound.class, e -> Mono.empty());
            })
            .switchIfEmpty(Mono.empty());
  }

  private List<ParticipantInfoDTO> getListParticipantsInfo(ArrayList<ParticipantDTO> participants) {

    Map<Long, String> champions = championService.findByKeyIn(participants.stream()
                    .map(ParticipantDTO::getChampionId)
                    .toList())
            .stream()
            .collect(Collectors.toMap(Champion::getKey, Champion::getName));

    Map<Integer, String> spells = spellService.findSpellsByIds(participants.stream()
                    .flatMap(p -> Stream.of(p.getSpell1Id(), p.getSpell2Id()))
                    .toList())
            .stream()
            .collect(Collectors.toMap(Spell::getSpellId, Spell::getEmoji));

//    Mono<List<SummonerDTO>> summoners = summonerApiService.findMatchSummonersByName(participants.stream()
//            .map(ParticipantDTO::getSummonerName)
//            .toList());
//
//    summoners.subscribe(s -> logger.info("summoners list size="+s.size()));


    return participants.stream()
            .map(participant -> {
              ParticipantInfoDTO p = mapper.map(participant, ParticipantInfoDTO.class);
              if (p.getChampionName() == null) {
                p.setChampionName(champions.get(participant.getChampionId()));
              }

              if (participant.getSpell1Id() != null && participant.getSpell2Id() != null) {
                p.setSpell1Emoji(spells.get(participant.getSpell1Id()));
                p.setSpell2Emoji(spells.get(participant.getSpell2Id()));
              }
              return p;
            })
            .toList();
  }

  private void initApiKey() {
    this.webClient = WebClient.create();
    Optional<RiotApi> riotApi = this.intRiotApi.findById(1L);
    apiKey = riotApi.map(RiotApi::getApiKey).orElse(null);
    if (apiKey == null) {
      throw new ServiceException(ConstantsExceptions.ERROR_RIOT_API_KEY_NOT_FOUNT);
    }
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
