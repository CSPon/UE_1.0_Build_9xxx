package com.cs.ue.util.world;

public enum TerrainType
{
	VOID(0x00),
	AIR(0x00),
	GRASS(0x01),
	DIRT(0x02),
	SAND(0x03),
	WATER(0x04);
	
	public final int ID;
	TerrainType(int ID)
	{
		this.ID = ID;
	}
}
