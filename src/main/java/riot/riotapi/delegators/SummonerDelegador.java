package riot.riotapi.delegators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import riot.riotapi.dtos.SummonerDTO;
import riot.riotapi.entities.Summoner;
import riot.riotapi.services.interfaces.IntSummonerApiService;
import riot.riotapi.services.interfaces.IntSummonerService;

@Service
public class SummonerDelegador {

  private final IntSummonerService intSummonerService;
  private final IntSummonerApiService intSummonerApiService;

  @Autowired
  private SummonerDelegador(IntSummonerService intSummonerService, IntSummonerApiService intSummonerApiService) {
    this.intSummonerService = intSummonerService;
    this.intSummonerApiService = intSummonerApiService;
  }

  public void saveSummoner(Summoner summoner) {
    this.intSummonerService.saveSummoner(summoner);
  }

  public SummonerDTO getSummonerByName(String name) {
    return this.intSummonerApiService.getSummonerByName(name);
  }

  public SummonerDTO getSummonerByAccountId(String accountId) {
    return this.intSummonerApiService.getSummonerByAccountId(accountId);
  }

  public SummonerDTO getSummonerByPuuid(String puuid) {
    return this.intSummonerApiService.getSummonerByPuuid(puuid);
  }
}
