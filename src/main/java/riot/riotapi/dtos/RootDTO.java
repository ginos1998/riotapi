package riot.riotapi.dtos;

import lombok.Data;

@Data
public class RootDTO {
  private String type;
  private String format;
  private String version;
  private Data data;
}
