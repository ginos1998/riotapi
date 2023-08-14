package riot.riotapi.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "summoner")
public class Summoner {
  @Id
  @Column(name = "summoner_id")
  private String summonerId;
  @Column(name = "account_ID")
  private String accountId;
  @Column(name = "puuid")
  private String puuid;
  @Column(name = "name")
  private String name;
  @Column(name = "profile_icon_id")
  private int profileIconId;
  @Column(name = "revision_date")
  private long revisionDate;
  @Column(name = "summoner_level")
  private int summonerLevel;

  public Summoner(String summonerId) {
    this.summonerId = summonerId;
  }
}
