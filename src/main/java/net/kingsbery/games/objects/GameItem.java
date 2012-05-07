package net.kingsbery.games.objects;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Represents something we can purchase
 * 
 * @author jamie
 * 
 */
public class GameItem implements Serializable {

  private String name;
  private int count;
  private int cost;

  public GameItem(){
    
  }
  
  @JsonCreator
  public GameItem(@JsonProperty("name") String name,
      @JsonProperty("count") int count, @JsonProperty("cost") int cost) {
    this.name = name;
    this.count = count;
    this.cost = cost;
  }

  public String getName() {
    return this.name;
  }

  public int getCount() {
    return count;
  }

  public int getCost() {
    return this.cost;
  }

  public String toString() {
    return name + " @ " +this.cost + ": " + count;
  }

  @Override
  public int hashCode() {
    return this.name.hashCode() + this.count + this.cost;
  }

  @Override
  public boolean equals(Object that) {
    if (that instanceof GameItem) {
      GameItem other = (GameItem) that;
      return this.getName().equals(other.getName())
          && this.getCount() == other.getCount()
          && this.getCost() == other.getCost();
    }else{
      return false;
    }
  }

}
