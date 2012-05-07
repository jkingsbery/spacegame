package net.kingsbery.games.world;

import net.kingsbery.games.graphics.LunarColony;
import net.kingsbery.games.math.Vector3;

import org.codehaus.jackson.annotate.JsonIgnore;

public class Player {

  public Vector3 position = new Vector3(0, 0, 1.5);
  public Vector3 focus = new Vector3(100, 0, 1.5);
  public Vector3 up = new Vector3(0, 0, 1);

  @JsonIgnore
  public Vector3 getDirection() {
    return position.minus(focus).normalize();
  }

  public Vector3 perp() {
    return getDirection().cross(up).normalize();
  }

  public void walkForward(LunarColony colony) {
    Vector3 direction = getDirection().projectXY(0);
    Vector3 newPosition = position.plus(direction.scale(-1));
    if (colony.noCollision(newPosition, 1)) {
      focus = focus.plus(direction.scale(-1));
      position = newPosition;
    }
  }

  public void walkBackward() {
    Vector3 direction = getDirection();
    position = position.plus(direction);
    focus = focus.plus(direction);
  }

  public void strafeLeft() {
    Vector3 leftDir = perp();
    position = position.plus(leftDir);
    focus = focus.plus(leftDir);
  }

  public void strafeRight() {
    Vector3 leftDir = perp();
    position = position.plus(leftDir.scale(-1));
    focus = focus.plus(leftDir);
  }

  public void jump() {

  }

}
