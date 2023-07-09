package riot.riotapi.utils;

import riot.riotapi.entities.Champion;

import java.util.Map;

public class CommonFunctions {

  private CommonFunctions() {
    // default
  }
  public static boolean isNotNullOrEmpty(Map<String, Champion> map) {
    return map != null && !map.isEmpty();
  }
}
