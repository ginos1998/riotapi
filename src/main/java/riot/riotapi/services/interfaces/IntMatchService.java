package riot.riotapi.services.interfaces;

import riot.riotapi.dtos.match.MatchDTO;
import riot.riotapi.dtos.match.MatchInfoDTO;

public interface IntMatchService {
    void saveMatch(MatchInfoDTO matchInfoDTO);

    MatchDTO findMatchDTOByMatchId(Long matchId);
}
