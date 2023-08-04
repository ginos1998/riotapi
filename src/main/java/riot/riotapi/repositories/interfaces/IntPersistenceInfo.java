package riot.riotapi.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import riot.riotapi.entities.Info;

public interface IntPersistenceInfo extends JpaRepository<Info, Long> {

  @Query(value = " SELECT info " +
                  " FROM Info AS info " +
                  " WHERE info.key = :key ")
  Info findByChampKey(Long key);
}
