package riot.riotapi.repositories.factories;

import riot.riotapi.repositories.interfaces.*;

public class PersistenceFactory {
  private static IntPersistenceSummoner intPersistenceSummoner;
  private static IntPersistenceChampionData intPersistenceChampionData;
  private static IntPersistenceChampion intPersistenceChampion;
  private static IntPersistenceInfo intPersistenceInfo;
  private static IntPersistenceStats intPersistenceStats;
  private static IntRiotApi intRiotApi;

  private PersistenceFactory() {
    // default
  }

  public static IntPersistenceSummoner getIntPersistenceSummoner() {
    return intPersistenceSummoner;
  }

  public static void setIntPersistenceSummoner(IntPersistenceSummoner intPersistenceSummoner) {
    PersistenceFactory.intPersistenceSummoner = intPersistenceSummoner;
  }

  public static IntPersistenceChampionData getIntPersistenceChampionData() {
    return intPersistenceChampionData;
  }

  public static void setIntPersistenceChampionData(IntPersistenceChampionData intPersistenceChampionData) {
    PersistenceFactory.intPersistenceChampionData = intPersistenceChampionData;
  }

  public static IntPersistenceChampion getIntPersistenceChampion() {
    return intPersistenceChampion;
  }

  public static void setIntPersistenceChampion(IntPersistenceChampion intPersistenceChampion) {
    PersistenceFactory.intPersistenceChampion = intPersistenceChampion;
  }

  public static IntPersistenceInfo getIntPersistenceInfo() {
    return intPersistenceInfo;
  }

  public static void setIntPersistenceInfo(IntPersistenceInfo intPersistenceInfo) {
    PersistenceFactory.intPersistenceInfo = intPersistenceInfo;
  }

  public static IntPersistenceStats getIntPersistenceStats() {
    return intPersistenceStats;
  }

  public static void setIntPersistenceStats(IntPersistenceStats intPersistenceStats) {
    PersistenceFactory.intPersistenceStats = intPersistenceStats;
  }

  public static IntRiotApi getIntRiotApi() {
    return intRiotApi;
  }

  public static void setIntRiotApi(IntRiotApi intRiotApi) {
    PersistenceFactory.intRiotApi = intRiotApi;
  }
}
