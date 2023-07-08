package riot.riotapi.services;

import riot.riotapi.services.interfaces.IntChampionApiService;
import riot.riotapi.services.interfaces.IntSummonerApiService;
import riot.riotapi.services.interfaces.IntSummonerService;

public class ServiceFactory {
  private static IntSummonerService intSummonerService;
  private static IntSummonerApiService intSummonerApiService;

  private static IntChampionApiService intChampionApiService;

  private ServiceFactory() {
    // default
  }

  public static IntSummonerService getIntSummonerService() {
    return intSummonerService;
  }

  public static void setIntSummonerService(IntSummonerService intSummonerService) {
    ServiceFactory.intSummonerService = intSummonerService;
  }

  public static IntSummonerApiService getIntSummonerApiService() {
    return intSummonerApiService;
  }

  public static void setIntSummonerApiService(IntSummonerApiService intSummonerApiService) {
    ServiceFactory.intSummonerApiService = intSummonerApiService;
  }

  public static IntChampionApiService getIntChampionApiService() {
    return intChampionApiService;
  }

  public static void setIntChampionApiService(IntChampionApiService intChampionApiService) {
    ServiceFactory.intChampionApiService = intChampionApiService;
  }
}
