import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import net.kingsbery.games.DockMenu;
import net.kingsbery.games.GameStateTracker;
import net.kingsbery.games.Ship;
import net.kingsbery.games.objects.GameItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class PurchaseMenuItemTest {

  private static Log log = LogFactory.getLog(PurchaseMenuItemTest.class);
  
  @Test
  public void foo(){
    
    GameStateTracker tracker=mock(GameStateTracker.class);
    Ship ship=mock(Ship.class);
    Ship player=mock(Ship.class);
    Map<String, GameItem> items=new HashMap<String,GameItem>();
    items.put("Widget",new GameItem("Widget",10000,1000));
    when(ship.getItems()).thenReturn(items);
    when(tracker.getPlayer()).thenReturn(player);
    DockMenu dock = new DockMenu(tracker, ship);
    log.info(dock.getOptions());
    dock.enter();//Purchase Items
    log.info(dock.getOptions());
    dock.enter();//Widgets
    dock.dec();
    dock.dec();
    dock.dec();
    dock.enter();
//    log.info(dock.getOptions());
    verify(player).addItem(new GameItem("Widget",3,1000));
  }
  
}
