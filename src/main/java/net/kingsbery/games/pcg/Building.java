package net.kingsbery.games.pcg;

import java.awt.Color;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

import net.kingsbery.games.math.Vector;

import org.codehaus.jackson.annotate.JsonIgnore;

public class Building {

  private Vector center;
  private Vector direction;
  private double width;
  private double depth;
  private double height;
  private Color color=Color.blue;
  
  
  public Building(Vector center, Vector direction, double width, double depth, double height,Color color) {
    this.center = center;
    this.direction=direction;
    this.width = width;
    this.depth = depth;
    this.height = height;
    this.color=color;
  }
  
  public Building(){
    
  }

  public Building(Vector vector, int i, int j, int k) {
    this(vector,Vector.I,i,j,k,Color.blue);
  }

  public double getHeight() {
    return this.height;
  }
  
  public void setHeight(double height){
    this.height=height;
  }

  public Vector getCenter(){
    return this.center;
  }
  
  public void setCenter(Vector x){
    this.center=x;
  }
  
  public Vector getDirection(){
    return this.direction;
  }
  
  public void setDirection(Vector dir){
    this.direction=dir;
  }
  
  public double getWidth(){
    return this.width;
  }
  
  public void setWidth(double w){
    this.width=w;
  }
  
  public double getDepth(){
    return this.depth;
  }
  
  public void setDepth(double d){
    this.depth=d;
  }
  
  public Color getColor(){
    return this.color;
  }
  
  public void setColor(Color color){
    this.color=color;
  }
  
  /**
   * Returns a list of points that make up the base of the building.
   * 
   * @return
   */
  public List<Vector> getBase() {
    Vector a = new Vector(center.getX() - width / 2, center.getY() - depth / 2);
    Vector b = new Vector(center.getX() + width / 2, center.getY() - depth / 2);
    Vector c = new Vector(center.getX() + width / 2, center.getY() + depth / 2);
    Vector d = new Vector(center.getX() - width / 2, center.getY() + depth / 2);
    List<Vector> result = new ArrayList<Vector>();
    result.add(a);
    result.add(b);
    result.add(c);
    result.add(d);
    return result;
  }

  @JsonIgnore
  public List<Wall> getWalls() {
    List<Wall> result = new ArrayList<Wall>();
    List<Vector> base = getBase();
    for (int i = 0; i < base.size() - 1; i++) {
      result.add(new Wall(base.get(i), base.get(i + 1)));
    }
    result.add(new Wall(base.get(base.size() - 1), base.get(0)));
    return result;
  }
  
  

  
  public static class Wall {

    private Vector start;
    private Vector end;

    public Wall(Vector start, Vector end) {
      this.start = start;
      this.end = end;
    }

    public Vector getStart() {
      return this.start;
    }
    

    public Vector getEnd() {
      return this.end;
    }

  }


  public Polygon asPolygon() {
    Polygon result = new Polygon();
    for(Vector v : this.getBase()){
      result.addPoint((int)v.getX(),(int)v.getY());
    }
    return result;
  }

  @JsonIgnore
  public boolean isCylinder() {
    return false;
  }

}
