package net.kingsbery.games;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import net.kingsbery.games.math.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GameKeyListener implements KeyListener {

  private static final Log log = LogFactory.getLog(GameKeyListener.class);

  private GameStateTracker tracker;

  public GameKeyListener(GameStateTracker tracker) {
    this.tracker = tracker;
  }

  public GameLoop getGameLoop() {
    return tracker.getGameLoop();
  }

  @Override
  public void keyTyped(KeyEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (tracker.hasMessage()) {
      if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        tracker.popMessage();
      }
    } else if (GameMode.SPACE == tracker.getMode()) {
      if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
        System.out.println("END");
        tracker.pause();
      } else if (e.getKeyCode() == KeyEvent.VK_UP) {
        this.getGameLoop().getPlayerShip().thrust(new Vector(0, -1));
      } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
        this.getGameLoop().getPlayerShip().thrust(new Vector(0, 1));
      } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        this.getGameLoop().getPlayerShip().thrust(new Vector(-1, 0));
      } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        this.getGameLoop().getPlayerShip().thrust(new Vector(1, 0));
      } else if (e.getKeyCode() == KeyEvent.VK_N) {
        getGameLoop().getPlayerShip().nextTarget(getGameLoop().getPlanets());
      } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        tracker.playerAction();
      }
    } else if (GameMode.PAUSE_MENU == tracker.getMode()
        || GameMode.DOCKED == tracker.getMode()) {
      Menu menu = tracker.getMenu();
      if (e.getKeyCode() == KeyEvent.VK_UP) {
        menu.dec();
        log.debug("Up");
      } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
        menu.inc();
      } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        menu.enter();
      }
    } else if (GameMode.LANDING == tracker.getMode()) {
      Ship lander = this.tracker.getLander();
      if (e.getKeyCode() == KeyEvent.VK_UP) {
        lander.thrust(new Vector(0, 3));
      } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
        lander.thrust(new Vector(0, -3));
      } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        lander.thrust(new Vector(-3, 0));
      } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        lander.thrust(new Vector(3, 0));
      } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
      } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
        System.exit(0);
      }
    } else if (GameMode.MODELING == tracker.getMode()) {
      Vector focus = tracker.getFocus();
      if (e.getKeyCode() == KeyEvent.VK_UP) {
        tracker.shiftFocus(new Vector(0, -1));
      } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
        tracker.shiftFocus(new Vector(0, 1));
      } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        tracker.shiftFocus(new Vector(-1, 0));
      } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        tracker.shiftFocus(new Vector(1, 0));
      } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
        System.exit(0);
      }
    } else {
      if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
        System.exit(0);
      }
    }

  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (GameMode.SPACE == tracker.getMode()) {
      getGameLoop().getPlayerShip().stopThrust();
    } else if (GameMode.LANDING == tracker.getMode()) {
      tracker.getLander().stopThrust();
    }
  }

}
