package riot.riotapi.dtos.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StyleDTO {
    private String description;
    private ArrayList<SelectionDTO> selections;
    private Integer style;
}
