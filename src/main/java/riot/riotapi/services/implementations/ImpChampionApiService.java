package riot.riotapi.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import riot.riotapi.dtos.match.ParticipantDTO;
import riot.riotapi.entities.Champion;
import riot.riotapi.entities.ChampionData;
import riot.riotapi.entities.Info;
import riot.riotapi.entities.Stats;
import riot.riotapi.exceptions.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import riot.riotapi.dtos.ChampionDTO;
import riot.riotapi.dtos.ChampionDataDTO;
import riot.riotapi.repositories.interfaces.IntPersistenceChampion;
import riot.riotapi.repositories.interfaces.IntPersistenceChampionData;
import riot.riotapi.repositories.interfaces.IntPersistenceInfo;
import riot.riotapi.repositories.interfaces.IntPersistenceStats;
import riot.riotapi.services.interfaces.IntChampionApiService;
import riot.riotapi.utils.CommonFunctions;
import riot.riotapi.utils.ConstantsExceptions;
import riot.riotapi.utils.URIs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ImpChampionApiService implements IntChampionApiService {

  private final WebClient webClient;
  private final IntPersistenceChampionData intPersistenceChampionData;
  private final IntPersistenceChampion intPersistenceChampion;
  private final IntPersistenceStats intPersistenceStats;
  private final IntPersistenceInfo intPersistenceInfo;
  private final RestTemplate restTemplate;

  @Autowired
  private ImpChampionApiService(IntPersistenceChampionData intPersistenceChampionData, IntPersistenceChampion intPersistenceChampion,
                                IntPersistenceStats intPersistenceStats, IntPersistenceInfo intPersistenceInfo) {

    this.intPersistenceChampionData = intPersistenceChampionData;
    this.intPersistenceChampion = intPersistenceChampion;
    this.intPersistenceStats = intPersistenceStats;
    this.intPersistenceInfo = intPersistenceInfo;
    this.restTemplate = new RestTemplate();
    this.webClient = WebClient.create();

  }

  @Override
  public ChampionDataDTO getChampionByName(String champName) {

    if (!CommonFunctions.isNotNullOrEmpty(champName)) {
      throw new ServiceException(ConstantsExceptions.ERROR_BAD_INPUT_CHAMP_NAME);
    }

    ChampionDataDTO championDataDTO;

    try {
      String uri = URIs.URI_LOL_CHAMPION.replace("###", champName);
      championDataDTO = restTemplate.getForObject(uri, ChampionDataDTO.class);
    } catch (HttpClientErrorException ex) {
      log.info(ConstantsExceptions.ERROR_SEARCHING_CHAMPION + champName);
      throw new ServiceException(ConstantsExceptions.ERROR_SEARCHING_CHAMPION + champName, ex);
    }

    return championDataDTO;
  }

  @Override
  public ChampionDataDTO getAllChampions() {
    ChampionDataDTO championDataDTO;

    try {
      championDataDTO = restTemplate.getForObject(URIs.URI_ALL_LOL_CHAMPIONS, ChampionDataDTO.class);
    } catch (HttpClientErrorException ex) {
      log.info(ConstantsExceptions.ERROR_SEARCHING_CHAMPIONS);
      throw new ServiceException(ConstantsExceptions.ERROR_SEARCHING_CHAMPIONS, ex);
    }

    return championDataDTO;
  }

  @Override
  public String importAllChampions() throws ServiceException {

    ChampionDataDTO cd = getAllChampions();

    if (cd.getData() == null) {
      throw new ServiceException(ConstantsExceptions.ERROR_IMPORTING_CHAMPIONS);
    }

    String response;

    try {

      ModelMapper mapper = new ModelMapper();

      ChampionData championData = mapper.map(cd, ChampionData.class);

      List<ChampionData> champDataList = this.intPersistenceChampionData.findAll();
      if (CommonFunctions.isNotNullOrEmpty(champDataList)) {
        for (ChampionData data: champDataList) {
          if (data.getVersion().equals(championData.getVersion())) {
            data.setLastUpdate(new Date());
            intPersistenceChampionData.save(data);
            championData = data;
            break;
          }
        }
      } else {
        championData.setLastUpdate(new Date());
        this.intPersistenceChampionData.save(championData);
      }

      response = mapChampionsData(cd.getData(), championData);

    } catch (Exception ex) {
      log.info(ConstantsExceptions.ERROR_IMPORTING_CHAMPIONS);
      throw new ServiceException(ConstantsExceptions.ERROR_IMPORTING_CHAMPIONS, ex);
    }


    return response;
  }

  private String mapChampionsData(Map<String, ChampionDTO> champs, ChampionData championData) {
    if (CommonFunctions.isNotNullOrEmpty(champs) && championData != null) {
      ModelMapper mapper = new ModelMapper();
      for (ChampionDTO champ: champs.values()) {
        Champion champion = mapper.map(champ, Champion.class);
        champion.setChampData(championData);
        this.intPersistenceChampion.save(champion);

        Info info = mapper.map(champ.getInfoDTO(), Info.class);
        info.setKey(champion.getKey());
        this.intPersistenceInfo.save(info);

        Stats stats = mapper.map(champ.getStatsDTO(), Stats.class);
        stats.setKey(champion.getKey());
        this.intPersistenceStats.save(stats);
      }
    } else {
      return "ERROR";
    }

    return "OK";
  }

  /**
   * Calls riotapi-db to get a flux data of ChampionDTO, with championId as path variable
   * @param championIdList a list of champion's ids
   * @return flux data of ChampionDTO
   */
  public Flux<ChampionDTO> getChampionByIdFlux(List<Long> championIdList) {
    Flux<Long> championIdFlux = Flux.fromIterable(championIdList);
    return championIdFlux.flatMapSequential(championId ->
        webClient.get()
            .uri(URIs.URI_RIOT_API_GATEWAY.concat("/champion/").concat(String.valueOf(championId)))
            .retrieve()
            .bodyToMono(ChampionDTO.class)
            .onErrorResume(err -> {
              log.error("An error has occurred while getting champion from RiotApi Gateway. Error: {}", err.getMessage());
              return Mono.just(new ChampionDTO());
            })
        );
  }

  /**
   * Calls riotapi-db to get a flux data of ChampionDTO,
   * with championId as path variable and a list of spell's ids as query param
   * @param participants match participants
   * @return flux data of ChampionDTO
   */
  public Flux<ChampionDTO> getChampionByMatchParticipants(List<ParticipantDTO> participants) {
    List<String> dynamicUrls = new ArrayList<>();
    participants.forEach(participantDTO -> {
      String url = participantDTO.getChampionId() + String.format("?spellIds=%s,%s", participantDTO.getSpell1Id(), participantDTO.getSpell2Id());
      dynamicUrls.add(url);
    });
    Flux<String> dynamicUrlsFlux = Flux.fromIterable(dynamicUrls);
    return dynamicUrlsFlux.flatMapSequential(dynamicUrl ->
        webClient.get()
            .uri(URIs.URI_RIOT_API_GATEWAY.concat("/champion/").concat(String.valueOf(dynamicUrl)))
            .retrieve()
            .bodyToMono(ChampionDTO.class)
            .onErrorResume(err -> {
              log.error("An error has occurred while getting champion from RiotApi Gateway. Error: {}", err.getMessage());
              return Mono.just(new ChampionDTO());
            })
    );
  }

}
