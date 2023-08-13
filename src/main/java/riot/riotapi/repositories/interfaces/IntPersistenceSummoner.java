package riot.riotapi.repositories.interfaces;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import riot.riotapi.entities.Summoner;

import java.util.List;
import java.util.Optional;

@Repository
public interface IntPersistenceSummoner extends GenericRepository<Summoner, String> {

  @Query(value = " SELECT s.* FROM summoner AS s WHERE s.name ilike :name ",
          nativeQuery = true)
  List<Summoner> getSummonerByName(String name);

  @Query(value = " SELECT s.* FROM summoner AS s WHERE s.account_id ilike :accountId ",
      nativeQuery = true)
  List<Summoner> getSummonerByAccountId(String accountId);

  @Query(value = " SELECT s.* FROM summoner AS s WHERE s.puuid ilike :puuid ",
      nativeQuery = true)
  List<Summoner> getSummonerByPuuid(String puuid);

  Optional<Summoner> findByNameContainingIgnoreCase(String name);

}
