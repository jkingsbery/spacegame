package net.kingsbery.games.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.kingsbery.games.DockMenu.ExitMenuItem;
import net.kingsbery.games.Menu;
import net.kingsbery.games.Ship;
import net.kingsbery.games.objects.GameItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PurchaseSuppliesMenuItem extends MenuItem {

  private static final Log log = LogFactory.getLog(PurchaseSuppliesMenuItem.class);
  private Menu menu;
  private Ship ship;
  private Ship player;
  
  public PurchaseSuppliesMenuItem(Menu menu, Ship ship, Ship player){
    this.menu=menu;
    this.ship=ship;
    this.player=player;
  }
  
  @Override
  public String getName() {
    return "Purchase Supplies";
  }

  @Override
  public void action() {
    Map<String, GameItem> available = ship.getItems();
    log.info("Available items " + ship.getItems());
    List<MenuItem> result = new ArrayList<MenuItem>();
    
    for (GameItem item : available.values()) {
      assert item!=null;
      if(item.getCount()>0){
        result.add(new PurchaseMenuItem(menu,item,player,this.ship));
      }
    }
    result.add(new ExitMenuItem(((InGameMenu)menu).getTracker(),menu));
    menu.push(result);
  }

}
