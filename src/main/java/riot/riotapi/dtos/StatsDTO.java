package riot.riotapi.dtos;

import lombok.Data;

@Data
public class StatsDTO {
  private int hp;
  private int hpperlevel;
  private int mp;
  private int mpperlevel;
  private int movespeed;
  private int armor;
  private double armorperlevel;
  private int spellblock;
  private double spellblockperlevel;
  private int attackrange;
  private int hpregen;
  private int hpregenperlevel;
  private int mpregen;
  private int mpregenperlevel;
  private int crit;
  private int critperlevel;
  private int attackdamage;
  private int attackdamageperlevel;
  private double attackspeedperlevel;
  private double attackspeed;
}
