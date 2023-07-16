package riot.riotapi.services.interfaces;

import riot.riotapi.exceptions.ServiceFactoryException;
import riot.riotapi.dtos.ChampionDataDTO;

public interface IntChampionApiService {

  ChampionDataDTO getChampionByName(String champName);

  ChampionDataDTO getAllChampions();

  String importAllChampions() throws ServiceFactoryException;
}
