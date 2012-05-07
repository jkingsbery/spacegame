package net.kingsbery.games.json;

import static org.junit.Assert.*;

import java.io.IOException;

import net.kingsbery.games.Ship;
import net.kingsbery.games.math.Vector;
import net.kingsbery.games.scenario.TransportScientistToMarsScenario;
import net.kingsbery.games.util.JsonMapper;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

public class ShipFromJsonTest {

  @Test
  public void shipName() throws IOException{
    Ship freedom= getShip("freedom.json");
    assertEquals("Freedom Station",freedom.getName());
  }

  @Test
  public void shipPosition() throws IOException{
    Ship freedom= getShip("freedom.json");
    assertEquals(new Vector(3500,300),freedom.getPosition());
  }

  @Test
  public void missions() throws IOException{
    Ship freedom= getShip("freedom.json");
    assertEquals(2,freedom.getMissions().size());
    assertTrue(freedom.getMissions().contains(TransportScientistToMarsScenario.class));
  }
  
  @Test
  public void gameItemCount() throws IOException{
    Ship freedom= getShip("freedom.json");
    assertEquals(100,freedom.getItemCount("Nitrogen"));
  }

  
  
  @Test
  public void gameItemCost() throws IOException{
    Ship freedom= getShip("freedom.json");
    assertEquals(10,freedom.getCost("Nitrogen"));
  }

  @Test
  public void canRefuel() throws IOException{
    Ship freedom= getShip("freedom.json");
    assertTrue(freedom.getCanRefuel());
  }

  
  private Ship getShip(String name) throws IOException, JsonParseException,
      JsonMappingException {
    ObjectMapper mapper = JsonMapper.getInstance();
    Ship planet = mapper.readValue(this.getClass().getClassLoader()
        .getResourceAsStream(name), Ship.class);
    return planet;
  }
}
