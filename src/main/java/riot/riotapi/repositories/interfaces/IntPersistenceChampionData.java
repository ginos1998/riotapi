package riot.riotapi.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import riot.riotapi.entities.ChampionData;

public interface IntPersistenceChampionData extends JpaRepository<ChampionData, Long> {

}
