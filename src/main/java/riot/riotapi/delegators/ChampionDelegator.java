package riot.riotapi.delegators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import riot.riotapi.exceptions.ServiceFactoryException;
import riot.riotapi.dtos.ChampionDataDTO;
import riot.riotapi.services.interfaces.IntChampionApiService;

@Service
public class ChampionDelegator {

  private final IntChampionApiService intChampionApiService;
  @Autowired
  private ChampionDelegator(IntChampionApiService intChampionApiService) {
    this.intChampionApiService = intChampionApiService;
  }

  public ChampionDataDTO getChampionByName(String champName) {
    return intChampionApiService.getChampionByName(champName);
  }

  public ChampionDataDTO getAllChampions() {
    return intChampionApiService.getAllChampions();
  }

  public String importAllChampions() throws ServiceFactoryException {
    return intChampionApiService.importAllChampions();
  }
}
