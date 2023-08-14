package riot.riotapi.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import riot.riotapi.dtos.match.*;
import riot.riotapi.entities.RiotApi;
import riot.riotapi.entities.Summoner;
import riot.riotapi.exceptions.ServiceException;
import riot.riotapi.filters.MatchFilter;
import riot.riotapi.repositories.interfaces.IntRiotApi;
import riot.riotapi.services.interfaces.IntMatchApiService;
import riot.riotapi.utils.CommonFunctions;
import riot.riotapi.utils.Constants;
import riot.riotapi.utils.ConstantsExceptions;
import riot.riotapi.utils.URIs;

import java.util.*;

@Service
public class ImpMatchApiService implements IntMatchApiService {
  private final IntRiotApi intRiotApi;
  private WebClient webClient;
  private String apiKey;
  @Autowired
  public ImpMatchApiService(IntRiotApi intRiotApi) {
    this.intRiotApi = intRiotApi;
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
