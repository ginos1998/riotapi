package riot.riotapi.repositories.interfaces;

import org.springframework.data.jpa.repository.Query;
import riot.riotapi.entities.ChampionData;

public interface IntPersistenceChampionData extends GenericRepository<ChampionData, Long> {
  @Query(value = " SELECT cd.* " +
                  " FROM champ_data AS cd " +
                  " ORDER BY cd.id_champ_data LIMIT 1 ",
        nativeQuery = true)
  ChampionData getLastChampionData();
}
