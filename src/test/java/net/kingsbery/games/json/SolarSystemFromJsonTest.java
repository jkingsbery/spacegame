package net.kingsbery.games.json;

import static org.junit.Assert.*;

import java.io.IOException;

import net.kingsbery.games.GameLoop;
import net.kingsbery.games.math.Vector;
import net.kingsbery.games.util.JsonMapper;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

public class SolarSystemFromJsonTest {

  private static final String SOLAR_SYSTEM_JSON = "soltest.json";

  @Test
  public void getPlanetCount() throws IOException {
    GameLoop loop = getSolarSystem(SOLAR_SYSTEM_JSON);
    assertEquals("Earth", loop.getPlanet("Earth").getName());
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void planetNotFound() throws IOException {
    GameLoop loop = getSolarSystem(SOLAR_SYSTEM_JSON);
    loop.getPlanet("Centauri Prime");
  }
  
  @Test
  public void playerPosition() throws IOException{
    GameLoop loop = getSolarSystem(SOLAR_SYSTEM_JSON);
    assertEquals(new Vector(-750,0),loop.getPlayerShip().getPosition());
    
  }


  @Test
  public void planetsKnownByPlayer() throws IOException{
    GameLoop loop = getSolarSystem(SOLAR_SYSTEM_JSON);
    assertTrue(loop.getPlayerShip().isKnown("Earth"));
    
  }

  
  private GameLoop getSolarSystem(String name) throws IOException,
      JsonParseException, JsonMappingException {
    ObjectMapper mapper = JsonMapper.getInstance();
    GameLoop planet = mapper.readValue(this.getClass().getClassLoader()
        .getResourceAsStream(name), GameLoop.class);
    return planet;
  }
  
}
