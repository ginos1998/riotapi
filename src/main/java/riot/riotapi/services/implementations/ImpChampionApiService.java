package riot.riotapi.services.implementations;

import riot.riotapi.exceptions.ServiceFactoryException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import riot.riotapi.entities.Champion;
import riot.riotapi.entities.ChampionData;
import riot.riotapi.repositories.factories.PersistenceFactory;
import riot.riotapi.services.interfaces.IntChampionApiService;
import riot.riotapi.utils.CommonFunctions;
import riot.riotapi.utils.URIs;

import java.util.Date;
import java.util.Map;

@Service
public class ImpChampionApiService implements IntChampionApiService {

  private RestTemplate restTemplate;

  private ImpChampionApiService() {
    restTemplate = new RestTemplate();
  }

  @Override
  public ChampionData getChampionByName(String champName) {
    String uri = URIs.URI_LOL_CHAMPION.replace("###", champName);
    ChampionData cd = restTemplate.getForObject(uri, ChampionData.class);
    return cd;
  }

  @Override
  public ChampionData getAllChampions() {
    ChampionData cd = restTemplate.getForObject(URIs.URI_ALL_LOL_CHAMPIONS, ChampionData.class);
    return cd;
  }

  @Override
  public String importAllChampions() throws ServiceFactoryException {
    ChampionData cd = getAllChampions();
    cd.setLastUpdate(new Date());

    if (cd.getData() == null) {
      throw new ServiceFactoryException("ERROR");
    }

    PersistenceFactory.getIntPersistenceChampionData().save(cd);

    mapChampionsData(cd.getData(), cd.getId());

    PersistenceFactory.getIntPersistenceChampion().saveAll(cd.getData().values());

    return "OK";
  }

  private void mapChampionsData(Map<String, Champion> champs, Long champDataId) {
    if (CommonFunctions.isNotNullOrEmpty(champs)) {
      for (Champion champ: champs.values()) {
        champ.setChampDataId(champDataId);
      }
    }
  }
}
