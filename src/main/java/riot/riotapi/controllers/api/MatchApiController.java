package riot.riotapi.controllers.api;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import riot.riotapi.delegators.MatchDelegator;
import riot.riotapi.dtos.match.MatchDTO;
import riot.riotapi.dtos.match.MatchesDTO;
import riot.riotapi.filters.MatchFilter;
import riot.riotapi.utils.views.CommonView;


@RestController
@Api(value = "MatchApi", produces = "application/json")
@RequestMapping("/riot-api/las/partidas")
public class MatchApiController {

  private final MatchDelegator matchDelegator;

  @Autowired
  public MatchApiController(MatchDelegator matchDelegator) {
    this.matchDelegator = matchDelegator;
  }

  @GetMapping("/by-puuid/{puuid}")
  @ApiOperation(value = "Obtiene una lista de partidas.", response = MatchesDTO.class, responseContainer = "dto", produces = "application/json")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Devuelve un dto con un array de partidas."),
          @ApiResponse(code = 204, message = "Para el puuid, período startTime-endTime, queue ó type dado, no existen partidas."),
          @ApiResponse(code = 404, message = "Not found."),
          @ApiResponse(code = 409, message = "Error al obtener las partidas"),
          @ApiResponse(code = 500, message = "Ha ocurrido un error inesperado.")

  })
  public ResponseEntity<MatchesDTO> getMatchesByPuuid(@PathVariable String puuid, @ModelAttribute MatchFilter filter) {

    MatchesDTO summMatches = matchDelegator.getSummonerMatchesByPuuid(puuid, filter);

    if (summMatches != null) {
      return ResponseEntity.ok(summMatches);
    } else {
      return ResponseEntity.noContent().build();
    }
  }

  @GetMapping("/by-summoner-name/{sumName}")
  @ApiOperation(value = "Obtiene una lista de partidas.", response = MatchesDTO.class, responseContainer = "Array", produces = "application/json")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Devuelve un dto con un array de partidas."),
          @ApiResponse(code = 204, message = "Para el puuid, período startTime-endTime, queue ó type dado, no existen partidas."),
          @ApiResponse(code = 409, message = "Error al obtener las partidas"),
          @ApiResponse(code = 404, message = "Not found."),
          @ApiResponse(code = 500, message = "Ha ocurrido un error inesperado.")

  })
  public ResponseEntity<MatchesDTO> getMatchesBySummonerName(@PathVariable String sumName, @ModelAttribute MatchFilter filter) {
    MatchesDTO sumMatchesDTO = matchDelegator.getMatchesBySummonerName(sumName, filter);

    if (sumMatchesDTO != null) {
      return ResponseEntity.ok(sumMatchesDTO);
    } else {
      return ResponseEntity.noContent().build();
    }
  }

  @GetMapping("/by-matchId/{matchId}")
  @ApiOperation(value = "Obtiene datos de una partida.", response = MatchesDTO.class, responseContainer = "dto", produces = "application/json")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Devuelve un dto con datos de una partida."),
          @ApiResponse(code = 204, message = "No existe partida para el matchId dado."),
          @ApiResponse(code = 409, message = "Error al obtener la partida."),
          @ApiResponse(code = 404, message = "Not found."),
          @ApiResponse(code = 500, message = "Ha ocurrido un error inesperado.")

  })
  @JsonView(CommonView.HistoryView.class)
  public ResponseEntity<MatchDTO> getMatchById(@PathVariable String matchId, @RequestParam(required = false, defaultValue = "false") Boolean saveData) {
    MatchDTO sumMatchesDTO = matchDelegator.getMatchById(matchId, saveData);

    if (sumMatchesDTO != null) {
      return ResponseEntity.ok(sumMatchesDTO);
    } else {
      return ResponseEntity.noContent().build();
    }
  }

  @GetMapping("/current-match/{sumName}")
  @ApiOperation(value = "Obtiene datos de una partida en juego.", response = MatchesDTO.class, responseContainer = "dto", produces = "application/json")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Devuelve un dto con datos de una partida en juego."),
          @ApiResponse(code = 204, message = "No existe partida en juego para el sumName dado."),
          @ApiResponse(code = 409, message = "Error al obtener la partida en juego."),
          @ApiResponse(code = 404, message = "Not found."),
          @ApiResponse(code = 500, message = "Ha ocurrido un error inesperado.")

  })
  @JsonView(CommonView.LiveView.class)
  public ResponseEntity<MatchDTO> getCurrentMatchInfo(@PathVariable String sumName) {
    MatchDTO matchDTO = matchDelegator.getCurrentMatchInfo(sumName);

    if (matchDTO != null) {
      return ResponseEntity.ok(matchDTO);
    } else {
      return ResponseEntity.noContent().build();
    }
  }

  @GetMapping("/live-match/{sumName}")
  @JsonView(CommonView.LiveView.class)
  public Mono<MatchDTO> getSummonerLiveMatch(@PathVariable String sumName) {
    return matchDelegator.getSummonerLiveMatch(sumName);
  }

}
