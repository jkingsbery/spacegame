package net.kingsbery.games;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.kingsbery.games.math.Vector;
import net.kingsbery.games.scenario.Scenario;
import net.kingsbery.games.util.JsonMapper;
import net.kingsbery.games.util.MapFileReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.ObjectMapper;

public class GameLoop implements Serializable {

  private static Log log = LogFactory.getLog(GameLoop.class);

  private Ship playerShip;

  private List<Planet> planets;

  private List<Ship> ships;

  private Class<? extends Scenario> introScenario;

  private Map<String, Scenario> scenarios = new HashMap<String, Scenario>();

  private Planet landedOn;

  public void setScenarios(Map<String, Scenario> scenarios) {
    this.scenarios = scenarios;
  }

  public Map<String, Scenario> getScenarios() {
    return scenarios;
  }

  public static InputStream loadResource(String name) {
    return MapFileReader.class.getClassLoader().getResourceAsStream(name);
  }

  public GameLoop() {
  }

  public GameMode update() {
    for (Ship ship : getAllShips()) {
      if (ship.isAlive()) {
        Vector gravity = Vector.ZERO;
        for (Planet planet : this.getPlanets()) {
          gravity = gravity.plus(planet.calculateGravity(ship));
        }
        ship.setGravity(gravity);
        ship.update();
        if (ship == getPlayerShip()) {
          for (Planet planet : this.getPlanets()) {
            if (getPlayerShip().getPosition().getDistance(planet.getLocation()) < planet
                .getSize() && getPlayerShip().canLandOn(planet)
            /*
             * && getPlayerShip() .getVelocity() .normalize() .minus(
             * getPlayerShip().getPosition().direction(
             * planet.getLocation())).size() < 1
             */) {
              this.landedOn=planet;
              return GameMode.LANDING;
            } else if (ship.getPosition() == null
                || ship.getDistance(planet) < planet.getSize()) {

              ship.crash();
            }
          }
        }
      }
    }
    return GameMode.SPACE;
  }

  public List<Ship> getShips() {
    return ships;
  }

  @JsonIgnore
  public List<Ship> getAllShips() {
    List<Ship> result = new ArrayList<Ship>(ships);
    result.add(this.playerShip);
    return result;
  }

  public Ship getPlayerShip() {
    return this.playerShip;
  }

  public List<Planet> getPlanets() {
    return this.planets;
  }

  public List<Ship> nonPlayerShips() {
    return this.ships;
  }

  public boolean playerInDanger() {
    for (Planet planet : getPlanets()) {
      assert playerShip != null;
      assert playerShip.getPosition() != null;
      assert planet.getLocation() != null;
      if (playerShip.getPosition().getDistance(planet.getLocation()) < planet
          .getSize() * 1.5) {
        return true;
      }
    }
    return false;
  }

  public Planet getPlanet(String string) {
    for (Planet planet : planets) {
      if (string.equals(planet.getName())) {
        return planet;
      }
    }
    throw new IllegalArgumentException("No such planet: " + string);
  }

  public Ship getShip(String string) {
    for (Ship ship : ships) {
      if (string.equals(ship.getName())) {
        return ship;
      }
    }
    throw new RuntimeException("No such ship: " + string);
  }

  @JsonIgnore
  public List<Ship> getNonPlayerShips() {
    List<Ship> result = new ArrayList<Ship>(this.ships);
    result.remove(playerShip);
    return result;
  }

  public static GameLoop load(File file) {
    try {
      ObjectMapper mapper = JsonMapper.getInstance();
      GameLoop planet;
      InputStream stream = new FileInputStream(file);
      planet = mapper.readValue(stream, GameLoop.class);
      stream.close();
      return planet;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static GameLoop load(String name) {
    try {
      ObjectMapper mapper = JsonMapper.getInstance();
      GameLoop planet;
      InputStream stream = GameLoop.class.getClassLoader().getResourceAsStream(
          name);
      planet = mapper.readValue(stream, GameLoop.class);
      stream.close();
      return planet;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @JsonIgnore
  public Scenario getIntroScenarioInstance() {
    if (introScenario != null) {
      try {
        return ((Class<? extends Scenario>) introScenario).newInstance();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    } else {
      return null;
    }
  }

  public Class<? extends Scenario> getIntroScenario() {
    return introScenario;
  }

  public void setIntroScenario(Class<? extends Scenario> x) {
    this.introScenario = x;
  }

}
