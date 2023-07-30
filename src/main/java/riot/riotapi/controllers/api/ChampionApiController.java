package riot.riotapi.controllers.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import riot.riotapi.exceptions.ServiceException;
import org.springframework.web.bind.annotation.*;
import riot.riotapi.delegators.ChampionDelegator;
import riot.riotapi.dtos.ChampionDataDTO;

@Slf4j
@RestController
@RequestMapping("/riot-api/campeon")
public class ChampionApiController {

  private final ChampionDelegator championDelegator;

  @Autowired
  public ChampionApiController(ChampionDelegator championDelegator) {
    this.championDelegator = championDelegator;
  }
  // ************************* GET METHODS ************************* //
  @GetMapping("/nombre")
  public ChampionDataDTO getChampionByName(@RequestParam("champName") String champName) {
    return this.championDelegator.getChampionByName(champName);
  }

  @GetMapping("/todos")
  public ChampionDataDTO getAllChampions() {
    return this.championDelegator.getAllChampions();
  }

  // ************************* POST METHODS ************************* //
  @PostMapping("/importar/todos")
  public String importAllChampions() {
    String response = "ERROR";
    try {
      response = this.championDelegator.importAllChampions();
    } catch (ServiceException sfe) {
      log.info(sfe.getMessage());
    }
    return response;
  }
}
