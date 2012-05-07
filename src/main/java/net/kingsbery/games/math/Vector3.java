package net.kingsbery.games.math;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Vector3 implements Serializable {

  public static final Vector3 ZERO = new Vector3(0,0,0);
  public static final Vector3 I = new Vector3(1,0,0);
  public static final Vector3 J = new Vector3(0,1,0);
  public static final Vector3 K = new Vector3(0,0,1);
  final double x;
  final double y;
  final double z;
  
  @JsonCreator
  public Vector3(@JsonProperty("x") double x, @JsonProperty("y") double y,@JsonProperty("z") double z){
    this.x=x;
    this.y=y;
    this.z=z;
  }

  public Vector3 plus(Vector3 accel) {
    double a=this.x+accel.getX();
    double b=this.y+accel.getY();
    double z=this.z+accel.getZ();
    return new Vector3(a,b,z);
  }

  public double getX() {
    return this.x;
  }
  
  public double getY(){
    return this.y;
  }
  
  public double getZ(){
    return this.z;
  }

  public Vector3 minus(Vector3 a) {
    double x=this.x-a.getX();
    double y=this.y-a.getY();
    double z=this.z-a.getZ();
    return new Vector3(x,y,z);
  }
  
  public double size(){
    return Math.sqrt(this.x*this.x+this.y*this.y+this.z*this.z);
  }

  public Vector3 normalize() {
    return this.scale(1/size());
  }
  
  public String toString(){
    return "("+x+","+y+","+z+")";
  }

  public Vector3 scale(double d) {
    return new Vector3(this.x*d,this.y*d,this.z*d);
  }

  @JsonIgnore
  public boolean isValid() {
    return !Double.isNaN(this.x)&& !Double.isNaN(this.y) && !Double.isNaN(this.z);
  }
  
  public double getDistance(Vector3 other) {
    double x = getX() - other.getX();
    double y = getY() - other.getY();
    double z =getZ()-other.getZ();
    return Math.sqrt(x * x + y * y+z*z);
  }
  
  public Vector3 direction(Vector3 b) {
    return b.minus(this).normalize();
  }
  
  @Override
  public int hashCode(){
    return (int)(this.x+this.y+this.z);
  }
  
  @Override
  public boolean equals(Object other){
    if(other instanceof Vector3){
      Vector3 that=(Vector3)other;
      return this.x==that.x && this.y==that.y && this.z==that.z;
    }else{
      return false;
    }
  }

  public double dot(Vector3 p) {
    return this.getX()*p.getX()+this.getY()*p.getY();
  }

  public Vector3 cross(Vector3 up) {
    double newX=this.y*up.z-this.z*up.y;
    double newY=this.z*up.x-this.x*up.z;
    double newZ=this.x*up.y-this.y*up.x;
    return new Vector3(newX,newY,newZ);
  }

  public Vector3 projectXY(double d) {
    return new Vector3(this.x,this.y,d);
  }

  public Vector projectXY() {
    return new Vector(x,y);
  }
}
