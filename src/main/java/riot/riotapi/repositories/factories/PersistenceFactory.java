package riot.riotapi.repositories.factories;

import riot.riotapi.repositories.interfaces.IntPersistenceSummoner;

public class PersistenceFactory {
  private static IntPersistenceSummoner intPersistenceSummoner;

  private PersistenceFactory() {
    // default
  }

  public static IntPersistenceSummoner getIntPersistenceSummoner() {
    return intPersistenceSummoner;
  }

  public static void setIntPersistenceSummoner(IntPersistenceSummoner intPersistenceSummoner) {
    PersistenceFactory.intPersistenceSummoner = intPersistenceSummoner;
  }
}
