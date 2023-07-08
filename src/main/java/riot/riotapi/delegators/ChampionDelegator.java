package riot.riotapi.delegators;

import riot.riotapi.entities.ChampionData;
import riot.riotapi.services.ServiceFactory;

public class ChampionDelegator {

  private ChampionDelegator() {
    // default
  }

  public static ChampionData getChampionByName(String champName) {
    return ServiceFactory.getIntChampionApiService().getChampionByName(champName);
  }

  public static ChampionData getAllChampions() {
    return ServiceFactory.getIntChampionApiService().getAllChampions();
  }
}
