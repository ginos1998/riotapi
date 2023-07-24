package riot.riotapi.services.interfaces;

import riot.riotapi.dtos.match.MatchesDTO;
import riot.riotapi.filters.MatchFilter;

import java.util.List;

public interface IntMatchApiService {
  MatchesDTO getMatchesByPuuid(MatchFilter filter);
}
