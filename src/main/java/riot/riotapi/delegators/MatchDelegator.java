package riot.riotapi.delegators;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import riot.riotapi.dtos.match.*;
import riot.riotapi.entities.Summoner;
import riot.riotapi.exceptions.ServiceException;
import riot.riotapi.filters.MatchFilter;
import riot.riotapi.services.interfaces.IntMatchApiService;
import riot.riotapi.services.interfaces.IntMatchService;
import riot.riotapi.utils.CommonFunctions;
import riot.riotapi.utils.ConstantsExceptions;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatchDelegator {
  private final IntMatchApiService matchApiService;
  private final IntMatchService matchService;
  private final SummonerDelegador summonerDelegador;
  private final ModelMapper mapper;

  @Autowired
  public MatchDelegator(IntMatchApiService matchApiService, IntMatchService matchService, SummonerDelegador summonerDelegador) {
    this.matchApiService = matchApiService;
    this.summonerDelegador = summonerDelegador;
    this.matchService = matchService;
    this.mapper = new ModelMapper();
  }

  public MatchesDTO getSummonerMatchesByPuuid(String puuid, MatchFilter filter) {
    return matchApiService.getSummonerMatchesByPuuid(findSummoner(null, puuid), filter);
  }

  public MatchesDTO getMatchesBySummonerName(String sumName, MatchFilter filter) {
    return matchApiService.getSummonerMatchesByPuuid(findSummoner(sumName, null), filter);
  }

  public MatchDTO getMatchById(String matchId, Boolean saveData) {
    MatchDTO matchDTO;
    try {
      matchDTO = findMatchIfExists(matchId);

      if (matchDTO == null) {
        MatchRootDTO rootDTO = matchApiService.getMatchById(matchId);
        matchDTO = createsMatchDto(rootDTO.getInfo());

        if (saveData) {
          this.matchService.saveMatch(rootDTO.getInfo());
          this.summonerDelegador.saveSummoners(rootDTO.getInfo().getParticipants());
          this.summonerDelegador.saveSummonerMatch(rootDTO.getInfo().getMatchId(), rootDTO.getInfo().getParticipants());
        }
      }

    } catch (Exception ex) {
      throw new ServiceException("Ha ocurrido un error inesperado al buscar la partida solicitada.\n".concat(ex.getMessage()));
    }

    return matchDTO;
  }

  private MatchDTO findMatchIfExists(String matchId){
    String[] aux = matchId.split("_");

    if (aux.length == 2 && CommonFunctions.containsOnlyNumbers(aux[1])) {
      MatchDTO matchDTO = matchService.findMatchDTOByMatchId(Long.valueOf(aux[1]));
      if (matchDTO != null) {
        List<ParticipantInfoDTO> part = summonerDelegador.findMatchParticipantsByMatchId(Long.valueOf(aux[1]));
        matchDTO.setParticipants(part);
        return matchDTO;
      }
    }
    return null;
  }

  private MatchDTO createsMatchDto(MatchInfoDTO infoDTO) {
    if (infoDTO == null) {
      throw new ServiceException("Ha ocurrido un error inesperado al obtener los datos de la partida.");
    }
    MatchDTO matchDTO = new MatchDTO();
    matchDTO.setMode(infoDTO.getMode());
    matchDTO.setGameVersion(infoDTO.getVersion());
    matchDTO.setStartTime(CommonFunctions.getDateAsString(infoDTO.getStartTime()));
    matchDTO.setEndTime(CommonFunctions.getDateAsString(infoDTO.getEndTime()));
    matchDTO.setDuration(CommonFunctions.getDurationMMssAsString(infoDTO.getDuration()));
    matchDTO.setCreation(CommonFunctions.getDateAsString(infoDTO.getCreation()));
    matchDTO.setParticipants(getListParticipantsInfo(infoDTO.getParticipants()));

    return matchDTO;
  }
  private List<ParticipantInfoDTO> getListParticipantsInfo(ArrayList<ParticipantDTO> participants) {
    return participants.stream()
            .map(participant -> mapper.map(participant, ParticipantInfoDTO.class))
            .toList();
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
