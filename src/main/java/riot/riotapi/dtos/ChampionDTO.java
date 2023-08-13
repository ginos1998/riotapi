package riot.riotapi.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class ChampionDTO {
  @JsonProperty("id")
  private String championId;
  private String key;
  private String name;
  private String title;
  private ImageDTO imageDTO;
  private ArrayList<SkinDTO> skinDTOS;
  private String lore;
  private String blurb;
  private ArrayList<String> allytips;
  private ArrayList<String> enemytips;
  private ArrayList<String> tags;
  private String partype;
  @JsonProperty("info")
  private InfoDTO infoDTO;
  @JsonProperty("stats")
  private StatsDTO statsDTO;
  private ArrayList<SpellDTO> spells;
  private PassiveDTO passiveDTO;
  private ArrayList<Object> recommended;
}
