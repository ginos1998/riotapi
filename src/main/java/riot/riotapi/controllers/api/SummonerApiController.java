package riot.riotapi.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import riot.riotapi.delegators.SummonerDelegador;
import riot.riotapi.dtos.SummonerDTO;
import riot.riotapi.dtos.summoner.SummonerStatsDTO;
import riot.riotapi.utils.CommonFunctions;

import java.util.List;

@RestController
@RequestMapping("/riot-api/las/invocador")
public class SummonerApiController {

  private final SummonerDelegador summonerDelegador;

  @Autowired
  public SummonerApiController(SummonerDelegador summonerDelegador) {
    this.summonerDelegador = summonerDelegador;
  }

  @GetMapping()
  public ResponseEntity<List<SummonerDTO>> getSummonerBy(@RequestParam(required = false) String name,
                                                         @RequestParam(required = false) String accountId,
                                                         @RequestParam(required = false) String puuid,
                                                         @RequestParam(required = false, defaultValue = "false") Boolean saveIfExists) {

    List<SummonerDTO> sum = this.summonerDelegador.getSummonerBy(name, accountId, puuid, saveIfExists);

    if (CommonFunctions.isNotNullOrEmpty(sum)){
      return ResponseEntity.ok(sum);
    } else {
      return ResponseEntity.noContent().build();
    }
  }

  @GetMapping("/stats")
  public Mono<SummonerStatsDTO> getSummonerStatsDTOMono(@RequestParam String summonerName) {
    return summonerDelegador.getSummonerStatsByNameMono(summonerName);
  }

}
