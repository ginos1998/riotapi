package riot.riotapi.services.interfaces;

import exceptions.ServiceFactoryException;
import riot.riotapi.entities.ChampionData;

public interface IntChampionApiService {

  ChampionData getChampionByName(String champName);

  ChampionData getAllChampions();

  String importAllChampions() throws ServiceFactoryException;
}