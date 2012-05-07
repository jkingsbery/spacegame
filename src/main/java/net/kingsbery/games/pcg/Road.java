package net.kingsbery.games.pcg;

import java.awt.Polygon;
import java.awt.geom.Area;

import net.kingsbery.games.math.Vector;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Road {
  Vector a;
  Vector b;

  @JsonCreator
  public Road(@JsonProperty("start") Vector a, @JsonProperty("end") Vector b) {
    this.a = a;
    this.b = b;
  }

  public String toString() {
    return a + "," + b;
  }

  public Road plus(Vector vector) {
    return new Road(a.plus(vector), b.plus(vector));
  }

  private static double getSlope(Vector a, Vector b) {
    return (b.getY() - a.getY()) / (b.getX() - a.getX());
  }

  public boolean divides(Vector pt1, Vector pt2) {
    Vector E = pt2.minus(pt1);
    Vector F = b.minus(a);
    Vector P = new Vector(-E.getY(), E.getX());
    double h = ((pt1.minus(a)).dot(P)) / (F.dot(P));
    boolean same = (pt1.equals(a) && pt2.equals(b))
        || (pt1.equals(b) && pt2.equals(a));
    return !same && (0 <= h && h <= 1);
  }

  private static boolean vertical(Vector a2, Vector b2) {
    return a2.getX() == b2.getX();
  }

  public Vector getStart(){
    return this.a;
  }
  
  public Vector getEnd(){
    return this.b;
  }
  
  public int length() {
    return (int) a.getDistance(b);
  }

  @JsonIgnore
  public Vector getPerpendicular() {
    return this.b.normal(this.a);
  }

  @JsonIgnore
  public Vector getDirection() {
    return this.b.minus(this.a).normalize();
  }

  public Polygon asPolygon() {
    Polygon result=new Polygon();
    Vector perp = this.getPerpendicular().scale(3);
    result.addPoint((int)(this.getStart().getX()-perp.getX()), (int)(this.getStart().getY()-perp.getY()));
    result.addPoint((int)(this.getStart().getX()+perp.getX()), (int)(this.getStart().getY()+perp.getY()));
    result.addPoint((int)(this.getEnd().getX()+perp.getX()), (int)(this.getEnd().getY()+perp.getY()));
    result.addPoint((int)(this.getEnd().getX()-perp.getY()), (int)(this.getEnd().getY()-perp.getY()));
    
    return result;
  }
}