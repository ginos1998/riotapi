package riot.riotapi.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import riot.riotapi.repositories.factories.PersistenceFactory;
import riot.riotapi.repositories.interfaces.IntPersistenceSummoner;
import riot.riotapi.services.ServiceFactory;
import riot.riotapi.services.interfaces.IntChampionApiService;
import riot.riotapi.services.interfaces.IntSummonerApiService;
import riot.riotapi.services.interfaces.IntSummonerService;

@Configuration
public class FactoryConfig {

  public FactoryConfig(){
    // default
  }

  //***************************** SERVICE FACTORY *****************************//

  @Autowired
  public void setIntSummonerService(IntSummonerService intSummonerService) {
    ServiceFactory.setIntSummonerService(intSummonerService);
  }

  @Autowired
  public void setIntSummonerApiService(IntSummonerApiService intSummonerApiService) {
    ServiceFactory.setIntSummonerApiService(intSummonerApiService);
  }

  @Autowired
  public void setIntChampionApiService(IntChampionApiService intChampionApiService) {
    ServiceFactory.setIntChampionApiService(intChampionApiService);
  }

  //***************************** PERSISTENCE FACTORY *****************************//

  @Autowired
  public void setIntPersistenceSummoner(IntPersistenceSummoner intPersistenceSummoner) {
    PersistenceFactory.setIntPersistenceSummoner(intPersistenceSummoner);
  }
}
