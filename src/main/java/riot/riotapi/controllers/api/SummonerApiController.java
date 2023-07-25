package riot.riotapi.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import riot.riotapi.delegators.SummonerDelegador;
import riot.riotapi.dtos.SummonerDTO;
import riot.riotapi.dtos.mappers.imp.SummonerMapper;

@RestController
@RequestMapping("/riot-api/las/invocador")
public class SummonerApiController {

  private final SummonerDelegador summonerDelegador;

  @Autowired
  public SummonerApiController(SummonerDelegador summonerDelegador) {
    this.summonerDelegador = summonerDelegador;
  }

  @GetMapping("/nombre/{name}")
  public SummonerDTO getSummonerByName(@PathVariable String name) {
    SummonerDTO sum = this.summonerDelegador.getSummonerByName(name);
    saveSummoner(sum);
    return sum;
  }

  @GetMapping("/idCuenta/{accountId}")
  public SummonerDTO getSummonerByAccountId(@PathVariable String accountId) {
    SummonerDTO sum = this.summonerDelegador.getSummonerByAccountId(accountId);
    saveSummoner(sum);
    return sum;
  }

  @GetMapping("/puuid/{puuid}")
  public SummonerDTO getSummonerByPuuid(@PathVariable String puuid) {
    SummonerDTO sum = this.summonerDelegador.getSummonerByPuuid(puuid);
    saveSummoner(sum);
    return sum;
  }

  private void saveSummoner(SummonerDTO sum) {
    if (sum != null) {
      SummonerMapper mapper = new SummonerMapper();
      this.summonerDelegador.saveSummoner(mapper.toSummoner(sum));
    }
  }


}
