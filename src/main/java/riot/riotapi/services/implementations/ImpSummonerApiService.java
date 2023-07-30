package riot.riotapi.services.implementations;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import riot.riotapi.dtos.SummonerDTO;
import riot.riotapi.entities.RiotApi;
import riot.riotapi.exceptions.ServiceException;
import riot.riotapi.repositories.interfaces.IntRiotApi;
import riot.riotapi.services.interfaces.IntSummonerApiService;
import riot.riotapi.utils.CommonFunctions;
import riot.riotapi.utils.ConstantsExceptions;
import riot.riotapi.utils.URIs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ImpSummonerApiService implements IntSummonerApiService {
  private final IntRiotApi intRiotApi;
  private WebClient webClient;
  private String apiKey;

  public ImpSummonerApiService(IntRiotApi intRiotApi) {
    this.intRiotApi = intRiotApi;
  }

  private void initApiKey() {
    this.webClient = WebClient.create();
    Optional<RiotApi> riotApi = this.intRiotApi.findById(1L);
    apiKey = riotApi.map(RiotApi::getApiKey).orElse(null);
  }

  private SummonerDTO getQueryParamResult(String url, String param) {

    if (!CommonFunctions.isNotNullOrEmpty(apiKey)) {
      initApiKey();
    }

    return webClient.get()
        .uri(url, param, apiKey)
        .retrieve()
        .bodyToMono(SummonerDTO.class)
        .block();
  }

  private String getApiKeyURLFormat() {
    return "?api_key=" + apiKey;
  }

  @Override
  public List<SummonerDTO> getSummonerByName(String name) {
    List<SummonerDTO> dtoList;

    try {
      validateInput(name);
      String url = URIs.URI_SUMMONER_ACCOUNT_NAME + name + getApiKeyURLFormat();
      dtoList = new ArrayList<>(Collections.singletonList(getQueryParamResult(url, name)));
    } catch (Exception ex) {
      throw new ServiceException(ConstantsExceptions.ERROR_SEARCHING_SUMMONER.concat(name), ex);
    }

    return dtoList;
  }

  @Override
  public List<SummonerDTO> getSummonerByAccountId(String accountId) {
    List<SummonerDTO> dtoList;

    try {
      validateInput(accountId);
      String url = URIs.URI_SUMMONER_ACCOUNT_ID + accountId + getApiKeyURLFormat();
      dtoList = new ArrayList<>(Collections.singletonList(getQueryParamResult(url, accountId)));
    } catch (Exception ex) {
      throw new ServiceException(ConstantsExceptions.ERROR_SEARCHING_SUMMONER.concat(accountId), ex);
    }

    return dtoList;
  }

  @Override
  public SummonerDTO getSummonerByPuuid(String puuid) {
    String url = URIs.URI_SUMMONER_ACCOUNT_PUUID + puuid + getApiKeyURLFormat();

    return getQueryParamResult(url, puuid);
  }

  private void validateInput(String input) {
    if (!CommonFunctions.isNotNullOrEmpty(input)) {
      throw new ServiceException(ConstantsExceptions.ERROR_SEARCHING_SUMMONER.concat(input));
    }
  }

}
