package net.kingsbery.games.graphics;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import net.kingsbery.games.math.Matrix3;
import net.kingsbery.games.math.Vector3;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FullScreenKeyListener implements KeyListener {

  private static final Log log = LogFactory.getLog(FullScreenKeyListener.class);

  private GameWorldGraphics colony;

  public FullScreenKeyListener(GameWorldGraphics colony) {
    this.colony = colony;
  }

  @Override
  public void keyTyped(KeyEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void keyReleased(KeyEvent e) {
    // TODO Auto-generated method stub

  }

  private static final double rotation = Math.PI / 72 / 2;

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
      System.exit(0);
    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
      Vector3 leftDir = colony.getPlayer().perp();
      colony.getPlayer().focus=rotate(rotate(colony.getPlayer().focus,leftDir,rotation),leftDir,rotation);
    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
      Vector3 leftDir = colony.getPlayer().perp();
      colony.getPlayer().focus=rotate(rotate(colony.getPlayer().focus,leftDir,-rotation),leftDir,-rotation);
    } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
      colony.getPlayer().focus = rotateZ(rotateZ(colony.getPlayer().focus, rotation), rotation);
    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
      colony.getPlayer().focus = rotateZ(rotateZ(colony.getPlayer().focus, -rotation), -rotation);
    } else if (e.getKeyCode() == KeyEvent.VK_W) {
      colony.getPlayer().walkForward(colony);
    } else if (e.getKeyCode() == KeyEvent.VK_S) {
      colony.getPlayer().walkBackward();
    } else if (e.getKeyCode() == KeyEvent.VK_A) {
      colony.getPlayer().strafeLeft();
    } else if (e.getKeyCode() == KeyEvent.VK_D) {
      colony.getPlayer().strafeRight();
    } else if(e.getKeyCode()==KeyEvent.VK_SPACE){
      colony.getPlayer().jump();
    }
    log.info(colony.getPlayer().position+","+colony.getPlayer().focus+","+colony.getPlayer().up);
  }

  
  


  private Vector3 rotateX(Vector3 v,  double rotation) {
    Vector3 dir = colony.getPlayer().position.minus(v);
    Vector3 newDir = Matrix3.rotationMatrixX(rotation).mult(dir);
    return newDir.plus(colony.getPlayer().position);
  }
  
  private Vector3 rotate(Vector3 v, Vector3 u, double rotation) {
    Vector3 dir = colony.getPlayer().position.minus(v);
    Vector3 newDir = Matrix3.rotationMatrix(u,rotation).mult(dir);
    return newDir.plus(colony.getPlayer().position);
  }
  
  private Vector3 rotateZ(Vector3 v, double rotation) {
    Vector3 dir = colony.getPlayer().position.minus(v);
    Vector3 newDir = Matrix3.rotationMatrixZ(rotation).mult(dir);
    return newDir.plus(colony.getPlayer().position);
  }

}
