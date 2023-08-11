package riot.riotapi.dtos.match;

import lombok.Data;

@Data
public class ParticipantInfoDTO {
    private String summonerName;
    private String championName;
    private String lane;
    private Boolean win;
    private Integer teamId;
}
