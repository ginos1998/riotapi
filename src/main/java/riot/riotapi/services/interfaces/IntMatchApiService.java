package riot.riotapi.services.interfaces;

import riot.riotapi.dtos.match.MatchesDTO;
import riot.riotapi.filters.MatchFilter;

public interface IntMatchApiService {
  MatchesDTO getSummonerMatchesByPuuid(MatchFilter filter);
}
