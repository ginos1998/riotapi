package riot.riotapi.entities;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Champion {
  private String id;
  private String key;
  private String name;
  private String title;
  private Image image;
  private ArrayList<Skin> skins;
  private String lore;
  private String blurb;
  private ArrayList<String> allytips;
  private ArrayList<String> enemytips;
  private ArrayList<String> tags;
  private String partype;
  private Info info;
  private Stats stats;
  private ArrayList<Spell> spells;
  private Passive passive;
  private ArrayList<Object> recommended;
}
