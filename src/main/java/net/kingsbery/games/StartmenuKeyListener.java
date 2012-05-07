package net.kingsbery.games;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class StartmenuKeyListener implements KeyListener {

  private StartMenu menu;

  public StartmenuKeyListener(StartMenu menu) {
    this.menu=menu;
  }

  @Override
  public void keyTyped(KeyEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_UP) {
      menu.dec();
    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
      menu.inc();
    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
      menu.enter();
    }

  }

  @Override
  public void keyReleased(KeyEvent e) {
  }

}
