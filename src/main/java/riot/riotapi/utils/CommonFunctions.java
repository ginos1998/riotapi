package riot.riotapi.utils;

import java.util.List;
import java.util.Map;

public class CommonFunctions {

  private CommonFunctions() {
    // default
  }
  public static boolean isNotNullOrEmpty(Map map) {
    return map != null && !map.isEmpty();
  }

  public static boolean isNotNullOrEmpty(String str) {
    return str != null && !str.isEmpty();
  }

  public static boolean isNotNullOrEmpty(List list) {
    return list != null && !list.isEmpty();
  }
}
