package riot.riotapi.delegators;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import riot.riotapi.dtos.match.MatchesDTO;
import riot.riotapi.entities.Summoner;
import riot.riotapi.exceptions.ServiceException;
import riot.riotapi.filters.MatchFilter;
import riot.riotapi.services.interfaces.IntMatchApiService;
import riot.riotapi.utils.CommonFunctions;
import riot.riotapi.utils.ConstantsExceptions;

import java.util.List;

@Service
public class MatchDelegator {
  private final IntMatchApiService matchApiService;
  private final SummonerDelegador summonerDelegador;
  private final ModelMapper mapper;

  @Autowired
  public MatchDelegator(IntMatchApiService matchApiService, SummonerDelegador summonerDelegador) {
    this.matchApiService = matchApiService;
    this.summonerDelegador = summonerDelegador;
    this.mapper = new ModelMapper();
  }

  public MatchesDTO getSummonerMatchesByPuuid(String puuid, MatchFilter filter) {
    return matchApiService.getSummonerMatchesByPuuid(findSummoner(null, puuid), filter);
  }

  public MatchesDTO getMatchesBySummonerName(String sumName, MatchFilter filter) {
    return matchApiService.getSummonerMatchesByPuuid(findSummoner(sumName, null), filter);
  }

  private Summoner findSummoner(String name, String puuid) {
    List<Summoner> sumList = summonerDelegador.getSummonerBy(name, null, puuid, true)
            .stream()
            .map(s -> mapper.map(s, Summoner.class))
            .toList();

    if (!CommonFunctions.hasUniqueValue(sumList)) {
      throw new ServiceException(ConstantsExceptions.ERROR_SEARCHING_CHAMPION.concat(name));
    }
    return sumList.get(0);
  }

}
