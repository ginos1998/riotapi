package riot.riotapi.delegators;

import riot.riotapi.entities.Summoner;
import riot.riotapi.services.ServiceFactory;

public class SummonerDelegador {

  private SummonerDelegador() {
    // default constructor
  }

  public static void saveSummoner(Summoner summoner) {
    ServiceFactory.getIntSummonerService().saveSummoner(summoner);
  }
}
