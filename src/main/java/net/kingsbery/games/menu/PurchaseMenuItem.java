package net.kingsbery.games.menu;

import net.kingsbery.games.Menu;
import net.kingsbery.games.Menu.CounterFrameCallback;
import net.kingsbery.games.Ship;
import net.kingsbery.games.objects.GameItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PurchaseMenuItem extends MenuItem {

  private static final Log log = LogFactory.getLog(PurchaseMenuItem.class);
  
  private GameItem item;
  private Menu menu;
  private Ship player;
  private Ship dockedWith;

  public PurchaseMenuItem(Menu menu, GameItem item, Ship player, Ship dockedWith) {
    this.item = item;
    this.menu = menu;
    this.player = player;
    this.dockedWith = dockedWith;
    assert this.player != null;

  }

  private GameItem getItem() {
    return this.item;
  }

  @Override
  public void action() {
    menu.pushCounter(new CounterFrameCallback() {

      @Override
      public void handle(int x) {
        if (player.getDollars() >= dockedWith.getCost(item) * x) {
          log.info("Buying " + x + " of " + getItem().getName() + " for "
              + dockedWith.getCost(item) + " each");
          player.addItem(new GameItem(getItem().getName(), x, item.getCost()));
          item=dockedWith.removeItem(getItem().getName(), x);
          player.decreaseMoney(dockedWith.getCost(getItem())*x);
          menu.pop();
        }
      }
    },item.getCount(),0);
  }

  @Override
  public String getName() {
    return getItem().getName() + "(" + getItem().getCount() + ")"
        + dockedWith.getCost(getItem());
  }

}
