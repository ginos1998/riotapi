package riot.riotapi.dtos.match;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class MatchesDTO {
  @JsonProperty("matchesList")
  private ArrayList<String> matchesList;
  private String puuid;
  private String summonerName;
}
