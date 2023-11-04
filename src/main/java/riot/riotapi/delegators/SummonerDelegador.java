package riot.riotapi.delegators;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import riot.riotapi.dtos.SummonerDTO;
import riot.riotapi.dtos.match.LiveMatchRootDTO;
import riot.riotapi.dtos.match.ParticipantDTO;
import riot.riotapi.dtos.match.ParticipantInfoDTO;
import riot.riotapi.dtos.summoner.SummonerChampionMasteryDTO;
import riot.riotapi.dtos.summoner.SummonerStatsDTO;
import riot.riotapi.dtos.summoner.SummonerTierDTO;
import riot.riotapi.entities.Summoner;
import riot.riotapi.exceptions.ServiceException;
import riot.riotapi.services.interfaces.IntMatchApiService;
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
  private final IntMatchApiService intMatchApiService;
  private final ModelMapper mapper;

  @Autowired
  private SummonerDelegador(IntSummonerService intSummonerService, IntSummonerApiService intSummonerApiService,
                            IntMatchApiService intMatchApiService) {
    this.intSummonerService = intSummonerService;
    this.intSummonerApiService = intSummonerApiService;
    this.intMatchApiService = intMatchApiService;
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

  public Mono<SummonerStatsDTO> getSummonerStatsByNameMono(String summonerName) {
    return intSummonerApiService.getSummonerByNameMono(summonerName)
        .flatMap(sum -> {
          Mono<List<SummonerTierDTO>> summonerTierDTOMono = intSummonerApiService.getSummonerTierFlux(sum.getSummonerId());
          Mono<LiveMatchRootDTO> liveMatchRootDTOMono = intMatchApiService.getLiveMatchBySummonerIdMono(sum.getSummonerId());
          Mono<List<SummonerChampionMasteryDTO>> summonerChampionMasteryDTOListMono = intSummonerApiService.getSummonerChampionMasteryDTOListBySummonerPUUIDMono(sum.getPuuid());

          return Mono.zip(summonerTierDTOMono, liveMatchRootDTOMono, summonerChampionMasteryDTOListMono)
              .map(zipped -> {
                List<SummonerTierDTO> summonerTierDTOList = zipped.getT1();
                LiveMatchRootDTO liveMatchRootDTO = zipped.getT2();
                List<SummonerChampionMasteryDTO> summonerChampionMasteryDTOList = zipped.getT3();
                SummonerStatsDTO summonerStatsDTO = new SummonerStatsDTO(sum, summonerTierDTOList, summonerChampionMasteryDTOList);
                summonerStatsDTO.setIsPlaying(liveMatchRootDTO.getMatchId() != null);
                return summonerStatsDTO;
              });
        });
  }




}
