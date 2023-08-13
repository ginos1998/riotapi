package riot.riotapi.dtos.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerksDTO {
    private StatPerksDTO statPerks;
    private ArrayList<StyleDTO> styles;
    private ArrayList<Integer> perksId;
    private Integer perkStyle;
    private Integer perkSubStyle;
}
