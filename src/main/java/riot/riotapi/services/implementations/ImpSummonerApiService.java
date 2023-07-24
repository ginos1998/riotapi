package riot.riotapi.services.implementations;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import riot.riotapi.dtos.SummonerDTO;
import riot.riotapi.entities.RiotApi;
import riot.riotapi.repositories.factories.PersistenceFactory;
import riot.riotapi.services.interfaces.IntSummonerApiService;
import riot.riotapi.utils.CommonFunctions;
import riot.riotapi.utils.Constants;
import riot.riotapi.utils.URIs;

import java.lang.constant.Constable;
import java.util.Optional;

@Service
public class ImpSummonerApiService implements IntSummonerApiService {
  private WebClient webClient;
  private String apiKey;

  public ImpSummonerApiService() {
    // default
  }

  private void initApiKey() {
    this.webClient = WebClient.create();
    Optional<RiotApi> riotApi = PersistenceFactory.getIntRiotApi().findById(1L);
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
  public SummonerDTO getSummonerByName(String name) {
    String url = URIs.URI_SUMMONER_ACCOUNT_NAME + name + getApiKeyURLFormat();

    return getQueryParamResult(url, name);
  }

  @Override
  public SummonerDTO getSummonerByAccountId(String accountId) {
    String url = URIs.URI_SUMMONER_ACCOUNT_ID + accountId + getApiKeyURLFormat();

    return getQueryParamResult(url, accountId);
  }

  @Override
  public SummonerDTO getSummonerByPuuid(String puuid) {
    String url = URIs.URI_SUMMONER_ACCOUNT_PUUID + puuid + getApiKeyURLFormat();

    return getQueryParamResult(url, puuid);
  }

}
