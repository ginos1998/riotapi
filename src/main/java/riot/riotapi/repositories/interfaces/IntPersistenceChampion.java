package riot.riotapi.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import riot.riotapi.entities.Champion;

public interface IntPersistenceChampion extends JpaRepository<Champion, Long> {

  String saveAllChampions(Long champDataId);
}
