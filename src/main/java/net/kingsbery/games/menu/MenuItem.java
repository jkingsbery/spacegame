package net.kingsbery.games.menu;

/**
 * There are two types of menu items: ones that have an action and ones that
 * have a submenu.
 * 
 * @author jamie
 * 
 */
public abstract class MenuItem {

  public MenuItem() {
  }
  
  public abstract String getName();

  public String toString(){
    return this.getName();
  }
  
  public abstract void action();
}