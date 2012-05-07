package net.kingsbery.games.scenario;

import java.io.Serializable;

import net.kingsbery.games.GameLoop;
import net.kingsbery.games.GameStateTracker;
import net.kingsbery.games.Ship;
import net.kingsbery.games.ShipUpgrade;

public class TransportScientistToMarsScenario implements Scenario, Serializable {

  int step = 0;
  private boolean complete;
  boolean fuelTank=false;
  
  @Override
  public void check(GameStateTracker tracker) {
    GameLoop loop = tracker.getGameLoop();
    Ship player = loop.getPlayerShip();
    switch (step) {
    case 0:
      tracker
          .setMessage("A scientist, Dr. Nelson Fenimore, needs transportation to Mars.\n"+
              " Before you can transport Dr. Fenimore, you should purchase two things.");
      tracker.setMessage("First, get a fuel tank that contains more fuel.");
      tracker.setMessage("Second, get an upgrade for your navigation system with Mars's coordinates");
      tracker.setMessage("This mission is of a sensitive nature, so please be quick and discrete.");
      step = 1;
      break;
    case 1:
      if (player.getMaxFuel()>=500 && player.isKnown("Mars")) {
        tracker.setMessage("Looks like you're ready.");
        tracker.setMessage("Dr. Fenimore can be found at Odyssey station. Pick him up and take him to Mars");
        step=2;
      }
      break;
    case 2:
      if(player.dockable(loop.getShip("Odyssey"))){
        tracker.setMessage("DR. FENIMORE: OK, I'm all set, better be on our way.");
        tracker.setMessage("We'll be going to Deimos station, which is an outpost on the outer of Mars's two moons.");
        tracker.setMessage("We'll get our directions from there.");
        step=3;
      }
      break;
    case 3:
      if(player.dockable(loop.getShip("Deimos Station"))){
        tracker.setMessage("DR. FENIMORE: We've arrived.");
        tracker.setMessage("Wait here while I get the status what's happening on the surface.");
        tracker.setMessage("(An hour later...)");
        tracker.setMessage("DR. FENIMORE: Sorry for keeping you waiting.");
        tracker.setMessage("I suppose I should tell you what's going on...");
        tracker.setMessage("My team has been working here on Mars excavating a suspected alien site.");
        tracker.setMessage("Along with historical artifacts, we've discovered alien technology. \nThis technology could advance mankind a hundred years!");
        tracker.setMessage("I know you're a long way from home, but I need you to take me down to the surface.");
        tracker.setMessage("I promise, I'll make it worth you're while");
        player.addUpgrade(ShipUpgrade.MarsLandingGear);
        step=4;
      }
      break;
    case 4:
      if(tracker.hasLanded() && tracker.landedOn()==tracker.getGameLoop().getPlanet("Mars")){
        tracker.setMessage("Landed on Mars!");
        tracker.setMessage("DR. FENIMORE: I need to check in at the site.\nI'll return in a few hours.");
        tracker.setMessage("(A few hours later)");
        tracker.setMessage("One of my research assistents found this object, which looks like a map...");
        tracker.setMessage("There's some kind of object located here, near Pluto-Charon.");
        tracker.setMessage("We'll need to make it out there, but we're not ready for the journey yet.");
        tracker.setMessage("After all, no one's been past Saturn before\n and there's no substantial human presence beyond the asteroid belt.\n");
        tracker.setMessage("You have to help me. The map shows landmarks in the vicinity of Jupiter and Saturn, as well as something in the Asteroid Belt.");
        tracker.setMessage("I'll leave the order to you, but we need to investigate these places in order to prepare for whatever's out there at Pluto.");
        tracker.setMessage("[Asteriod Belt, Jupiter, Saturn coordinates added]");
        step=5;
        complete();
      }
    default:
      break;
    }
  }

  private void complete() {
    this.complete = true;
  }

  @Override
  public boolean isDone() {
    return complete;
  }

  @Override
  public boolean prerequisitesMet(GameStateTracker tracker) {
    return tracker.hasCompletedScenario(LunarLandingScenario.class);
  }

  @Override
  public String toString() {
    return "Transport Scientist to Mars";
  }

  @Override
  public boolean isGeneral() {
    return false;
  }

  @Override
  public int getStep() {
    return this.step;
  }

  @Override
  public void setDone(boolean done) {
    this.complete=done;
  }

  @Override
  public void setStep(int step) {
    this.step=step;
  }

}
