package riot.riotapi.services.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import riot.riotapi.dtos.SummonerDTO;
import riot.riotapi.dtos.match.ParticipantDTO;
import riot.riotapi.dtos.match.ParticipantInfoDTO;
import riot.riotapi.entities.Champion;
import riot.riotapi.entities.Summoner;
import riot.riotapi.entities.match.Match;
import riot.riotapi.entities.match.SummonerMatch;
import riot.riotapi.exceptions.ServiceException;
import riot.riotapi.repositories.interfaces.IntPersistenceSummoner;
import riot.riotapi.repositories.interfaces.IntPersistenceSummonerMatch;
import riot.riotapi.services.interfaces.IntSummonerService;
import riot.riotapi.utils.CommonFunctions;
import riot.riotapi.utils.ConstantsExceptions;

import java.util.List;
import java.util.Optional;

@Service
public class ImpSummonerService implements IntSummonerService {

  private final IntPersistenceSummoner intPersistenceSummoner;
  private final IntPersistenceSummonerMatch persistenceSummonerMatch;
  private final ModelMapper mapper;
  @Autowired
  public ImpSummonerService(IntPersistenceSummoner intPersistenceSummoner, IntPersistenceSummonerMatch persistenceSummonerMatch) {
    this.intPersistenceSummoner = intPersistenceSummoner;
    this.persistenceSummonerMatch = persistenceSummonerMatch;
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

  @Override
  public Optional<Summoner> findByNameContainingIgnoreCase(String name) {
    return intPersistenceSummoner.findByNameContainingIgnoreCase(name);
  }

  @Override
  public void saveSummonerMatch(Long matchId, List<ParticipantDTO> participantDTOList) {
    if (CommonFunctions.isNotNullOrEmpty(participantDTOList)) {
      List<Summoner> summonerList = participantDTOList.stream()
              .map(sum -> new Summoner(sum.getSummonerId()))
              .toList();
      Match match = new Match(matchId);

      List<SummonerMatch> sumMatchList = participantDTOList.stream()
              .map(aux -> new SummonerMatch(match, summonerList.stream()
                      .filter(sum -> sum.getSummonerId().equals(aux.getSummonerId()))
                      .findFirst()
                      .orElseThrow(),
                      aux.getLane(), aux.getWin(), new Champion(aux.getChampionId()), aux.getTeamId()))
              .toList();

      this.persistenceSummonerMatch.saveAll(sumMatchList);
    }
  }

  @Override
  public List<ParticipantInfoDTO> findMatchParticipantsByMatchId(Long matchId) {
    return this.persistenceSummonerMatch.findMatchParticipantsByMatchId(matchId);
  }

  private void validateInput(String input) {
    if (!CommonFunctions.isNotNullOrEmpty(input)) {
      throw new ServiceException(ConstantsExceptions.ERROR_BAD_INPUT_SUM_NAME);
    }
  }
}
