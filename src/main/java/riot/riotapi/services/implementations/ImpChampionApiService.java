package riot.riotapi.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import riot.riotapi.dtos.mappers.imp.ChampionMapper;
import riot.riotapi.entities.Champion;
import riot.riotapi.entities.ChampionData;
import riot.riotapi.entities.Info;
import riot.riotapi.entities.Stats;
import riot.riotapi.exceptions.ServiceFactoryException;
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
import riot.riotapi.utils.URIs;

import java.util.Date;
import java.util.Map;

@Service
public class ImpChampionApiService implements IntChampionApiService {

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
    restTemplate = new RestTemplate();
  }

  @Override
  public ChampionDataDTO getChampionByName(String champName) {
    String uri = URIs.URI_LOL_CHAMPION.replace("###", champName);
    return restTemplate.getForObject(uri, ChampionDataDTO.class);
  }

  @Override
  public ChampionDataDTO getAllChampions() {
    return restTemplate.getForObject(URIs.URI_ALL_LOL_CHAMPIONS, ChampionDataDTO.class);
  }

  @Override
  public String importAllChampions() throws ServiceFactoryException {
    String response;

    ChampionDataDTO cd = getAllChampions();
    cd.setLastUpdate(new Date());

    if (cd.getData() == null) {
      throw new ServiceFactoryException("ERROR");
    }

    ChampionMapper mapper = new ChampionMapper();

    ChampionData championData = mapper.toChampionData(cd);
    this.intPersistenceChampionData.save(championData);

    response = mapChampionsData(cd.getData(), championData);

    return response;
  }

  private String mapChampionsData(Map<String, ChampionDTO> champs, ChampionData championData) {
    ChampionMapper mapper;
    if (CommonFunctions.isNotNullOrEmpty(champs) && championData != null) {
      mapper = new ChampionMapper();
      for (ChampionDTO champ: champs.values()) {
        Champion champion = mapper.toChampion(champ);
        champion.setChampData(championData);
        this.intPersistenceChampion.save(champion);

        Info info = mapper.toInfo(champ.getInfoDTO());
        info.setChampion(champion);
        this.intPersistenceInfo.save(info);

        Stats stats = mapper.toStats(champ.getStatsDTO());
        stats.setChampion(champion);
        this.intPersistenceStats.save(stats);
      }
    } else {
      return "ERROR";
    }

    return "OK";
  }
}
