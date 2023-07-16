package riot.riotapi.services.implementations;

import org.springframework.stereotype.Service;
import riot.riotapi.dtos.SummonerDTO;
import riot.riotapi.entities.Summoner;
import riot.riotapi.repositories.factories.PersistenceFactory;
import riot.riotapi.services.interfaces.IntSummonerService;

@Service
public class ImpSummonerService implements IntSummonerService {
  @Override
  public void saveSummoner(Summoner summoner) {
    PersistenceFactory.getIntPersistenceSummoner().save(summoner);
  }
}
