package riot.riotapi.dtos.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BanDTO {
    private Integer first;
    private Integer pickTurn;
}
