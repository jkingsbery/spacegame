package net.kingsbery.games;

import java.awt.event.KeyListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import net.kingsbery.games.math.Vector;
import net.kingsbery.games.scenario.Scenario;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GameStateTracker implements Serializable {

  private static final Log log = LogFactory.getLog(GameStateTracker.class);

  private transient App app;
  private transient GameKeyListener keyListener;
  private transient Menu menu;

  private boolean stillPlaying = true;
  private GameMode mode = GameMode.SPACE;
  private GameLoop gameLoop;
  private Queue<String> messages = new LinkedBlockingQueue<String>();


  // TODO this really should be a ship or planet
  private Ship dockedWith;
  private Ship lander;
  private boolean landed;

  public GameStateTracker(App app) {
    setApp(app);
  }

  public void setApp(App app) {
    this.app = app;
  }

  public GameMode getMode() {
    return mode;
  }

  public GameLoop getGameLoop() {
    return gameLoop;
  }

  public boolean stillPlaying() {
    return this.stillPlaying;
  }

  public void gameOver() {
    this.stillPlaying = false;
  }

  public void setGameLoop(GameLoop gameLoop) {
    this.gameLoop = gameLoop;
  }

  public Menu getMenu() {
    return menu;
  }

  public void setMessage(String msg) {
    log.info(msg);
    this.messages.add(msg);
  }

  public String getMessage() {
    return this.messages.peek();
  }

  public boolean hasMessage() {
    return !this.messages.isEmpty()
        && (this.mode == GameMode.SPACE || this.mode == GameMode.LANDING);
  }

  public void popMessage() {
    this.messages.remove();
  }

  public boolean live() {
    return !hasMessage() && getMode() == GameMode.SPACE;
  }

  public void addScenario(Scenario scenario) {
    this.getScenarios().put(scenario.getClass().getSimpleName(), scenario);
  }

  public void update() {
    if (playerAlive()) {
      for (Scenario scenario : this.getScenarios().values()) {
        if (!scenario.isDone()) {
          scenario.check(this);
        }
      }
      if (landing()) {
        if (this.lander.getPosition().getY() > 800) {
          this.enterOrbit();
          this.lander = null;
        } else if (this.lander.getPosition().getY() > 0) {
          this.lander.setGravity(new Vector(0, -1));
          this.lander.update();
        } else if (this.lander.getVelocity().size() > 15) {
          this.gameLoop.getPlayerShip().crash();
        } else if (!this.hasLanded()) {
          this.touchDown();
          lander.fullStop();
          System.out.println("Successfully landed");
        } else if (this.landed) {
          this.lander.setGravity(Vector.ZERO);
          lander.update();
          if (lander.getVelocity().size() > 1) {
            System.out.println("Lift off!");
            this.lander.update();
            this.liftOff();
          }
        }

      }
      else if (live()) {
        GameMode newGameMode = getGameLoop().update();
        if (newGameMode == GameMode.LANDING) {
          this.attemptLanding();
        }
      }
    }
  }

  private boolean playerAlive() {
    return this.getGameLoop()!=null && this.getPlayer()!=null && this.getPlayer().isAlive();
  }

  private void enterOrbit() {
    log.info("Entering orbit");
    resume();
    this.getPlayer().setVelocity(
        this.getPlayer().getVelocity().normalize()
            .scale(-1 * this.lander.getVelocity().size()/3));
  }

  private void liftOff() {
    this.landed = false;
  }

  public void playerAction() {
    for (Ship ship : gameLoop.getNonPlayerShips()) {
      if (gameLoop.getPlayerShip().dockable(ship)) {
        this.mode = GameMode.DOCKED;
        this.dockedWith = ship;
        this.menu = new DockMenu(this, ship);
        this.update();
      }
    }
  }

  public Ship dockedWith() {
    return this.dockedWith;
  }

  public void pause() {
    this.mode = GameMode.PAUSE_MENU;
    this.menu = new PauseMenu(this);
  }

  public void start() {
    Scenario introScenario = gameLoop.getIntroScenarioInstance();
    if (introScenario != null && !hasStartedScenario(introScenario)) {
      addScenario(introScenario);
    }
    this.resume();
  }

  public void resume() {
    this.mode = GameMode.SPACE;
  }

  public void goBack() {
    this.dockedWith = null;
    this.mode = GameMode.SPACE;
  }

  public void startMenu() {
    this.app.setStartMenu(new StartMenu(app));
  }

  public void setMessage(Object x) {
    setMessage(x.toString());
  }

  public boolean hasCompletedScenario(Class<? extends Scenario> clazz) {
    assert this.getScenarios()!=null;
    return hasStartedScenario(clazz)
        && this.getScenarios().get(clazz.getSimpleName()).isDone();
  }
  
  public boolean hasStartedScenario(
      Class<? extends Scenario> clazz) {
    return this.getScenarios().containsKey(clazz.getSimpleName());
  }

  public boolean hasStartedScenario(Scenario scenario) {
    return hasStartedScenario(scenario.getClass());
  }
  
  public Map<String,Scenario> getScenarios(){
    return this.getGameLoop().getScenarios();
  }

  public KeyListener getKeyListener() {
    if (keyListener == null) {
      this.keyListener = new GameKeyListener(this);
    }
    return this.keyListener;
  }

  public List<String> getMessages() {
    return new ArrayList<String>(this.messages);
  }

  

  public Ship getPlayer() {
    return getGameLoop().getPlayerShip();
  }

  private void attemptLanding() {
    this.lander = new Ship("Player", 13, 0, 500);
    this.lander.setVelocity(new Vector(0, -1
        * this.getPlayer().getVelocity().size()));
    this.mode = GameMode.LANDING;
  }

  public Ship getLander() {
    return this.lander;
  }

  private boolean landing() {
    return this.mode == GameMode.LANDING;
  }

  private void touchDown() {
    this.landed = true;
  }

  public boolean hasLanded() {
    return this.landed;
  }

  public Planet landedOn() {
    for (Planet planet : this.getGameLoop().getPlanets()) {
      if (this.getPlayer().getPosition().getDistance(planet.getLocation()) < planet
          .getSize()) {
        return planet;
      }
    }
    return null;
  }

  public void showModel() {
    this.mode=GameMode.MODELING;
    
  }

  /**
   * Offset used in Colony Model mode
   */
  Vector focus = new Vector(0,0);
  public Vector getFocus() {
    return focus;
  }
  public void shiftFocus(Vector v){
    focus=focus.plus(v);
  }

  public void showModel3d() {
    this.mode=GameMode.MODELING_ADVANCED;
  }

}
