package net.kingsbery.games;

import java.util.ArrayList;
import java.util.List;

import net.kingsbery.games.DockMenu.ExitMenuItem;
import net.kingsbery.games.menu.InGameMenu;
import net.kingsbery.games.menu.MenuItem;
import net.kingsbery.games.menu.ShipUpgradeMenuItem;

public class BuyShipUpgradeMenuItem extends MenuItem {

  private InGameMenu menu;
  private Ship ship;

  public BuyShipUpgradeMenuItem(InGameMenu dockMenu, Ship ship) {
    this.menu=dockMenu;
    this.ship=ship;
  }

  @Override
  public String getName() {
    return "Purchase Ship Upgrades";
  }

  @Override
  public void action() {
    List<MenuItem> menuItems = new ArrayList<MenuItem>();
    for(ShipUpgrade upgrade : ShipUpgrade.values()){
      if(!this.ship.hasUpgrade(upgrade) && upgrade.hasPrerequisites(this.menu.getTracker())){
        menuItems.add(new ShipUpgradeMenuItem(upgrade,menu.getTracker(),menu.getTracker().getPlayer()));
      }
    }
    menuItems.add(new ExitMenuItem(menu.getTracker(),menu));
    menu.push(menuItems);

  }

}
