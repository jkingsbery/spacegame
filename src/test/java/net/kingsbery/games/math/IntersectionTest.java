package net.kingsbery.games.math;

import static org.junit.Assert.*;
import net.kingsbery.games.pcg.Road;

import org.junit.Test;

public class IntersectionTest {

  @Test
  public void foo(){
    Road road = new Road(new Vector(0,100),new Vector(100,0));
    assertTrue(road.divides(new Vector(0,0),new Vector(100,100)));
  }
  
}
