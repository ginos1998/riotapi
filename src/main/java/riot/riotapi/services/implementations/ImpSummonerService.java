package riot.riotapi.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import riot.riotapi.entities.Summoner;
import riot.riotapi.repositories.interfaces.IntPersistenceSummoner;
import riot.riotapi.services.interfaces.IntSummonerService;

@Service
public class ImpSummonerService implements IntSummonerService {

  private final IntPersistenceSummoner intPersistenceSummoner;
  @Autowired
  public ImpSummonerService(IntPersistenceSummoner intPersistenceSummoner) {
    this.intPersistenceSummoner = intPersistenceSummoner;
  }
  @Override
  public void saveSummoner(Summoner summoner) {
    this.intPersistenceSummoner.save(summoner);
  }
}
