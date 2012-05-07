package net.kingsbery.games.math;

import static org.junit.Assert.*;

import org.junit.Test;

public class CrossProductTest {

  
  @Test
  public void i(){
    assertEquals(Vector3.ZERO,Vector3.I.cross(Vector3.I));
  }
  
  @Test
  public void j(){
    assertEquals(Vector3.ZERO,Vector3.I.cross(Vector3.I));
  }
  
  @Test
  public void k(){
    assertEquals(Vector3.ZERO,Vector3.I.cross(Vector3.I));
  }
  
  @Test
  public void ij(){
    assertEquals(Vector3.K,Vector3.I.cross(Vector3.J));
  }
  
  @Test
  public void jk(){
    assertEquals(Vector3.I,Vector3.J.cross(Vector3.K));
  }
  
  @Test
  public void ki(){
    assertEquals(Vector3.J,Vector3.K.cross(Vector3.I));
  }
  
  @Test
  public void foo(){
    System.out.println(new Vector3(100,0,0).cross(Vector3.K));
  }
  
}
