package net.kingsbery.games;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.util.Random;

import net.kingsbery.games.math.Vector;
import net.kingsbery.games.pcg.LSystem.StochasticLSystem;
import net.kingsbery.games.pcg.SystemDrawer;
import net.kingsbery.games.util.MapFileReader;

public class App {

  int windowWidth = 50;
  int windowHeight = 40;

  Random random = new Random(0);

  public GameStateTracker tracker;

  private static Color[] COLORS = new Color[] { Color.red, Color.blue,
      Color.green, Color.white, Color.black, Color.yellow, Color.gray,
      Color.cyan, Color.pink, Color.lightGray, Color.magenta, Color.orange,
      Color.darkGray };
  private static DisplayMode[] BEST_DISPLAY_MODES = new DisplayMode[] {
      new DisplayMode(640, 480, 32, 0), new DisplayMode(640, 480, 16, 0),
      new DisplayMode(640, 480, 8, 0) };

  Frame mainFrame;

  public static double frameRate = 20.0;
  private StartMenu startMenu;

  public App(int numBuffers, GraphicsDevice device) {
    try {
      GraphicsConfiguration gc = device.getDefaultConfiguration();
      mainFrame = new Frame(gc);
      mainFrame.setUndecorated(true);
      mainFrame.setIgnoreRepaint(true);
      device.setFullScreenWindow(mainFrame);
      if (device.isDisplayChangeSupported()) {
        chooseBestDisplayMode(device);
      }
      Rectangle bounds = mainFrame.getBounds();
      mainFrame.createBufferStrategy(numBuffers);

      BufferStrategy bufferStrategy = mainFrame.getBufferStrategy();

      setStartMenu(new StartMenu(this));

      while (!(tracker == null && startMenu == null)) {
        // TODO: I may want the game update rate to be different than the draw
        // update rate.
        if (tracker != null) {
          tracker.update();
        }
        for (int i = 0; i < numBuffers; i++) {
          Graphics g = bufferStrategy.getDrawGraphics();
          if (!bufferStrategy.contentsLost()) {
            draw(bounds, g);
            bufferStrategy.show();
            g.dispose();
          }
          try {
            Thread.sleep((int) (1000 / frameRate));
          } catch (InterruptedException e) {
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      device.setFullScreenWindow(null);
    }
  }

  private synchronized void draw(Rectangle bounds, Graphics g) {
    if (startMenu != null) {
      drawMain(startMenu, bounds, g);
    } else if (tracker.getMode() == GameMode.LANDING) {
      drawLanding(bounds, g);
    } else if (tracker.getMode() == GameMode.SPACE) {
      drawSpace(bounds, g);
    } else if (tracker.getMode() == GameMode.PAUSE_MENU
        || tracker.getMode() == GameMode.DOCKED) {
      drawSpace(bounds, g);
      drawInGameMenu(tracker.getMenu(), bounds, g);
    } else if(tracker.getMode()==GameMode.MODELING){
      drawColonyModel(bounds,g);
    }
    if (tracker != null && tracker.hasMessage()) {
      drawMessage(bounds, g);
    }
  }

  private void drawColonyModel(Rectangle bounds, Graphics g) {
    g.setColor(Color.gray);
    g.fillRect(0,0, bounds.width, bounds.height);
    g.setColor(Color.white);
//    SystemDrawer.draw(LSystem.getPlantSystem(), 9, bounds, g);
    SystemDrawer.draw(tracker.getFocus(),new StochasticLSystem(),7,bounds,g);
  }

  private void drawLanding(Rectangle bounds, Graphics g) {
    
    g.setColor(tracker.landedOn().getAwtForegroundColor());
    g.fillRect(0, 0, bounds.width, bounds.height);

    g.setColor(tracker.landedOn().getAwtColor());
    g.fillRect(0, bounds.height * 4 / 5, bounds.width, bounds.height / 5);

    // x=0 is center, y is altitude

    Vector position = tracker.getLander().getPosition();
    g.drawImage(MapFileReader.shipImages[tracker.getGameLoop().getPlayerShip().getImage()],
        (int) (bounds.getWidth() / 2 + position.getX()),
        (int) (bounds.getHeight() * 4 / 5 - position.getY()), null);
  }

  // TODO Wrap text
  /*
   *  TODO Need to add synchronization to make sure there are no race conditions between drawing player
   *  getting through all the messages.
   */
  
  private void drawMessage(Rectangle bounds, Graphics g) {
    assert tracker != null;
    if (tracker.getMessage() != null) {
      int left = bounds.width / 2 - 400;
      int top = bounds.height / 2 - 400;
      int width = 1000;
      int height = 300;
      g.setColor(Color.gray);
      g.fillRect(left, top, width, height);
      g.setColor(Color.black);
      g.fillRect(left + 10, top + 10, width - 20, height - 20);
      g.setColor(Color.blue);
      g.setFont(new Font("Arial", Font.BOLD, 24));
      String lines[] = tracker.getMessage().split("\n");
      for (int i = 0; i < lines.length; i++) {
        g.drawString(lines[i].trim(), left + 40, top + 40 + 30 * i);
      }
    }
  }

  private static void drawInGameMenu(Menu pauseMenu, Rectangle bounds,
      Graphics g) {
    if (pauseMenu.stillGoing()) {
      int left = (int) (bounds.getWidth() / 2 - 200);
      int top = (int) (bounds.getHeight() / 2 - 400);
      g.setColor(Color.gray);
      g.fillRect(left, top, 400, 600);
      g.setColor(Color.black);
      g.fillRect(left + 10, top + 10, 400 - 20, 600 - 20);
      g.setFont(new Font("Arial", Font.BOLD, 48));
      if (pauseMenu.hasOptions()) {
        for (int i = 0; i < pauseMenu.getOptions().size(); i++) {
          String str = pauseMenu.getOptions().get(i).toString();
          g.setColor(pauseMenu.getCurrent() == i ? Color.white : Color.blue);
          assert str != null : "Menu "
              + pauseMenu.getOptions().get(i).getClass().getSimpleName()
              + " needs a name";
          g.drawString(str, left + 10, top + 60 * (i + 1));

        }
      }else if(pauseMenu.isCounter()){
        g.setColor(Color.white);
        g.drawString(Integer.toString(pauseMenu.getValue()), left+10, top+60);
      }else{
        throw new RuntimeException("Cannot recognize menu type for " + pauseMenu);
      }
    }
  }

  private static void drawMain(StartMenu menu, Rectangle bounds, Graphics g) {
    g.setColor(Color.black);
    g.fillRect(0, 0, bounds.width, bounds.height);
    g.setColor(Color.blue);
    g.setFont(new Font("Arial", Font.BOLD, 60));
    g.drawString("Space!", (int) (bounds.getWidth() / 2 - 200),
        (int) (bounds.getHeight() / 2 - 200));
    g.setFont(new Font("Arial", Font.BOLD, 48));

    for (int i = 0; i < menu.getOptions().size(); i++) {
      String str = menu.getOptions().get(i).toString();
      g.setColor(menu.getCurrent() == i ? Color.white : Color.blue);
      g.drawString(str, (int) (bounds.getWidth() / 2 - 200),
          (int) (bounds.getHeight() / 2 - 200 + 60 * (i + 1)));

    }
  }

  private void drawSpace(Rectangle bounds, Graphics g) {
    if (tracker.getGameLoop().playerInDanger()) {
      g.setColor(new Color(200, 0, 0));
    } else {
      g.setColor(Color.black);
    }

    g.fillRect(0, 0, bounds.width, bounds.height);

    int centerX = bounds.width / 2 + bounds.x;
    int centerY = bounds.height / 2 + bounds.y;

    double offsetX = tracker.getGameLoop().getPlayerShip().getX() - centerX;
    double offsetY = tracker.getGameLoop().getPlayerShip().getY() - centerY;

    for (Planet planet : tracker.getGameLoop().getPlanets()) {
      drawPlanet(g, planet, offsetX, offsetY);
    }

    g.drawImage(MapFileReader.shipImages[tracker.getGameLoop().getPlayerShip().getImage()], centerX - 21,
        centerY - 21, null);

    for (Ship ship : tracker.getGameLoop().nonPlayerShips()) {
      if (ship.isAlive()) {
        g.drawImage(MapFileReader.shipImages[ship.getImage()], (int) (ship.getX() - offsetX - 21),
            (int) (ship.getY() - offsetY - 21), null);
      }
    }

    g.setColor(Color.red);
    Font font = new Font("Arial", Font.BOLD, 16);
    g.setFont(font);
    g.drawString(tracker.getGameLoop().getPlayerShip().getStatus(), 0, 20);
    g.drawString("Speed: " + tracker.getGameLoop().getPlayerShip().getSpeed(),
        10, 40);
    g.drawString("Funds: $" + tracker.getGameLoop().getPlayerShip().getDollars(),
        10, 60);
    for (Ship ship : tracker.getGameLoop().nonPlayerShips()) {
      if (tracker.getGameLoop().getPlayerShip().dockable(ship)) {
        g.drawString(ship.getName() + " in range (Press ENTER to Dock)", 10,
            100);
      } else if (tracker.getGameLoop().getPlayerShip().closeTo(ship)) {
        g.drawString(ship.getName(), 10, 100);
      }
    }

    // Navigation
    if (tracker.getGameLoop().getPlayerShip().getTarget() != null) {
      int top = (int) (bounds.getHeight() - 200);
      int left = 100;
      Planet target = tracker.getGameLoop().getPlanet(tracker.getGameLoop().getPlayerShip().getTarget());
      Vector direction = tracker.getGameLoop().getPlayerShip().getPosition()
          .direction(target.getLocation()).scale(100);
      g.setColor(target.getAwtColor());
      g.drawLine(left, top, (int) (left + direction.getX()),
          (int) (top + direction.getY()));
      g.drawString(
          target.getName()
              + " - "
              + tracker.getGameLoop().getPlayerShip().getPosition()
                  .getDistance(target.getLocation()), left, top);
    }
    // Minimap
    paintMinimap(bounds, g);

    // Game over.
    if (!tracker.getGameLoop().getPlayerShip().isAlive()) {
      g.setColor(Color.red);
      g.setFont(new Font("Arial", Font.BOLD, 36));
      g.drawString("GAME OVER", (int) (bounds.getWidth() / 2 - 100),
          (int) (bounds.getHeight() / 2 - 10));
    }
  }

  private void paintMinimap(Rectangle bounds, Graphics g) {
    g.setColor(Color.black);
    int miniMapLeftEdge = bounds.width - 220;
    g.fillRect(miniMapLeftEdge, 0, 200, 200);
    g.setColor(Color.white);
    g.drawRect(miniMapLeftEdge, 0, 200, 200);
    g.setColor(Color.white);
    Ship playerShip = tracker.getGameLoop().getPlayerShip();
    int scaling = playerShip.maxKnownDistance(tracker.getGameLoop()) / 100;
    int leftMost = playerShip.getLeftMost(tracker.getGameLoop());
    g.fillOval(
        (int) (miniMapLeftEdge + 20 + ((playerShip.getX() - leftMost) / scaling)),
        (int) (playerShip.getY() / scaling) + 20, 2, 2);
    for (Planet planet : tracker.getGameLoop().getPlanets()) {
      if (playerShip.isKnown(planet.getName())) {
        g.setColor(planet.getAwtColor());
        Vector location = planet.getLocation();
        int size = planet.getSize();
        if(size<100){
          size=100;
        }
        g.fillOval(
            (int) (miniMapLeftEdge + 20 + ((location.getX() - leftMost) / scaling)),
            (int) (location.getY() / scaling) + 20,
            25,//1000 / scaling * size / 300,
            25);//1000 / scaling * size / 300);
      }
    }
  }

  private void drawPlanet(Graphics g, Planet planet, double offsetX,
      double offsetY) {
    g.setColor(planet.getAwtColor());
    Vector location = planet.getLocation();
    int pixelsWide = planet.getSize();
    int pixelsHigh = planet.getSize();
    g.fillOval((int) (location.getX() - offsetX) - pixelsWide,
        (int) (location.getY() - offsetY) - pixelsHigh, pixelsWide * 2,
        pixelsHigh * 2);
  }

  private static DisplayMode getBestDisplayMode(GraphicsDevice device) {
    for (int x = 0; x < BEST_DISPLAY_MODES.length; x++) {
      DisplayMode[] modes = device.getDisplayModes();
      for (int i = 0; i < modes.length; i++) {
        if (modes[i].getWidth() == BEST_DISPLAY_MODES[x].getWidth()
            && modes[i].getHeight() == BEST_DISPLAY_MODES[x].getHeight()
            && modes[i].getBitDepth() == BEST_DISPLAY_MODES[x].getBitDepth()) {
          return BEST_DISPLAY_MODES[x];
        }
      }
    }
    return null;
  }

  public static void chooseBestDisplayMode(GraphicsDevice device) {
    DisplayMode best = getBestDisplayMode(device);
    if (best != null) {
      device.setDisplayMode(best);
    }
  }

  public static void main(String[] args) {
    try {
      int numBuffers = 2;
      if (args != null && args.length > 0) {
        numBuffers = Integer.parseInt(args[0]);
        if (numBuffers < 2 || numBuffers > COLORS.length) {
          System.err.println("Must specify between 2 and " + COLORS.length
              + " buffers");
          System.exit(1);
        }
      }
      GraphicsEnvironment env = GraphicsEnvironment
          .getLocalGraphicsEnvironment();
      GraphicsDevice device = env.getDefaultScreenDevice();
      new App(numBuffers, device);
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.exit(0);
  }

  public synchronized void setTracker(GameStateTracker gameStateTracker) {
    tracker = gameStateTracker;
    if (startMenu != null) {
      mainFrame.removeKeyListener(startMenu.getKeyListener());
    }
    mainFrame.addKeyListener(tracker.getKeyListener());
    this.startMenu = null;
  }

  public synchronized void setStartMenu(StartMenu menu) {
    this.startMenu = menu;
    if (tracker != null) {
      mainFrame.removeKeyListener(tracker.getKeyListener());
    }
    mainFrame.addKeyListener(startMenu.getKeyListener());
    this.tracker = null;
  }

  public synchronized void done() {
    this.tracker = null;
    this.startMenu = null;
    if (tracker != null) {
      mainFrame.removeKeyListener(tracker.getKeyListener());
    }
    if (startMenu != null) {
      mainFrame.removeKeyListener(startMenu.getKeyListener());
    }
  }
}