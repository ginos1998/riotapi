package riot.riotapi.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MasteryDTO {
  private String mastery;
  private String dsEmoji;
  private Long level;
}
