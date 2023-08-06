package riot.riotapi.utils;

public class URIs {

  private URIs() {
    // default constructor
  }

  public static final String URI_SUMMONER_ACCOUNT_NAME = "https://la2.api.riotgames.com/lol/summoner/v4/summoners/by-name/"; // /<name>?api_key=<apikey>
  public static final String URI_SUMMONER_ACCOUNT_ID = "https://la2.api.riotgames.com/lol/summoner/v4/summoners/by-account/"; // /<accountId>?api_key=<apikey>
  public static final String URI_SUMMONER_ACCOUNT_PUUID = "https://la2.api.riotgames.com/lol/summoner/v4/summoners/by-puuid/"; // /<puuid>?api_key=<apikey>

  public static final String URI_ALL_LOL_CHAMPIONS = "https://ddragon.leagueoflegends.com/cdn/13.13.1/data/en_US/champion.json";

  public static final String URI_LOL_CHAMPION = "https://ddragon.leagueoflegends.com/cdn/13.13.1/data/en_US/champion/###.json";
  public static final String URI_LOL_MATCHES_BY_PUUID = "https://americas.api.riotgames.com/lol/match/v5/matches/by-puuid/#/ids";

}
