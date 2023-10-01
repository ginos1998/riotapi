package riot.riotapi.delegators;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import riot.riotapi.dtos.SummonerDTO;
import riot.riotapi.dtos.match.ParticipantDTO;
import riot.riotapi.dtos.match.ParticipantInfoDTO;
import riot.riotapi.dtos.summoner.SummonerTierDTO;
import riot.riotapi.entities.Summoner;
import riot.riotapi.exceptions.ServiceException;
import riot.riotapi.services.interfaces.IntSummonerApiService;
import riot.riotapi.services.interfaces.IntSummonerService;
import riot.riotapi.utils.CommonFunctions;
import riot.riotapi.utils.ConstantsExceptions;

import java.util.ArrayList;
import java.util.List;

@Service
public class SummonerDelegador {

  private final IntSummonerService intSummonerService;
  private final IntSummonerApiService intSummonerApiService;
  private final ModelMapper mapper;
  private final Logger logger = LoggerFactory.getLogger(SummonerDelegador.class);

  @Autowired
  private SummonerDelegador(IntSummonerService intSummonerService, IntSummonerApiService intSummonerApiService) {
    this.intSummonerService = intSummonerService;
    this.intSummonerApiService = intSummonerApiService;
    this.mapper = new ModelMapper();
  }

  private List<SummonerDTO> getSummonerByName(String name, boolean saveIfExists) {
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

  private List<SummonerDTO> getSummonerByAccountId(String accountId, boolean saveIfExists) {
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

  private List<SummonerDTO> getSummonerByPuuid(String puuid, boolean saveIfExists) {
    List<SummonerDTO> dtoList;

    try {
      dtoList = intSummonerService.getSummonerByPuuid(puuid);

      if(!CommonFunctions.isNotNullOrEmpty(dtoList)){
        dtoList = this.intSummonerApiService.getSummonerByPuuid(puuid);
        if(CommonFunctions.isNotNullOrEmpty(dtoList) && saveIfExists) {
          this.intSummonerService.saveSummoner(mapper.map(dtoList.get(0), Summoner.class));
        }
      }
    } catch (Exception ex) {
      throw new ServiceException(ConstantsExceptions.ERROR_SEARCHING_SUMMONER.concat(puuid));
    }
    return dtoList;
  }

  public List<SummonerDTO> getSummonerBy(String name, String accountId, String puuid, boolean saveIfExists) {
    List<SummonerDTO> dtoList;

    if (CommonFunctions.isNotNullOrEmpty(name)) {
      dtoList = this.getSummonerByName(name, saveIfExists);
    } else if (CommonFunctions.isNotNullOrEmpty(accountId)) {
      dtoList = this.getSummonerByAccountId(accountId, saveIfExists);
    } else if (CommonFunctions.isNotNullOrEmpty(puuid)) {
      dtoList = this.getSummonerByPuuid(puuid, saveIfExists);
    } else {
      dtoList = new ArrayList<>();
    }

    return dtoList;
  }

  public void saveSummoners(List<ParticipantDTO> participantDTOS) {
    if (CommonFunctions.isNotNullOrEmpty(participantDTOS)) {
      for (ParticipantDTO participant: participantDTOS) {
        Summoner sum = new Summoner();
        sum.setSummonerLevel(participant.getSummonerLevel());
        sum.setSummonerId(participant.getSummonerId());
        sum.setName(participant.getSummonerName());
        sum.setPuuid(participant.getPuuid());
        sum.setProfileIconId(participant.getProfileIcon());
        this.intSummonerService.saveSummoner(sum);
      }
    }
  }

  public void saveSummonerMatch(Long matchId, List<ParticipantDTO> participantDTOList) {
    this.intSummonerService.saveSummonerMatch(matchId, participantDTOList);
  }

  public List<ParticipantInfoDTO> findMatchParticipantsByMatchId(Long matchId) {
    return this.intSummonerService.findMatchParticipantsByMatchId(matchId);
  }

  public Mono<List<SummonerTierDTO>> getSummonerTierFlux(String summonerId, String summonerName) {
    try {
      if (CommonFunctions.isNotNullOrEmpty(summonerId)) {
        return this.intSummonerApiService.getSummonerTierFlux(summonerId);
      } else if (CommonFunctions.isNotNullOrEmpty(summonerName)) {
        return this.intSummonerApiService.getSummonerByNameMono(summonerName)
            .map(SummonerDTO::getSummonerId)
            .flatMap(this.intSummonerApiService::getSummonerTierFlux);
      } else {
        return Mono.empty();
      }
    } catch (Exception e) {
      logger.error("An error has ocurred getting summoner tier with " + (summonerId != null ? summonerId : summonerName)
                    + ": " + e.getMessage());
      throw new ServiceException("An error has ocurred getting summoner tier with " + (summonerId != null ? summonerId : summonerName), e);
    }

  }




}
