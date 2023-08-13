package riot.riotapi.dtos.match;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class LiveMatchRootDTO {
    @JsonProperty("gameId")
    private Long matchId;
    private Integer mapId;
    @JsonProperty("gameMode")
    private String mode;
    @JsonProperty("gameType")
    private String type;
    private Integer gameQueueConfigId;
    private ArrayList<ParticipantDTO> participants;
    private String platformId;
    private ArrayList<BanDTO> bannedChampions;
    @JsonProperty("gameStartTime")
    private Long startTime;
    @JsonProperty("gameLength")
    private Integer duration;
}
