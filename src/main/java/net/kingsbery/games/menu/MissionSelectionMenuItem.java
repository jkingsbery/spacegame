package net.kingsbery.games.menu;

import java.util.ArrayList;
import java.util.List;

import net.kingsbery.games.DockMenu.ExitMenuItem;
import net.kingsbery.games.Ship;
import net.kingsbery.games.scenario.Scenario;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MissionSelectionMenuItem extends MenuItem {

  private static Log log = LogFactory.getLog(MissionSelectionMenuItem.class);
  
  private InGameMenu menu;
  private Ship ship;
  public MissionSelectionMenuItem(InGameMenu dockMenu, Ship ship) {
    this.menu=dockMenu;
    this.ship=ship;
  }

  @Override
  public String getName() {
    return "Available Missions";
  }

  @Override
  public void action() {
    ArrayList<MenuItem> possibleScenarios = new ArrayList<MenuItem>();

    List<Class<? extends Scenario>> scenarioClasses = ship.getMissions();
    for (Class<? extends Scenario> clazz : scenarioClasses) {
      Scenario scenario = getInstance(clazz);
      if (scenario.prerequisitesMet(menu.getTracker())
          && (!menu.getTracker().hasStartedScenario(scenario))) {
        log.info("Scenario available " + scenario);
        possibleScenarios
            .add(new ScenarioMenuItem(menu, scenario, menu.getTracker()));
      }
      else{
        log.info("Scenario unavailable " + scenario);
      }
    }
    possibleScenarios.add(new ExitMenuItem(menu.getTracker(), menu));

    menu.push(possibleScenarios);

  }
  private Scenario getInstance(Class<? extends Scenario> clazz) {
    try {
      return clazz.newInstance();
    } catch (Exception e) {
      throw new RuntimeException();
    }
  }

}
