package riot.riotapi.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import riot.riotapi.entities.Summoner;

public interface IntPersistenceSummoner extends JpaRepository<Summoner, String> {

}
