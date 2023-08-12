package riot.riotapi.services.interfaces;

import riot.riotapi.dtos.SummonerDTO;
import riot.riotapi.entities.Summoner;

import java.util.List;
import java.util.Optional;

public interface IntSummonerService {
  void saveSummoner(Summoner summoner);

  List<SummonerDTO> getSummonerByName(String name);
  List<SummonerDTO> getSummonerByAccountId(String accountId);
  List<SummonerDTO> getSummonerByPuuid(String puuid);

  Optional<Summoner> findByNameContainingIgnoreCase(String name);
  void saveSummonerMatch(Long matchId, List<String> listSummonerPuuid);
}
