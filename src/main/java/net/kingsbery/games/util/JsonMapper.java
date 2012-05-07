package net.kingsbery.games.util;

import java.awt.Color;
import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.module.SimpleModule;

public class JsonMapper {

  private static ObjectMapper mapper;

  public static synchronized ObjectMapper getInstance() {
    if (mapper == null) {
      mapper = new ObjectMapper();
      SimpleModule testModule = new SimpleModule("MyModule", new Version(1, 0,
          0, null)).addDeserializer(Color.class, new ColorDeserializer()).addSerializer(Color.class,new ColorSerializer());
      mapper.registerModule(testModule);
      mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT,true);
    }
    return mapper;
  }
  
  public static class ColorDeserializer extends JsonDeserializer<Color>{

    @Override
    public Color deserialize(JsonParser parser, DeserializationContext ctx)
        throws IOException, JsonProcessingException {
      Integer i = Integer.parseInt(parser.getText().trim().substring(1), 16);
      Color color = new Color(i);
      return color;
    }
    
  }
  
  private static class ColorSerializer extends JsonSerializer<Color>{

    @Override
    public void serialize(Color color, JsonGenerator jgen,
        SerializerProvider arg2) throws IOException, JsonProcessingException {
      jgen.writeString(Integer.toHexString(color.getRGB()));
    }
    
  }

}
