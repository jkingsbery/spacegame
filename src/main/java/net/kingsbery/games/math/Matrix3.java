package net.kingsbery.games.math;

import static java.lang.Math.*;

public class Matrix3 {

  public static final Matrix3 LEFT_TURN = rotationMatrixZ(PI / 2);
  public static final Matrix3 RIGHT_TURN = rotationMatrixZ(-PI / 2);

  // TODO Add axis of rotation. In 2D, this is implicitly the Z axis, but in 3D
  // we need to specify
  public static Matrix3 rotationMatrixZ(double rotation) {
    return new Matrix3(new double[][] {
        { cos(rotation), -1 * sin(rotation), 0 },
        { sin(rotation), cos(rotation), 0 }, { 0, 0, 1 } });
  }
  
  public static Matrix3 rotationMatrix(Vector3 u, double t){
    double a11=cos(t)+u.getX()*u.getX()*(1-cos(t));
    double a12=u.getX()*u.getY()*(1-cos(t))-u.getZ()*sin(t);
    double a13=u.getX()*u.getZ()*(1-cos(t))+u.getY()*sin(t);
    double a21=u.getY()*u.getX()*(1-cos(t))+u.getZ()*sin(t);
    double a22=cos(t)+u.getY()*u.getY()*(1-cos(t));
    double a23=u.getY()*u.getZ()*(1-cos(t))-u.getX()*sin(t);
    double a31=u.getZ()*u.getX()*(1-cos(t))-u.getY()*sin(t);
    double a32=u.getZ()*u.getY()*(1-cos(t))+u.getX()*sin(t);
    double a33=cos(t)+u.getZ()*u.getZ()*(1-cos(t));
    return new Matrix3(new double[][]{{a11,a12,a13},{a21,a22,a23},{a31,a32,a33}});
  }

  public static Matrix3 rotationMatrixY(double rotation) {
    return new Matrix3(new double[][] { { cos(rotation), 0, sin(rotation) },
        { 0, 1, 0 }, { -sin(rotation), 0, cos(rotation) } });
  }

  public static Matrix3 rotationMatrixX(double rotation) {
    return new Matrix3(new double[][] { { 1, 0, 0 },
        { 0, cos(rotation), -sin(rotation) },
        { 0, sin(rotation), cos(rotation) } });
  }

  double a[][];

  // row,column
  public Matrix3(double[][] a) {
    this.a = a;
  }

  public Vector3 mult(Vector3 v) {
    double result[] = new double[3];
    for (int i = 0; i < result.length; i++) {
      result[i] = v.getX() * a[i][0] + v.getY() * a[i][1] + v.getZ() * a[i][2];
    }
    return new Vector3(result[0], result[1], result[2]);
  }

}
