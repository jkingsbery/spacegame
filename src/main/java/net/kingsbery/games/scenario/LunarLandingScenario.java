package net.kingsbery.games.scenario;

import java.io.Serializable;

import net.kingsbery.games.GameStateTracker;
import net.kingsbery.games.Ship;
import net.kingsbery.games.ShipUpgrade;
import net.kingsbery.games.objects.GameItem;

public class LunarLandingScenario implements Scenario,Serializable {

  int step=0;
  private boolean complete;
  
  @Override
  public void check(GameStateTracker tracker) {
    Ship player = tracker.getGameLoop().getPlayerShip();
    switch (step) {
    case 0:
      tracker.setMessage("Some scientists back on Earth are looking for more rocks to study, but we don't have more here. \n"+
          "You'll have to go down to get more.");
      tracker.setMessage("Since you have the $3000 for Lunar Landing Gear, I'll install the necessary equipment for my usual fee.");
      tracker.setMessage("Don't worry, you'll more than make up for it - \nthose scientists are looking to pay $5000 for those rocks down there");
      player.addUpgrade(ShipUpgrade.LunarLandingGear);
      player.decreaseMoney(3000);
      tracker.setMessage("OK, with that installed, try landing. Remember, nice and easy does it");
      step=1;
      break;
    case 1:
      if(tracker.hasLanded() && tracker.landedOn()==tracker.getGameLoop().getPlanet("Moon")){
        tracker.setMessage("Congratulations on your first successful landing!");
        tracker.setMessage("We'll load these Moon Rocks on your ship for you.");
        tracker.setMessage("To lift off, just press UP until you're back into orbit.\n"+
            "After you get far enough away from the planet, the main navigation view will reappear.");
        tracker.setMessage("Take these rocks back to Freedom Station for your payment.");
        tracker.setMessage("We'll also make these available for you to purchase from now on.");
        player.addItem(new GameItem("Moon Rocks",500,0));
        tracker.getGameLoop().getShip("Odyssey").addItem(new GameItem("Moon Rocks",10000,1));
        tracker.getGameLoop().getShip("Odyssey").setCost("Moon Rocks",1);
        step=2;
      }
      break;
    case 2:
      if(player.dockable(tracker.getGameLoop().getShip("Freedom Station"))){
        tracker.setMessage("Thanks for the delivery!");
        tracker.setMessage("Any time in the future you have Moon Rocks to sell, we'll buy them at $10 each");
        tracker.getGameLoop().getShip("Freedom Station").setCost("Moon Rocks",10);
        player.addMoney(5000);
        player.removeAll("Moon Rocks");
        complete();
      }
    default:
      break;
    }  
  }

  private void complete() {
    this.complete=true;
  }

  @Override
  public boolean isDone() {
    return complete;
  }

  @Override
  public boolean prerequisitesMet(GameStateTracker tracker) {
    return tracker.hasCompletedScenario(IntroductionScenario.class) && tracker.getPlayer().getDollars()>3000;
  }
  
  @Override
  public String toString(){
    return "Lunar Landing";
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
