package riot.riotapi.repositories.interfaces;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import riot.riotapi.entities.Champion;

import java.util.List;

@Repository
public interface IntPersistenceChampion extends GenericRepository<Champion, Long> {
  @Query(value = " SELECT champ.* " +
                  " FROM champion AS champ " +
                  " WHERE champ.name ILIKE :name ",
        nativeQuery = true)
  List<Champion> getChampionByName(String name);

  Champion findByKey(Long key);

  List<Champion> findByKeyIn(List<Long> champIds);
}
