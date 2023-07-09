package riot.riotapi.repositories.factories;

import riot.riotapi.repositories.interfaces.IntPersistenceChampion;
import riot.riotapi.repositories.interfaces.IntPersistenceChampionData;
import riot.riotapi.repositories.interfaces.IntPersistenceSummoner;

public class PersistenceFactory {
  private static IntPersistenceSummoner intPersistenceSummoner;
  private static IntPersistenceChampionData intPersistenceChampionData;

  private static IntPersistenceChampion intPersistenceChampion;

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

  public static void setIntPersistenceChampion(IntPersistenceChampion intPersistenceChampion) {
    PersistenceFactory.intPersistenceChampion = intPersistenceChampion;
  }

  public static IntPersistenceChampion getIntPersistenceChampion() {
    return intPersistenceChampion;
  }
}
