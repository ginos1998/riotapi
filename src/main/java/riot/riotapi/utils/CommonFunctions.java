package riot.riotapi.utils;

import riot.riotapi.dtos.ChampionDTO;

import java.util.Map;

public class CommonFunctions {

  private CommonFunctions() {
    // default
  }
  public static boolean isNotNullOrEmpty(Map<String, ChampionDTO> map) {
    return map != null && !map.isEmpty();
  }
}
