package net.kingsbery.games;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.kingsbery.games.math.Vector;
import net.kingsbery.games.objects.GameItem;
import net.kingsbery.games.scenario.Scenario;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.annotate.JsonIgnore;

public class Ship implements Serializable {

  private static Log log = LogFactory.getLog(Ship.class);

  private String name;
  private int fuel = 60;
  private int maxFuel = 200;

  private boolean alive = true;

  private Vector respawnPosition;

  private Vector startingVeloctiy;

  private List<String> knownPlanets;

  private List<Class<? extends Scenario>> missions;

  public Ship(String name, int image, int x, int y) {
    this.name = name;
    this.image = image;
    this.position = new Vector(x, y);
  }

  // Needed by Jackson
  public Ship() {

  }

  @JsonIgnore
  public String getStatus() {
    return this.getName() + " - Fuel: " + fuel + "/" + maxFuel;
  }

  private Vector thrusterAcceleration = new Vector(0, 0);

  private Vector gravityAcceleration = new Vector(0, 0);

  private Vector velocity = new Vector(0, 0);

  private Vector position;

  private int image;

  public void thrust(Vector accel) {
    if (fuel > 0) {
      fuel--;
      this.thrusterAcceleration = accel;
    }
  }

  public void setGravity(Vector accel) {
    assert accel.isValid();
    this.gravityAcceleration = accel;
  }

  public void stopThrust() {
    this.thrusterAcceleration = new Vector(0, 0);
  }

  public void update() {
    Vector oldLocation = position;
    this.velocity = this.velocity.plus(this.thrusterAcceleration
        .plus(this.gravityAcceleration));
    if (Double.isNaN(this.velocity.getX())) {
      throw new RuntimeException("Cannot add acceleration of "
          + this.gravityAcceleration + " to " + this.velocity);
    }
    this.position = this.position.plus(this.velocity);
    if (Double.isNaN(this.position.getX())) {
      throw new RuntimeException("Cannot add velocity of " + this.velocity
          + " to " + oldLocation);
    }
  }

  @JsonIgnore
  public double getX() {

    return getPosition().getX();
  }

  @JsonIgnore
  public double getY() {
    return position.getY();
  }

  public Vector getPosition() {
    if (this.position == null) {
      log.error("Ship " + getName() + " has no position!");
      return respawnPosition;
    }
    return this.position;
  }

  public Vector getVelocity() {
    return this.velocity;
  }

  public int getImage() {
    return this.image;
  }

  public String getName() {
    return name;
  }

  public boolean closeTo(Ship station) {
    assert station != null;
    assert getPosition() != null;
    return getPosition().getDistance(station.getPosition()) < 100;
  }

  public boolean closeSpeed(Ship station) {
    return getVelocity().getDistance(station.getVelocity()) < 1;
  }

  public boolean dockable(Ship other) {
    return this.closeTo(other) && this.closeSpeed(other);
  }

  @JsonIgnore
  public double getSpeed() {
    return this.velocity.size();
  }

  int targetIndex = -1;
  String target = null;

  // TODO refactor to be any game object (Planet or ship)
  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  public void nextTarget(List<Planet> planets) {

    if (-1 == targetIndex) {
      targetIndex = 0;
      target = planets.get(targetIndex).getName();
    } else if (planets.size() == targetIndex + 1) {
      targetIndex = -1;
      target = null;
    } else {
      targetIndex++;
      target = planets.get(targetIndex).getName();
    }

    if (target != null && !isKnown(target)) {
      nextTarget(planets);
    }

  }

  @JsonIgnore
  public boolean isAutopilot() {
    return target != null;
  }

  public void reverse() {
    this.thrust(this.velocity.scale(-1));
  }

  public void turnAround(){
      this.setVelocity(this.velocity.scale(-1));
      log.info("New velocity: " + this.velocity);
  }
  
  public void crash() {
    if (isPlayer()) {
      alive = false;
      log.info("Ship " + name + " crashed.");
    } else {
      // Fudge factor so Odyssey stops dieing.
      this.velocity = startingVeloctiy;
      this.position = respawnPosition;
    }
  }

  // FIXME There's probably a better way of representing whether this ship is
  // the human player.
  @JsonIgnore
  public boolean isPlayer() {
    return getName().equals("Player");
  }

  public boolean isAlive() {
    return alive;
  }

  public int getMaxFuel() {
    return this.maxFuel;
  }

  public int getFuel() {
    return this.fuel;
  }

  public void setFuel(int fuel) {
    this.fuel = fuel;
  }

  @JsonIgnore
  public boolean isEmpty() {
    return this.fuel == 0;
  }

  private Map<String, GameItem> items = new HashMap<String, GameItem>();

  private int dollars = 0;

  private boolean canRefuel;

  private List<ShipUpgrade> upgrades = new ArrayList<ShipUpgrade>();

  public void addItem(String str) {
    this.addItem(new GameItem(str, 1, 0));
  }

  public void addItem(GameItem item) {
    if (!this.items.containsKey(item.getName())) {
      this.items.put(item.getName(), item);
    } else {
      GameItem old = this.items.get(item.getName());
      this.items.put(
          item.getName(),
          new GameItem(item.getName(), item.getCount() + old.getCount(), old
              .getCost()));
    }
  }

  /**
   * Should only be used at initialization
   * 
   * @param items
   */
  public void setItems(Map<String, GameItem> items) {
    this.items = items;
  }

  public void addItems(List<GameItem> gameItems) {
    if (gameItems != null) {
      for (GameItem item : gameItems) {
        this.addItem(item);
      }
    }
  }

  public Map<String, GameItem> getItems() {
    return this.items;
  }

  public void removeAll(String str) {
    this.items.put(str, new GameItem(str, 0, 0));
  }

  public GameItem removeItem(String str, int count) {
    if (this.items.containsKey(str)) {
      this.items.put(str, new GameItem(str, this.items.get(str).getCount()
          - count, this.items.get(str).getCost()));
      return this.items.get(str);
    } else {
      throw new RuntimeException("Can't sell any '" + str
          + "' when you don't have any!");
    }
  }

  public int getItemCount(String itemName) {
    if (this.items.containsKey(itemName)) {
      return this.items.get(itemName).getCount();
    } else {
      return 0;
    }
  }

  public void setDollars(int dollars) {
    this.dollars = dollars;
  }

  public void addMoney(int i) {
    this.dollars += i;
  }

  public int getDollars() {
    return this.dollars;
  }

  public void decreaseMoney(int i) {
    this.dollars -= i;
  }

  public int getCost(String supplyName) {
    if (this.getItems().get(supplyName) == null) {
      return 0;
    }
    return this.getItems().get(supplyName).getCost();
    // assert this.cost != null;
    // if (!this.cost.containsKey(supplyName)) {
    // log.info("Cannot find cost for " + supplyName + " at ship "
    // + this.getName());
    // }
    // return this.cost.get(supplyName);
  }

  public void setCost(String supplyName, int amount) {
    GameItem old = this.getItems().get(supplyName);
    if (old == null) {
      this.getItems().put(supplyName, new GameItem(supplyName, 0, amount));
    } else {
      this.getItems().put(supplyName,
          new GameItem(supplyName, old.getCount(), amount));
    }
  }

  /**
   * returns the maximum distance between known points. If we had many points,
   * we'd do a convex hull, but in this case this is easier.
   */
  public int maxKnownDistance(GameLoop loop) {
    int maxDistance = Integer.MIN_VALUE;
    assert knownPlanets != null;
    for (String a : knownPlanets) {
      for (String b : knownPlanets) {
        Planet i = loop.getPlanet(a);
        Planet j = loop.getPlanet(b);
        double distance = i.getLocation().getDistance(j.getLocation());
        if (distance > maxDistance) {
          maxDistance = (int) distance;
        }
      }
    }
    return maxDistance;
  }

  public int getLeftMost(GameLoop loop) {
    double mostLeft = Integer.MAX_VALUE;
    for (String x : knownPlanets) {
      if (loop.getPlanet(x).getLocation().getX() < mostLeft) {
        mostLeft = loop.getPlanet(x).getLocation().getX();
      }
    }
    return (int) mostLeft;
  }

  public boolean isKnown(String planet) {
    return this.knownPlanets.contains(planet);
  }

  public void setKnownPlanets(List<String> known) {
    this.knownPlanets = known;
  }

  public List<String> getKnownPlanets() {
    return this.knownPlanets;
  }

  public void addKnownPlanet(Planet planet) {
    this.knownPlanets.add(planet.getName());
  }

  public List<Class<? extends Scenario>> getMissions() {
    return this.missions;
  }

  public void setCanRefuel(boolean x) {
    this.canRefuel = x;
  }

  public boolean getCanRefuel() {
    return this.canRefuel;
  }

  public void addUpgrade(ShipUpgrade upgrade) {
    this.upgrades.add(upgrade);
  }

  public boolean hasUpgrade(ShipUpgrade upgrade) {
    return (this.upgrades.contains(upgrade));
  }

  public boolean canLandOn(Planet planet) {
    if (this.upgrades.contains(ShipUpgrade.LunarLandingGear)
        && planet.getMass() <= 7500) {
      return true;
    } else if (this.upgrades.contains(ShipUpgrade.MarsLandingGear)
        && planet.getMass() <= 15000) {
      return true;
    } else {
      return false;
    }
  }

  public void fullStop() {
    this.velocity = Vector.ZERO;
  }

  public void setVelocity(Vector velocity) {
    this.velocity = velocity;
  }

  public int getCost(GameItem item) {
    return this.getCost(item.getName());
  }

  public void removeItem(GameItem item) {
    removeItem(item.getName(), 1);
  }

  public double getDistance(Planet planet) {
    assert getPosition() != null : "Ship " + getName()
        + " doesn't have a position!";
    assert planet != null;
    assert planet.getLocation() != null;
    return getPosition().getDistance(planet.getLocation());
  }

  /**
   * Should only be used be serializer
   */
  public void setPosition(Vector position) {
    this.position = position;
  }

  public void setRespawnPosition(Vector position) {
    this.respawnPosition = position;
  }

  public Vector getRespawnPosition() {
    return this.respawnPosition;
  }

  public void setRespawnVelocity(Vector velocity) {
    this.startingVeloctiy = velocity;
  }

  public Vector getRespawnVelocity() {
    return this.startingVeloctiy;
  }

  public void setMaxFuel(int i) {
    this.maxFuel = i;
  }
  public void setUpgrades(List<ShipUpgrade> upgrades){
    this.upgrades=upgrades;
  }
  public List<ShipUpgrade> getUpgrades(){
    return this.upgrades;
  }

  public Vector getDirection() {
      return this.getVelocity().scale(1/this.getVelocity().size());
  }
}
