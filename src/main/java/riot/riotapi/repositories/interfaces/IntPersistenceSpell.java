package riot.riotapi.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import riot.riotapi.entities.Spell;

import java.util.List;
import java.util.Optional;

@Repository
public interface IntPersistenceSpell extends JpaRepository<Spell, Integer> {
    Optional<List<Spell>> findBySpellIdIn(List<Integer> spellsIds);
}
