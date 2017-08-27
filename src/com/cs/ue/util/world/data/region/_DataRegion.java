package com.cs.ue.util.world.data.region;

import com.cs.ue.util.world.data.tile.DataTile;

public interface _DataRegion
{
	// Get grid position of the region
	public int getX();
	public int getY();
	
	public DataTile[][] getRegion();
}
