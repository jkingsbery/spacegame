package net.kingsbery.games.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import net.kingsbery.games.App;
import net.kingsbery.games.MapDefinition;
import net.kingsbery.games.Planet;
import net.kingsbery.games.Ship;
import net.kingsbery.games.objects.GameItem;
import net.kingsbery.games.scenario.Scenario;

@Deprecated
//TODO Doesn't seem like this needs to be serializable
public class MapFileReader implements MapDefinition, Serializable {

  private List<Planet> planets;
  private ArrayList<Ship> ships;
  private Ship player;

  private static class PlanetBuilder {
    private String name;
    private int mass;
    private int x;
    private int y;
    private int size;
    private String colorStr;

    public Planet build() {
      return new Planet(name, mass, x, y, size, parseStringToColor(colorStr));
    }

  }

  public static Color parseStringToColor(String hexstring) {
    Integer i = Integer.parseInt(hexstring.substring(1), 16);
    Color color = new Color(i);
    return color;
  }

  //TODO doesn't seem like this should need to be serializable
  public class ShipBuilder implements Serializable {
    String name;
    public int image;
    public int x;
    public int y;
    public double vx = 0;
    public double vy = 0;
    public String[] known = new String[0];
    public List<Class<? extends Scenario>> possibleMissions = new ArrayList<Class<? extends Scenario>>();
    public boolean canRefuel = false;
    public HashMap<String, Integer> prices=  new HashMap<String, Integer>(){{
      put("Nitrogen", 10);
      put("Food", 25);
      put("Water", 1);
    }};
    public List<GameItem> inStock= new ArrayList<GameItem>();

    public Ship build() {
      List<Planet> knownPlanets = new ArrayList<Planet>();
      for (String str : known) {
        knownPlanets.add(getPlanet(str));
      }
//      Ship ship = new Ship(name, image,new Vector( x, y),
//          new Vector(vx, vy), null, possibleMissions, canRefuel,prices,inStock);
//      return ship;
      return new Ship();
    }
  }

  public MapFileReader(InputStream is) {
    try {
      planets = new ArrayList<Planet>();
      ships = new ArrayList<Ship>();
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.startsWith("Planet")) {
          PlanetBuilder current = new PlanetBuilder();
          current.name = line.split(":")[1];
          buildPlanet(reader, current);
          planets.add(current.build());
        } else if (line.startsWith("Ship")) {
          ShipBuilder current = new ShipBuilder();
          current.name = line.split(":")[1];
          buildShip(reader, current);
          Ship ship = current.build();
          if ("Player".equals(ship.getName())) {
            player = ship;
          } else {
            ships.add(current.build());
          }
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public Planet getPlanet(String string) {
    for (Planet p : planets) {
      if (p.getName().equals(string)) {
        return p;
      }
    }
    throw new RuntimeException("No such planet '" + string + "'");
  }

  private void buildShip(BufferedReader reader, ShipBuilder builder)
      throws IOException {
    String line;
    while ((line = reader.readLine()) != null && !"end".equals(line)) {
      if (line.startsWith("image")) {
        builder.image = Integer.parseInt(line.split("=")[1]);
      } else if (line.startsWith("x")) {
        builder.x = Integer.parseInt(line.split("=")[1]);
      } else if (line.startsWith("y")) {
        builder.y = Integer.parseInt(line.split("=")[1]);
      } else if (line.startsWith("vx")) {
        builder.vx = Double.parseDouble(line.split("=")[1]);
      } else if (line.startsWith("vy")) {
        builder.vy = Double.parseDouble(line.split("=")[1]);
      } else if (line.startsWith("known")) {
        builder.known = line.split("=")[1].split(",");
      } else if (line.startsWith("missions")) {
        String missionNames[] = line.split("=")[1].split(",");
        for (String name : missionNames) {
          try {
            builder.possibleMissions.add((Class<? extends Scenario>) Class
                .forName("net.kingsbery.games.scenario." + name));
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }
      } else if (line.startsWith("canRefuel")) {
        builder.canRefuel = Boolean.parseBoolean(line.split("=")[1]);
      } else if (line.startsWith("prices")) {
        builder.prices = new HashMap<String, Integer>();
        for (String str : line.split("=")[1].split(",")) {
          String pair[] = str.split(":");
          builder.prices.put(pair[0], Integer.parseInt(pair[1]));
        }
      } else if(line.startsWith("availableItems")){
        List<GameItem> gameItems = new ArrayList<GameItem>();
        String items[]=line.split("=")[1].split(",");
        for(String x : items){
          String pair[]=x.split(":");
          gameItems.add(new GameItem(pair[0],Integer.parseInt(pair[1]),0));
        }
        builder.inStock=gameItems;
      } else {
        throw new RuntimeException("Cannot parse " + line);
      }
    }
  }

  public void buildPlanet(BufferedReader reader, PlanetBuilder builder)
      throws IOException {
    String line;
    while ((line = reader.readLine()) != null && !"end".equals(line)) {
      if (line.startsWith("mass")) {
        builder.mass = Integer.parseInt(line.split("=")[1]);
      } else if (line.startsWith("x")) {
        builder.x = Integer.parseInt(line.split("=")[1]);
      } else if (line.startsWith("y")) {
        builder.y = Integer.parseInt(line.split("=")[1]);
      } else if (line.startsWith("size")) {
        builder.size = Integer.parseInt(line.split("=")[1]);
      } else if (line.startsWith("color")) {
        builder.colorStr = line.split("=")[1];
      } else {
        throw new RuntimeException("Unexpected line: " + line);
      }
    }

  }

  public static void main(String args[]) throws IOException {
    MapFileReader reader = new MapFileReader(MapFileReader.class
        .getClassLoader().getResourceAsStream("sol.map"));
    System.out.println(reader.getPlanets().get(0).getLocation());
    System.out.println(reader.getPlanets().get(0).getSize());
    System.out.println(reader.getPlanets().get(0).getAwtColor());
  }

  public List<Planet> getPlanets() {
    return this.planets;
  }

  public List<Ship> getShips() {
    return this.ships;
  }

  public static BufferedImage[] shipImages;
  static {
    try {
      shipImages = loadSpaceships();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  static BufferedImage[] loadSpaceships() throws IOException {
    BufferedImage[] result = new BufferedImage[100];
    InputStream resourceAsStream = App.class.getClassLoader()
        .getResourceAsStream("samplespaceships.png");
    assert resourceAsStream != null;
    BufferedImage spriteSheet = ImageIO.read(resourceAsStream);
    for (int number = 0; number < 100; number++) {
      int x = (number % 10) * 44;
      int y = (number / 10) * 44;
      result[number] = spriteSheet.getSubimage(x, y, 44, 44);
    }
    return result;
  }

  public Ship getPlayer() {
    // return new Ship("Player", MapFileReader.shipImages[0], (int) 300,
    // 330);
    return this.player;
  }

}
