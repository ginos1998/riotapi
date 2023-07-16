package riot.riotapi.services.interfaces;

import riot.riotapi.dtos.SummonerDTO;

public interface IntSummonerApiService {
  SummonerDTO getSummonerByName(String name);
  SummonerDTO getSummonerByAccountId(String accountId);
  SummonerDTO getSummonerByPuuid(String puuid);
}
