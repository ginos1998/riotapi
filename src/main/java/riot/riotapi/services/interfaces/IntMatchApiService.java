package riot.riotapi.services.interfaces;

import riot.riotapi.dtos.match.MatchDTO;
import riot.riotapi.dtos.match.MatchesDTO;
import riot.riotapi.entities.Summoner;
import riot.riotapi.filters.MatchFilter;

public interface IntMatchApiService {
  MatchesDTO getSummonerMatchesByPuuid(Summoner summoner, MatchFilter filter);
  MatchDTO getMatchById(String matchId);
}
