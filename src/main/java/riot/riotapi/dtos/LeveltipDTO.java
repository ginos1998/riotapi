package riot.riotapi.dtos;

import lombok.Data;

import java.util.ArrayList;

@Data
public class LeveltipDTO {
  private ArrayList<String> label;
  private ArrayList<String> effect;
}
