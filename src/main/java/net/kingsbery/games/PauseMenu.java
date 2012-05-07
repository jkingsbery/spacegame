package net.kingsbery.games;

import java.util.ArrayList;
import java.util.List;

import net.kingsbery.games.menu.ExitGameMenuItem;
import net.kingsbery.games.menu.InGameMenu;
import net.kingsbery.games.menu.MenuItem;
import net.kingsbery.games.menu.ResumeMenuItem;
import net.kingsbery.games.menu.SaveGameMenuItem;

public class PauseMenu extends InGameMenu {

  public PauseMenu(GameStateTracker tracker) {
    super(tracker);
    List<MenuItem> items = new ArrayList<MenuItem>();
    if(getTracker().getPlayer().isAlive()){
      items.add(new ResumeMenuItem(getTracker()));
      items.add(new SaveGameMenuItem(getTracker()));
    }
    items.add(new ExitGameMenuItem(getTracker()));
    this.push(items);
  }

}
