package riot.riotapi.dtos.match;

import lombok.Data;
import lombok.NoArgsConstructor;
import riot.riotapi.utils.CommonFunctions;

import java.util.List;

@Data
@NoArgsConstructor
public class MatchDTO {
    private String mode;
    private String creation;
    private String startTime;
    private String endTime;
    private String duration;
    private String gameVersion;
    private List<ParticipantInfoDTO> participants;

    public MatchDTO(String mode, Long creation, Long startTime, Long endTime, Long duration, String gameVersion) {
        this.mode = mode;
        // perdon diosito
        this.creation = CommonFunctions.getDateAsString(creation);
        this.startTime = CommonFunctions.getDateAsString(startTime);
        this.endTime = CommonFunctions.getDateAsString(endTime);
        this.duration = CommonFunctions.getDateAsString(duration);
        this.gameVersion = gameVersion;
    }
}
