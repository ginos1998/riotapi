package riot.riotapi.controllers.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import riot.riotapi.delegators.MatchDelegator;
import riot.riotapi.dtos.match.MatchesDTO;
import riot.riotapi.filters.MatchFilter;


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
}
