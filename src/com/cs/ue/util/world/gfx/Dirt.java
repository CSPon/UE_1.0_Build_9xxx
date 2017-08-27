package com.cs.ue.util.world.gfx;

public class Dirt implements _GfxTile
{
	private final int texture_id = 2;
	
	private final float coordX = ((float) texture_id % 16f) * 0.0625f;
	private final float coordY = (float) (Math.floor((float) texture_id / 16f) * 0.0625f);
	
	private final float[] texVertex = {coordX, coordY, 0.0f, 
			coordX + 0.0625f, coordY, 0.0f,
			coordX + 0.0625f, coordY + 0.0625f, 0.0f,
			coordX, coordY + 0.0625f, 0.0f};
	
	private final float[] colVertex = {1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f};
	
	@Override
	public int getTextureID()
	{
		return this.texture_id;
	}

	@Override
	public float[] getTextureVectex()
	{
		return this.texVertex;
	}

	@Override
	public float[] getColorVectex()
	{
		return this.colVertex;
	}
}
