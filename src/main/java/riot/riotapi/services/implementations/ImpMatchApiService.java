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
import reactor.util.function.Tuple4;
import riot.riotapi.dtos.SummonerDTO;
import riot.riotapi.dtos.discord.GuildEmojiDTO;
import riot.riotapi.dtos.match.*;
import riot.riotapi.dtos.summoner.SummonerTierDTO;
import riot.riotapi.entities.Champion;
import riot.riotapi.entities.Spell;
import riot.riotapi.entities.Summoner;
import riot.riotapi.exceptions.ServiceException;
import riot.riotapi.filters.MatchFilter;
import riot.riotapi.services.interfaces.IntGuildEmojiService;
import riot.riotapi.services.interfaces.IntMatchApiService;
import riot.riotapi.services.interfaces.IntSpellService;
import riot.riotapi.services.interfaces.IntSummonerApiService;
import riot.riotapi.utils.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ImpMatchApiService implements IntMatchApiService {
  private final WebClient webClient;

  @Value("${riot.apikey}")
  private String apiKey;
  private final ModelMapper mapper;
  private final ImpChampionService championService;
  private final IntSpellService spellService;
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  private final IntSummonerApiService summonerApiService;
  private final IntGuildEmojiService guildEmojiService;
  private final List<String> matchTypesList = Arrays.asList("ranked", "normal", "tourney", "tutorial");
  @Autowired
  public ImpMatchApiService(ImpChampionService championService, IntSpellService spellService, IntSummonerApiService summonerApiService,
                            IntGuildEmojiService guildEmojiService) {
    mapper = new ModelMapper();
    this.championService = championService;
    this.spellService = spellService;
    this.summonerApiService = summonerApiService;
    this.guildEmojiService = guildEmojiService;
    webClient = WebClient.create();
  }

  @Override
  public MatchesDTO getSummonerMatchesByPuuid(Summoner summoner, MatchFilter filter) {
    try {

      validatesFilter(filter);

      String[] matchesList = webClient.get()
          .uri(buildDynamicURL(summoner.getPuuid(), filter))
          .header(Constants.HEADER_NAME_RIOT, this.apiKey)
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
              .header(Constants.HEADER_NAME_RIOT, this.apiKey)
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
              .header(Constants.HEADER_NAME_RIOT, this.apiKey)
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
  public Mono<MatchDTO> getSummonerLiveMatch(String sumName, String guildId) {
    return summonerApiService.getSummonerByNameMono(sumName)
            .flatMap(summonerDTO -> {
                logger.info("Start live match api request with ".concat(sumName));
                return getLiveMatchBySummonerIdMono(summonerDTO.getSummonerId())
                        .flatMap(liveMatch -> mapLiveMatchToMatchDTOMono(liveMatch, guildId))
                        .onErrorResume(error -> {
                            logger.error("An error has ocurred getting liveMatch: " + error.getMessage());
                            return Mono.empty();
                        })
                        .switchIfEmpty(Mono.empty());
            })
            .onErrorResume(error -> {
                logger.error("An error has ocurred getting summonerDTO: " +error.getMessage());
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
              .header(Constants.HEADER_NAME_RIOT, this.apiKey)
              .retrieve()
              .bodyToMono(LiveMatchRootDTO.class)
              .onErrorResume(err -> {
                  logger.error("An error has occurred getting liveMatch: " + err.getMessage());
                  return Mono.empty();
              });

  }

    /**
     * Maps liveMatchDTO object into MatchDTO, and returns non-blocking data
     * @param liveMatchDTO liveMatch
     * @return Mono data.
     */
  private Mono<MatchDTO> mapLiveMatchToMatchDTOMono(LiveMatchRootDTO liveMatchDTO, String guildId) {
      Mono<List<ParticipantInfoDTO>> participantsInfoDTOMono = getListParticipantsInfo(liveMatchDTO.getParticipants(), liveMatchDTO.getMode(), guildId);
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
  private Mono<List<ParticipantInfoDTO>> getListParticipantsInfo(ArrayList<ParticipantDTO> participants, String matchMode, String guildId) {
    List<Champion> championList = championService.findByKeyIn(participants.stream()
                                                                            .map(ParticipantDTO::getChampionId)
                                                                            .toList());

    Map<Long, String> championsMap = championList.stream()
                                                .collect(Collectors.toMap(Champion::getKey, Champion::getName));

    Flux<String> championsNameFlux = Flux.fromIterable(championList.stream().map(Champion::getName).toList());

    Flux<GuildEmojiDTO> champEmoji = guildEmojiService.createChampionEmojiByName(guildId, championsNameFlux);

    Map<Integer, String> spells = spellService.findSpellsByIds(participants.stream()
                    .flatMap(p -> Stream.of(p.getSpell1Id(), p.getSpell2Id()))
                    .toList())
            .stream()
            .collect(Collectors.toMap(Spell::getSpellId, Spell::getEmoji));

    Flux<SummonerDTO> summoners = summonerApiService.findMatchSummonersByName(participants.stream()
            .map(ParticipantDTO::getSummonerName)
            .toList());
    Flux<List<SummonerTierDTO>> summonerTierDTOFlux = summoners.flatMap(sum -> summonerApiService.getSummonerTierFlux(sum.getSummonerId()));

    Flux<ParticipantDTO> participantDTOFlux = Flux.fromIterable(participants);

    Flux<ParticipantInfoDTO> resultFlux = Flux.zip(participantDTOFlux, summoners, champEmoji, summonerTierDTOFlux) // here the magic appears
            .map(res -> this.completeParticipantInfoDTO(res, championsMap, spells));

    return resultFlux.collectList();

  }

  private ParticipantInfoDTO completeParticipantInfoDTO(Tuple4<ParticipantDTO, SummonerDTO, GuildEmojiDTO, List<SummonerTierDTO>> res,
                                                        Map<Long, String> championsMap, Map<Integer, String> spells) {
      ParticipantDTO participantDTO = res.getT1();
      SummonerDTO summonerDTO = res.getT2();
      GuildEmojiDTO emoji = res.getT3();
      List<SummonerTierDTO> summonerTierDTO = res.getT4();

      ParticipantInfoDTO participantInfoDTO = mapper.map(participantDTO, ParticipantInfoDTO.class);

      if (participantInfoDTO.getChampionName() == null) {
          participantInfoDTO.setChampionName(championsMap.get(participantDTO.getChampionId()));
      }

      if (participantDTO.getSpell1Id() != null && participantDTO.getSpell2Id() != null) {
          participantInfoDTO.setSpell1Emoji(spells.get(participantDTO.getSpell1Id()));
          participantInfoDTO.setSpell2Emoji(spells.get(participantDTO.getSpell2Id()));
      }

      participantInfoDTO.setSummonerLevel(summonerDTO.getSummonerLevel());
      participantInfoDTO.setChampionEmoji(DiscordUtils.formatDiscordEmoji(emoji.getName(), emoji.getId()));

      logger.info("for summoner {} tier: {}", summonerDTO.getName(), summonerTierDTO);

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
            && !matchTypesList.contains(filter.getType())) {
      throw new ServiceException(String.format("El type %s no está permitido", filter.getType()));
    }
  }
}
