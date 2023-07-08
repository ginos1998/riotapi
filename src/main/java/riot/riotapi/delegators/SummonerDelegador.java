package riot.riotapi.delegators;

import org.springframework.web.bind.annotation.PathVariable;
import riot.riotapi.entities.Summoner;
import riot.riotapi.services.ServiceFactory;

public class SummonerDelegador {

  private SummonerDelegador() {
    // default constructor
  }

  public static void saveSummoner(Summoner summoner) {
    ServiceFactory.getIntSummonerService().saveSummoner(summoner);
  }

  public static Summoner getSummonerByName(String name) {
    return ServiceFactory.getIntSummonerApiService().getSummonerByName(name);
  }

  public static Summoner getSummonerByAccountId(String accountId) {
    return ServiceFactory.getIntSummonerApiService().getSummonerByAccountId(accountId);
  }

  public static Summoner getSummonerByPuuid(String puuid) {
    return ServiceFactory.getIntSummonerApiService().getSummonerByPuuid(puuid);
  }
}
