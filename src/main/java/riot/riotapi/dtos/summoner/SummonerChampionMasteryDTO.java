package riot.riotapi.dtos.summoner;

import lombok.Data;

@Data
public class SummonerChampionMasteryDTO {
  private String puuid;
  private Long championId;
  private String championName;
  private Long championLevel;
  private Long championPoints;
  private Long lastPlayTime;
  private Long championPointsSinceLastLevel;
  private Long championPointsUntilNextLevel;
  private Boolean chestGranted;
  private Long tokensEarned;
  private String summonerId;
  private String masteryEmoji;
  private String champEmoji;
}
