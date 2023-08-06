package riot.riotapi.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import riot.riotapi.delegators.MatchDelegator;
import riot.riotapi.dtos.match.MatchesDTO;
import riot.riotapi.filters.MatchFilter;


@RestController
@RequestMapping("/riot-api/las/partidas")
public class MatchApiController {

  private final MatchDelegator matchDelegator;

  @Autowired
  public MatchApiController(MatchDelegator matchDelegator) {
    this.matchDelegator = matchDelegator;
  }

  @GetMapping("/by-puuid")
  public ResponseEntity<MatchesDTO> getMatchesByPuuid(@ModelAttribute MatchFilter filter) {

    MatchesDTO summMatches = matchDelegator.getSummonerMatchesByPuuid(filter);

    if (summMatches != null) {
      return ResponseEntity.ok(summMatches);
    } else {
      return ResponseEntity.noContent().build();
    }
  }
}
