package riot.riotapi.delegators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import riot.riotapi.dtos.match.MatchesDTO;
import riot.riotapi.filters.MatchFilter;
import riot.riotapi.services.interfaces.IntMatchApiService;

@Service
public class MatchDelegator {
  private final IntMatchApiService matchApiService;

  @Autowired
  public MatchDelegator(IntMatchApiService matchApiService) {
    this.matchApiService = matchApiService;
  }

  public MatchesDTO getSummonerMatchesByPuuid(MatchFilter filter) {

    return matchApiService.getSummonerMatchesByPuuid(filter);
  }
}
