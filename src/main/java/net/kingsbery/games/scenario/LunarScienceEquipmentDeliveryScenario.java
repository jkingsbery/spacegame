package net.kingsbery.games.scenario;

import java.io.Serializable;

import net.kingsbery.games.GameStateTracker;
import net.kingsbery.games.Ship;
import net.kingsbery.games.objects.GameItem;

public class LunarScienceEquipmentDeliveryScenario implements Scenario,Serializable {

  int step=0;
  private boolean complete;
  
  @Override
  public void check(GameStateTracker tracker) {
    Ship player = tracker.getGameLoop().getPlayerShip();
    switch (step) {
    case 0:
      tracker.setMessage("Take this scientific equipment and deliver it to Odyssey Station\n in orbit around Earth. \n"+
          "You'll get paid $1000 on delivery");
      player.addItem(new GameItem("Lunar Equipment",1,0));
      step=1;
      break;
    case 1:
      if(player.dockable(tracker.getGameLoop().getShip("Odyssey"))){
        tracker.setMessage("Thanks, here's your money.");
        player.removeAll("Lunar Equipment");
        player.addMoney(300);
        complete();      }
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
    return "Deliver Lunar Science Equipment";
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
