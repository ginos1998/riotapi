package riot.riotapi.entities;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Spell {
  private String id;
  private String name;
  private String description;
  private String tooltip;
  private Leveltip leveltip;
  private Integer maxrank;
  private ArrayList<Integer> cooldown;
  private String cooldownBurn;
  private ArrayList<Integer> cost;
  private String costBurn;
  private Datavalues datavalues;
  private ArrayList<ArrayList<Integer>> effect;
  private ArrayList<String> effectBurn;
  private ArrayList<Object> vars;
  private String costType;
  private String maxammo;
  private ArrayList<Integer> range;
  private String rangeBurn;
  private Image image;
  private String resource;
}
