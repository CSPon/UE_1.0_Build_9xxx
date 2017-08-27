package com.cs.ue;

import static org.lwjgl.opengl.GL11.*;

import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.cs.ue.disp.ScreenM;
import com.cs.ue.sys.Manager;
import com.cs.ue.util.texture.UTexture;

/**
 * Uniform Engine Version 1.0.9<br>
 * @see VersionLog
 * @author Charlie Shin
 *
 */
public abstract class UniformEngine
{
	// Move view setting and render setting to screen
	public static final String VERSION = "Uniform Engine 1 NX (v1.0.9000)";
	
	public static final int KEY_ESCAPE = Keyboard.KEY_ESCAPE;
	public static final int KEY_RETURN = Keyboard.KEY_RETURN;
	public static final int KEY_BACKSPACE = Keyboard.KEY_BACK;
	public static final int KEY_UP = Keyboard.KEY_UP;
	public static final int KEY_DOWN = Keyboard.KEY_DOWN;
	public static final int KEY_LEFT = Keyboard.KEY_LEFT;
	public static final int KEY_RIGHT = Keyboard.KEY_RIGHT;
	public static final int KEY_LSHIFT = Keyboard.KEY_LSHIFT;
	public static final int KEY_RSHIFT = Keyboard.KEY_RSHIFT;
	public static final int KEY_LCONTROL = Keyboard.KEY_LCONTROL;
	public static final int KEY_RCONTROL = Keyboard.KEY_RCONTROL;
	public static final int KEY_F1 = Keyboard.KEY_F1;
	public static final int KEY_F2 = Keyboard.KEY_F2;
	public static final int KEY_F3 = Keyboard.KEY_F3;
	public static final int KEY_F4 = Keyboard.KEY_F4;
	public static final int KEY_F5 = Keyboard.KEY_F5;
	public static final int KEY_F6 = Keyboard.KEY_F6;
	public static final int KEY_F7 = Keyboard.KEY_F7;
	public static final int KEY_F8 = Keyboard.KEY_F8;
	public static final int KEY_F9 = Keyboard.KEY_F9;
	public static final int KEY_F10 = Keyboard.KEY_F10;
	public static final int KEY_F11 = Keyboard.KEY_F11;
	public static final int KEY_F12 = Keyboard.KEY_F12;
	
	public ScreenM display;
	public Manager manager;
	
	// Debug Call
	private boolean devmode = false;
	private int logicCalled, deltaLogicCalled;
	
	/**
	 * Creates screen and displays it.<br><br>
	 * In order to have specified screen, parameter must contains 4 parameters<br>
	 * <b>First</b> parameter determines full screen mode. This is boolean value.<br>
	 * <b>Second</b> parameter determines screen width. This is short value.<br>
	 * <b>Third</b> parameter determines screen ratio(ie. 16:9). This is float value.<br>
	 * <b>Fourth</b> parameter determines desired frame rate(or target frame rate). This is byte value.<br><br>
	 * If there is no specified parameters, screen will be adjusted to 1024*576 with 60fps, full screen disabled.
	 * <b>TIPS ON HOW TO SET SCREEN RATIO</b><br>
	 * To set screen ratio, divide height ratio by width ratio.<br>
	 * For example, 16:9 screen ratio will be determined by dividing 9 by 16(0.5625).<br>
	 * @param args arguments passed from .bat file.
	 */
	public void init(String[] args)
	{
		if(args.length > 0 && args.length < 8)
		{
			boolean fullscreen = Boolean.valueOf(args[0]);
			short width = Short.valueOf(args[1]);
			float ratio = Float.valueOf(args[2]);
			byte rate = Byte.valueOf(args[3]);
			boolean vsync = Boolean.valueOf(args[4]);
			short xPos = Short.valueOf(args[5]);
			short yPos = Short.valueOf(args[6]);
			
			display = new ScreenM(width, ratio, vsync, xPos, yPos);
			display.setTitle(VERSION);
			display.setDisplayMode(display.setFullScreen(fullscreen));
			
			manager = new Manager(display.getWidth(), display.getHeight());
			manager.setTargetRate(rate);
			
		}
		else
		{
			display = new ScreenM((short) 1024, 0.5625f, true, (short) 8, (short) 8);
			display.setTitle(VERSION);
			display.setFullScreen(false);
			
			manager = new Manager(display.getWidth(), display.getHeight());
			manager.setTargetRate((byte) 60);
		}
	}
	
	/**
	 * Performs internal process which developer does not need to perform. 
	 */
	protected void run()
	{
		while(manager.isRunning())
		{
			preRender();
			
			logicCheck();
			input();
			update();
			render();
			
			postRender();
		}
		cleanup();
		display.destroy();
		System.exit(0);
	}
	
	/**
	 * Updates keyboard/mouse related variables.<br>
	 * Call this method before render() method.
	 */
	private void input()
	{
		manager.poll();
		keyboard();
		mouse();
	}
	
	/**
	 * Updates graphics-related variables.<br>
	 * Call this method before render() method.
	 */
	public abstract void update();
	
	/**
	 * Updates internal calculations.<br>
	 * <b>IMPORTANT!</b> Do not use this method to run gfx-related updates.<br>
	 * Method is called per-frame wise.
	 */
	public abstract void logic();
	
	/**
	 * Update internal calculations.<br>
	 * <b>IMPORTANT!</b> Do not use this method to run gfx-related updates.<br>
	 * Method is called per-second wise.
	 */
	public abstract void deltaLogic();
	
	/**
	 * Handles keyboard actions.<br>
	 * Game developers must handle this part since Uniform Engine only supports rendering.
	 */
	public abstract void keyboard();
	
	/**
	 * Handles mouse actions.<br>
	 * Game developers must handle this part since Uniform Engine only supports rendering.
	 */
	public abstract void mouse();
	
	/**
	 * Cleans up before destroying screen.<br>
	 * Cleanup() method must be called before destroy() method and after run() loop.
	 */
	public abstract void cleanup();
	
	/**
	 * Renders graphics data into screen.
	 */
	public abstract void render();
	
	public boolean renderDevMode()
	{
		if(devmode)
		{
			String data = "*Developer Mode(Demonstration) " + VERSION + "*\n" +
					"" + getFPS() + "\n" + 
					"" + getUsage() + "\n" +
					"" + "Logic Count : " + logicCalled + "\n" + "Delta Count : " + deltaLogicCalled;
		
			glBlendFunc(GL_ONE_MINUS_DST_COLOR, GL_ONE_MINUS_SRC_COLOR);
			renderString(UTexture.TEX_FONT_ID, data.toString(), 0, 0, 16);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		}
		
		return devmode;
	}
	
	/**
	 * Creates screen.<br>
	 * Creates screen then starts the thread.
	 */
	public void create()
	{
		display.create();
		manager.initInput();
		manager.startClock();
		
		manager.start();
	}
	
	/**
	 * Checks if there is another processor available for multi-threading.
	 * @return true if processor is available, false if not.
	 */
	public int isThreadAvailable()
	{
		return Runtime.getRuntime().availableProcessors();
	}
	
	/**
	 * Performs pre-render, such as screen cleaning and delta-calculations.
	 */
	public void preRender()
	{
		manager.calcDelta();
		manager.clear();
	}
	
	private int delta = 0;
	private void logicCheck()
	{
		int deltaTotal = 0;
		if(isThreadAvailable() > 1)
		{
			deltaTotal = isThreadAvailable() * (int)manager.getFramePerSecond();
			for(int i = 0; i < isThreadAvailable(); i ++)
			{
				new Logic().run();
			}
		}
		else
		{
			logic(); logicCalled++;
			deltaTotal = (int)manager.getFramePerSecond();
			delta++;
		}
		
		if(delta > deltaTotal)
		{
			deltaLogic();
			deltaLogicCalled++;
			delta = 0;
		}
	}
	
	/**
	 * Performs post-render, such as frame sync and memory usage check.
	 */
	public void postRender()
	{
		Display.update();
		if(manager.getRate() > 0)
			Display.sync(manager.getRate());
		
		manager.getFPS();
		manager.getMemory();
	}
	
	/**
	 * Gets frame rate as String data.
	 * @return String data of frame rate.
	 */
	public String getFPS()
	{
		return manager.getFPSString();
	}
	
	/**
	 * Gets memory usage as String data.
	 * @return String data of memory usage.
	 */
	public String getUsage()
	{
		return manager.devMode();
	}
	
	/**
	 * Renders string onto the screen on desired position. <br>
	 * Remember : All remdering is done based ontop-left position.
	 * @param string String to be rendered.
	 * @param x integer value of x position.
	 * @param y integer value of y position.
	 * @param size integer value of font size.
	 */
	public static void renderString(int fontid, Object string, int x, int y, int size)
	{
		x += -(getDisplay().getWidth() / 2);
		y += -(getDisplay().getHeight() / 2);
		
		String[] lines = string.toString().split("\n");
		
		for(int i = 0; i < lines.length; i++)
		{
			char[] line = lines[i].toCharArray();
			int accum = 0;
			
			glPushMatrix();
			glTranslatef(0, 0, 0);
			glBindTexture(GL_TEXTURE_2D, fontid);
			for(char c : line)
			{
				glPushMatrix();
				glTranslatef(x + accum, y + (i * size), 0);
				
				float startX = ((int)c % 16) * 8;
				float endX = startX + 8;
				
				float startY = ((int)c / 16) * 8;
				float endY = startY + 8;
				
				startX /= 128;
				startY /= 128;
				endX /= 128;
				endY /= 128;
				
				glBegin(GL_QUADS);			
					glTexCoord2f(startX, startY);
					glVertex3i(0, 0, 0);
					
					glTexCoord2f(endX, startY);
					glVertex3i(size, 0, 0);
					
					glTexCoord2f(endX, endY);
					glVertex3i(size, size, 0);
					
					glTexCoord2f(startX, endY);
					glVertex3i(0, size, 0);
				glEnd();
				glPopMatrix();
				
				accum += (size / 2);
			}
			glBindTexture(GL_TEXTURE_2D, 0);
			glPopMatrix();
		}
	}
	
	// From UDisplay
	public void setTitle(String title)
	{
		display.setTitle(title);
	}
	public void setFullScreen(boolean fullscreen)
	{
		display.setFullScreen(fullscreen);
	}
	public void setDepthBuffer(float zNear, float zFar)
	{
		display.setDepthBuffer(zNear, zFar);
	}
	public short getScrWidth()
	{
		return display.getWidth();
	}
	public short getScrHeight()
	{
		return display.getHeight();
	}
	public void setBackgroundColor(float r, float g, float b)
	{
		display.setBackgroundColor(r, g, b);
	}
	public static DisplayMode getDisplay()
	{
		return ScreenM.display;
	}
	
	// From UGame
	protected void stop()
	{
		manager.stop();
	}
	public float getDelta()
	{
		return this.manager.getDelta();
	}
	public Manager getInput()
	{
		return this.manager;
	}
	
	private class Logic extends Thread
	{
		public void run()
		{
			delta++;
			logicCalled++;
			logic();
		}
	}
	
	/**
	 * Renders default mouse pointer to screen using immediate method.<br>
	 * If there is any other mouse pointer to render, override this default method.
	 * @param r float value of red components.
	 * @param g float value of green components.
	 * @param b float value of blue components.
	 */
	public void renderPointer(float r, float g, float b)
	{
		glBlendFunc(GL_ONE_MINUS_DST_COLOR, GL_ZERO);
		
		glPushMatrix();
		glTranslatef(manager.getMouseX(), manager.getMouseY(), 0);
		
		glBindTexture(GL_TEXTURE_2D, 0);
		glColor3f(r, g, b);
		glBegin(GL_LINES);
		{
			for(int i = -1; i <=1; i++)
			{
				glVertex2i(i, -16); glVertex2i(i, 16);
				glVertex2i(-16, i); glVertex2i(16, i);
			}
		}
		glEnd();
		glPopMatrix();
		
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public void enableDevMode()
	{
		devmode = (!devmode) ? true : false;
	}
	
	public void noiseTest(float grain)
	{
		Random rnd = new Random();
		if(grain > 1) grain = 1;
		else if(grain < 0) grain = 0;
		int grainTotal = (int) (grain * 100000);
		
		for(int i = 0; i < grainTotal; i++)
		{
			glPushMatrix();
			{
				glTranslatef(rnd.nextInt(getScrWidth()) - (getScrWidth() / 2), rnd.nextInt(getScrHeight()) - (getScrHeight() / 2), 0);
				glBegin(GL_POINTS);
				{
					glColor3f(rnd.nextFloat() % 1, rnd.nextFloat() % 1, rnd.nextFloat() % 1);
					glVertex2i(0, 0);
				}
				glEnd();
			}
			glPopMatrix();
		}
	}
}
