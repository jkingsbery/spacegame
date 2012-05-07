package net.kingsbery.games.menu;

import java.io.File;

import net.kingsbery.games.GameStateTracker;
import net.kingsbery.games.util.JsonMapper;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

public class SaveGameMenuItem extends MenuItem {

  private GameStateTracker tracker;

  public SaveGameMenuItem(GameStateTracker tracker) {
    this.tracker = tracker;
  }

  @Override
  public String getName() {
    return "Save Game";
  }

  @Override
  public void action() {
    try {
      ObjectMapper mapper = JsonMapper.getInstance();
      mapper.writeValue(new File("save.json"), tracker.getGameLoop());
      tracker.goBack();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

}
