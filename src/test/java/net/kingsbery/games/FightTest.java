package net.kingsbery.games;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class FightTest {

  
  private Player player;

  @Before
  public void setup(){
    player = new Player(50);    
  }

  @Test
  public void fightOneBogie(){
    Player bogie = new Player(10);
    assertFalse(bogie.isDead());
    player.hit(bogie);
    assertTrue(bogie.isDead());
  }
  
  @Test
  public void fightBossRequiresManyHits(){
    Player boss = new Player(100);
    player.hit(boss);
    assertFalse(boss.isDead());
  }
  
  @Test
  public void playerWithAdvancedSwordKillsBoss(){
    Player boss = new Player(100);
    player.setWeapon(Weapon.AdvancedSword);
    player.hit(boss);
    assertTrue(boss.isDead());
  }
  
  @Test
  public void tooManyHitsKillsPlayer(){
    Player bogie = new Player(10);
    bogie.hit(player);
    bogie.hit(player);
    bogie.hit(player);
    bogie.hit(player);
    bogie.hit(player);
    assertTrue(player.isDead());
  }
  
  
  @Test
  public void playerHealsThenHesOk(){
    Player bogie = new Player(10);
    bogie.hit(player);
    bogie.hit(player);
    bogie.hit(player);
    player.heal();
    bogie.hit(player);
    bogie.hit(player);
    assertFalse(player.isDead());
  }
  
  @Test
  public void playerCanOnlyHealWithPotion(){
    Player bogie = new Player(10);
    bogie.hit(player);
    bogie.hit(player);
    bogie.hit(player);
    player.heal();
    bogie.hit(player);
    bogie.hit(player);
    bogie.hit(player);
    bogie.hit(player);
    bogie.hit(player);
    player.heal();
    assertTrue(player.isDead());
  }
}
