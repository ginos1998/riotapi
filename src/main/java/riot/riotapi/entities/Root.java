package riot.riotapi.entities;

import lombok.Data;

@Data
public class Root {
  private String type;
  private String format;
  private String version;
  private Data data;
}
