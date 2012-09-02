package net.kingsbery.games.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.Area;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import javax.swing.JFrame;

import net.kingsbery.games.math.Vector;
import net.kingsbery.games.math.Vector3;
import net.kingsbery.games.pcg.Building;
import net.kingsbery.games.pcg.Building.Wall;
import net.kingsbery.games.pcg.ColonyMap;
import net.kingsbery.games.pcg.Road;
import net.kingsbery.games.util.JsonMapper;
import net.kingsbery.games.world.Player;

import com.sun.opengl.util.FPSAnimator;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;

public class SpaceStationGraphics extends GameWorldGraphics {

  /** The GL unit (helper class). */
  private GLU glu;

  /** The frames per second setting. */
  private int fps = 60;

  /** The OpenGL animator. */
  private FPSAnimator animator;

  /** The earth texture. */
  private Texture earthTexture;

  private ColonyMap colonyMap;

  /**
   * A new mini starter.
   * 
   * @param capabilities
   *          The GL capabilities.
   * @param width
   *          The window width.
   * @param height
   *          The window height.
   */
  public SpaceStationGraphics(GLCapabilities capabilities, int width, int height) {
    addGLEventListener(this);
    try {
      this.colonyMap = JsonMapper.getInstance().readValue(
      // new File("fra-mauro-colony-buildings.json"), ColonyMap.class);
          new File("tranquility-colony-buildings.json"), ColonyMap.class);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * @return Some standard GL capabilities (with alpha).
   */
  public static GLCapabilities createGLCapabilities() {
    GLCapabilities capabilities = new GLCapabilities();
    capabilities.setRedBits(8);
    capabilities.setBlueBits(8);
    capabilities.setGreenBits(8);
    capabilities.setAlphaBits(8);
    return capabilities;
  }

  /**
   * Sets up the screen.
   * 
   * @see javax.media.opengl.GLEventListener#init(javax.media.opengl.GLAutoDrawable)
   */
  public void init(GLAutoDrawable drawable) {
    drawable.setGL(new DebugGL(drawable.getGL()));
    final GL gl = drawable.getGL();

    // Enable z- (depth) buffer for hidden surface removal.
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(GL.GL_LEQUAL);

    // Enable smooth shading.
    gl.glShadeModel(GL.GL_SMOOTH);

    // Define "clear" color.
    gl.glClearColor(0f, 0f, 0f, 0.5f);

    // We want a nice perspective.
    gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);

    // Create GLU.
    glu = new GLU();

    // Load earth texture.
    try {
      InputStream stream = getClass().getClassLoader().getResourceAsStream(
          "earthmap1k.jpg");
      TextureData data = TextureIO.newTextureData(stream, false, "jpg");
      earthTexture = TextureIO.newTexture(data);
    } catch (IOException exc) {
      exc.printStackTrace();
      System.exit(0);
    }

    // Start animator.
    animator = new FPSAnimator(this, fps);
    animator.start();
  }

  /**
   * The only method that you should implement by yourself.
   * 
   * @see javax.media.opengl.GLEventListener#display(javax.media.opengl.GLAutoDrawable)
   */
  double earthSpin = 0.0;

  public void display(GLAutoDrawable drawable) {
    if (!animator.isAnimating()) {
      return;
    }
    final GL gl = drawable.getGL();

    // Clear screen.
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

    // Set camera.
    setCamera(gl, glu);

    // Prepare light parameters.
    float SHINE_ALL_DIRECTIONS = 1;
    float[] lightPos = { 30, 30, 30, SHINE_ALL_DIRECTIONS };
    float[] lightColorAmbient = { 0.2f, 0.2f, 0.2f, 1f };
    float[] lightColorSpecular = { 1f, 1f, 1f, 1f };
    //
    // // Set light parameters.
    gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, lightPos, 0);
    // gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, lightColorAmbient, 0);
    gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPECULAR, lightColorSpecular, 0);
    // gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, lightColorAmbient, 0);
    // Enable lighting in GL.
    gl.glEnable(GL.GL_LIGHT1);
    gl.glEnable(GL.GL_LIGHTING);

    gl.glEnable(GL.GL_COLOR_MATERIAL);

    // Set material properties.
    float[] rgba = { 0.3f, 0.5f, 1f };
    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT, rgba, 0);
    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, rgba, 0);
    gl.glMaterialf(GL.GL_FRONT_AND_BACK, GL.GL_SHININESS, 1.0f);

    // Apply texture.

    // earthTexture.enable();
    // earthTexture.bind();

    // Draw sphere (possible styles: FILL, LINE, POINT).
    // gl.glColor3f(0.3f, 0.5f, 1f);
    // GLUquadric earth = glu.gluNewQuadric();
    // glu.gluQuadricTexture(earth, true);
    // glu.gluQuadricDrawStyle(earth, GLU.GLU_FILL);
    // glu.gluQuadricNormals(earth, GLU.GLU_FLAT);
    // glu.gluQuadricOrientation(earth, GLU.GLU_OUTSIDE);
    // final float radius = 6.378f;
    // final int slices = 16;
    // final int stacks = 16;
    // glu.gluSphere(earth, radius, slices, stacks);
    // glu.gluDeleteQuadric(earth);

    float rgba3[] = new float[] { 0.8f, 0.8f, 0.8f, 1.0f };
    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, rgba3, 0);
    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, rgba3, 0);
    gl.glMaterialf(GL.GL_FRONT_AND_BACK, GL.GL_SHININESS, 1.0f);
    gl.glPushMatrix();
    gl.glRotated(90, 1.0, 0.0,0.0);
    gl.glTranslated(-1 * 0, 0, 0.0);
    GLUquadric quad = glu.gluNewQuadric();
    glu.gluQuadricDrawStyle(quad, GLU.GLU_FILL);
    glu.gluQuadricNormals(quad, GLU.GLU_FLAT);
    glu.gluQuadricOrientation(quad, GLU.GLU_OUTSIDE);
    glu.gluCylinder(quad, 5, 5,100, 16, 16);
    glu.gluDeleteQuadric(quad);

    gl.glPopMatrix();

  }

  private void drawRoad(GL gl, Road road) {
    float rgba3[] = { 1f, 1f, 1f, 1f };
    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT, rgba3, 0);
    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, rgba3, 0);
    gl.glMaterialf(GL.GL_FRONT_AND_BACK, GL.GL_SHININESS, 1.0f);
    gl.glBegin(GL.GL_POLYGON);
    Polygon poly = road.asPolygon();
    for (int i = 0; i < poly.npoints; i++) {
      gl.glVertex3d(-1 * (double) poly.xpoints[i], (double) poly.ypoints[i],
          0.001);
    }
    gl.glEnd();
  }

  private void drawGround(final GL gl) {
    Random rnd = new Random(13);
    int tileSize = 10;
    float rgba3[] = { 0.3f, 0.3f, 0.3f };
    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT, rgba3, 0);
    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, rgba3, 0);
    gl.glMaterialf(GL.GL_FRONT_AND_BACK, GL.GL_SHININESS, 1.0f);
    gl.glBegin(GL.GL_POLYGON);
    gl.glVertex3f(-2000, -2000, 0);
    gl.glVertex3f(-2000, 2000, 0);
    gl.glVertex3f(2000, 2000, 0);
    gl.glVertex3f(2000, -2000, 0);
    gl.glEnd();
  }

  private void drawBuilding(final GL gl, Building building) {
    float rgba3[] = asIntArray(building.getColor());
    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, rgba3, 0);
    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, rgba3, 0);
    gl.glMaterialf(GL.GL_FRONT_AND_BACK, GL.GL_SHININESS, 1.0f);
    if (building.isCylinder()) {
      gl.glPushMatrix();
      gl.glTranslated(-1 * building.getCenter().getX(), building.getCenter()
          .getY(), 0.0);
      GLUquadric quad = glu.gluNewQuadric();
      glu.gluQuadricDrawStyle(quad, GLU.GLU_FILL);
      glu.gluQuadricNormals(quad, GLU.GLU_FLAT);
      glu.gluQuadricOrientation(quad, GLU.GLU_OUTSIDE);
      glu.gluCylinder(quad, building.getWidth() / 2, building.getWidth() / 2,
          building.getHeight(), 16, 16);
      glu.gluDeleteQuadric(quad);

      gl.glPopMatrix();
    } else {
      for (Wall wall : building.getWalls()) {
        gl.glBegin(GL.GL_POLYGON);
        gl.glVertex3d(-1 * wall.getStart().getX(), wall.getStart().getY(), 0);
        gl.glVertex3d(-1 * wall.getEnd().getX(), wall.getEnd().getY(), 0);
        gl.glVertex3d(-1 * wall.getEnd().getX(), wall.getEnd().getY(),
            building.getHeight());
        gl.glVertex3d(-1 * wall.getStart().getX(), wall.getStart().getY(),
            building.getHeight());
        gl.glEnd();
      }
      gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE,
          new float[] { 1.0f, 1.0f, 1.0f, 1.0f }, 0);
      gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, new float[] { 1.0f,
          1.0f, 1.0f, 1.0f }, 0);
    }
  }

  private static float[] asIntArray(Color color) {
    return new float[] { color.getRed() / 255.0f, color.getGreen() / 255.0f,
        color.getBlue() / 255.0f, color.getAlpha() / 255.0f };
  }

  /**
   * Resizes the screen.
   * 
   * @see javax.media.opengl.GLEventListener#reshape(javax.media.opengl.GLAutoDrawable,
   *      int, int, int, int)
   */
  public void reshape(GLAutoDrawable drawable, int x, int y, int width,
      int height) {
    final GL gl = drawable.getGL();
    gl.glViewport(0, 0, width, height);
  }

  /**
   * Changing devices is not supported.
   * 
   * @see javax.media.opengl.GLEventListener#displayChanged(javax.media.opengl.GLAutoDrawable,
   *      boolean, boolean)
   */
  public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
      boolean deviceChanged) {
    throw new UnsupportedOperationException(
        "Changing display is not supported.");
  }

  private double radians = Math.PI / 4;

  /**
   * @param gl
   *          The GL context.
   * @param glu
   *          The GL unit.
   * @param distance
   *          The distance from the screen.
   */

  public boolean noCollision(Vector3 player, int size) {
    Building playerOutline = new Building(player.projectXY(), Vector.I, size,
        size, 2.0, Color.blue);
    for (Building building : colonyMap.getBuildings()) {
      Area area = new Area();
      area.add(new Area(playerOutline.asPolygon()));
      area.intersect(new Area(building.asPolygon()));
      if (!area.isEmpty()) {
        return false;
      }
    }
    return true;
  }

}
