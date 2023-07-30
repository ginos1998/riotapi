package riot.riotapi.services.interfaces;

import riot.riotapi.dtos.SummonerDTO;
import riot.riotapi.entities.Summoner;

import java.util.List;

public interface IntSummonerService {
  void saveSummoner(Summoner summoner);

  List<SummonerDTO> getSummonerByName(String name);
  List<SummonerDTO> getSummonerByAccountId(String accountId);
}
