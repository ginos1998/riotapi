package riot.riotapi.dtos.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamDTO {
    private ArrayList<BanDTO> bans;
    private ObjetivesDTO objetives;
    private Integer teamId;
    private Boolean win;
}
