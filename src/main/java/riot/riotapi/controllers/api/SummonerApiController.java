package riot.riotapi.controllers.api;

import org.springframework.web.bind.annotation.*;
import riot.riotapi.delegators.SummonerDelegador;
import riot.riotapi.dtos.SummonerDTO;
import riot.riotapi.dtos.mappers.imp.SummonerMapper;

@RestController
@RequestMapping("/riot-api/las/invocador")
public class SummonerApiController {

  public SummonerApiController() {
    // default
  }

  @GetMapping("/nombre/{name}")
  public SummonerDTO getSummonerByName(@PathVariable String name) {
    SummonerDTO sum = SummonerDelegador.getSummonerByName(name);
    saveSummoner(sum);
    return sum;
  }

  @GetMapping("/idCuenta/{accountId}")
  public SummonerDTO getSummonerByAccountId(@PathVariable String accountId) {
    SummonerDTO sum = SummonerDelegador.getSummonerByAccountId(accountId);
    saveSummoner(sum);
    return sum;
  }

  @GetMapping("/puuid/{puuid}")
  public SummonerDTO getSummonerByPuuid(@PathVariable String puuid) {
    SummonerDTO sum = SummonerDelegador.getSummonerByPuuid(puuid);
    saveSummoner(sum);
    return sum;
  }

  private void saveSummoner(SummonerDTO sum) {
    if (sum != null) {
      SummonerMapper mapper = new SummonerMapper();
      SummonerDelegador.saveSummoner(mapper.toSummoner(sum));
    }
  }


}
