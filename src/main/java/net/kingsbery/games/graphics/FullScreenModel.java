package net.kingsbery.games.graphics;

/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * This test takes a number up to 13 as an argument (assumes 2 by
 * default) and creates a multiple buffer strategy with the number of
 * buffers given.  This application enters full-screen mode, if available,
 * and flips back and forth between each buffer (each signified by a different
 * color).
 */

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;

public class FullScreenModel {

  private static Color[] COLORS = new Color[] { Color.red, Color.blue,
      Color.green, Color.white, Color.black, Color.yellow, Color.gray,
      Color.cyan, Color.pink, Color.lightGray, Color.magenta, Color.orange,
      Color.darkGray };
  private static DisplayMode[] BEST_DISPLAY_MODES = new DisplayMode[] {
      new DisplayMode(640, 480, 32, 0), new DisplayMode(640, 480, 16, 0),
      new DisplayMode(640, 480, 8, 0) };

  Frame mainFrame;

  public FullScreenModel(int numBuffers, GraphicsDevice device) {
    try {
      GLCapabilities capabilities = SpaceDisplay.createGLCapabilities();
      GameWorldGraphics canvas = new SpaceStationGraphics(capabilities, 800, 500);
      GraphicsConfiguration gc = device.getDefaultConfiguration();
      mainFrame = new Frame(gc);
      mainFrame.setUndecorated(true);
      mainFrame.setIgnoreRepaint(true);
      mainFrame.add(canvas);
      canvas.requestFocus();
      canvas.addKeyListener(new FullScreenKeyListener(canvas));
      device.setFullScreenWindow(mainFrame);

      if (device.isDisplayChangeSupported()) {
        chooseBestDisplayMode(device);
      }
      mainFrame.add(canvas);
      canvas.requestFocus();
      int fps = 30;
      while (true) {
        Thread.sleep(1000 / fps);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      device.setFullScreenWindow(null);
    }
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

      GraphicsEnvironment env = GraphicsEnvironment
          .getLocalGraphicsEnvironment();
      GraphicsDevice device = env.getDefaultScreenDevice();
      FullScreenModel test = new FullScreenModel(numBuffers, device);
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.exit(0);

  }
}
