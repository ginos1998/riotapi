package riot.riotapi.services.implementations;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import riot.riotapi.entities.ChampionData;
import riot.riotapi.services.interfaces.IntChampionApiService;
import riot.riotapi.utils.URIs;

@Service
public class ImpChampionApiService implements IntChampionApiService {

  private RestTemplate restTemplate;

  private ImpChampionApiService() {
    restTemplate = new RestTemplate();
  }

  @Override
  public ChampionData getChampionByName(String champName) {
    String uri = URIs.URI_LOL_CHAMPION.replace("###", champName);

    return restTemplate.getForObject(uri, ChampionData.class);
  }

  @Override
  public ChampionData getAllChampions() {

    return restTemplate.getForObject(URIs.URI_ALL_LOL_CHAMPIONS, ChampionData.class);
  }
}
