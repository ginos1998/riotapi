package riot.riotapi.services.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import riot.riotapi.dtos.SummonerDTO;
import riot.riotapi.entities.Summoner;
import riot.riotapi.exceptions.ServiceException;
import riot.riotapi.repositories.interfaces.IntPersistenceSummoner;
import riot.riotapi.services.interfaces.IntSummonerService;
import riot.riotapi.utils.CommonFunctions;
import riot.riotapi.utils.ConstantsExceptions;

import java.util.List;

@Service
public class ImpSummonerService implements IntSummonerService {

  private final IntPersistenceSummoner intPersistenceSummoner;
  private final ModelMapper mapper;
  @Autowired
  public ImpSummonerService(IntPersistenceSummoner intPersistenceSummoner) {
    this.intPersistenceSummoner = intPersistenceSummoner;
    this.mapper = new ModelMapper();
  }
  @Override
  public void saveSummoner(Summoner summoner) {
    this.intPersistenceSummoner.save(summoner);
  }

  @Override
  public List<SummonerDTO> getSummonerByName(String name) {
    if (!CommonFunctions.isNotNullOrEmpty(name)) {
      throw new ServiceException(ConstantsExceptions.ERROR_BAD_INPUT_SUM_NAME);
    }

    List<SummonerDTO> dtoList;

    try {
      String sumName = "%".concat(name).concat("%");
      dtoList = intPersistenceSummoner.getSummonerByName(sumName)
          .stream()
          .map(s -> mapper.map(s, SummonerDTO.class))
          .toList();
    } catch (Exception ex) {
      throw new ServiceException(ConstantsExceptions.ERROR_SEARCHING_SUMMONER.concat(name));
    }

    return dtoList;
  }

  @Override
  public List<SummonerDTO> getSummonerByAccountId(String accountId) {

    List<SummonerDTO> sumDtoList;

    try {
      validateInput(accountId);
      String id = "%".concat(accountId).concat("%");
      sumDtoList = intPersistenceSummoner.getSummonerByAccountId(id)
          .stream()
          .map(s -> mapper.map(s, SummonerDTO.class))
          .toList();
    } catch (Exception ex) {
      throw new ServiceException(ConstantsExceptions.ERROR_SEARCHING_SUMMONER.concat(accountId));
    }

    return sumDtoList;
  }

  @Override
  public List<SummonerDTO> getSummonerByPuuid(String puuid) {
    List<SummonerDTO> sumDtoList;

    try {
      validateInput(puuid);
      String id = "%".concat(puuid).concat("%");
      sumDtoList = intPersistenceSummoner.getSummonerByPuuid(id)
          .stream()
          .map(s -> mapper.map(s, SummonerDTO.class))
          .toList();
    } catch (Exception ex) {
      throw new ServiceException(ConstantsExceptions.ERROR_SEARCHING_SUMMONER.concat(puuid));
    }

    return sumDtoList;
  }

  private void validateInput(String input) {
    if (!CommonFunctions.isNotNullOrEmpty(input)) {
      throw new ServiceException(ConstantsExceptions.ERROR_BAD_INPUT_SUM_NAME);
    }
  }
}
