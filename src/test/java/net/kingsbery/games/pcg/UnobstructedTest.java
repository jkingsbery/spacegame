package net.kingsbery.games.pcg;

import static org.junit.Assert.*;
import net.kingsbery.games.math.Vector;

import org.junit.Test;

public class UnobstructedTest {

  @Test
  public void sameBuildingIntersects(){
    Building a=new Building(new Vector(0,0),10,10,10);
    Building b=new Building(new Vector(0,0),10,10,10);
    assertTrue(ColonyMap.intersection(a, b));
  }
  
  @Test
  public void notQuiteSameIntersect(){
    Building a=new Building(new Vector(0,0),10,10,10);
    Building b=new Building(new Vector(5,5),10,10,10);
    assertTrue(ColonyMap.intersection(a, b));
  }
  
  
  @Test
  public void farBuildingsDontIntersect(){
    Building a=new Building(new Vector(0,0),10,10,10);
    Building b=new Building(new Vector(1000,1000),10,10,10);
    assertFalse(ColonyMap.intersection(a, b));
  }
  
}
