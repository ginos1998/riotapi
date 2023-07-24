package riot.riotapi.services.implementations;

import org.springframework.web.reactive.function.client.WebClient;
import riot.riotapi.entities.RiotApi;

import java.util.Optional;

public abstract class ImpApiService {
  protected WebClient webClient;
  protected String apiKey;

  protected ImpApiService(){
    // default
  }

  protected ImpApiService(WebClient webClient, Optional<RiotApi> riotApi) {
    this.webClient = webClient;
    this.apiKey = riotApi.map(RiotApi::getApiKey).orElse(null);
  }

  public WebClient getWebClient() {
    return webClient;
  }

  public void setWebClient(WebClient webClient) {
    this.webClient = webClient;
  }

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }
}
