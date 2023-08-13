package riot.riotapi.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SummonerDTO {
  @JsonProperty("id")
  private String summonerId;
  private String accountId;
  private String puuid;
  private String name;
  private int profileIconId;
  private long revisionDate;
  private int summonerLevel;
}