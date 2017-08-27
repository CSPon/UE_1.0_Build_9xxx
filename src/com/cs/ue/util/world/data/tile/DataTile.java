package com.cs.ue.util.world.data.tile;

import org.lwjgl.util.vector.Vector2f;

import com.cs.ue.util.world.TerrainType;

public class DataTile implements _DataTile
{
	private int x, y;
	private int height;
	private TerrainType terrain;
	
	private float seed;
	
	private Vector2f[] vertices;
	public byte[] corners;
	
	public DataTile(int x, int y, int z, TerrainType terrain)
	{
		this.x = x; this.y = y; this.height = z;
		this.terrain = terrain;
		
		vertices = new Vector2f[4];
		corners = new byte[4];
		
		/**
		 * Isometric tile, CCW rule.
		 */
		
		vertices[0] = new Vector2f(0, 0);	// Top
		vertices[1] = new Vector2f(-2, 1);	// Left
		vertices[2] = new Vector2f(0, 2);	// Bottom
		vertices[3] = new Vector2f(2, 1);	// Right
	}

	@Override
	public final int getRow()
	{
		return this.x;
	}

	@Override
	public final int getCol()
	{
		return this.y;
	}

	@Override
	public int getHeight()
	{
		return this.height;
	}

	@Override
	public void setHeight(int z)
	{
		this.height = z;
	}

	@Override
	public TerrainType getTileType()
	{
		return this.terrain;
	}

	@Override
	public void setSeed(float seed)
	{
		this.seed = seed;
	}

	@Override
	public float getSeed()
	{
		return 0;
	}
	
	public Vector2f[] getVertices()
	{	
		return this.vertices;
	}
	
	public void setVertices(Vector2f[] vertices)
	{
		this.vertices = vertices;
	}
}
