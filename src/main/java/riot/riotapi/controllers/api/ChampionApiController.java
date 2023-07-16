package riot.riotapi.controllers.api;

import riot.riotapi.exceptions.ServiceFactoryException;
import org.springframework.web.bind.annotation.*;
import riot.riotapi.delegators.ChampionDelegator;
import riot.riotapi.dtos.ChampionDataDTO;

@RestController
@RequestMapping("/riot-api/campeon")
public class ChampionApiController {

  private ChampionApiController() {
    // default
  }
  // ************************* GET METHODS ************************* //
  @GetMapping("/nombre")
  public ChampionDataDTO getChampionByName(@RequestParam("champName") String champName) {
    return ChampionDelegator.getChampionByName(champName);
  }

  @GetMapping("/todos")
  public ChampionDataDTO getAllChampions() {
    return ChampionDelegator.getAllChampions();
  }

  // ************************* POST METHODS ************************* //
  @PostMapping("/importar/todos")
  public String importAllChampions() {
    String response = "ERROR";
    try {
      response = ChampionDelegator.importAllChampions();
    } catch (ServiceFactoryException sfe) {
      System.out.println(sfe.getMessage());
    }
    return response;
  }
}
