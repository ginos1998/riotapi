package riot.riotapi.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import riot.riotapi.entities.Stats;

public interface IntPersistenceStats extends JpaRepository<Stats, Long> {

}
