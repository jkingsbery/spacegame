package net.kingsbery.games.graphics;

import javax.media.opengl.GL;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import net.kingsbery.games.math.Vector3;
import net.kingsbery.games.world.Player;

public abstract class GameWorldGraphics extends GLCanvas implements GLEventListener{

  private Player player = new Player();
  
  public Player getPlayer() {
    return this.player;
  }
  
  protected void setCamera(GL gl, GLU glu) {
    // Change to projection matrix.
    gl.glMatrixMode(GL.GL_PROJECTION);
    gl.glLoadIdentity();

    // Perspective.

    // Perspective.
    float widthHeightRatio = (float) getWidth() / (float) getHeight();
    glu.gluPerspective(45, widthHeightRatio, 1, 1000);
    glu.gluLookAt(player.position.getX(), player.position.getY(),
        player.position.getZ(), player.focus.getX(), player.focus.getY(),
        player.focus.getZ(), player.up.getX(), player.up.getY(),
        player.up.getZ());

    // Change back to model view matrix.
    gl.glMatrixMode(GL.GL_MODELVIEW);
    gl.glLoadIdentity();
  }

  public abstract boolean noCollision(Vector3 newPosition, int i);



}
