package riot.riotapi.dtos;

import lombok.Data;

import java.util.ArrayList;

@Data
public class SpellDTO {
  private String id;
  private String name;
  private String description;
  private String tooltip;
  private LeveltipDTO leveltipDTO;
  private Integer maxrank;
  private ArrayList<Integer> cooldown;
  private String cooldownBurn;
  private ArrayList<Integer> cost;
  private String costBurn;
  private DatavaluesDTO datavaluesDTO;
  private ArrayList<ArrayList<Integer>> effect;
  private ArrayList<String> effectBurn;
  private ArrayList<Object> vars;
  private String costType;
  private String maxammo;
  private ArrayList<Integer> range;
  private String rangeBurn;
  private ImageDTO imageDTO;
  private String resource;
}
