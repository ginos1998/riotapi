package riot.riotapi.utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

  public static boolean isNotNullOrEmpty(String[] arr) { return arr != null && arr.length > 0; }

  public static String buildURIWithQueryParams(String baseURI, Map<String, String> queryParams) {
    StringBuilder builder = new StringBuilder(baseURI);

    if (!queryParams.isEmpty()) {
      builder.append("?");
      boolean firstParam = true;

      for (Map.Entry<String, String> entry : queryParams.entrySet()) {
        if (!firstParam) {
          builder.append("&");
        }
        String key = entry.getKey();
        String value = entry.getValue();

        builder.append(URLEncoder.encode(key, StandardCharsets.UTF_8));
        builder.append("=");
        builder.append(URLEncoder.encode(value, StandardCharsets.UTF_8));

        firstParam = false;
      }
    }

    return builder.toString();
  }

  public static boolean hasUniqueValue(List list) {
    return isNotNullOrEmpty(list) && list.size() == 1;
  }

  public static Long getCurrentTimeEpochSeconds() {
    return Instant.now().getEpochSecond();
  }
  /**
   * Le resta a la fecha actual la cantidad de días dados.
   * @param days días a restar.
   * @return fecha en segundos en formato Epoch.
   */
  public static Long substractDaysToCurrentDate(int days) {
    Instant now = Instant.now();
    return now.minus(days, ChronoUnit.DAYS).getEpochSecond();
  }

  public static boolean isNullOrNegative(Integer num) {
    return num == null || num < 0;
  }

  public static boolean isNullOrNegative(Long num) {
    return num == null || num < 0;
  }

  public static boolean isNotNullAndPositive(Integer num) {
    return num != null && num > 0;
  }
}
