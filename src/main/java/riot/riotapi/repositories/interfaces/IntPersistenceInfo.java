package riot.riotapi.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import riot.riotapi.entities.Info;

public interface IntPersistenceInfo extends JpaRepository<Info, Long> {
}
