package net.kingsbery.games;

import java.awt.Color;
import java.io.Serializable;

import net.kingsbery.games.math.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.annotate.JsonIgnore;

public class Planet implements Serializable {
  private static final Log log = LogFactory.getLog(Planet.class);
  
  private int mass;
  private int x;
  private int y;
  private int size;
  private Color color;
  private String name;

  //Need empty constructor for Jackson.
  public Planet(){
    
  }
  
  public Planet(String name,int mass, int x, int y, int size, Color color) {
    this.name=name;
    this.mass = mass;
    this.x = x;
    this.y = y;
    this.size = size;
    this.color = color;
  }

  public double getMass() {
    return mass;
  }

  public void setLocation(Vector location){
    this.x=(int) location.getX();
    this.y=(int) location.getY();
  }
  
  public Vector getLocation() {
    return new Vector(x, y );
  }

  public int getSize() {
    return this.size;
  }

  public String getColor(){
    return Integer.toHexString(color.getRGB());
  }
  
  @JsonIgnore
  public Color getAwtColor() {
    return color;
  }
  
  @JsonIgnore
  public Color getAwtForegroundColor() {
    return Color.pink;
  }
  
  public Vector calculateGravity(Ship ship) {
    assert ship!=null;
    assert ship.getPosition()!=null;
    assert this.getLocation()!=null;
    double distance = ship.getPosition().getDistance(this.getLocation());
    if (Double.isNaN(distance)) {
      ship.getPosition().getDistance( this.getLocation());
      throw new RuntimeException("Invaid distance");
    }
    Vector direction = ship.getPosition().direction(this.getLocation());
    Vector thrust = direction.scale(this.getMass()/(distance*distance));
    log.debug(ship.getName()+","+ship.getPosition() + "," + this.getLocation() + "," + distance+","+thrust+","+thrust.size());
    assert thrust.isValid() : "Gravity invalid. Distance: " + distance
        + "; direction: " + direction;
    return thrust;
  }

  public String getName() {
    return this.name;
  }

  
  public String toString(){
    return this.name;
  }



}