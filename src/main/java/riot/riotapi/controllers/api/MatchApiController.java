package riot.riotapi.controllers.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import riot.riotapi.dtos.match.MatchesDTO;

@RequestMapping("/riot-api/las/partidas")
public class MatchApiController {

  @GetMapping("/{puuid}")
  public MatchesDTO getMatchesByPuuid(@PathVariable String puuid) {

    return null;
  }
}
