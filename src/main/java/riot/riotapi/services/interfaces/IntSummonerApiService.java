package riot.riotapi.services.interfaces;

import riot.riotapi.dtos.SummonerDTO;

import java.util.List;

public interface IntSummonerApiService {
  List<SummonerDTO> getSummonerByName(String name);
  List<SummonerDTO> getSummonerByAccountId(String accountId);
  List<SummonerDTO> getSummonerByPuuid(String puuid);
}
