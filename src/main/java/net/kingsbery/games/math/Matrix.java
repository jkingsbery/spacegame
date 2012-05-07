package net.kingsbery.games.math;


public class Matrix {

  public static final Matrix LEFT_TURN = rotationMatrix(Math.PI/2);
  public static final Matrix RIGHT_TURN = rotationMatrix(-Math.PI/2);

  public static final Matrix LEFT_TURN_45 = rotationMatrix(Math.PI/4);
  public static final Matrix RIGHT_TURN_45 = rotationMatrix(-Math.PI/4);
  
  public static final Matrix LEFT_TURN_5 = rotationMatrix(Math.PI/4/9);
  public static final Matrix RIGHT_TURN_5 = rotationMatrix(-Math.PI/4/9);
  
  public static final Matrix LEFT_TURN_135 = rotationMatrix(3*Math.PI/4);
  public static final Matrix RIGHT_TURN_135 = rotationMatrix(-3*Math.PI/4);
  
  public static Matrix rotationMatrix(double rotation){
    return new Matrix(Math.cos(rotation), -1
        * Math.sin(rotation), Math.sin(rotation), Math.cos(rotation));
  }
  
  double a11;
  double a12;
  double a21;
  double a22;

  public Matrix(double a11, double a12, double a21, double a22) {
    this.a11 = a11;
    this.a12 = a12;
    this.a21 = a21;
    this.a22 = a22;
  }

  public Vector mult(Vector v) {
    return new Vector(v.getX() * a11 + v.getY() * a12, v.getX() * a21
        + v.getY() * a22);
  }

}
