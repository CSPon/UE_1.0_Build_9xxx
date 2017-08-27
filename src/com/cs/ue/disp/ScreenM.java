package com.cs.ue.disp;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PERSPECTIVE_CORRECTION_HINT;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glTexParameteri;

import java.awt.Toolkit;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class ScreenM implements ScreenI
{
	public static DisplayMode display;
	private String title;
	private boolean enable_fullscreen, vsync;
	
	private short xPos, yPos;
	
	private float zNear, zFar;
	
	public ScreenM(short width, float ratio, boolean vsync, short xPos, short yPos)
	{
		initialize(width, ratio, vsync, xPos, yPos);
	}
	
	@Override
	public void initialize(short width, float ratio, boolean vsync, short xPos, short yPos)
	{
		zNear = -1; zFar = 1;
		title = "";
		enable_fullscreen = false;
		this.vsync = vsync;
		if(ratio > 1 || ratio < 0)
		{
			float newRatio = (Toolkit.getDefaultToolkit().getScreenSize().height) / (Toolkit.getDefaultToolkit().getScreenSize().width);
			display = new DisplayMode(width, (short) (width * newRatio));
		}
		else display = new DisplayMode(width, (short) (width * ratio));
		
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	@Override
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	@Override
	public DisplayMode setFullScreen(boolean fullscreen)
	{
		enable_fullscreen = fullscreen;
		if(this.enable_fullscreen)
		{
			try
			{
				for(DisplayMode mode : Display.getAvailableDisplayModes())
				{
					if(mode.isFullscreenCapable() && mode.getWidth() == this.getWidth())
						return mode;
				}
			}
			catch(Exception err){err.printStackTrace(); System.exit(1);}
		}
		
		return display;
	}
	
	@Override
	public void setDisplayMode(DisplayMode newMode)
	{
		display = newMode;
	}

	@Override
	public void setBackgroundColor(float r, float g, float b)
	{
		try
		{
			Display.setInitialBackground(r, g, b);
		}
		catch(Exception err){err.printStackTrace(); System.exit(1);}
	}

	@Override
	public void setDepthBuffer(float zNear, float zFar)
	{
		this.zNear = zNear;
		this.zFar = zFar;
	}

	@Override
	public void create()
	{
		try
		{
			Display.setVSyncEnabled(vsync);
			Display.setDisplayMode(display);
			Display.setFullscreen(enable_fullscreen);
			Display.setTitle(title);
			Display.setLocation(xPos, yPos);
			Display.create();
			
			enableDisplay(display);
		}
		catch(Exception err)
		{
			err.printStackTrace();
			System.exit(1);
		}
	}
	
	@Override
	public short getWidth()
	{
		return (short) display.getWidth();
	}
	
	@Override
	public short getHeight() 
	{
		return (short) display.getHeight();
	}
	
	@Override
	public void destroy()
	{
		disableDisplay();
		Display.destroy();
	}
	
	@Override
	public void enableDisplay(DisplayMode display)
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(-(display.getWidth() / 2), (display.getWidth() / 2), (display.getHeight() / 2), -(display.getHeight() / 2), zNear, zFar);
		glMatrixMode(GL_MODELVIEW);
		
		glEnable(GL_TEXTURE_2D);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST_MIPMAP_LINEAR);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
	}
	
	@Override
	public void disableDisplay() 
	{
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
		glDisable(GL_DEPTH_TEST);
	}
}
