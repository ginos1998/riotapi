package riot.riotapi.delegators;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import riot.riotapi.dtos.SummonerDTO;
import riot.riotapi.dtos.match.*;
import riot.riotapi.entities.Spell;
import riot.riotapi.entities.Summoner;
import riot.riotapi.exceptions.ServiceException;
import riot.riotapi.filters.MatchFilter;
import riot.riotapi.services.interfaces.IntChampionService;
import riot.riotapi.services.interfaces.IntMatchApiService;
import riot.riotapi.services.interfaces.IntMatchService;
import riot.riotapi.services.interfaces.IntSpellService;
import riot.riotapi.utils.CommonFunctions;
import riot.riotapi.utils.ConstantsExceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class MatchDelegator {
  private final IntMatchApiService matchApiService;
  private final IntMatchService matchService;
  private final IntChampionService championService;
  private final IntSpellService spellService;
  private final SummonerDelegador summonerDelegador;
  private final ModelMapper mapper;

  @Autowired
  public MatchDelegator(IntMatchApiService matchApiService, IntMatchService matchService, IntChampionService championService,
                        IntSpellService spellService, SummonerDelegador summonerDelegador) {
    this.matchApiService = matchApiService;
    this.summonerDelegador = summonerDelegador;
    this.championService = championService;
    this.spellService = spellService;
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

        if (Boolean.TRUE.equals(saveData)) {
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

  public MatchDTO getCurrentMatchInfo(String sumName) {
    SummonerDTO sum = this.summonerDelegador.getSummonerBy(sumName, null, null, true)
            .stream()
            .findFirst()
            .orElse(null);

    MatchDTO matchDTO = null;
    if (sum != null && sum.getSummonerId() != null) {
      LiveMatchRootDTO liveMatchDTO = matchApiService.getCurrentMatchInfo(sum.getSummonerId());
      matchDTO = createsMatchDto(liveMatchDTO);
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

  private MatchDTO createsMatchDto(LiveMatchRootDTO liveMatchDTO) {
    if (liveMatchDTO == null) {
      throw new ServiceException("Ha ocurrido un error inesperado al obtener los datos de la partida en juego.");
    }
    MatchDTO matchDTO = new MatchDTO();
    matchDTO.setMode(liveMatchDTO.getMode());
    matchDTO.setStartTime(CommonFunctions.getDateAsString(liveMatchDTO.getStartTime()));
    matchDTO.setDuration(CommonFunctions.getDurationMMssAsString(liveMatchDTO.getDuration()));
    matchDTO.setParticipants(getListParticipantsInfo(liveMatchDTO.getParticipants()));

    return matchDTO;
  }
  private List<ParticipantInfoDTO> getListParticipantsInfo(ArrayList<ParticipantDTO> participants) {
    return participants.stream()
            .map(participant -> {
              ParticipantInfoDTO p = mapper.map(participant, ParticipantInfoDTO.class);
              if (participant.getChampionName() == null && participant.getChampionId() != null) {
                p.setChampionName(this.championService.findByKey(participant.getChampionId()).getName());
              }
              if (participant.getSpell1Id() != null && participant.getSpell2Id() != null) {
                List<Integer> spellsIds = Stream.of(p.getSpell1Id(), p.getSpell2Id()).toList();
                List<String> spells = this.spellService.findSpellsByIds(spellsIds)
                        .stream().map(Spell::getSpell).toList();
                p.setSpellName1(spells.get(0));
                p.setSpellName2(spells.get(1));
              }
              return p;
            })
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

  public Mono<MatchDTO> getSummonerLiveMatch(String sumName) {
    return matchApiService.getSummonerLiveMatch(sumName);
  }
}
