package riot.riotapi.dtos.match;

import lombok.Data;

import java.util.List;

@Data
public class MatchDTO {
    private String mode;
    private String creation;
    private String startTime;
    private String endTime;
    private String duration;
    private String gameVersion;
    private List<ParticipantInfoDTO> participants;

}
