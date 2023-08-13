package riot.riotapi.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import riot.riotapi.dtos.match.MatchDTO;
import riot.riotapi.entities.match.Match;

@Repository
public interface IntPersistenceMatch extends JpaRepository<Match, Long> {

    @Query(value = " SELECT new riot.riotapi.dtos.match.MatchDTO(m.mode, m.creation, m.startTime, m.endTime, m.duration, m.version) " +
            " FROM Match as m " +
            " WHERE m.matchId = :matchId ")
    MatchDTO fGetMatchDTOByMatchId(Long matchId);
}
