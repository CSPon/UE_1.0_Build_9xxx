package com.cs.ue.util.world.gfx;

public class Terrain
{
	public float[] getTextureCoord(int textureID)
	{
		float[] coord = new float[2];
		
		coord[0] = ((float) textureID % 16f) * 0.0625f;
		coord[1] = (float) (Math.floor((float) textureID / 16f) * 0.0625f);
		
		return coord;
	}
	
	public float[] getTextureVertex(float[] textureCoord)
	{
		float coordX = textureCoord[0];
		float coordY = textureCoord[1];
		
		float[] texVertex = {coordX, coordY, 0.0f, 
				coordX + 0.0625f, coordY, 0.0f,
				coordX + 0.0625f, coordY + 0.0625f, 0.0f,
				coordX, coordY + 0.0625f, 0.0f};
		
		return texVertex;
	}
	
	public float[] getColorVertex(float[] textureCoord, float r, float g, float b)
	{
		float[] colVertex = {r, g, b, 
				r, g, b, 
				r, g, b, 
				r, g, b};
		
		return colVertex;
	}
}
