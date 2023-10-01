package riot.riotapi.services.interfaces;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import riot.riotapi.dtos.SummonerDTO;
import riot.riotapi.dtos.summoner.SummonerTierDTO;

import java.util.List;

public interface IntSummonerApiService {
  List<SummonerDTO> getSummonerByName(String name);
  List<SummonerDTO> getSummonerByAccountId(String accountId);
  List<SummonerDTO> getSummonerByPuuid(String puuid);
  Flux<SummonerDTO> findMatchSummonersByName(List<String> summonersNames);
  Mono<SummonerDTO> getSummonerByNameMono(String sumName);
  Flux<SummonerTierDTO> getSummonerTierFlux(String summonerId);
}
