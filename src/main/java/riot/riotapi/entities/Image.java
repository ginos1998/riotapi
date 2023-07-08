package riot.riotapi.entities;

import lombok.Data;

@Data
public class Image {
  private String full;
  private String sprite;
  private String group;
  private int x;
  private int y;
  private int w;
  private int h;
}
