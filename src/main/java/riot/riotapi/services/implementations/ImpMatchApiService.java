package riot.riotapi.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import riot.riotapi.dtos.match.MatchesDTO;
import riot.riotapi.entities.RiotApi;
import riot.riotapi.filters.MatchFilter;
import riot.riotapi.repositories.interfaces.IntRiotApi;
import riot.riotapi.services.interfaces.IntMatchApiService;
import riot.riotapi.utils.CommonFunctions;
import riot.riotapi.utils.URIs;

@Service
public class ImpMatchApiService implements IntMatchApiService {

  private final IntRiotApi intRiotApi;
  private final WebClient webClient;
  private String apiKey;
  @Autowired
  public ImpMatchApiService(IntRiotApi intRiotApi) {
    this.intRiotApi = intRiotApi;
    this.webClient = WebClient.create();
  }

  @Override
  public MatchesDTO getMatchesByPuuid(MatchFilter filter) {

    if (!CommonFunctions.isNotNullOrEmpty(apiKey)) {
      RiotApi riotApi = this.intRiotApi.findById(1L).orElse(null);
      apiKey = riotApi != null ? riotApi.getApiKey() : "";
    }

    return this.webClient.get()
        .uri(addParamsToUri(filter), this.apiKey)
        .retrieve()
        .bodyToMono(MatchesDTO.class)
        .block();
  }

  private String addParamsToUri(MatchFilter filter) {
    String uri = URIs.URI_LOL_MATCHES_BY_PUUID;

    if (filter.getPuuid() != null) {
      uri.replace("{1}", filter.getPuuid());
    }
    if (filter.getStartTime() >= 0) {
      uri.replace("{2}", Long.toString(filter.getStartTime()));
    }
    if(filter.getEndTime() >= 0) {
      uri.replace("{3}", Long.toString(filter.getEndTime()));
    }
    if(filter.getQueue() >= 0) {
      uri.replace("{4}", Integer.toString(filter.getQueue()));
    }
    if(filter.getType() != null){
      uri.replace("{5}", filter.getType());
    }
    if(filter.getStart() >= 0) {
      uri.replace("{6}", Integer.toString(filter.getStart()));
    }
    if (filter.getCount() >= 0) {
      uri.replace("{7}", Integer.toString(filter.getCount()));
    }
    return uri;
  }
}
