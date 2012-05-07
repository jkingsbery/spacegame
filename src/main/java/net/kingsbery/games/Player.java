package net.kingsbery.games;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Player {

  private int totalExperience = 0;
  private int nextLevelUp = 100;
  private int level = 1;
  private static Log log = LogFactory.getLog(Player.class);

  private int hitPoints = 50;
  private int maxHitPoints = 50;

  private Weapon weapon = Weapon.CardboardSword;
  private int numHealingPotions=1;
  
  public Player(int i) {
    this.hitPoints = i;
    this.maxHitPoints = i;
  }

  public Player() {
    this(50);
  }

  public int getLevel() {
    return level;
  }

  public void addExperience(int i) {
    totalExperience += i;
    log.debug(totalExperience + "," + nextLevelUp);
    while (totalExperience >= nextLevelUp) {
      nextLevelUp *= 2;
      level++;
    }
  }

  public void hit(Player bogie) {
    weapon.effect(bogie);
  }

  public void decreaseHitPoints(int i) {
    hitPoints -= i;
  }

  public boolean isDead() {
    return hitPoints <= 0;
  }

  public void heal() {
    if(this.hasHealingPotion()){
      this.hitPoints = this.maxHitPoints;
      this.removeHealingPotion();
    }
  }

  private void removeHealingPotion() {
    if(this.numHealingPotions>0) {
      this.numHealingPotions--;
    }
  }

  private boolean hasHealingPotion() {
    return this.numHealingPotions>0;
  }

  public void setWeapon(Weapon weapon) {
    this.weapon = weapon;
  }

}
