package net.kingsbery.games;

import java.util.ArrayList;
import java.util.List;

import net.kingsbery.games.menu.InGameMenu;
import net.kingsbery.games.menu.MenuItem;
import net.kingsbery.games.menu.MissionSelectionMenuItem;
import net.kingsbery.games.menu.PurchaseSuppliesMenuItem;
import net.kingsbery.games.menu.SellSuppliesMenuItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * For now, "docking" just brings up a menu, which is not very satisfying but
 * allows for focusing on the interesting part (exploring space).
 * 
 * @author jamie
 * 
 */
public class DockMenu extends InGameMenu {
  private static Log log = LogFactory.getLog(DockMenu.class);
  private Ship ship;

  public DockMenu(GameStateTracker tracker, Ship ship) {
    super(tracker);
    log.info("Docking with " + ship);
    this.ship = ship;
    List<MenuItem> values=new ArrayList<MenuItem>();
    if(ship.getCanRefuel()){
      values.add(new RefuelMenuItem(this,tracker.getPlayer()));
      values.add(new BuyShipUpgradeMenuItem(this,ship));
    }
    values.add(new PurchaseSuppliesMenuItem(this,ship,tracker.getPlayer()));
    values.add(new SellSuppliesMenuItem(this, ship));
    values.add(new MissionSelectionMenuItem(this,ship));
    values.add(new ExitMenuItem(tracker,this));
    this.push(values);
    
  }
  
  public static class RefuelMenuItem extends MenuItem{

    private Ship ship;
    private Menu menu;

    public RefuelMenuItem(DockMenu menu, Ship ship){
      this.ship=ship;
      this.menu=menu;
    }
    
    @Override
    public String getName() {
      return "Refuel";
    }

    @Override
    public void action() {
      ship.setFuel(ship.getMaxFuel());
      menu.pop();
    }
    
  }

  private List<MenuItem> getMenuItems(Ship ship) {
    List<MenuItem> result = new ArrayList<MenuItem>();
    if (ship.getCanRefuel()) {
      result.add(new RefuelMenuItem(this,this.getPlayer()));
//      return new String[] { "Refuel", "Purchase Supplies", "Sell Supplies","Upgrade Ship",
//          "Missions", "Exit" };
    } else {
//      return new String[] { "Purchase Supplies", "Sell Supplies", "Missions",
//          "Exit" };
    }
    result.add(new ExitMenuItem(getTracker(),this));
    return result;
  }


  
  @MenuAction("Upgrade Ship")
  public void upgradeShip(){
  }
  
  public static class ExitMenuItem extends MenuItem{

    private GameStateTracker tracker;
    private Menu menu;

    public ExitMenuItem(GameStateTracker tracker, Menu menu) {
      this.tracker=tracker;
      this.menu=menu;
    }
    
    @Override
    public void action(){
      if(!menu.stillGoing()){
        tracker.goBack();
      }
      menu.pop();
    }

    @Override
    public String getName() {
      return "Back";
    }
    
  }
  

  private Ship getPlayer() {
    return getTracker().getGameLoop().getPlayerShip();
  }

}
