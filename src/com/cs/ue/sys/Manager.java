package com.cs.ue.sys;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Manager extends SysManager
{
	public static final byte WORLD_ISO = 0xA;
	public static final byte WORLD_TDV = 0XB;
	public static final byte DRAW_RULE_DIRECT = 0xA;
	public static final byte DRAW_RULE_RANGED = 0XB;
	
	private boolean running;
	
	private byte rate;
	
	/**
	 * Font ID is a unique number which has been given to a font texture, which buffered in via TextureLoader class.<br>
	 */
	protected static int fontid;
	
	public Manager(final short width, final short height)
	{
		this.width = width;
		this.height = height;
	}
	
	public void setTargetRate(final byte rate)
	{
		this.rate = rate;
	}
	
	public void initInput()
	{
		try
		{
			keys = new boolean[256];
			Keyboard.create();
			Keyboard.enableRepeatEvents(true);
			Mouse.create();
			Mouse.setGrabbed(true);
		}
		catch(Exception err)
		{
			err.printStackTrace();
			System.exit(1);
		}
	}
	
	public void start()
	{
		running = true;
	}
	
	public void stop()
	{
		running = false;
	}
	
	public boolean isRunning()
	{
		return this.running;
	}
	
	public void calcDelta()
	{
		calculateDelta();
	}
	
	public byte getRate()
	{
		return this.rate;
	}
}
