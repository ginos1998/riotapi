package riot.riotapi.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import riot.riotapi.dtos.match.MatchesDTO;
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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

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
