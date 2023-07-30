package riot.riotapi.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import riot.riotapi.delegators.SummonerDelegador;
import riot.riotapi.dtos.SummonerDTO;
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

  @GetMapping("/nombre/{name}")
  public ResponseEntity<List<SummonerDTO>> getSummonerByName(@PathVariable String name,
                                                             @RequestParam(required = false, defaultValue = "false") Boolean saveIfExists) {

    List<SummonerDTO> sum = this.summonerDelegador.getSummonerByName(name, saveIfExists);

    if (CommonFunctions.isNotNullOrEmpty(sum)){
      return ResponseEntity.ok(sum);
    } else {
      return ResponseEntity.noContent().build();
    }

  }

  @GetMapping("/idCuenta/{accountId}")
  public ResponseEntity<List<SummonerDTO>> getSummonerByAccountId(@PathVariable String accountId,
                                                                  @RequestParam(required = false, defaultValue = "false") Boolean saveIfExists) {

    List<SummonerDTO> sum = this.summonerDelegador.getSummonerByAccountId(accountId, saveIfExists);

    if (CommonFunctions.isNotNullOrEmpty(sum)){
      return ResponseEntity.ok(sum);
    } else {
      return ResponseEntity.noContent().build();
    }
  }

  @GetMapping("/puuid/{puuid}")
  public ResponseEntity<List<SummonerDTO>> getSummonerByPuuid(@PathVariable String puuid,
                                                              @RequestParam(required = false, defaultValue = "false") Boolean saveIfExists) {

    List<SummonerDTO> sum = this.summonerDelegador.getSummonerByPuuid(puuid, saveIfExists);

    if (CommonFunctions.isNotNullOrEmpty(sum)){
      return ResponseEntity.ok(sum);
    } else {
      return ResponseEntity.noContent().build();
    }
  }


}
