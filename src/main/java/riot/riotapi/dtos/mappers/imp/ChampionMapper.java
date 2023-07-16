package riot.riotapi.dtos.mappers.imp;

import riot.riotapi.dtos.ChampionDTO;
import riot.riotapi.dtos.ChampionDataDTO;
import riot.riotapi.dtos.InfoDTO;
import riot.riotapi.dtos.StatsDTO;
import riot.riotapi.dtos.mappers.interfaces.IntChampionMapper;
import riot.riotapi.entities.Champion;
import riot.riotapi.entities.ChampionData;
import riot.riotapi.entities.Info;
import riot.riotapi.entities.Stats;

public class ChampionMapper implements IntChampionMapper {
  public ChampionMapper() {
    // default
  }

  @Override
  public ChampionData toChampionData(ChampionDataDTO dto) {
    ChampionData champData = null;
    if (dto != null) {
      champData = new ChampionData();
      champData.setLastUpdate(dto.getLastUpdate());
      champData.setFormat(dto.getFormat());
      champData.setType(dto.getType());
      champData.setVersion(dto.getVersion());
    }

    return champData;
  }

  @Override
  public Champion toChampion(ChampionDTO dto) {
    Champion champion = null;

    if(dto != null) {
      champion = new Champion();
      champion.setId(dto.getId());
      champion.setName(dto.getName());
      champion.setKey(Long.parseLong(dto.getKey()));
      champion.setTitle(dto.getTitle());
    }

    return champion;
  }

  @Override
  public Info toInfo(InfoDTO dto) {
    Info info = null;

    if (dto != null) {
      info = new Info();
      info.setAttack(dto.getAttack());
      info.setDefense(dto.getDefense());
      info.setMagic(dto.getMagic());
      info.setDifficulty(dto.getDifficulty());
    }

    return info;
  }

  @Override
  public Stats toStats(StatsDTO dto) {
    Stats stats = null;

    if(dto != null) {
      stats = new Stats();
      stats.setHp(dto.getHp());
      stats.setHpperlevel(dto.getHpperlevel());
      stats.setMp(dto.getMp());
      stats.setMpperlevel(dto.getMpperlevel());
      stats.setMovespeed(dto.getMovespeed());
      stats.setArmor(dto.getArmor());
      stats.setArmorperlevel(dto.getArmorperlevel());
      stats.setSpellblock(dto.getSpellblock());
      stats.setSpellblockperlevel(dto.getSpellblockperlevel());
      stats.setAttackrange(dto.getAttackrange());
      stats.setHpregen(dto.getHpregen());
      stats.setHpregenperlevel(dto.getHpregenperlevel());
      stats.setCrit(dto.getCrit());
      stats.setCritperlevel(dto.getCritperlevel());
      stats.setAttackdamage(dto.getAttackdamage());
      stats.setAttackdamageperlevel(dto.getAttackdamageperlevel());
      stats.setAttackspeed(dto.getAttackspeed());
      stats.setAttackspeedperlevel(dto.getAttackspeedperlevel());
    }
    return stats;
  }
}
