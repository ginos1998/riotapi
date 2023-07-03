package riot.riotapi.controllers.api;

import org.springframework.web.bind.annotation.*;
import riot.riotapi.delegators.SummonerDelegador;
import riot.riotapi.entities.Summoner;
import riot.riotapi.services.implementations.ImpSummonerApiService;

@RestController
@RequestMapping("/invocador")
public class SummonerApiController {

  private ImpSummonerApiService summonerApiService;

  public SummonerApiController() {
    // default
    summonerApiService = new ImpSummonerApiService();
  }

  @GetMapping("/nombre/{name}")
  public Summoner getSummonerByName(@PathVariable String name) {
    Summoner sum = summonerApiService.getSummonerByName(name);

    if (sum != null) {
      SummonerDelegador.saveSummoner(sum);
    }
    return sum;
  }

  @GetMapping("/idCuenta/{accountId}")
  public Summoner getSummonerByAccountId(@PathVariable String accountId) {
    return summonerApiService.getSummonerByAccountId(accountId);
  }

  @GetMapping("/puuid/{puuid}")
  public Summoner getSummonerByPuuid(@PathVariable String puuid) {
    return summonerApiService.getSummonerByPuuid(puuid);
  }


}
