package riot.riotapi.utils;

public class ConstantsExceptions {

  private ConstantsExceptions() {
    // default
  }

  public static final String ERROR_BAD_INPUT_CHAMP_NAME = "El valor de champName no puede ser null o vacío.";
  public static final String ERROR_IMPORTING_CHAMPIONS = "Ha ocurrido un error al importar los campeones.";
  public static final String ERROR_SEARCHING_CHAMPION = "Ha ocurrido un error al buscar un campeon con el nombre: ";
  public static final String ERROR_SEARCHING_SUMMONER = "Ha ocurrido un error al buscar un invocador con el parametro: ";
  public static final String ERROR_SEARCHING_CHAMPIONS = "Ha ocurrido un error buscando todos los campeones.";
  public static final String ERROR_BAD_INPUT_SUM_NAME = "Para buscar un invocador, la entrada no puede ser null o vacia";
  public static final String ERROR_RIOT_API_KEY_NOT_FOUNT = "Error al buscar la api key de Riot Games.";
  public static final String ERROR_GETTING_SUMMONER_MATCHES = "Error al obtener partidas del jugador con el puuid %s";
}
