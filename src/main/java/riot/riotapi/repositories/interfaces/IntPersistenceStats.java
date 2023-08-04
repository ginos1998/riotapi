package riot.riotapi.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import riot.riotapi.entities.Stats;

@Repository
public interface IntPersistenceStats extends JpaRepository<Stats, Long> {

  @Query(value = " SELECT stats " +
                  " FROM Stats AS stats " +
                  " WHERE stats.key = :key ")
  Stats findByChampKey(Long key);
}
