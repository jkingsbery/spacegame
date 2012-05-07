package net.kingsbery.games.pcg;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import javax.imageio.ImageIO;

import net.kingsbery.games.math.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.annotate.JsonIgnore;

public class ColonyMap {
  private static final Log log = LogFactory.getLog(ColonyMap.class);

  private String name;

  private static class Region {
    List<Vector> points = new ArrayList<Vector>();
    private boolean boundaryRegion;
    private Color color;

    public void addPoint(Vector point) {
      this.points.add(point);
    }

    public Color getColor() {
      return color;
    }

    @Override
    public String toString() {
      return points.toString();
    }

    public void setColor(Color color) {
      this.color = color;
    }

    public void setBoundaryRegion(boolean b) {
      this.boundaryRegion = b;
    }

    public boolean getBoundaryRegion() {
      return this.boundaryRegion;
    }
  }

  private Color[][] tiles;

  /**
   * Get region containing the point.
   * 
   * @param point
   * @param color
   */
  public Region getRegion(Vector point, Color color) {
    if (!filledOut(point)) {
      Region result = new Region();
      result.setColor(color);
      log.info("Creating a region with color "
          + Integer.toHexString(color.getRGB()));
      Stack<Vector> pointsToCheck = new Stack<Vector>();
      pointsToCheck.add(point);
      while (!pointsToCheck.isEmpty() && pointsToCheck.size() < 100000) {
        Vector pt = pointsToCheck.pop();
        if (onBoundary(pt)) {
          result.setBoundaryRegion(true);
        }
        assignTile(color, pt);
        for (Vector v : getNeighbors(pt)) {
          if (!filledOut(v)) {
            pointsToCheck.push(v);
          }
        }
      }
      this.addRegion(result);
      return result;
    } else {
      return null;
    }
  }

  private boolean onBoundary(Vector pt) {
    return (int) pt.getX() == minX || (int) pt.getX() == maxX - 1
        || (int) pt.getY() == minY || (int) pt.getY() == maxY - 1;
  }

  private void addRegion(Region result) {
    this.regions.add(result);
  }

  public Set<Region> getRegions() {
    return this.regions;
  }

  public void assignTile(Color color, Vector pt) {
    tiles[(int) pt.getX() - minX][(int) pt.getY() - minY] = color;
  }

  public Color tileValue(Vector pt) {
    if(!inBounds(pt)){
      return Color.red;
    }
    return tiles[(int) pt.getX() - minX][(int) pt.getY() - minY];
  }

  private boolean inBounds(Vector pt) {
    return minX<pt.getX() && pt.getX()<maxX && minY<pt.getY() && pt.getY()<maxY; 
  }

  private boolean filledOut(Vector pt) {
    return tiles[(int) pt.getX() - minX][(int) pt.getY() - minY] != null;
  }

  private Collection<Vector> getNeighbors(Vector pt) {
    Collection<Vector> result = new ArrayList<Vector>();
    if (pt.getX() + 1 < maxX) {
      result.add(pt.plus(new Vector(1, 0)));
    }
    if (pt.getX() - 1 > minX) {
      result.add(pt.plus(new Vector(-1, 0)));
    }
    if (pt.getY() - 1 > minY) {
      result.add(pt.plus(new Vector(0, -1)));
    }
    if (pt.getY() + 1 < maxY) {
      result.add(pt.plus(new Vector(0, 1)));
    }
    return result;
  }

  private boolean divided(Vector point, Vector pt) {
    for (Road road : roads) {
      if (road.divides(point, pt)) {
        return true;
      }
    }
    return false;
  }

  private List<Road> roads = new ArrayList<Road>();
  private List<Vector> intersections = new ArrayList<Vector>();
  private Set<Region> regions = new HashSet<Region>();

  public List<Road> getRoads() {
    return this.roads;
  }

  public void setRoads(List<Road> roads) {
    this.roads = roads;
  }

  public void drawRoad(Vector position, Vector nextPosition) {
    Road newRoad = new Road(position, nextPosition);
    log.info(newRoad);
    if (newRoad.length() > 0) {
      roads.add(newRoad);
      if (!intersections.contains(position)) {
        intersections.add(position);
      }
      if (!intersections.contains(nextPosition)) {
        intersections.add(nextPosition);
      }
    }
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public static void toImage(ColonyMap map) throws IOException {
    int width = 1000;
    int height = 1000;
    BufferedImage bi = new BufferedImage(width, height,
        BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = bi.createGraphics();
    g.setColor(Color.gray);
    g.fillRect(0, 0, width, height);
    g.setPaint(Color.white);
    int offsetX = -500;
    int offsetY = -800;

    for (Vector key : map.getTileCoords()) {
      g.setColor(map.tileValue(key));
      g.drawLine((int) key.getX() - offsetX, (int) key.getY() - offsetY,
          (int) key.getX() - offsetX, (int) key.getY() - offsetY);
    }
    ImageIO.write(bi, "PNG", new File(map.getFileName() + ".png"));
  }

  private Set<Vector> getTileCoords() {
    Set<Vector> result = new HashSet<Vector>();
    for (int i = minX; i < maxX; i++) {
      for (int j = minY; j < maxY; j++) {
        result.add(new Vector(i, j));
      }
    }
    return result;
  }

  @JsonIgnore
  public String getFileName() {
    return getName().replace(" ", "-").toLowerCase();
  }

  private static Polygon fromRegion(Region r, Vector offset) {
    Polygon p = new Polygon();
    for (Vector v : r.points) {
      p.addPoint((int) (v.getX() - offset.getX()),
          (int) (v.getY() - offset.getY()));
    }
    assert p.npoints == r.points.size();
    return p;
  }

  private int minX;

  private int minY;

  private int maxX;

  private int maxY;

  private List<Building> buildings = new ArrayList<Building>();

  public Color[][] getTiles() {
    return this.tiles;
  }

  public int getMinX() {
    return this.minX;
  }

  public int getMinY() {
    return this.minY;
  }

  public int getMaxX() {
    return this.maxX;
  }

  public int getMaxY() {
    return this.maxY;
  }

  public void createRegions() {
    minX = Integer.MAX_VALUE;
    minY = Integer.MAX_VALUE;
    maxX = Integer.MIN_VALUE;
    maxY = Integer.MIN_VALUE;
    for (Vector pt : intersections) {
      if (pt.getX() < minX) {
        minX = (int) pt.getX();
      }
      if (pt.getY() < minY) {
        minY = (int) pt.getY();
      }
      if (pt.getX() > maxX) {
        maxX = (int) pt.getX();
      }
      if (pt.getY() > maxY) {
        maxY = (int) pt.getY();
      }
    }
    // Step 1: Draw Tiles for the roads.
    Map<Vector, Color> tiles = new HashMap<Vector, Color>();
    int color = 0;
    Color roadColors[] = new Color[] { Color.white };
    for (Road road : roads) {
      int tilesStarting = tiles.size();
      Color which = roadColors[color++ % roadColors.length];
      log.info("Drawing Road: " + road + "," + road.length() + "," + which);
      if ((int) road.a.getX() == (int) road.b.getX()) {
        Vector start = road.a.getY() < road.b.getY() ? road.a : road.b;
        Vector finish = road.a.getY() < road.b.getY() ? road.b : road.a;
        for (int y = (int) start.getY(); y < finish.getY(); y++) {
          tiles.put(new Vector((int) road.a.getX(), (int) y), which);
        }
      } else {
        Vector start = road.a.getX() < road.b.getX() ? road.a : road.b;
        Vector finish = road.a.getX() < road.b.getX() ? road.b : road.a;
        int dx = (int) (finish.getX() - start.getX());
        int dy = (int) (finish.getY() - start.getY());
        for (int x = (int) start.getX(); x < finish.getX(); x++) {
          int y = (int) (start.getY() + dy * (x - start.getX()) / dx);
          tiles.put(new Vector((int) x, (int) y), which);
        }
        start = road.a.getY() < road.b.getY() ? road.a : road.b;
        finish = road.a.getY() < road.b.getY() ? road.b : road.a;
        dx = (int) (finish.getX() - start.getX());
        dy = (int) (finish.getY() - start.getY());
        for (int y = (int) start.getY(); y < finish.getY(); y++) {
          int x = (int) (start.getX() + dx * (y - start.getY()) / dy);
          tiles.put(new Vector((int) x, (int) y), which);
        }
      }
      if (tiles.size() == tilesStarting) {
        throw new RuntimeException("Unable to add tiles for road " + road);
      }
    }

    // Step 2: Translate this into a 2 dimensional array
    Color[][] array = new Color[maxX - minX][maxY - minY];
    this.tiles = array;
    for (int i = minX; i < maxX; i++) {
      for (int j = minY; j < maxY; j++) {
        array[i - minX][j - minY] = tiles.get(new Vector(i, j));
      }
    }

    // Step 3: Pick Points and Start Filling in regions
    Random rnd = new Random();
    for (int x = minX; x < maxX; x++) {
      for (int y = minY; y < maxY; y++) {
        getRegion(new Vector((int) x, (int) y), randomColor(rnd));
      }
    }

    log.info("Tiles size: " + tiles.size());
    log.info("Roads: " + this.roads.size());
    // return this.regions;
  }

  private Color randomColor(Random rnd) {
    return new Color(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255), 255);
  }

  public void addBuilding(Building building) {
    this.buildings.add(building);
  }

  public Collection<Building> getBuildings() {
    return this.buildings;
  }

  public void setBuildings(List<Building> buildings) {
    this.buildings = buildings;
  }

  public static void main(String args[]) throws IOException {
    SystemDrawer.main(args);
  }

  public boolean unobstructed(Building first) {
    for (Road road : roads) {
      Area area = new Area();
      area.add(new Area(first.asPolygon()));
      area.intersect(new Area(road.asPolygon()));
      if (!area.isEmpty()) {
        return false;
      }
    }
    for (Building other : getBuildings()) {
      if(intersection(first, other)){
        return false;
      }
    }
    return true;
  }

  public static boolean intersection(Building first, Building other) {
    Area area = new Area();
    area.add(new Area(first.asPolygon()));
    area.intersect(new Area(other.asPolygon()));
    return !area.isEmpty();    
  }

}
