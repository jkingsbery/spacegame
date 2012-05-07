package net.kingsbery.games.scenario;

import net.kingsbery.games.GameStateTracker;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
public interface Scenario {

  void check(GameStateTracker gameStateTracker);

  boolean isDone();

  void setDone(boolean done);
  
  boolean prerequisitesMet(GameStateTracker tracker);

  @JsonIgnore
  boolean isGeneral();
  
  int getStep();
  
  void setStep(int step);

}
