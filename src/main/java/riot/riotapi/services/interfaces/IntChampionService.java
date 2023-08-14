package riot.riotapi.services.interfaces;

import riot.riotapi.dtos.ChampionDataDTO;
import riot.riotapi.entities.Champion;

public interface IntChampionService {
  ChampionDataDTO getChampionByName(String champName);
  ChampionDataDTO getAllChampions();
  Champion findByKey(Long key);
}
