package riot.riotapi.dtos;

import java.util.Date;
import java.util.Map;

@lombok.Data
public class ChampionDataDTO {
  private String type;
  private String format;
  private String version;
  private Date lastUpdate;
  private Map<String, ChampionDTO> data;
}
