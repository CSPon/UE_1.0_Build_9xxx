package com.cs.ue.sys;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * Handles both user inputs game times
 * @author Charlie Shin
 *
 */
public abstract class SysManager implements HandlerI, InputI
{
	protected float delta;
	
	protected boolean[] keys;
	protected byte kX, kY, mB;
	protected short mX, mY, mDx, mDy;
	
	// Frame Rate
	protected long lastfps, lastframe;
	protected long fps, max_fps, min_fps, cur_fps;
	protected String fpswatch;
	
	// Memory
	protected String memory;
	
	// Display Property
	protected short width, height;

	@Override
	public void poll()
	{
		Keyboard.poll();
		
		for(short i = 0; i < keys.length; i++)
			keys[i] = Keyboard.isKeyDown(i);
		
		kX = 0; kY = 0;
		
		if(keys[Keyboard.KEY_UP])
			kY = (byte) (1);
		if(keys[Keyboard.KEY_DOWN])
			kY = (byte) (-1);
		if(keys[Keyboard.KEY_LEFT])
			kX = (byte) (1);
		if(keys[Keyboard.KEY_RIGHT])
			kX = (byte) (-1);
		
		Mouse.poll();
		
		mX = (short) (Mouse.getX() - (width / 2));
		mY = (short) ((height / 2) - Mouse.getY());
		mDx = (short) (Mouse.getDX() - (width / 2));
		mDy = (short) ((height / 2) - Mouse.getDY());
		mB = (byte) Mouse.getEventButton();
	}

	@Override
	public boolean hasKeyNext()
	{
		return Keyboard.next();
	}

	@Override
	public boolean hasMouseNext()
	{
		return Mouse.next();
	}

	@Override
	public boolean[] getKeys()
	{
		return this.keys;
	}
	
	@Override
	public boolean isCurrentKeyEvent()
	{
		return Keyboard.getEventKeyState();
	}

	@Override
	public char getKeyEventChar()
	{
		return Keyboard.getEventCharacter();
	}

	@Override
	public byte getMouseButton()
	{
		return this.mB;
	}

	@Override
	public short getMouseX()
	{
		return this.mX;
	}

	@Override
	public short getMouseY()
	{
		return this.mY;
	}

	@Override
	public short getMouseDeltaX()
	{
		return this.mDx;
	}

	@Override
	public short getMouseDeltaY()
	{
		return this.mDy;
	}

	@Override
	public byte getKeyboardX()
	{
		return this.kX;
	}

	@Override
	public byte getKeyboardY()
	{
		return this.kY;
	}

	@Override
	public void clear()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	@Override
	public void update(float delta)
	{
		kX *= delta; kY *= delta;
	}

	@Override
	public long getTime()
	{
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	@Override
	public float calculateDelta()
	{
		long time = getTime();
		delta = (time - lastframe);
		lastframe = time;
		
		return delta;
	}
	
	@Override
	public float getDelta()
	{
		return this.delta;
	}

	@Override
	public void startClock()
	{
		calculateDelta();
		lastfps = getTime();
	}

	@Override
	public void getFPS()
	{
		if(getTime() - lastfps > 1000)
		{
			fpswatch = "FPS: " + fps;
			cur_fps = fps;
			if(fps <= min_fps)
				min_fps = fps;
			if(fps > max_fps)
			{
				min_fps = max_fps;
				max_fps = fps;
			}
			fps = 0;
			lastfps += 1000;
		}
		fps++;
	}

	@Override
	public long getFramePerSecond()
	{
		return cur_fps;
	}

	@Override
	public String getFPSString()
	{
		return fpswatch;
	}

	@Override
	public void getMemory()
	{
		int maxMem = (int) (Runtime.getRuntime().maxMemory() / 1048576L);
		int totalMem = (int) (Runtime.getRuntime().totalMemory() / 1048576L);
		int freeMem = (int) (Runtime.getRuntime().freeMemory() / 1048576L);
		int usedMem = totalMem - freeMem;
		
		int usedPercentage = (int) (((float)usedMem / (float)totalMem) * 100);
		int freePercentage = (int) (((float)freeMem / (float)totalMem) * 100);
		int allocPercentage = (int) (((float)totalMem / (float)maxMem) * 100);
		
		memory = "Used Memory : " + usedMem + "MB (" + usedPercentage + "%) Free Memory : " + freeMem + "MB (" + freePercentage + "%)\n" +
				"Allocated : " + totalMem + "MB/" + maxMem + "MB (" + allocPercentage + "%)";
	}

	@Override
	public String devMode()
	{
		return memory;
	}
}
