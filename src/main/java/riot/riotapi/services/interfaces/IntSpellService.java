package riot.riotapi.services.interfaces;

import riot.riotapi.entities.Spell;

import java.util.List;

public interface IntSpellService {
    List<Spell> findSpellsByIds(List<Integer> spellIds);
}
