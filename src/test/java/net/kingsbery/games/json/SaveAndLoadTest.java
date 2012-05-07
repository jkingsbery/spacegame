package net.kingsbery.games.json;

import java.io.File;

import net.kingsbery.games.GameLoop;
import net.kingsbery.games.util.JsonMapper;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

public class SaveAndLoadTest {

  @Test
  public void foo() throws Exception{
    String source="sol.json";
    ObjectMapper mapper = JsonMapper.getInstance();
    GameLoop result = mapper.readValue(this.getClass().getClassLoader()
        .getResourceAsStream(source), GameLoop.class);
    
    mapper.writeValue(new File("test.json"), result);
    mapper.readValue(new File("test.json"), GameLoop.class);
  }
  
  
  

}
