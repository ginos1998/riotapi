package riot.riotapi.services.interfaces;

import riot.riotapi.dtos.ChampionDataDTO;

public interface IntChampionService {
  ChampionDataDTO getChampionByName(String champName);
  ChampionDataDTO getAllChampions();
}
