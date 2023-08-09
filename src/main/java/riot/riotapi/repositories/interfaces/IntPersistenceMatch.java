package riot.riotapi.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import riot.riotapi.entities.match.Match;

public interface IntPersistenceMatch extends JpaRepository<Match, Long> {

}
