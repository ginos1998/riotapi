package riot.riotapi.services.interfaces;

import riot.riotapi.dtos.ChampionDataDTO;
import riot.riotapi.entities.Champion;

import java.util.List;

public interface IntChampionService {
  ChampionDataDTO getChampionByName(String champName);
  ChampionDataDTO getAllChampions();
  Champion findByKey(Long key);
  List<Champion> findByKeyIn(List<Long> champIds);
}
