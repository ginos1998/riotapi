package riot.riotapi.dtos.match;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;
import riot.riotapi.utils.views.CommonView;

@Data
@NoArgsConstructor
public class ParticipantInfoDTO {
    @JsonView({CommonView.HistoryView.class, CommonView.LiveView.class})
    private String summonerName;
    @JsonView({CommonView.HistoryView.class, CommonView.LiveView.class})
    private int summonerLevel;
    @JsonView({CommonView.HistoryView.class, CommonView.LiveView.class})
    private String championName;
    @JsonView(CommonView.HistoryView.class)
    private String lane;
    @JsonView(CommonView.HistoryView.class)
    private Boolean win;
    @JsonView({CommonView.HistoryView.class, CommonView.LiveView.class})
    private Integer teamId;
    public Integer spell1Id;
    public Integer spell2Id;
    @JsonView(CommonView.LiveView.class)
    public String spellName1;
    @JsonView(CommonView.LiveView.class)
    public String spellName2;
    public String spell1Emoji;
    public String spell2Emoji;

    public ParticipantInfoDTO(String summonerName, String championName, String lane, Boolean win, Integer teamId) {
        this.summonerName = summonerName;
        this.championName = championName;
        this.lane = lane;
        this.win = win;
        this.teamId = teamId;
    }
}
