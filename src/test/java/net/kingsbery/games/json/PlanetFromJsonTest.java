package net.kingsbery.games.json;

import static org.junit.Assert.*;

import java.awt.Color;
import java.io.IOException;

import net.kingsbery.games.Planet;
import net.kingsbery.games.util.JsonMapper;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

public class PlanetFromJsonTest {

  

  @Test
  public void earthName() throws IOException{
    Planet planet = getPlanet("earth.json");
    assertEquals(Color.blue,planet.getAwtColor());
  }

  @Test
  public void earthMass() throws IOException{
    Planet planet = getPlanet("earth.json");
    assertEquals(45000,planet.getMass(),Double.MIN_VALUE);
  }
  
  @Test
  public void earthColor() throws IOException{
    Planet planet = getPlanet("earth.json");
    assertEquals("Earth",planet.getName());
    assertEquals(Color.blue,planet.getAwtColor());
    
  }
  
  
  @Test
  public void marsName() throws IOException{
    Planet planet = getPlanet("mars.json");
    assertEquals("Mars",planet.getName());
    assertEquals(Color.red,planet.getAwtColor());
  }

  
  @Test
  public void marsColor() throws IOException{
    Planet planet = getPlanet("mars.json");
    assertEquals("Mars",planet.getName());
    assertEquals(Color.red,planet.getAwtColor());
  }

  @Test
  public void marsLocation() throws IOException{
    Planet planet = getPlanet("mars.json");
   assertEquals(3000,planet.getLocation().getX(),Double.MIN_VALUE);
   assertEquals(300,planet.getLocation().getY(),Double.MIN_VALUE);
  }
  
  
  private Planet getPlanet(String name) throws IOException, JsonParseException,
      JsonMappingException {
    ObjectMapper mapper = JsonMapper.getInstance();
    Planet planet = mapper.readValue(this.getClass().getClassLoader().getResourceAsStream(name),Planet.class);
    return planet;
  }
}
