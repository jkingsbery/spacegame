package net.kingsbery.games;

import java.util.List;

public interface MapDefinition {

  List<Planet> getPlanets();

  List<Ship> getShips();

  Ship getPlayer();

}
