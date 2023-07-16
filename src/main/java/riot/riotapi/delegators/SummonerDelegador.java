package riot.riotapi.delegators;

import riot.riotapi.dtos.SummonerDTO;
import riot.riotapi.entities.Summoner;
import riot.riotapi.services.ServiceFactory;

public class SummonerDelegador {

  private SummonerDelegador() {
    // default constructor
  }

  public static void saveSummoner(Summoner summoner) {
    ServiceFactory.getIntSummonerService().saveSummoner(summoner);
  }

  public static SummonerDTO getSummonerByName(String name) {
    return ServiceFactory.getIntSummonerApiService().getSummonerByName(name);
  }

  public static SummonerDTO getSummonerByAccountId(String accountId) {
    return ServiceFactory.getIntSummonerApiService().getSummonerByAccountId(accountId);
  }

  public static SummonerDTO getSummonerByPuuid(String puuid) {
    return ServiceFactory.getIntSummonerApiService().getSummonerByPuuid(puuid);
  }
}
