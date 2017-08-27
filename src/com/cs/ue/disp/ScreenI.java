package com.cs.ue.disp;

import org.lwjgl.opengl.DisplayMode;

public interface ScreenI
{
	/**
	 * Sets up window screen settings.<br>
	 * To enable/disable full screen mode, see setFullScreen(boolean) method.
	 * @param width Short value of desired window width.
	 * @param ratio float value of screen ratio. If set to -1 or 1, screen ratio will be adjusted to player's screen ratio.
	 */
	void initialize(final short width, final float ratio, boolean vsync, short xPos, short yPos);
	
	/**
	 * Sets title of this window.
	 * @param title String line of title for this window.
	 */
	void setTitle(final String title);
	
	/**
	 * Enable/disables full screen mode.
	 * @param fullscreen true to enable full screen, false if not.
	 */
	DisplayMode setFullScreen(final boolean fullscreen);
	
	void setDisplayMode(DisplayMode newMode);
	
	/**
	 * 
	 * @param r 0 to 1
	 * @param g 0 to 1
	 * @param b 0 to 1
	 * @see org.lwjgl.opengl.Display#setInitialBackground(float, float, float)
	 */
	void setBackgroundColor(float r, float g, float b);
	
	void setDepthBuffer(float zNear, float zFar);
	
	/**
	 * Creates window using all settings.<br>
	 * If there is any missing settings, create method will automatically adjust to appropriate setting.
	 */
	void create();
	
	/**
	 * Gets window's width.
	 * @return Short value of window width.
	 */
	short getWidth();
	
	/**
	 * Gets window's height.
	 * @return Short value of window height.
	 */
	short getHeight();
	
	/**
	 * Destroys current window.<br>
	 * Use this method only at the end of loop section.
	 */
	void destroy();
	
	/**
	 * Enables display.
	 * @param display DisplayMode
	 */
	void enableDisplay(DisplayMode display);
	
	/**
	 * Disables display.<br>
	 * This has to be called after program ends execution.
	 */
	void disableDisplay();
}
