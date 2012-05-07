package net.kingsbery.games;

import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.kingsbery.games.menu.MenuItem;

public class StartMenu extends Menu {

  private App app;
  private KeyListener keyListener;

  public StartMenu(App app) {
    List<MenuItem> items = new ArrayList<MenuItem>();
    items.add(new StartGameMenuItem());
    items.add(new DebugGameMenuItem());
    items.add(new LoadGameMenuItem());
    items.add(new ModelingMenuItem());
    items.add(new CloseProgramMenuItem());
    push(items);

    this.app = app;
  }


  private class ModelingMenuItem extends MenuItem{

    @Override
    public String getName() {
      return "Colony Model";
    }

    @Override
    public void action() {
      GameStateTracker gameStateTracker = new GameStateTracker(app);
      app.setTracker(gameStateTracker);
      gameStateTracker.showModel();
    }
    
  }
  
  private class StartGameMenuItem extends MenuItem {

    @Override
    public String getName() {
      return "Start Game";
    }

    @Override
    public void action() {
      GameStateTracker gameStateTracker = new GameStateTracker(app);
      gameStateTracker.setGameLoop(GameLoop.load("sol.json"));
      app.setTracker(gameStateTracker);
      gameStateTracker.start();

    }
  }

  private class DebugGameMenuItem extends MenuItem {

    @Override
    public String getName() {
      return "Debug";
    }

    @Override
    public void action() {
      GameStateTracker gameStateTracker = new GameStateTracker(app);
      gameStateTracker.setGameLoop(GameLoop.load("debug.json"));
      app.setTracker(gameStateTracker);
      gameStateTracker.start();

    }
  }

  private class LoadGameMenuItem extends MenuItem {

    @Override
    public String getName() {
      return "Load Game";
    }

    @Override
    public void action() {
      GameStateTracker gameStateTracker = new GameStateTracker(app);
      gameStateTracker.setGameLoop(GameLoop.load(new File("save.json")));
      app.setTracker(gameStateTracker);
      gameStateTracker.start();
    }
  }

  private class CloseProgramMenuItem extends MenuItem {

    @Override
    public String getName() {
      return "Exit";
    }

    @Override
    public void action() {
      app.done();
    }
  }

  public boolean waitingForSelection() {
    return true;
  }

  public KeyListener getKeyListener() {
    if (keyListener == null) {
      this.keyListener = new StartmenuKeyListener(this);
    }
    return this.keyListener;
  }

}
