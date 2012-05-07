package net.kingsbery.games.scenario;

import java.io.Serializable;

import net.kingsbery.games.GameStateTracker;
import net.kingsbery.games.Ship;
import net.kingsbery.games.objects.GameItem;

public class MoonRockDeliveryScenario implements Scenario,Serializable {

  int step=0;
  private boolean complete;
  
  @Override
  public void check(GameStateTracker tracker) {
    Ship player = tracker.getGameLoop().getPlayerShip();
    switch (step) {
    case 0:
      tracker.setMessage("Take these moon rocks and deliver them to Freedom station in orbit around Earth. \n"+
          "You'll get paid $1000 on delivery");
      player.addItem(new GameItem("Moon Rocks",25,0));
      step=1;
      break;
    case 1:
      if(player.dockable(tracker.getGameLoop().getShip("Freedom Station"))){
        tracker.setMessage("Thanks, here's your money.");
        tracker.setMessage("You're ship doesn't look like it can handle the landing, \n"+
            "but if you ever make it to the Lunar surface and get a hold of some more Moon Rocks, keep me in mind.");
        tracker.setMessage("Maybe someone at Odyssey Station can set you up with a landing system to get down there.");
        player.removeAll("Moon Rocks");
        player.addMoney(1000);
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
    return tracker.hasCompletedScenario(IntroductionScenario.class);
  }
  
  @Override
  public String toString(){
    return "Deliver Moon Rocks";
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
