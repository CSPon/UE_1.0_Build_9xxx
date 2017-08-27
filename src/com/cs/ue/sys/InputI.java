package com.cs.ue.sys;

public interface InputI
{
	/**
	 * Polls out input devices.
	 * By polling, program checks if there is an input which needs to be resolved or to be discarded.
	 */
	public void poll();
	
	/**
	 * Checks if there is any keyboard input that needs to be resolved.
	 * @return true if there is an input, false if not.
	 */
	public boolean hasKeyNext();
	
	/**
	 * Check if there is any mouse input that needs to be resolved.
	 * @return true if there is an input, false if not.
	 */
	public boolean hasMouseNext();
	
	/**
	 * Returns 256 keys based on their status(Pressed or Released).
	 * @return An array of boolean which based on 256 keyboard layout.
	 */
	public boolean[] getKeys();
	
	/**
	 * Checks if there is any key pressed.<br>
	 * This is different then hasKeyNext, as this one checks buffers.
	 * @return True if there is any key event to be resolved, false if not.
	 */
	public boolean isCurrentKeyEvent();
	
	/**
	 * Returns a event key that needs to be resolved.<br>
	 * Calling isCurrentKeyEvent will prevent this method returning null.
	 * @return Character value between 0 to 255.
	 */
	public char getKeyEventChar();
	
	/**
	 * Gets current active button;
	 * @return 0 for left click, 1 for right click.
	 */
	public byte getMouseButton();
	
	/**
	 * Gets mouse position.
	 * @return short value of horizontal position of the mouse.
	 */
	public short getMouseX();
	
	/**
	 * Gets mouse position.
	 * @return short value of vertical position of the mouse.
	 */
	public short getMouseY();
	
	/**
	 * Gets how many pixel did mouse moved from initial point to final point.
	 * @return Short value of mouse distance
	 */
	public short getMouseDeltaX();
	
	/**
	 * Gets how many pixel did mouse moved from initial point to final point.
	 * @return Short value of mouse distnace
	 */
	public short getMouseDeltaY();
	
	/**
	 * Gets delta x made by keyboard.
	 * @return byte value of horizontal change ranged between -1 to 1.
	 */
	public byte getKeyboardX();
	
	/**
	 * Gets delta y made by keyboard.
	 * @return byte value of vertical change ranged between -1 to 1.
	 */
	public byte getKeyboardY();
}
