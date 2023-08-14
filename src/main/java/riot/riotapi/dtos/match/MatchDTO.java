package riot.riotapi.dtos.match;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;
import riot.riotapi.utils.CommonFunctions;
import riot.riotapi.utils.views.CommonView;

import java.util.List;

@Data
@NoArgsConstructor
public class MatchDTO {
    @JsonView({CommonView.HistoryView.class, CommonView.LiveView.class})
    private String mode;
    @JsonView(CommonView.HistoryView.class)
    private String creation;
    @JsonView({CommonView.HistoryView.class, CommonView.LiveView.class})
    private String startTime;
    @JsonView(CommonView.HistoryView.class)
    private String endTime;
    @JsonView(CommonView.HistoryView.class)
    private String duration;
    @JsonView(CommonView.HistoryView.class)
    private String gameVersion;
    @JsonView({CommonView.HistoryView.class, CommonView.LiveView.class})
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
