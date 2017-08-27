package com.cs.ue.util.world.data.region;

import static com.cs.ue.util.world.UWorld.waterlevel;

import com.cs.ue.util.world.TerrainType;
import com.cs.ue.util.world.UWorld;
import com.cs.ue.util.world.data.tile.DataTile;

public class DataRegion implements _DataRegion
{	
	private int x, y;
	
	// Data region contains specific size of blocks, defined by UWorld class
	private DataTile[][] region = new DataTile[UWorld.REGION_MAX_SIZE][UWorld.REGION_MAX_SIZE];
	
	// Creates new region with given index number
	public DataRegion(final int x, final int y, final float[][] seed, float maxheight)
	{
		this.x = x; this.y = y;
		
		// Iterate through each tile and apply seeds
		for(int i = 0; i < UWorld.REGION_MAX_SIZE; i++)
		{
			for(int j = 0; j < UWorld.REGION_MAX_SIZE; j++)
			{
				int leveled = (int) Math.floor(seed[i][j] / (1.0f / maxheight)); // Height is in integer.
				
				region[i][j] = new DataTile(i, j, leveled, TerrainType.VOID); // Initialize tile with void type first
				region[i][j].setSeed(seed[i][j]); // Apply raw seed value to the tile
			}
		}
	}

	@Override
	public int getX()
	{
		return this.x;
	}

	@Override
	public int getY()
	{
		return this.y;
	}

	@Override
	public DataTile[][] getRegion()
	{
		return this.region;
	}

	/**
	 * 
	 */
	public void adjustEdges()
	{
		for(int i = 0; i < region.length; i++)
			for(int j = 0; j < region.length; j++)
			{
				checkEdges(region[i][j]);
			} // End Tile Edge
	}
	
	private void checkEdges(DataTile tile)
	{
		if(tile == null) return;
		
		int[][] edges = new int[3][3]; // Collect edge data
		
		int k = 0; int l = 0;
		for(int i = -1; i <= 1; i++)
		{
			for(int j = -1; j <= 1; j++)
			{
				int I = (x * UWorld.REGION_MAX_SIZE) + tile.getRow() + i;
				int J = (y * UWorld.REGION_MAX_SIZE) + tile.getCol() + j;
				if(UWorld.getTile(I, J) != null)
					edges[k][l] = UWorld.getTile(I, J).getHeight();
				else edges[k][l] = -1;
				
				l++;
			}
			l = 0;
			k++;
		}
		
		int height = tile.getHeight();
		byte[] corners = tile.corners;
		
		if(edges[1][0] > -1)
		{
			if(edges[1][0] > height)
			{
				corners[0] = -1;
				corners[3] = -1;
			}
			else if(edges[1][0] < height && edges[1][0] < waterlevel)
			{
				corners[0] = 1;
				corners[3] = 1;
			}
			else
			{
				if(edges[2][0] > -1)
				{
					if(edges[2][0] > height)
						corners[3] = -1;
					else if(edges[2][0] < height && edges[2][0] < waterlevel)
						corners[3] = 1;
				}
				else if(edges[1][0] > height)
					corners[3] = -1;
				else if(edges[1][0] < height && edges[1][0] < waterlevel)
					corners[3] = 1;
				else if(edges[2][1] > -1)
				{
					if(edges[2][1] > height)
						corners[3] = -1;
					else if(edges[2][1] < height && edges[2][1] < waterlevel)
						corners[3] = 1;
				}
				
				if(edges[0][0] > -1)
				{
					if(edges[0][0] > height)
						corners[0] = -1;
					else if(edges[0][0] < height && edges[0][0] < waterlevel)
						corners[0] = 1;
				}
				else if(edges[1][0] > height)
					corners[0] = -1;
				else if(edges[1][0] < height && edges[1][0] < waterlevel)
					corners[0] = 1;
				else if(edges[0][1] > -1)
				{
					if(edges[0][1] > height)
						corners[0] = -1;
					else if(edges[0][1] < height && edges[0][1] < waterlevel)
						corners[0] = 1;
				}
			}
		}
		
		if(edges[0][1] > -1)
		{
			if(edges[0][1] > height)
			{
				corners[0] = -1;
				corners[1] = -1;
			}
			else if(edges[0][1] < height && edges[0][1] < waterlevel)
			{
				corners[0] = 1;
				corners[1] = 1;
			}
			else
			{
				if(edges[0][0] > -1)
				{
					if(edges[0][0] > height)
						corners[0] = -1;
					else if(edges[0][0] < height && edges[0][0] < waterlevel)
						corners[0] = 1;
				}
				else if(edges[0][1] > height)
					corners[0] = -1;
				else if(edges[0][1] < height && edges[0][1] < waterlevel)
					corners[0] = 1;
				else if(edges[1][0] > -1)
				{
					if(edges[1][0] > height)
						corners[0] = -1;
					else if(edges[1][0] < height && edges[1][0] < waterlevel)
						corners[0] = 1;
				}
				
				if(edges[0][2] > -1)
				{
					if(edges[0][2] > height)
						corners[1] = -1;
					else if(edges[0][2] < height && edges[0][2] < waterlevel)
						corners[1] = 1;
				}
				else if(edges[0][1] > height)
					corners[1] = -1;
				else if(edges[0][1] < height && edges[0][1] < waterlevel)
					corners[1] = 1;
				else if(edges[1][2] > -1)
				{
					if(edges[1][2] > height)
						corners[1] = -1;
					else if(edges[1][2] < height && edges[0][1] < waterlevel)
						corners[1] = 1;
				}
			}
		}
		
		if(edges[1][2] > -1)
		{
			if(edges[1][2] > height)
			{
				corners[1] = -1;
				corners[2] = -1;
			}
			else if(edges[1][2] < height && edges[1][2] < waterlevel)
			{
				corners[1] = 1;
				corners[2] = 1;
			}
			else
			{
				if(edges[0][2] > -1)
				{
					if(edges[0][2] > height)
						corners[1] = -1;
					else if(edges[0][2] < height && edges[0][2] < waterlevel)
						corners[1] = 1;
				}
				else if(edges[1][2] > height)
					corners[1] = -1;
				else if(edges[1][2] < height && edges[1][2] < waterlevel)
					corners[1] = 1;
				else if(edges[0][1] > -1)
				{
					if(edges[0][1] > height)
						corners[1] = -1;
					else if(edges[0][1] < height && edges[0][1] < waterlevel)
						corners[1] = 1;
				}
				
				if(edges[2][2] > -1)
				{
					if(edges[2][2] > height)
						corners[2] = -1;
					else if(edges[2][2] < height && edges[2][2] < waterlevel)
						corners[2] = 1;
				}
				else if(edges[1][2] > height)
					corners[1] = -1;
				else if(edges[1][2] < height && edges [1][2] < waterlevel)
					corners[1] = 1;
				else if(edges[2][1] > -1)
				{
					if(edges[2][1] > height)
						corners[2] = -1;
					else if(edges[2][1] < height && edges[2][1] < waterlevel)
						corners[2] = 1;
				}
			}
		}
		
		if(edges[2][1] > -1)
		{
			if(edges[2][1] > height)
			{
				corners[2] = -1;
				corners[3] = -1;
			}
			else if(edges[2][1] < height && edges[2][1] < waterlevel)
			{
				corners[2] = 1;
				corners[3] = 1;
			}
			else
			{
				if(edges[2][2] > -1)
				{
					if(edges[2][2] > height)
						corners[2] = -1;
					else if(edges[2][2] < height && edges[2][2] < waterlevel)
						corners[2] = 1;
				}
				else if(edges[2][1] > height)
					corners[2] = -1;
				else if(edges[2][1] < height && edges[2][1] < waterlevel)
					corners[2] = 1;
				else if(edges[1][2] > -1)
				{
					if(edges[1][2] > height)
						corners[2] = -1;
					else if(edges[1][2] < height && edges[1][2] < waterlevel)
						corners[2] = 1;
				}
				
				if(edges[2][0] > -1)
				{
					if(edges[2][0] > height)
						corners[3] = -1;
					else if(edges[2][0] < height && edges[2][0] < waterlevel)
						corners[3] = 1;
				}
				else if(edges[2][1] > height)
					corners[3] = -1;
				else if(edges[2][1] < height && edges[2][1] < waterlevel)
					corners[3] = 1;
				else if(edges[1][0] > -1)
				{
					if(edges[1][0] > height)
						corners[3] = -1;
					else if(edges[1][0] < height && edges[1][0] < waterlevel)
						corners[3] = 1;
				}
			}
		}
		
		if(tile.corners[0] == -1 && tile.corners[1] == -1 && tile.corners[2] == -1 && tile.corners[3] == -1)
		{
			tile.corners = new byte[]{0, 0, 0, 0};
			tile.setHeight(tile.getHeight() + 1);
			
			System.out.println("Tile got promoted");
			
			checkEdges(tile);
		}
		else if(tile.corners[0] == 1 && tile.corners[1] == 1 && tile.corners[2] == 1 && tile.corners[3] == 1)
		{
			tile.corners = new byte[]{0, 0, 0, 0};
			tile.setHeight(tile.getHeight() - 1);
			
			System.out.println("Tile got demoted");
			
			checkEdges(tile);
		}
	}
}
