package net.kingsbery.games;

public class Weapon {

  private int strength;
  public Weapon(int i) {
    this.strength=i;
  }
  public static final Weapon AdvancedSword = new Weapon(100);
  public static final Weapon CardboardSword = new Weapon(10);
  public void effect(Player bogie) {
    bogie.decreaseHitPoints(this.strength);    
  }
  
  

}
