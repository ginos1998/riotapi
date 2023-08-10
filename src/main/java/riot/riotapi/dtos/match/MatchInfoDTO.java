package riot.riotapi.dtos.match;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchInfoDTO {
    @JsonProperty("gameId")
    private Integer matchId;
    @JsonProperty("gameCreation")
    private Long creation;
    @JsonProperty("gameDuration")
    private Integer duration;
    @JsonProperty("gameStartTimestamp")
    private Long startTime;
    @JsonProperty("gameEndTimestamp")
    private Long endTime;
    @JsonProperty("gameMode")
    private String mode;
    @JsonProperty("gameName")
    private String name;
    @JsonProperty("gameType")
    private String type;
    @JsonProperty("gameVersion")
    private String version;
    @JsonProperty("mapId")
    private Integer mapId;
    public ArrayList<ParticipantDTO> participants;
    private String platformId;
    private Integer queueId;
    private ArrayList<TeamDTO> teams;
    private String tournamentCode;
}
