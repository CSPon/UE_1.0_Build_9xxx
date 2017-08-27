package com.cs.ue.util.texture;

import org.newdawn.slick.opengl.Texture;

/**
 * UTexturePack is a single texture pack container.<br>
 * Purpose of this class is to have multiple, separate texture for single session, if needed.
 * @author Charlie Shin
 *
 */
public class UTexturePack
{
	private Texture texture; // Texture pack
	private float pixres; // Resolution for single texture located in this texture pack
	
	public UTexturePack(Texture texture, float pixres)
	{
		setTexture(texture);
		setPixres(pixres);
	}

	public Texture getTexture()
	{
		return texture;
	}

	public void setTexture(Texture texture)
	{
		this.texture = texture;
	}

	public float getPixres()
	{
		return pixres;
	}

	public void setPixres(float pixres)
	{
		this.pixres = pixres;
	}
	
	public float getX(int id)
	{
		return id % (float)texture.getTextureWidth();
	}
	
	public float getY(int id)
	{
		return id / (float)texture.getTextureWidth();
	}
	
	/**
	 * Releases current texture.
	 */
	public void release()
	{
		if(texture != null)
			texture.release();
	}
	
	/**
	 * Replaces current texture pack with other.
	 * @param texture Texture pack to replace
	 */
	public void replace(Texture texture)
	{
		release();
		setTexture(texture);
	}
}
