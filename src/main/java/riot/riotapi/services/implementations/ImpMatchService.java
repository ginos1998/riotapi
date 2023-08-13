package riot.riotapi.services.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import riot.riotapi.dtos.match.MatchDTO;
import riot.riotapi.dtos.match.MatchInfoDTO;
import riot.riotapi.entities.match.Match;
import riot.riotapi.repositories.interfaces.IntPersistenceMatch;
import riot.riotapi.services.interfaces.IntMatchService;

@Service
public class ImpMatchService implements IntMatchService {

    private final IntPersistenceMatch persistenceMatch;
    private final ModelMapper mapper;

    @Autowired
    public ImpMatchService(IntPersistenceMatch persistenceMatch) {
        this.persistenceMatch = persistenceMatch;
        this.mapper = new ModelMapper();
    }

    @Override
    public void saveMatch(MatchInfoDTO matchInfoDTO) {
        persistenceMatch.save(mapper.map(matchInfoDTO, Match.class));
    }

    @Override
    public MatchDTO findMatchDTOByMatchId(Long matchId) {
        return persistenceMatch.fGetMatchDTOByMatchId(matchId);
    }


}
