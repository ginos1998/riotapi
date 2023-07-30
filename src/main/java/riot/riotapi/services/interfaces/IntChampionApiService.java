package riot.riotapi.services.interfaces;

import riot.riotapi.exceptions.ServiceException;
import riot.riotapi.dtos.ChampionDataDTO;
public interface IntChampionApiService {

  ChampionDataDTO getChampionByName(String champName) throws ServiceException;

  ChampionDataDTO getAllChampions();

  String importAllChampions() throws ServiceException;
}
