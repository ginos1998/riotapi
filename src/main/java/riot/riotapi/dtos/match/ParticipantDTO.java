package riot.riotapi.dtos.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantDTO {
    private Integer allInPings;
    private Integer assistMePings;
    private Integer assists;
    private Integer baitPings;
    private Integer baronKills;
    private Integer basicPings;
    private Integer bountyLevel;
    private ChallengesDTO challenges;
    private Integer champExperience;
    private Integer champLevel;
    private Long championId;
    private String championName;
    private Integer championTransform;
    private Integer commandPings;
    private Integer consumablesPurchased;
    private Integer damageDealtToBuildings;
    private Integer damageDealtToObjectives;
    private Integer damageDealtToTurrets;
    private Integer damageSelfMitigated;
    private Integer dangerPings;
    private Integer deaths;
    private Integer detectorWardsPlaced;
    private Integer doubleKills;
    private Integer dragonKills;
    private Boolean eligibleForProgression;
    private Integer enemyMissingPings;
    private Integer enemyVisionPings;
    private Boolean firstBloodAssist;
    private Boolean firstBloodKill;
    private Boolean firstTowerAssist;
    private Boolean firstTowerKill;
    private Boolean gameEndedInEarlySurrender;
    private Boolean gameEndedInSurrender;
    private Integer getBackPings;
    private Integer goldEarned;
    private Integer goldSpent;
    private Integer holdPings;
    private String individualPosition;
    private Integer inhibitorKills;
    private Integer inhibitorTakedowns;
    private Integer inhibitorsLost;
    private Integer item0;
    private Integer item1;
    private Integer item2;
    private Integer item3;
    private Integer item4;
    private Integer item5;
    private Integer item6;
    private Integer itemsPurchased;
    private Integer killingSprees;
    private Integer kills;
    private String lane;
    private Integer largestCriticalStrike;
    private Integer largestKillingSpree;
    private Integer largestMultiKill;
    private Integer longestTimeSpentLiving;
    private Integer magicDamageDealt;
    private Integer magicDamageDealtToChampions;
    private Integer magicDamageTaken;
    private Integer needVisionPings;
    private Integer neutralMinionsKilled;
    private Integer nexusKills;
    private Integer nexusLost;
    private Integer nexusTakedowns;
    private Integer objectivesStolen;
    private Integer objectivesStolenAssists;
    private Integer onMyWayPings;
    private Integer participantId;
    private Integer pentaKills;
    private PerksDTO perks;
    private Integer physicalDamageDealt;
    private Integer physicalDamageDealtToChampions;
    private Integer physicalDamageTaken;
    private Integer placement;
    private Integer playerAugment1;
    private Integer playerAugment2;
    private Integer playerAugment3;
    private Integer playerAugment4;
    private Integer playerSubteamId;
    private Integer profileIcon;
    private Integer pushPings;
    private String puuid;
    private Integer quadraKills;
    private String riotIdName;
    private String riotIdTagline;
    private String role;
    private Integer sightWardsBoughtInGame;
    private Integer spell1Casts;
    private Integer spell2Casts;
    private Integer spell3Casts;
    private Integer spell4Casts;
    private Integer subteamPlacement;
    private Integer summoner1Casts;
    private Integer summoner1Id;
    private Integer summoner2Casts;
    private Integer summoner2Id;
    private String summonerId;
    private Integer summonerLevel;
    private String summonerName;
    private Boolean teamEarlySurrendered;
    private Integer teamId;
    private String teamPosition;
    private Integer timeCCingOthers;
    private Integer timePlayed;
    private Integer totalAllyJungleMinionsKilled;
    private Integer totalDamageDealt;
    private Integer totalDamageDealtToChampions;
    private Integer totalDamageShieldedOnTeammates;
    private Integer totalDamageTaken;
    private Integer totalEnemyJungleMinionsKilled;
    private Integer totalHeal;
    private Integer totalHealsOnTeammates;
    private Integer totalMinionsKilled;
    private Integer totalTimeCCDealt;
    private Integer totalTimeSpentDead;
    private Integer totalUnitsHealed;
    private Integer tripleKills;
    private Integer trueDamageDealt;
    private Integer trueDamageDealtToChampions;
    private Integer trueDamageTaken;
    private Integer turretKills;
    private Integer turretTakedowns;
    private Integer turretsLost;
    private Integer unrealKills;
    private Integer visionClearedPings;
    private Integer visionScore;
    private Integer visionWardsBoughtInGame;
    private Integer wardsKilled;
    private Integer wardsPlaced;
    private Boolean win;
    public Integer spell1Id;
    public Integer spell2Id;
}
