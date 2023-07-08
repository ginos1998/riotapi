package riot.riotapi.controllers.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import riot.riotapi.delegators.ChampionDelegator;
import riot.riotapi.entities.ChampionData;

@RestController
@RequestMapping("/riot-api/campeon")
public class ChampionApiController {

  private ChampionApiController() {
    // default
  }

  @GetMapping("/nombre/{champName}")
  public ChampionData getChampionByName(@PathVariable String champName) {
    return ChampionDelegator.getChampionByName(champName);
  }

  @GetMapping("/todos")
  public ChampionData getAllChampions() {
    return ChampionDelegator.getAllChampions();
  }
}
