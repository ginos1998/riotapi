package riot.riotapi.services.interfaces;

import reactor.core.publisher.Mono;
import riot.riotapi.dtos.SummonerDTO;

import java.util.List;

public interface IntSummonerApiService {
  List<SummonerDTO> getSummonerByName(String name);
  List<SummonerDTO> getSummonerByAccountId(String accountId);
  List<SummonerDTO> getSummonerByPuuid(String puuid);
  Mono<List<SummonerDTO>> findMatchSummonersByName(List<String> summonersNames);
}
