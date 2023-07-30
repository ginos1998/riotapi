package riot.riotapi.delegators;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import riot.riotapi.dtos.SummonerDTO;
import riot.riotapi.entities.Summoner;
import riot.riotapi.exceptions.ServiceException;
import riot.riotapi.services.interfaces.IntSummonerApiService;
import riot.riotapi.services.interfaces.IntSummonerService;
import riot.riotapi.utils.CommonFunctions;
import riot.riotapi.utils.ConstantsExceptions;

import java.util.List;

@Service
public class SummonerDelegador {

  private final IntSummonerService intSummonerService;
  private final IntSummonerApiService intSummonerApiService;
  private final ModelMapper mapper;

  @Autowired
  private SummonerDelegador(IntSummonerService intSummonerService, IntSummonerApiService intSummonerApiService) {
    this.intSummonerService = intSummonerService;
    this.intSummonerApiService = intSummonerApiService;
    this.mapper = new ModelMapper();
  }

  public void saveSummoner(Summoner summoner) {
    this.intSummonerService.saveSummoner(summoner);
  }

  public List<SummonerDTO> getSummonerByName(String name, boolean saveIfExists) {
    List<SummonerDTO> sumDTOList;

    try {
      sumDTOList = this.intSummonerService.getSummonerByName(name);

      if (!CommonFunctions.isNotNullOrEmpty(sumDTOList)) {
        sumDTOList = this.intSummonerApiService.getSummonerByName(name);
        if (CommonFunctions.isNotNullOrEmpty(sumDTOList) && saveIfExists) {
          this.intSummonerService.saveSummoner(mapper.map(sumDTOList.get(0), Summoner.class));
        }
      }
    } catch (Exception ex) {
      throw new ServiceException(ConstantsExceptions.ERROR_SEARCHING_SUMMONER.concat(name));
    }

    return sumDTOList;
  }

  public List<SummonerDTO> getSummonerByAccountId(String accountId, boolean saveIfExists) {
    List<SummonerDTO> sum;

    try {
      sum = intSummonerService.getSummonerByAccountId(accountId);

      if(!CommonFunctions.isNotNullOrEmpty(sum)){
        sum = this.intSummonerApiService.getSummonerByAccountId(accountId);
        if(CommonFunctions.isNotNullOrEmpty(sum) && saveIfExists) {
          this.intSummonerService.saveSummoner(mapper.map(sum.get(0), Summoner.class));
        }
      }
    } catch (Exception ex) {
      throw new ServiceException(ConstantsExceptions.ERROR_SEARCHING_SUMMONER.concat(accountId));
    }
    return sum;
  }

  public SummonerDTO getSummonerByPuuid(String puuid) {
    return this.intSummonerApiService.getSummonerByPuuid(puuid);
  }
}
