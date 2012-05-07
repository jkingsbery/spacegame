package net.kingsbery.games.menu;

import net.kingsbery.games.GameStateTracker;
import net.kingsbery.games.Ship;
import net.kingsbery.games.ShipUpgrade;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ShipUpgradeMenuItem extends MenuItem {

  private static final Log log = LogFactory.getLog(ShipUpgradeMenuItem.class);
  
  private ShipUpgrade upgrade;
  private Ship ship;
  private GameStateTracker tracker;


  public ShipUpgradeMenuItem(ShipUpgrade upgrade,GameStateTracker tracker, Ship ship) {
    this.upgrade=upgrade;
    this.ship=ship;
    this.tracker=tracker;
  }
  
  @Override
  public void action(){
    log.info("Upgrading ship " + this.upgrade);
    ship.decreaseMoney(upgrade.getCost());
    if(upgrade==ShipUpgrade.MarsCoordinates){
      ship.addKnownPlanet(tracker.getGameLoop().getPlanet("Mars"));
    }else if(upgrade==ShipUpgrade.SmallFuelTank){
      ship.setMaxFuel(500);
      ship.setFuel(ship.getMaxFuel());
    }
    ship.addUpgrade(upgrade);
  }

  @Override
  public String getName() {
    return upgrade.name();
  }

}
