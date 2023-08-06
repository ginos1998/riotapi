package riot.riotapi.dtos.match;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchesDTO {
  private String[] matchesList;
  private String puuid;
  private String summonerName;
}
