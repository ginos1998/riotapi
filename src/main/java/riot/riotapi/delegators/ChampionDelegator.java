package riot.riotapi.delegators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import riot.riotapi.exceptions.ServiceException;
import riot.riotapi.dtos.ChampionDataDTO;
import riot.riotapi.services.interfaces.IntChampionApiService;
import riot.riotapi.services.interfaces.IntChampionService;

@Service
public class ChampionDelegator {

  private final IntChampionApiService intChampionApiService;
  private final IntChampionService intChampionService;
  @Autowired
  private ChampionDelegator(IntChampionApiService intChampionApiService, IntChampionService intChampionService) {
    this.intChampionApiService = intChampionApiService;
    this.intChampionService = intChampionService;
  }

  /**
   * Search a champion by his name. First, check if it exists in the database. Otherwise, send a request to the api.
   * @param champName .
   * @return ChampionDataDTO class
   */
  public ChampionDataDTO getChampionByName(String champName) {
    return intChampionService.getChampionByName(champName);
  }

  public ChampionDataDTO getAllChampions() {
    return intChampionService.getAllChampions();
  }

  public String importAllChampions() throws ServiceException {
    return intChampionApiService.importAllChampions();
  }
}
