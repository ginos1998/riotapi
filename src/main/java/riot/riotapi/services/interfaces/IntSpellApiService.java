package riot.riotapi.services.interfaces;

import reactor.core.publisher.Flux;
import riot.riotapi.entities.Spell;

import java.util.List;
import java.util.Map;

public interface IntSpellApiService {

  Flux<Spell> getSpellBySpellIdList(List<Integer> spellIdList);

  Flux<List<Spell>> getSpellPerChampionAndSpellIds(Map<Long, String> championSpellMap);

}
