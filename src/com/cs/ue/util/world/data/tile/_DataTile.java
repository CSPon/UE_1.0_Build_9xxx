package com.cs.ue.util.world.data.tile;

import com.cs.ue.util.world.TerrainType;

public interface _DataTile
{
	// Grid position of the tile
	public int getRow();
	public int getCol();
	public int getHeight();
	
	public void setHeight(int z);
	
	public TerrainType getTileType();
	
	// Original seed value
	public void setSeed(final float seed);
	public float getSeed();
}
