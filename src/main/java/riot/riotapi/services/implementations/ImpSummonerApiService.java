package riot.riotapi.services.implementations;

import org.springframework.web.reactive.function.client.WebClient;
import riot.riotapi.delegators.SummonerDelegador;
import riot.riotapi.entities.Summoner;
import riot.riotapi.services.interfaces.IntSummonerApiService;
import riot.riotapi.services.interfaces.IntSummonerService;
import riot.riotapi.utils.URIs;

public class ImpSummonerApiService implements IntSummonerApiService {
  private WebClient webClient;
  private final String apiKey = "RGAPI-8440711e-b278-4f39-bfa1-4116117fbd14";

  public ImpSummonerApiService() {
    this.webClient = WebClient.create();
  }

  private Summoner getQueryParamResult(String url, String param) {
    return webClient.get()
        .uri(url, param, apiKey)
        .retrieve()
        .bodyToMono(Summoner.class)
        .block();
  }

  private String getApiKeyURLFormat() {
    return "?api_key=" + apiKey;
  }

  @Override
  public Summoner getSummonerByName(String name) {
    String url = URIs.URI_SUMMONER_ACCOUNT_NAME + name + getApiKeyURLFormat();

    return getQueryParamResult(url, name);
  }

  @Override
  public Summoner getSummonerByAccountId(String accountId) {
    String url = URIs.URI_SUMMONER_ACCOUNT_ID + accountId + getApiKeyURLFormat();

    return getQueryParamResult(url, accountId);
  }

  @Override
  public Summoner getSummonerByPuuid(String puuid) {
    String url = URIs.URI_SUMMONER_ACCOUNT_PUUID + puuid + getApiKeyURLFormat();

    return getQueryParamResult(url, puuid);
  }

}
