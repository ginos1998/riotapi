package riot.riotapi.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "summoner")
public class Summoner {
  @Id
  private String id;
  private String accountId;
  private String puuid;
  private String name;
  private int profileIconId;
  private long revisionDate;
  private int summonerLevel;
}