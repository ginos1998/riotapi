package riot.riotapi.services;

import riot.riotapi.services.interfaces.IntSummonerService;

public class ServiceFactory {
  private static IntSummonerService intSummonerService;

  private ServiceFactory() {
    // default
  }

  public static IntSummonerService getIntSummonerService() {
    return intSummonerService;
  }

  public static void setIntSummonerService(IntSummonerService intSummonerService) {
    ServiceFactory.intSummonerService = intSummonerService;
  }
}
