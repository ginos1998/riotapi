package riot.riotapi.services.interfaces;

import riot.riotapi.entities.Summoner;

public interface IntSummonerApiService {
  Summoner getSummonerByName(String name);
  Summoner getSummonerByAccountId(String accountId);
  Summoner getSummonerByPuuid(String puuid);
}
