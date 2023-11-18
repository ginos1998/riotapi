package riot.riotapi.dtos.summoner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SummonerTierDTO {
  private String leagueId;
  private String queueType;
  private String tier;
  private String rank;
  private String summonerId;
  private String summonerName;
  private int leaguePoints;
  private int wins;
  private int losses;
  private boolean veteran;
  private boolean inactive;
  private boolean freshBlood;
  private boolean hotStreak;
  private String dsEmoji;
}
