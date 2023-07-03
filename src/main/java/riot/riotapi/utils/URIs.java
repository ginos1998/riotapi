package riot.riotapi.utils;

public class URIs {

  private URIs() {
    // default constructor
  }

  public static final String URI_SUMMONER_ACCOUNT_NAME = "https://la2.api.riotgames.com/lol/summoner/v4/summoners/by-name/"; // /<name>?api_key=<apikey>
  public static final String URI_SUMMONER_ACCOUNT_ID = "https://la2.api.riotgames.com/lol/summoner/v4/summoners/by-account/"; // /<accountId>?api_key=<apikey>
  public static final String URI_SUMMONER_ACCOUNT_PUUID = "https://la2.api.riotgames.com/lol/summoner/v4/summoners/by-puuid/"; // /<puuid>?api_key=<apikey>

}
