package riot.riotapi.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import riot.riotapi.entities.Spell;
import riot.riotapi.repositories.interfaces.IntPersistenceSpell;
import riot.riotapi.services.interfaces.IntSpellService;

import java.util.List;
import java.util.stream.Stream;

@Service
public class ImpSpellService implements IntSpellService {
    private final IntPersistenceSpell persistenceSpell;

    @Autowired
    public ImpSpellService(IntPersistenceSpell persistenceSpell) {
        this.persistenceSpell = persistenceSpell;
    }

    @Override
    public List<Spell> findSpellsByIds(List<Integer> spellIds) {
        return this.persistenceSpell.findBySpellIdIn(spellIds)
                .orElse(Stream.of(new Spell("unknown"), new Spell("unknown")).toList());
    }
}
