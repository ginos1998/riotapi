package riot.riotapi.services.implementations;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import riot.riotapi.dtos.SummonerDTO;
import riot.riotapi.services.interfaces.IntSummonerApiService;
import riot.riotapi.utils.Constants;
import riot.riotapi.utils.URIs;

import java.lang.constant.Constable;

@Service
public class ImpSummonerApiService implements IntSummonerApiService {
  private WebClient webClient;

  public ImpSummonerApiService() {
    this.webClient = WebClient.create();
  }

  private SummonerDTO getQueryParamResult(String url, String param) {
    return webClient.get()
        .uri(url, param, Constants.apiKey)
        .retrieve()
        .bodyToMono(SummonerDTO.class)
        .block();
  }

  private String getApiKeyURLFormat() {
    return "?api_key=" + Constants.apiKey;
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
