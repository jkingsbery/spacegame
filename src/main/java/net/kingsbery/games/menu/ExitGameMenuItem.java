package net.kingsbery.games.menu;

import net.kingsbery.games.GameStateTracker;

public class ExitGameMenuItem extends MenuItem {

  private GameStateTracker tracker;

  public ExitGameMenuItem(GameStateTracker tracker){
    this.tracker=tracker;
  }
  
  @Override
  public String getName() {
    return "Exit";
  }

  @Override
  public void action() {
    tracker.startMenu();
  }
}