package riot.riotapi.dtos.mappers.interfaces;

import org.mapstruct.Mapper;
import riot.riotapi.dtos.ChampionDTO;
import riot.riotapi.dtos.ChampionDataDTO;
import riot.riotapi.dtos.InfoDTO;
import riot.riotapi.dtos.StatsDTO;
import riot.riotapi.entities.Champion;
import riot.riotapi.entities.ChampionData;
import riot.riotapi.entities.Info;
import riot.riotapi.entities.Stats;

@Mapper
public interface IntChampionMapper {
  ChampionData toChampionData(ChampionDataDTO dto);
  Champion toChampion(ChampionDTO dto);
  Info toInfo(InfoDTO dto);
  Stats toStats(StatsDTO dto);
}
