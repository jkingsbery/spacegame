package net.kingsbery.games.scenario;

import java.io.Serializable;

import net.kingsbery.games.GameLoop;
import net.kingsbery.games.GameStateTracker;
import net.kingsbery.games.Ship;

public class IntroductionScenario implements Scenario,Serializable {

  int step = 0;
  boolean complete=false;
  
  public IntroductionScenario() {
  }
  
  public IntroductionScenario(int step){
    this.step=step;
  }

  /**
   * synchronized because we don't want same step running multiple times.
   */
  public synchronized void check(GameStateTracker tracker) {
    GameLoop gameLoop = tracker.getGameLoop();

    Ship playerShip = gameLoop.getPlayerShip();
    if (playerShip.isEmpty()&& step<100) {
      tracker
          .setMessage("OK, Well, you tried. Just stay tight and someone will come pick you up at some point.");
      step = 1000;
    } else {
      switch (step) {
      case 0:
        tracker.setMessage("CAPCOM: Hello? Hello? Yes, we hear you!");
        tracker.setMessage("We got your distress message from earlier. I'm sorry about your pilot.");
        tracker.setMessage("I know you're new to piloting space ships...");
        tracker.setMessage("... and that your ship is in rough shape with little fuel and damaged landing mechanisms...");
        tracker.setMessage("... and most of your other equipment is damaged ...");
        tracker.setMessage("but I'll talk you through getting back here to Earth.");
        tracker
            .setMessage("If you make it back here to Freedom station in Earth orbit, you'll be able to refuel.");
        tracker
            .setMessage("First, you're going to need to get out of Lunar orbit with using as little fuel as possible.\n A few well timed burns should do the trick.");
        tracker.setMessage("You don't need too much speed, just enough to escape the Moon's gravity.\nThe Earth's gravity will pull you in,\nand you'll need fuel for maneuvering in orbit in order to dock.");
        playerShip.setTarget("Earth");
        step = 1;
        break;
      case 1:
        if (gameLoop.getPlanet("Moon").getLocation()
            .getDistance(playerShip.getPosition()) > 600) {
          tracker.setMessage("Good! We're on our way.");
          tracker
              .setMessage("In the future, you can cycle through known location using your navigation system (N).\n"+
                  " The minimap will show where your ship is in relation toward Earth.");
          step = 2;
        }
        break;
      case 2:
        if (gameLoop.getPlanet("Earth").getLocation()
            .getDistance(playerShip.getPosition()) < 1200) {
          tracker
              .setMessage("We're coming up on Earth and Freedom station now.");
          tracker
              .setMessage("To avoid overshooting your target\n or crashing, slow down by accelerating\nin the opposite direction of your ships movement.");
          tracker
              .setMessage("Get close to Freedom station and we'll walk you through docking procedures");
          step = 3;
        }
        break;
      case 3:
        if (gameLoop.getShip("Freedom Station").getPosition()
            .getDistance(playerShip.getPosition()) < 100) {
          tracker.setMessage("Good! You're almost there.");
          tracker
              .setMessage("When you dock, you can refuel and get supplies.\n First, maneuver your ship until its speed matches your target.");
          step = 4;
        }
        break;
      case 4:
        if (playerShip.dockable(gameLoop.getShip("Freedom Station"))) {
          tracker.setMessage("Well done!");
          tracker
              .setMessage("Now that you are both close to another ship and have a similar speed,\nyour HUD will indicate that you can dock with Freedom Station by pressing ENTER");
          step = 5;
        }
        break;
      case 5:
        //TODO Get this to show up before docking menu.
        if(playerShip.getFuel()==playerShip.getMaxFuel() && tracker.dockedWith()==null ){
          tracker.setMessage("You've refueled, but your ship is still a mess.\n\nHow about making some money to make repairs?");
          tracker.setMessage("There isn't much Nitrogen on the Moon, so it usually goes for a good price.\n"+
              "Try purchasing some Nitrogen here at Freedom Station and delivering it to the Odyssey orbiter");
          tracker.setMessage("To get you started on your way, here's some money.");
          tracker.setMessage("I'll find a way for you to repay me later.");
          playerShip.addMoney(100);
          tracker.setMessage("Receive $100");
          step=6;
        }
        break;
      case 6:
        if(playerShip.getItemCount("Nitrogen")>0){
          tracker.setMessage("The Earth has more gravity making it harder to escape.\n"+
              " Again, you want to build speed on the backside of your orbit and\n let gravity slingshot you along!");
          tracker.setMessage("Also, don't forget to use (N) to get guidance navigating to different targets");
          step=7;
        }
        break;
      case 7:
        if(playerShip.dockable(gameLoop.getShip("Odyssey"))){
          tracker.setMessage("Now, dock with Odyssey\n and sell your Nitrogen.");
          step=8;
        }
        break;
      case 8:
        if(playerShip.getItemCount("Nitrogen")==0){
          tracker.setMessage("CAPCOM: It's not much, but that'll help you fund repairs.");
          //TODO implement
          tracker.setMessage("There's two ways of making money with a ship like that:\n buying and transporting goods from one place to another\nand doing missions for people without their own ship.");
          tracker.setMessage("Try checking in with Odyssey to see if they have any work for you.");
          complete();
          step=9;
        }
        break;
      default:
        break;
      }
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
    return true;
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
