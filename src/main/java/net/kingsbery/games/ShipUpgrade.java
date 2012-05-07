package net.kingsbery.games;

import net.kingsbery.games.scenario.LunarLandingScenario;
import net.kingsbery.games.scenario.MoonRockDeliveryScenario;
import net.kingsbery.games.scenario.TransportScientistToMarsScenario;

public enum ShipUpgrade {

  SmallFuelTank(2500) {
    @Override
    public boolean hasPrerequisites(GameStateTracker tracker) {
      return  tracker.hasCompletedScenario(LunarLandingScenario.class);
    }
  }, LunarLandingGear(3000) {
    @Override
    public boolean hasPrerequisites(GameStateTracker tracker) {
      return  tracker.hasCompletedScenario(MoonRockDeliveryScenario.class);
    }
  },MarsCoordinates(2000) {
    @Override
    public boolean hasPrerequisites(GameStateTracker tracker) {
      return tracker.hasStartedScenario(TransportScientistToMarsScenario.class);
    }
  }, MarsLandingGear(3000) {
    @Override
    public boolean hasPrerequisites(GameStateTracker tracker) {
      return tracker.getPlayer().hasUpgrade(LunarLandingGear);
    }
  };

  private int cost;
  
  private ShipUpgrade(int cost){
    this.cost=cost;
  }
  
  public int getCost() {
    return this.cost;
  }

  public abstract boolean hasPrerequisites(GameStateTracker tracker);
  
}
