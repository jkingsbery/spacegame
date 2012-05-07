package net.kingsbery.games.menu;

import net.kingsbery.games.GameStateTracker;
import net.kingsbery.games.Menu;
import net.kingsbery.games.scenario.Scenario;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ScenarioMenuItem extends MenuItem{

  private static final Log log = LogFactory.getLog(ScenarioMenuItem.class);
  
  private Scenario scenario;
  private GameStateTracker tracker;
  private Menu menu;


  public ScenarioMenuItem(Menu menu,Scenario scenario,GameStateTracker tracker){
    this.scenario=scenario;
    this.menu=menu;
    this.tracker=tracker;
  }
  
  private Scenario getItem(){
    return this.scenario;
  }
  
  @Override
  public void action(){
    Scenario scenario = (Scenario) getItem();
    log.info("Starting scenario " + scenario.getClass().getSimpleName());
    tracker.addScenario(scenario);
    menu.pop();
  }

  @Override
  public String getName() {
    return scenario.toString();
  }
  
}
