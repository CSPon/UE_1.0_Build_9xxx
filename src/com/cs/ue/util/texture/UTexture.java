package com.cs.ue.util.texture;

import static org.lwjgl.opengl.GL11.GL_NEAREST;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

/**
 * Handles world textures and can be used to load custom texture pack(limit 1)
 * @author Charlie Shin
 *
 */
public class UTexture
{
	/**
	 * Determines default texture pack's resolution.
	 */
	public static float TEX_RES_GLOBAL;
	
	/**
	 * Determines custom texture pack's resolution.
	 */
	public static float TEX_RES_CUSTOM;
	
	public static int TEX_FONT_ID;
	
	private Texture texturepack;
	private Texture custompack;
	private Texture font;
	
	private float globalRes = 16f;
	
	/**
	 * Loads font texture.
	 */
	public void loadFont()
	{
		font = loadTexture("lib/", "font", "png");
		TEX_FONT_ID = font.getTextureID();
	}
	
	public void defineResolution(final float globalRes)
	{
		this.globalRes = globalRes;
	}
	
	/**
	 * Gets texture pack and calculates global resolution for the texture.<br>
	 * If method fails to load designated texture pack, it will attempt to load default texture pack once again.
	 * @param dir directory of texture location.
	 * @param name name of texture file.
	 * @param format format of texture file.
	 */
	public void targetTexturePack(String dir, String name, String format)
	{
		try
		{
			if(texturepack != null)
				texturepack.release();
			
			texturepack = loadTexture(dir, name, format);
			
			System.out.println(texturepack.getImageWidth());
			System.out.println(texturepack.getTextureWidth());
		}
		catch(Exception err)
		{
			err.printStackTrace();
			loadDefaultTexturePack();
		}
	}
	
	/**
	 * Adds custom pack aside of regular texture pack.<br>
	 * Intention of this custom texture pack is for programmers to have additional textures.<br>
	 * If somehow fail to load the custom texture pack, it will remain null.
	 * @param dir directory of the file's location.
	 * @param name name of the file.
	 * @param format format of the texture pack.
	 * @param globalRes custom resolution(-1 for default resolution of 16px).
	 */
	public void addCustomPack(String dir, String name, String format, final float globalRes)
	{
		try
		{	
			if(custompack != null)
				custompack.release();
			
			custompack = loadTexture(dir, name, format);
			if(this.globalRes != globalRes)
				this.globalRes = globalRes;
		}
		catch(Exception err)
		{
			custompack = null;
		}
	}
	
	/**
	 * Gets x-location of texture with specified ID.<br>
	 * ID starts from 0, ends with whatever number of textures there is in one texture file(exclusive).
	 * @param id ID of the texture, from 0.
	 * @param size Length of one side of the texture file.
	 * @return float value of x position of desired texture.
	 */
	public float getX(int id, float size)
	{
		return id % size;
	}
	
	/**
	 * Gets y-location of texture with specified ID.<br>
	 * ID starts from 0, ends with whatever number of textures there is in one texture file(exclusive).
	 * @param id ID of the texture, from 0.
	 * @param size Length of one side of the texture file.
	 * @return float value of y position of desired texture.
	 */
	public float getY(int id, float size)
	{
		return id / size;
	}
	
	private Texture loadTexture(String dir, String name, String format)
	{
		try
		{
			return TextureLoader.getTexture(format, new FileInputStream(dir + name + "." + format), GL_NEAREST);
		}
		catch(FileNotFoundException err){return null;}
		catch(IOException err){return null;}
	}
	
	public void loadDefaultTexturePack()
	{
		texturepack = loadTexture("lib/tex/", "texturepack", "png");
	}
	
	/**
	 * Gets texture ID of the texture pack.
	 * @return integer value.
	 */
	public int getTexturePack()
	{
		return texturepack.getTextureID();
	}
	
	/**
	 * Gets entire texture data.
	 * @return Texture object
	 */
	public Texture getTextureRaw()
	{
		return this.texturepack;
	}
	
	/**
	 * Gets texture ID of the custom texture pack.
	 * @return integer value.
	 */
	public int getCustomPack()
	{
		return custompack.getTextureID();
	}
	
	/**
	 * Gets entire custom texture data.
	 * @return Texture object
	 */
	public Texture getCustomRaw()
	{
		return this.custompack;
	}
	
	/**
	 * Gets font texture ID.
	 * @return integer value.
	 */
	public int getFont()
	{
		return font.getTextureID();
	}
	
	/**
	 * Releases texture data from memory, cleaning up.
	 */
	public void cleanup()
	{
		texturepack.release();
		if(custompack != null)
			custompack.release();
	}
}
