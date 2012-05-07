package net.kingsbery.games.menu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.kingsbery.games.DockMenu.ExitMenuItem;
import net.kingsbery.games.Ship;
import net.kingsbery.games.objects.GameItem;

public class SellSuppliesMenuItem extends MenuItem {

  private InGameMenu menu;
  private Ship ship;

  public SellSuppliesMenuItem(InGameMenu menu,Ship ship){
    this.menu=menu;
    this.ship=ship;
  }
  
  @Override
  public String getName() {
    return "Sell Items";
  }

  @Override
  public void action() {
    Collection<GameItem> values = menu.getTracker().getGameLoop().getPlayerShip()
        .getItems().values();
    List<MenuItem> menuItems = new ArrayList<MenuItem>();
    for (GameItem item : values) {
      if (item.getCount() > 0) {
        menuItems.add(new SellMenuItem(menu, item, menu.getTracker().getPlayer(), ship));
      }
    }
    menuItems.add(new ExitMenuItem(menu.getTracker(), menu));
    menu.push(menuItems);
  }

}
