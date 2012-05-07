package net.kingsbery.games.ai;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.kingsbery.games.MapDefinition;
import net.kingsbery.games.Planet;
import net.kingsbery.games.Ship;

public class Orbiter {
  
  public static class OrbiterMapDefinition implements MapDefinition{

    private Ship player;
    private ArrayList<Planet> planets;

    public OrbiterMapDefinition(){
//      player = new Ship("Player", 13, new Vector(250, 0),new Vector(0,-6),null,null,false,null,null);
      player = new Ship();
      Planet moon = new Planet("Moon",7500,0,0,60,Color.gray);
      planets = new ArrayList<Planet>();
      planets.add(moon);
      
    }
    
    @Override
    public List<Planet> getPlanets() {
      return planets;
    }

    @Override
    public List<Ship> getShips() {
      return new ArrayList<Ship>();
    }

    @Override
    public Ship getPlayer() {
      return player;
    }
    
  }
  
  //FIXME Restore
  public static void main(String args[]){
//    OrbiterMapDefinition defn = new OrbiterMapDefinition();
//    GameLoop loop = new GameLoop(defn);
//    int i=0;
//    while(i<100000 && defn.getPlayer().isAlive() && 
//        defn.getPlayer().getPosition().getDistance(defn.getPlanets().get(0).getLocation())<400){
//      loop.update();
//      i++;
//    }
//    System.out.println("Survived " + i + " iterations ");
  }
  
}
