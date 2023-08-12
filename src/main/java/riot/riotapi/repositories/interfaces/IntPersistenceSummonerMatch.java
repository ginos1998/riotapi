package riot.riotapi.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import riot.riotapi.entities.match.SummonerMatch;

public interface IntPersistenceSummonerMatch extends JpaRepository<SummonerMatch, Long> {
}
