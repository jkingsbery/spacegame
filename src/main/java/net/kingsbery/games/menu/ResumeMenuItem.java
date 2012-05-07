package net.kingsbery.games.menu;

import net.kingsbery.games.GameStateTracker;

public class ResumeMenuItem extends MenuItem {

  private GameStateTracker tracker;

  public ResumeMenuItem(GameStateTracker tracker) {
    this.tracker=tracker;
  }

  @Override
  public String getName() {
    return "Resume";
  }

  @Override
  public void action() {
    this.tracker.goBack();
  }

}
