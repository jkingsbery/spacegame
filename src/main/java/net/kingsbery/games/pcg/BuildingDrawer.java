package net.kingsbery.games.pcg;

import java.awt.Color;
import java.io.File;

import net.kingsbery.games.math.Vector;
import net.kingsbery.games.util.JsonMapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;

public class BuildingDrawer {

  private static final Log log = LogFactory.getLog(BuildingDrawer.class);

  public static void main(String args[]) throws Exception {
    ObjectMapper mapper = JsonMapper.getInstance();
    ColonyMap map = mapper.readValue(new File("tranquility-colony.json"),
        ColonyMap.class);
    for (Road road : map.getRoads()) {
      Vector perp = road.getPerpendicular();
      Vector start = road.getStart();

      log.info("Starting adding buildings at " + road);
      int edge = 0;
      Vector center = start;
      while (edge < road.length()) {
        log.info("Adding buildings along rode at point " + center);
        
        Vector target = center.plus(perp.scale(25));
        Building first = new Building(target,perp, 20, 20, 10,map.tileValue(target));
        if(map.unobstructed(first)&&first.getColor()!=Color.white){
          map.addBuilding(first);
        }
        Vector target2 = center.plus(perp.scale(-25));
        Building second = new Building(target2,perp.scale(-1), 20, 20, 10,map.tileValue(target2));
        if(map.unobstructed(second)&&second.getColor()!=Color.white){
          map.addBuilding(second);
        }
        center = center.plus(road.getDirection().scale(25));
        edge += 25;
      }
    }
    mapper.writeValue(new File(map.getFileName() + "-buildings.json"), map);

  }
}
