package riot.riotapi.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import riot.riotapi.entities.RiotApi;

public interface IntRiotApi extends JpaRepository<RiotApi, Long> {
}
