package riot.riotapi.services.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import riot.riotapi.entities.Spell;
import riot.riotapi.services.interfaces.IntSpellApiService;
import riot.riotapi.utils.URIs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ImpSpellApiService implements IntSpellApiService {

  private final Logger logger = LoggerFactory.getLogger(ImpSpellApiService.class);
  private final WebClient webClient = WebClient.create();

  @Override
  public Flux<Spell> getSpellBySpellIdList(List<Integer> spellIdList) {
    String spellIdsString = spellIdList.stream()
        .map(Object::toString)
        .collect(Collectors.joining(","));

    String uri = URIs.URI_RIOT_API_GATEWAY.concat("/spells/by-ids/").concat(spellIdsString);

    return webClient.get()
        .uri(uri)
        .retrieve()
        .bodyToFlux(Spell.class)
        .onErrorResume(err -> {
          logger.error("An error has occurred while getting spells. Error: {}", err.getMessage());
          return Mono.just(new Spell());
        });
  }

  @Override
  public Flux<List<Spell>> getSpellPerChampionAndSpellIds(Map<Long, String> championSpellMap) {
    List<String> spellIdsList = new ArrayList<>();
    championSpellMap.forEach((championId, spellIds) -> spellIdsList.add(spellIds));
    Flux<String> spellIdsFlux = Flux.fromIterable(spellIdsList);
    return spellIdsFlux.flatMapSequential(spellIds -> {
          String uri = URIs.URI_RIOT_API_GATEWAY.concat("/spells/by-ids/").concat(spellIds);
          return webClient.get()
              .uri(uri)
              .retrieve()
              .bodyToMono(new ParameterizedTypeReference<List<Spell>>() {})
              .onErrorResume(err -> {
                logger.error("An error has occurred while getting spells. Error: {}", err.getMessage());
                return Mono.just(new ArrayList<>());
              });
        }
        );
  }


}
