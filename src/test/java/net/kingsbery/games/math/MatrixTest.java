package net.kingsbery.games.math;

import static org.junit.Assert.*;

import org.junit.Test;

public class MatrixTest {

  private static final double ACCURACY = 1e-5;

  @Test
  public void left90degrees() {
    assertEquals(0, Matrix.LEFT_TURN.a11, ACCURACY);
    assertEquals(-1, Matrix.LEFT_TURN.a12, ACCURACY);
    assertEquals(1, Matrix.LEFT_TURN.a21, ACCURACY);
    assertEquals(0, Matrix.LEFT_TURN.a22, ACCURACY);
  }

  @Test
  public void left45degrees() {
    assertEquals(0.7071068, Matrix.LEFT_TURN_45.a11, ACCURACY);
    assertEquals(-0.7071068, Matrix.LEFT_TURN_45.a12, ACCURACY);
    assertEquals(0.7071068, Matrix.LEFT_TURN_45.a21, ACCURACY);
    assertEquals(0.7071068, Matrix.LEFT_TURN_45.a22, ACCURACY);
  }
  
  @Test
  public void mult(){
    Vector r = Matrix.LEFT_TURN_45.mult(new Vector(0,1));
    assertEquals(-0.7071068,r.getX(),ACCURACY);
    assertEquals(0.7071068,r.getY(),ACCURACY);
  }
  
  @Test
  public void rotationAroundX(){
    Matrix3 a=Matrix3.rotationMatrix(Vector3.I,0.1);
    Matrix3 b=Matrix3.rotationMatrixX(0.1);
    for(int i=0; i<3; i++){
      for(int j=0; j<3; j++){
        assertEquals("At ("+i+","+j+"):" ,a.a[i][j],b.a[i][j],ACCURACY);          
      }
    }
  }
  
  @Test
  public void rotationAroundY(){
    Matrix3 a=Matrix3.rotationMatrix(Vector3.J,0.1);
    Matrix3 b=Matrix3.rotationMatrixY(0.1);
    for(int i=0; i<3; i++){
      for(int j=0; j<3; j++){
        assertEquals("At ("+i+","+j+"):" ,a.a[i][j],b.a[i][j],ACCURACY);          
      }
    }
  }
  
  @Test
  public void rotationAroundZ(){
    Matrix3 a=Matrix3.rotationMatrix(Vector3.K,0.1);
    Matrix3 b=Matrix3.rotationMatrixZ(0.1);
    for(int i=0; i<3; i++){
      for(int j=0; j<3; j++){
        assertEquals("At ("+i+","+j+"):" ,a.a[i][j],b.a[i][j],ACCURACY);        
      }
    }
  }
  
  
}
