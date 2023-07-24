package riot.riotapi.services.implementations;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import riot.riotapi.dtos.SummonerDTO;
import riot.riotapi.dtos.match.MatchesDTO;
import riot.riotapi.filters.MatchFilter;
import riot.riotapi.repositories.factories.PersistenceFactory;
import riot.riotapi.services.interfaces.IntMatchApiService;
import riot.riotapi.utils.Constants;
import riot.riotapi.utils.URIs;

import java.lang.constant.Constable;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImpMatchApiService extends ImpApiService implements IntMatchApiService {

  public ImpMatchApiService() {
    super(WebClient.create(), PersistenceFactory.getIntRiotApi().findById(1L));
  }

  @Override
  public MatchesDTO getMatchesByPuuid(MatchFilter filter) {
    MatchesDTO matchesDTOList = this.webClient.get()
        .uri(addParamsToUri(filter), this.apiKey)
        .retrieve()
        .bodyToMono(MatchesDTO.class)
        .block();
    return matchesDTOList;
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
