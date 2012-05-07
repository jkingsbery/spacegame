package net.kingsbery.games.util;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

public class MapFileReaderTest {

  @Test
  public void redString(){
    Color c = MapFileReader.parseStringToColor("FFFF0000");
    assertEquals(Color.red.getRed(),c.getRed());
    assertEquals(0,c.getBlue());
    assertEquals(0,c.getGreen());
  }
  
//  @Test
//  public void redString(){
//    Color c = MapFileReader.parseStringToColor("FFFFFFFF");
//    assertEquals(Color.red.getRed(),c.getRed());
//  }
}
