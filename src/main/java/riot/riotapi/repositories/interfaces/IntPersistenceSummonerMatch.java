package riot.riotapi.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import riot.riotapi.dtos.match.ParticipantInfoDTO;
import riot.riotapi.entities.match.SummonerMatch;

import java.util.List;

@Repository
public interface IntPersistenceSummonerMatch extends JpaRepository<SummonerMatch, Long> {

    @Query(value = " SELECT new riot.riotapi.dtos.match.ParticipantInfoDTO(sm.summoner.name, sm.champion.name, sm.lane, sm.win, sm.teamId) " +
            " FROM SummonerMatch sm " +
            " WHERE sm.match.matchId = :matchId ")
    List<ParticipantInfoDTO> findMatchParticipantsByMatchId(Long matchId);
}
