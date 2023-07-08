package riot.riotapi.entities;

import java.util.Map;

@lombok.Data
public class ChampionData {
  private String type;
  private String format;
  private String version;
  private Map<String, Champion> data;
}
