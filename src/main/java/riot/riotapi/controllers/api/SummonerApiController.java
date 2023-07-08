package riot.riotapi.controllers.api;

import org.springframework.web.bind.annotation.*;
import riot.riotapi.delegators.SummonerDelegador;
import riot.riotapi.entities.Summoner;

@RestController
@RequestMapping("/riot-api/las/invocador")
public class SummonerApiController {

  public SummonerApiController() {
    // default
  }

  @GetMapping("/nombre/{name}")
  public Summoner getSummonerByName(@PathVariable String name) {
    Summoner sum = SummonerDelegador.getSummonerByName(name);
    saveSummoner(sum);
    return sum;
  }

  @GetMapping("/idCuenta/{accountId}")
  public Summoner getSummonerByAccountId(@PathVariable String accountId) {
    Summoner sum = SummonerDelegador.getSummonerByAccountId(accountId);
    saveSummoner(sum);
    return sum;
  }

  @GetMapping("/puuid/{puuid}")
  public Summoner getSummonerByPuuid(@PathVariable String puuid) {
    Summoner sum = SummonerDelegador.getSummonerByPuuid(puuid);
    saveSummoner(sum);
    return sum;
  }

  private void saveSummoner(Summoner sum) {
    if (sum != null) {
      SummonerDelegador.saveSummoner(sum);
    }
  }


}
