package riot.riotapi.services.interfaces;

import reactor.core.publisher.Flux;
import riot.riotapi.dtos.ChampionDTO;
import riot.riotapi.exceptions.ServiceException;
import riot.riotapi.dtos.ChampionDataDTO;

import java.util.List;

public interface IntChampionApiService {

  ChampionDataDTO getChampionByName(String champName) throws ServiceException;

  ChampionDataDTO getAllChampions();

  String importAllChampions() throws ServiceException;

  Flux<ChampionDTO> getChampionByIdFlux(List<Long> championIdList);
}
