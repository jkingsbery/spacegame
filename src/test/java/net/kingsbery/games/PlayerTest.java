package net.kingsbery.games;

import static org.junit.Assert.*;

import org.junit.Test;

public class PlayerTest {

  @Test
  public void addExperienceLevelUp(){
    Player player = new Player();
    int level = player.getLevel();
    player.addExperience(100);
    assertEquals(level+1,player.getLevel());
  }
  
  @Test
  public void moreExperienceToLevelUpAgain(){
    Player player = new Player();
    player.addExperience(200);
    int level = player.getLevel();
    player.addExperience(100);
    assertEquals(level,player.getLevel());
  }
  
}
