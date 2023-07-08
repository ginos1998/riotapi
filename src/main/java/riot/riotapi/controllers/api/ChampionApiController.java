package riot.riotapi.controllers.api;

import org.springframework.web.bind.annotation.*;
import riot.riotapi.delegators.ChampionDelegator;
import riot.riotapi.entities.ChampionData;

@RestController
@RequestMapping("/riot-api/campeon")
public class ChampionApiController {

  private ChampionApiController() {
    // default
  }

  @GetMapping("/nombre")
  public ChampionData getChampionByName(@RequestParam("champName") String champName) {
    return ChampionDelegator.getChampionByName(champName);
  }

  @GetMapping("/todos")
  public ChampionData getAllChampions() {
    return ChampionDelegator.getAllChampions();
  }
}
