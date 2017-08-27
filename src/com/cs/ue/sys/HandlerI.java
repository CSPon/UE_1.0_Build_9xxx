package com.cs.ue.sys;

public interface HandlerI
{
	/**
	 * Cleans this screen for new graphics data.
	 */
	public void clear();
	/**
	 * Applies update to keyboard inputs and mouse inputs.<br>
	 * Delta parameter can be obtained via getDelta().<br>
	 * Delta parameter is used to adjust speed so the map movement speed is constant.
	 * @param delta integer value of delta
	 */
	public void update(float delta);
	
	/**
	 * Gets current time.<br>
	 * Only used for Uniform engine only.
	 * @return long value of current time based on ticks
	 */
	public long getTime();
	/**
	 * Calculates delta and returns delta value.
	 * Delta values are used to make movements constant in different frame rates.
	 * @return integer value of delta
	 */
	public float calculateDelta();
	
	/**
	 * Returns last reported delta value
	 * @return float delta value;
	 */
	public float getDelta();
	
	/**
	 * Starts FPS counting.
	 */
	public void startClock();
	
	/**
	 * Calculates frame rate.
	 */
	public void getFPS();
	/**
	 * Returns frame rate numerical value.
	 * @return long type value of current frame rate.
	 */
	public long getFramePerSecond();
	/**
	 * Returns frame rate data.<br>
	 * This method is to be used in debug/developer mode.
	 * @return String value of current frame rate.
	 */
	public String getFPSString();
	
	/**
	 * Calculates current memory usage.<br>
	 * This method calculates total allocate-able memory, total allocated memory, used momory, and free memory.<br>
	 * This method is to be used only by Uniform Engine. 
	 */
	public void getMemory();
	/**
	 * Gets current memory usage data via string.<br>
	 * Game developers can use this method to check current memory usage.
	 * @return String value of current memory usage.
	 */
	public String devMode();
}
