package net.kingsbery.games.menu;

import net.kingsbery.games.Menu;
import net.kingsbery.games.Menu.CounterFrameCallback;
import net.kingsbery.games.Ship;
import net.kingsbery.games.objects.GameItem;

public class SellMenuItem extends MenuItem {

  private GameItem gameItem;
  private Menu menu;
  private Ship player;
  private Ship dockedWith;

  public SellMenuItem(Menu menu, GameItem x, Ship player, Ship dockedWith) {
    this.gameItem = x;
    this.menu = menu;
    this.player = player;
    this.dockedWith = dockedWith;
  }

  private GameItem getItem() {
    return this.gameItem;
  }

  @Override
  public void action() {
    menu.pushCounter(new CounterFrameCallback() {

      @Override
      public void handle(int x) {
        gameItem=player.removeItem(getItem().getName(), x);
        dockedWith.addItem(new GameItem(getItem().getName(), x, dockedWith
            .getCost(getItem())));
        player.addMoney(dockedWith.getCost(getItem())*x);
        menu.pop();
      }

    }, getItem().getCount(), 0);
  }

  @Override
  public String getName() {
    return this.getItem() + " @ " + dockedWith.getCost(getItem());
  }

}
