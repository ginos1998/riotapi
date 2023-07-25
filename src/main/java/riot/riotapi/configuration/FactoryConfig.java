package riot.riotapi.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import riot.riotapi.repositories.factories.PersistenceFactory;
import riot.riotapi.repositories.interfaces.*;

@Configuration
public class FactoryConfig {

  public FactoryConfig(){
    // default
  }

  //***************************** PERSISTENCE FACTORY *****************************//

  @Autowired
  public void setIntPersistenceSummoner(IntPersistenceSummoner intPersistenceSummoner) {
    PersistenceFactory.setIntPersistenceSummoner(intPersistenceSummoner);
  }

  @Autowired
  public void setIntPersistenceChampion(IntPersistenceChampionData intPersistenceChampionData) {
    PersistenceFactory.setIntPersistenceChampionData(intPersistenceChampionData);
  }

  @Autowired
  public void setIntPersistenceChampion(IntPersistenceChampion intPersistenceChampion){
    PersistenceFactory.setIntPersistenceChampion(intPersistenceChampion);
  }

  @Autowired
  public void setIntPersistenceInfo(IntPersistenceInfo intPersistenceInfo) {
    PersistenceFactory.setIntPersistenceInfo(intPersistenceInfo);
  }

  @Autowired
  public void setIntPersistenceStats(IntPersistenceStats intPersistenceStats) {
    PersistenceFactory.setIntPersistenceStats(intPersistenceStats);
  }

  @Autowired
  public void setIntRiotApi(IntRiotApi intRiotApi) {
    PersistenceFactory.setIntRiotApi(intRiotApi);
  }
}
