package riot.riotapi.dtos.mappers.interfaces;

import riot.riotapi.dtos.SummonerDTO;
import riot.riotapi.entities.Summoner;

public interface IntSummonerMapper {
  Summoner toSummoner(SummonerDTO dto);
}
