package riot.riotapi.services.interfaces;

import reactor.core.publisher.Mono;
import riot.riotapi.dtos.match.LiveMatchRootDTO;
import riot.riotapi.dtos.match.MatchDTO;
import riot.riotapi.dtos.match.MatchRootDTO;
import riot.riotapi.dtos.match.MatchesDTO;
import riot.riotapi.entities.Summoner;
import riot.riotapi.filters.MatchFilter;

public interface IntMatchApiService {
  MatchesDTO getSummonerMatchesByPuuid(Summoner summoner, MatchFilter filter);
  MatchRootDTO getMatchById(String matchId);
  LiveMatchRootDTO getCurrentMatchInfo(String summonerId);
  Mono<MatchDTO> getSummonerLiveMatch(String sumName);
}
