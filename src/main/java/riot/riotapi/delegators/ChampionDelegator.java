package riot.riotapi.delegators;

import riot.riotapi.exceptions.ServiceFactoryException;
import riot.riotapi.dtos.ChampionDataDTO;
import riot.riotapi.services.ServiceFactory;

public class ChampionDelegator {

  private ChampionDelegator() {
    // default
  }

  public static ChampionDataDTO getChampionByName(String champName) {
    return ServiceFactory.getIntChampionApiService().getChampionByName(champName);
  }

  public static ChampionDataDTO getAllChampions() {
    return ServiceFactory.getIntChampionApiService().getAllChampions();
  }

  public static String importAllChampions() throws ServiceFactoryException {
    return ServiceFactory.getIntChampionApiService().importAllChampions();
  }
}
