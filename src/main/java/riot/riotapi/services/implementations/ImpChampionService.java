package riot.riotapi.services.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import riot.riotapi.dtos.ChampionDTO;
import riot.riotapi.dtos.ChampionDataDTO;
import riot.riotapi.dtos.InfoDTO;
import riot.riotapi.dtos.StatsDTO;
import riot.riotapi.entities.Champion;
import riot.riotapi.entities.ChampionData;
import riot.riotapi.exceptions.ServiceException;
import riot.riotapi.repositories.interfaces.IntPersistenceChampion;
import riot.riotapi.repositories.interfaces.IntPersistenceChampionData;
import riot.riotapi.repositories.interfaces.IntPersistenceInfo;
import riot.riotapi.repositories.interfaces.IntPersistenceStats;
import riot.riotapi.services.interfaces.IntChampionService;
import riot.riotapi.utils.CommonFunctions;
import riot.riotapi.utils.ConstantsExceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImpChampionService implements IntChampionService {

  private final ImpChampionApiService impChampionApiService;
  private final IntPersistenceChampionData intPersistenceChampionData;
  private final IntPersistenceChampion intPersistenceChampion;
  private final IntPersistenceStats intPersistenceStats;
  private final IntPersistenceInfo intPersistenceInfo;
  private final ModelMapper mapper;

  @Autowired
  public ImpChampionService(ImpChampionApiService impChampionApiService, IntPersistenceChampionData intPersistenceChampionData,
                            IntPersistenceChampion intPersistenceChampion, IntPersistenceStats intPersistenceStats,
                            IntPersistenceInfo intPersistenceInfo) {
    this.impChampionApiService = impChampionApiService;
    this.intPersistenceChampionData = intPersistenceChampionData;
    this.intPersistenceChampion = intPersistenceChampion;
    this.intPersistenceStats = intPersistenceStats;
    this.intPersistenceInfo = intPersistenceInfo;
    this.mapper = new ModelMapper();
  }

  @Override
  public ChampionDataDTO getChampionByName(String champName) {

    if (!CommonFunctions.isNotNullOrEmpty(champName)) {
      throw new ServiceException(ConstantsExceptions.ERROR_BAD_INPUT_CHAMP_NAME);
    }

    ChampionDataDTO championDataDTO;

    try {
      String name = "%".concat(champName).concat("%"); // to search with: like "%some name%"

      List<ChampionDTO> champDTOList = intPersistenceChampion.getChampionByName(name)
          .stream()
          .map(c -> mapper.map(c, ChampionDTO.class))
          .toList();

      ChampionData championData = intPersistenceChampionData.getLastChampionData();

      if (CommonFunctions.isNotNullOrEmpty(champDTOList) && championData != null) {
        Map<String, ChampionDTO> map = new HashMap<>();
        for (ChampionDTO champion: champDTOList) {
          InfoDTO infoDTO = findInfoByChampKey(champion.getKey());
          StatsDTO statsDTO = findStatsByChampKey(champion.getKey());

          champion.setInfoDTO(infoDTO);
          champion.setStatsDTO(statsDTO);

          map.put(champion.getName(), champion);
        }

        championDataDTO = mapper.map(championData, ChampionDataDTO.class);
        championDataDTO.setData(map);
      } else {
        championDataDTO = impChampionApiService.getChampionByName(champName);
      }
    } catch (Exception ex) {
      throw new ServiceException(ConstantsExceptions.ERROR_SEARCHING_CHAMPION + champName);
    }

    return championDataDTO;
  }

  @Override
  public ChampionDataDTO getAllChampions() {
    ChampionDataDTO championDataDTO;

    try {

      List<Champion> championList = intPersistenceChampion.findAll();

      if(CommonFunctions.isNotNullOrEmpty(championList)) {

        ChampionData championData = intPersistenceChampionData.getLastChampionData();
        Map<String, ChampionDTO> map = new HashMap<>();

        for (Champion champion: championList) {
          ChampionDTO champDTO = mapper.map(champion, ChampionDTO.class);
          InfoDTO infoDTO = findInfoByChampKey(champDTO.getKey());
          StatsDTO statsDTO = findStatsByChampKey(champDTO.getKey());
          champDTO.setInfoDTO(infoDTO);
          champDTO.setStatsDTO(statsDTO);
          map.put(champion.getName(), champDTO);
        }

        championDataDTO = mapper.map(championData, ChampionDataDTO.class);
        championDataDTO.setData(map);
      } else {
        championDataDTO = impChampionApiService.getAllChampions();
      }

    } catch (Exception ex) {
      throw new ServiceException(ConstantsExceptions.ERROR_SEARCHING_CHAMPIONS);
    }

    return championDataDTO;
  }

  @Override
  public Champion findByKey(Long key) {
    return this.intPersistenceChampion.findByKey(key);
  }

  @Override
  public List<Champion> findByKeyIn(List<Long> champIds) {
    return intPersistenceChampion.findByKeyIn(champIds);
  }

  private InfoDTO findInfoByChampKey(String key) {
    return mapper.map(intPersistenceInfo.findByChampKey(Long.parseLong(key)), InfoDTO.class);
  }

  private StatsDTO findStatsByChampKey (String key) {
    return mapper.map(intPersistenceStats.findByChampKey(Long.parseLong(key)), StatsDTO.class);
  }

}
