package net.kingsbery.games.pcg;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

import net.kingsbery.games.math.Matrix;
import net.kingsbery.games.math.Vector;
import net.kingsbery.games.pcg.LSystem.StochasticLSystem;
import net.kingsbery.games.util.JsonMapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;

public class SystemDrawer {

  private static Log log = LogFactory.getLog(SystemDrawer.class);

  private static class Frame {
    final Vector position;
    final Vector angle;

    public Frame(Vector pos, Vector a) {
      this.position = pos;
      this.angle = a;
    }
  }

  public static ColonyMap drawMap(LSystem system, int iter) {
    ColonyMap map = new ColonyMap();
    String str = system.eval(iter);

    Stack<Frame> stack = new Stack<Frame>();
    Vector position = new Vector(0, 0);
    Vector direction = new Vector(0, -1);
    int length = 1;
    System.out.println(str);
    for (int i = 0; i < str.length(); i++) {
      if (str.charAt(i) == '0' || str.charAt(i) == '1') {
        int j=i+1;
        while(j<str.length()&&isRoad(str.charAt(j))){
          j++;
        }
        j--;
        Vector nextPosition = position.plus(direction.scale(length*(j-i)));
        map.drawRoad(position,nextPosition);
        position = nextPosition;
        i=j;
      } else if (str.charAt(i) == '[') {
        stack.push(new Frame(position, direction));
      } else if (str.charAt(i) == ']') {
        Frame frame = stack.pop();
        position = frame.position;
        direction = frame.angle;
      } else if (str.charAt(i) == '<') {
        direction = Matrix.LEFT_TURN_5.mult(direction);
      } else if (str.charAt(i) == '>') {
        direction = Matrix.RIGHT_TURN_5.mult(direction);
      }
    }
    map.createRegions();
    return map;
  }

  private static boolean isRoad(char charAt) {
    return charAt=='0' || charAt=='1';
  }

  public static void draw(Vector focus, LSystem system, int iter,
      Rectangle bounds, Graphics g) {
    String str = system.eval(iter);

    Stack<Frame> stack = new Stack<Frame>();
    Vector position = new Vector(bounds.width / 2, bounds.height
        - (bounds.height / 8));
    Vector direction = new Vector(0, -1);
    int length = 1;
    for (int i = 0; i < str.length(); i++) {
      if (str.charAt(i) == '0' || str.charAt(i) == '1') {
        Vector nextPosition = position.plus(direction.scale(length));
        g.drawLine((int) (position.getX() - focus.getX()),
            (int) (position.getY() - focus.getY()),
            (int) (nextPosition.getX() - focus.getX()),
            (int) (nextPosition.getY() - focus.getY()));
        position = nextPosition;
      } else if (str.charAt(i) == '[') {
        stack.push(new Frame(position, direction));
      } else if (str.charAt(i) == ']') {
        Frame frame = stack.pop();
        position = frame.position;
        direction = frame.angle;
      } else if (str.charAt(i) == '<') {
        direction = Matrix.LEFT_TURN_5.mult(direction);
      } else if (str.charAt(i) == '>') {
        direction = Matrix.RIGHT_TURN_5.mult(direction);
      }
    }
    g.setColor(Color.blue);
    g.fillOval(bounds.width / 2, bounds.height / 2, 3, 3);
  }
  public static void main(String args[]) throws IOException {
    ColonyMap map = SystemDrawer.drawMap(new StochasticLSystem(), 7);
    map.setName("Tranquility Colony");
    ColonyMap.toImage(map);
    ObjectMapper mapper = JsonMapper.getInstance();
    mapper.writeValue(new File(map.getFileName()+".json"), map);
  }
}
