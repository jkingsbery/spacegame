package net.kingsbery.games.menu;

import net.kingsbery.games.GameStateTracker;
import net.kingsbery.games.Menu;

public class InGameMenu extends Menu {

  //TODO if we're going to pass around tracker everywhere, that'll add lots of coupling.
  public InGameMenu(GameStateTracker tracker){
    this.tracker=tracker;
  }
  private GameStateTracker tracker;
  
  public GameStateTracker getTracker() {
    return this.tracker;
  }
  
  @Override
  public void pop(){
    super.pop();
    if(!stillGoing()){
      getTracker().goBack();
    }
  }
  
}
