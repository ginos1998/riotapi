package riot.riotapi.dtos.match;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ParticipantInfoDTO {
    private String summonerName;
    private String championName;
    private String lane;
    private Boolean win;
    private Integer teamId;
    public Integer spell1Id;
    public Integer spell2Id;

    public ParticipantInfoDTO(String summonerName, String championName, String lane, Boolean win, Integer teamId) {
        this.summonerName = summonerName;
        this.championName = championName;
        this.lane = lane;
        this.win = win;
        this.teamId = teamId;
    }
}
