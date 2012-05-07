package net.kingsbery.games.math;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Vector implements Serializable {

  public static final Vector ZERO = new Vector(0,0);
  final double x;
  final double y;
  
  @JsonCreator
  public Vector(@JsonProperty("x") double x, @JsonProperty("y") double y){
    this.x=x;
    this.y=y;
  }

  public Vector plus(Vector accel) {
    double a=this.x+accel.getX();
    double b=this.y+accel.getY();
    return new Vector(a,b);
  }

  public double getX() {
    return this.x;
  }
  
  public double getY(){
    return this.y;
  }

  public Vector minus(Vector a) {
    double x=this.x-a.getX();
    double y=this.y-a.getY();
    return new Vector(x,y);
  }
  
  public double size(){
    return Math.sqrt(this.x*this.x+this.y*this.y);
  }

  public Vector normalize() {
    return this.scale(1/size());
  }
  
  public String toString(){
    return "("+x+","+y+")";
  }

  public Vector scale(double d) {
    return new Vector(this.x*d,this.y*d);
  }

  @JsonIgnore
  public boolean isValid() {
    return !Double.isNaN(this.x)&& !Double.isNaN(this.y);
  }
  
  public double getDistance(Vector other) {
    double x = getX() - other.getX();
    double y = getY() - other.getY();
    return Math.sqrt(x * x + y * y);
  }
  
  public Vector direction(Vector b) {
    return b.minus(this).normalize();
  }

  public Vector normal(Vector b) {
    double dx=b.x-x;
    double dy=b.y-y;
    return new Vector(-dy,dx).normalize();
  }
  
  @Override
  public int hashCode(){
    return (int)(this.x+this.y);
  }
  
  @Override
  public boolean equals(Object other){
    if(other instanceof Vector){
      Vector that=(Vector)other;
      return this.x==that.x && this.y==that.y;
    }else{
      return false;
    }
  }

  public double dot(Vector p) {
    return this.getX()*p.getX()+this.getY()*p.getY();
  }
}
