package riot.riotapi.dtos.mappers.imp;

import riot.riotapi.dtos.SummonerDTO;
import riot.riotapi.dtos.mappers.interfaces.IntSummonerMapper;
import riot.riotapi.entities.Summoner;

public class SummonerMapper implements IntSummonerMapper {
  @Override
  public Summoner toSummoner(SummonerDTO dto) {
    Summoner summoner = null;

    if(dto != null) {
      summoner = new Summoner();
      summoner.setSummonerLevel(dto.getSummonerLevel());
      summoner.setId(dto.getId());
      summoner.setName(dto.getName());
      summoner.setPuuid(dto.getPuuid());
      summoner.setAccountId(dto.getAccountId());
      summoner.setRevisionDate(dto.getRevisionDate());
      summoner.setProfileIconId(dto.getProfileIconId());
    }

    return summoner;
  }
}
