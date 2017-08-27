package com.cs.ue.util.world;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_DST_COLOR;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_COLOR;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import static com.cs.ue.code.Sample.renderString;

import org.lwjgl.util.vector.Vector2f;

import com.cs.ue.UniformEngine;
import com.cs.ue.code.Sample;
import com.cs.ue.util.texture.UTexture;
import com.cs.ue.util.world.data.region.DataRegion;
import com.cs.ue.util.world.data.tile.DataTile;

public class UWorld
{
	public static int waterlevel;
	
	public static DataRegion[][] dataRegion;
	
	public static final byte REGION_MAX_SIZE = 16;
	
	private MapWizardNew mapGeneratorX;
	
	//private DataRegion[][] dataRegion;
	
	private DataRegion highlighted;
	private DataTile focused;
	
	private int worldSize;
	
	private float octave, persistance, maxheight, watermass;
	
	private boolean generated, adjusted, created;
	
	private float offX, offY;
	
	private boolean applyHeight;
	
	public UWorld(final long seed, final int size)
	{
		mapGeneratorX = new MapWizardNew(seed); // Apply seed value to map generator
		
		this.worldSize = (size > 0) ? size : 1; // Set the size of this world(Minimum 1)
		dataRegion = new DataRegion[size][size]; // Create regions of chunk with appropriate size
	}
	
	public void create() // Creates world
	{
		// Iterate through data region and apply seeds
		// Seeds are currently generated individually
		// TODO Recursive map generation
		if(!generated)
		{
			for(int i = 0; i < worldSize; i++)
			{
				for(int j = 0; j < worldSize; j++)
				{
					dataRegion[i][j] = new DataRegion(i, j, mapGeneratorX.generateHeightMap(i, j, (int) REGION_MAX_SIZE, (int) octave, persistance), this.maxheight);
				}
			}
			
			generated = true;
		}
		else if(!adjusted)
		{
			for(int i = 0; i < worldSize; i++)
			{
				for(int j = 0; j < worldSize; j++)
				{
					dataRegion[i][j].adjustEdges();
				}
			}
			
			adjusted = true;
		}
		
		/*for(DataRegion[] row : dataRegion)
			for(DataRegion region : row)
				region.adjustEdges();*/
		
		// TODO Generate Noise
		// TODO Assign values
		// TODO Align values
		// TODO Push to VBO
		
		else if(generated && adjusted) created = true;
		
		focusAtRegion(worldSize / 2, worldSize / 2, 16);
	}
	
	public void update()
	{
	}
	
	public void worldSetting(float watermass, float maxheight, float octave, float persistance)
	{
		this.maxheight = maxheight;
		waterlevel = (byte) (Math.floor(watermass / (1.0f / (float) this.maxheight)));
		this.octave = octave;
		this.persistance = persistance;
	}
	
	public boolean isWorldCreated(){return created;}

	public void render(int radius, float width, float height)
	{
		float regionHeight = REGION_MAX_SIZE * radius;
		float regionWidth = regionHeight * 2;
		float tileHeight = radius;
		float tileWidth = tileHeight * 2;
		
		for(DataRegion[] region : dataRegion)
			for(DataRegion single : region)
			{
				glPushMatrix();
				float rX = (single.getX() - single.getY()) * regionWidth; rX -= offX;
				float rY = (single.getX() + single.getY()) * regionHeight; rY -= offY;
				
				glTranslatef(rX, rY, 0);
				for(DataTile[] row : single.getRegion())
					for(DataTile tile : row)
					{
						Vector2f[] vertex = tile.getVertices();
						
						float tX = (tile.getRow() - tile.getCol()) * (radius * 2);
						float tY = (tile.getRow() + tile.getCol()) * radius;
						float zBuffer = (applyHeight) ? tile.getHeight() * tileHeight: 0;
						
						glPushMatrix();
						{
							if(tile.getHeight() > waterlevel)
								glColor3f(1.f, 1.f, 1.f);
							else glColor3f(0.f, 0.f, 1.f);
							
							glTranslatef(tX, tY - zBuffer, 0);
							glBegin(GL_TRIANGLES);
							{
								glVertex2f(vertex[0].x * radius, (tile.corners[0] + vertex[0].y) * radius);
								glVertex2f(vertex[1].x * radius, (tile.corners[1] + vertex[1].y) * radius);
								glVertex2f(vertex[3].x * radius, (tile.corners[3] + vertex[3].y) * radius);
								
								glVertex2f(vertex[3].x * radius, (tile.corners[3] + vertex[3].y) * radius);
								glVertex2f(vertex[1].x * radius, (tile.corners[1] + vertex[1].y) * radius);
								glVertex2f(vertex[2].x * radius, (tile.corners[2] + vertex[2].y) * radius);
							}
							glEnd();
							
							glColor3f(0.0f, 0.0f, 0.0f);
							glBegin(GL_LINES);
							{
								glVertex2f(vertex[0].x * radius, (tile.corners[0] + vertex[0].y) * radius); glVertex2f(vertex[1].x * radius, (tile.corners[1] + vertex[1].y) * radius);
								glVertex2f(vertex[1].x * radius, (tile.corners[1] + vertex[1].y) * radius); glVertex2f(vertex[3].x * radius, (tile.corners[3] + vertex[3].y) * radius);
								glVertex2f(vertex[3].x * radius, (tile.corners[3] + vertex[3].y) * radius); glVertex2f(vertex[0].x * radius, (tile.corners[0] + vertex[0].y) * radius);
								
								glVertex2f(vertex[3].x * radius, (tile.corners[3] + vertex[3].y) * radius); glVertex2f(vertex[1].x * radius, (tile.corners[1] + vertex[1].y) * radius);
								glVertex2f(vertex[1].x * radius, (tile.corners[1] + vertex[1].y) * radius); glVertex2f(vertex[2].x * radius, (tile.corners[2] + vertex[2].y) * radius);
								glVertex2f(vertex[2].x * radius, (tile.corners[2] + vertex[2].y) * radius); glVertex2f(vertex[3].x * radius, (tile.corners[3] + vertex[3].y) * radius);
							}
							glEnd();
						}
						glPopMatrix();
					} // End Region
				glPopMatrix();
			} // End World
	} // End render

	public void mouse(short mX, short mY, short width, short height, int radius)
	{
		// TODO Auto-generated method stub
		
		mX += offX; mY += offY;
		
		float regionHeight = REGION_MAX_SIZE * radius;
		float regionWidth = regionHeight * 2;
		
		float tileHeight = radius;
		float tileWidth = tileHeight * 2;
		
		float isoX = (1f / 2f) * ((mX / regionWidth) + (mY / regionHeight));
		float isoY = (1f / 2f) * ((mY / regionHeight) - (mX / regionWidth));
		
		isoX = (float) Math.floor(isoX);
		isoY = (float) Math.floor(isoY);
		
		if(isoX < 0 || isoX > worldSize - 1 || isoY < 0 || isoY > worldSize - 1) highlighted = null;
		else highlighted = dataRegion[(int) isoX][(int) isoY];
		
		isoX = (1f / 2f) * ((mX / tileWidth) + (mY / tileHeight));
		isoY = (1f / 2f) * ((mY / tileHeight) - (mX / tileWidth));
		
		isoX = (float) Math.floor(isoX);
		isoY = (float) Math.floor(isoY);
		
		isoX %= 16; isoX = (float) Math.floor(isoX);
		isoY %= 16; isoY = (float) Math.floor(isoY);
		
		if(isoX < 0 || isoX > REGION_MAX_SIZE - 1 || isoY < 0 || isoY > REGION_MAX_SIZE - 1) focused = null;
		else if(highlighted != null) focused = highlighted.getRegion()[(int) isoX][(int) isoY];
	}
	
	public void focusAtRegion(int x, int y, int radius)
	{
		float regionHeight = REGION_MAX_SIZE * radius;
		float regionWidth = regionHeight * 2;
		
		float rX = (x - y) * regionWidth;
		float rY = ((x + y) * regionHeight) + regionHeight;
		
		offX = rX; offY = rY;
	}

	public void keyboard(float keyboardX, float keyboardY)
	{
		offX -= keyboardX; offY -= keyboardY;
	}
	
	public void applyHeight()
	{
		if(applyHeight) applyHeight = false;
		else applyHeight = true;
	}

	public static DataRegion[][] getWorld()
	{
		return dataRegion;
	}
	
	public static DataRegion getRegion(int i, int j)
	{
		return dataRegion[i][j];
	}
	
	public static DataTile getTile(int i, int j)
	{
		int regionI = i / REGION_MAX_SIZE;
		int regionJ = j / REGION_MAX_SIZE;
		int tileI = i - (regionI * REGION_MAX_SIZE);
		int tileJ = j - (regionJ * REGION_MAX_SIZE);
		
		
		if(regionI >= 0 && regionI < dataRegion.length &&
				regionJ >= 0 && regionJ < dataRegion[regionI].length &&
				tileI >= 0 && tileI < REGION_MAX_SIZE &&
				tileJ >= 0 && tileJ < REGION_MAX_SIZE)
			return dataRegion[regionI][regionJ].getRegion()[tileI][tileJ];
		else return null;
	}
	
	public void renderDevMode(int radius, short width, short height)
	{
		float regionHeight = REGION_MAX_SIZE * radius;
		float regionWidth = regionHeight * 2;
		float tileHeight = radius;
		float tileWidth = tileHeight * 2;
		
		if(highlighted != null)
		{
			float rX = (highlighted.getX() - highlighted.getY()) * regionWidth;
			float rY = (highlighted.getX() + highlighted.getY()) * regionHeight;
			rX -= offX; rY -= offY;
			
			glPushMatrix();
			{
				glTranslatef(rX, rY, 0);
				glBegin(GL_TRIANGLES);
				{
					glColor4f(1, 1, 0, 0.5f);
					glVertex2f(0, 0);
					glVertex2f(-regionWidth, regionHeight);
					glVertex2f(regionWidth, regionHeight);
					
					glVertex2f(-regionWidth, regionHeight);
					glVertex2f(0, 2 * regionHeight);
					glVertex2f(regionWidth, regionHeight);
				}
				glEnd();
				
				glBegin(GL_LINES);
				{
					glColor3f(0, 0, 0);
					glVertex2f(0, 0); glVertex2f(-regionWidth, regionHeight);
					glVertex2f(-regionWidth, regionHeight); glVertex2f(regionWidth, regionHeight);
					glVertex2f(regionWidth, regionHeight); glVertex2f(0, 0);
					
					glVertex2f(-regionWidth, regionHeight); glVertex2f(0, 2 * regionHeight);
					glVertex2f(0, 2 * regionHeight); glVertex2f(regionWidth, regionHeight);
					glVertex2f(regionWidth, regionHeight); glVertex2f(-regionWidth, regionHeight);
				}
				glEnd();
				
				if(focused != null)
				{
					Vector2f[] vertex = focused.getVertices();
					float tX = (focused.getRow() - focused.getCol()) * tileWidth;
					float tY = (focused.getRow() + focused.getCol()) * tileHeight;
					float zBuffer = (applyHeight) ? focused.getHeight() * tileHeight : 0;
					
					glPushMatrix();
					{
						glTranslatef(tX, tY - zBuffer, 0);
						glColor4f(0, 1, 1, 0.5f);
						glBegin(GL_TRIANGLES);
						{
							glVertex2f(vertex[0].x * radius, (focused.corners[0] + vertex[0].y) * radius);
							glVertex2f(vertex[1].x * radius, (focused.corners[1] + vertex[1].y) * radius);
							glVertex2f(vertex[3].x * radius, (focused.corners[3] + vertex[3].y) * radius);
							
							glVertex2f(vertex[3].x * radius, (focused.corners[3] + vertex[3].y) * radius);
							glVertex2f(vertex[1].x * radius, (focused.corners[1] + vertex[1].y) * radius);
							glVertex2f(vertex[2].x * radius, (focused.corners[2] + vertex[2].y) * radius);
						}
						glEnd();
						
						glColor3f(0.0f, 0.0f, 0.0f);
						glBegin(GL_LINES);
						{
							glVertex2f(vertex[0].x * radius, (focused.corners[0] + vertex[0].y) * radius); glVertex2f(vertex[1].x * radius, (focused.corners[1] + vertex[1].y) * radius);
							glVertex2f(vertex[1].x * radius, (focused.corners[1] + vertex[1].y) * radius); glVertex2f(vertex[3].x * radius, (focused.corners[3] + vertex[3].y) * radius);
							glVertex2f(vertex[3].x * radius, (focused.corners[3] + vertex[3].y) * radius); glVertex2f(vertex[0].x * radius, (focused.corners[0] + vertex[0].y) * radius);
							
							glVertex2f(vertex[3].x * radius, (focused.corners[3] + vertex[3].y) * radius); glVertex2f(vertex[1].x * radius, (focused.corners[1] + vertex[1].y) * radius);
							glVertex2f(vertex[1].x * radius, (focused.corners[1] + vertex[1].y) * radius); glVertex2f(vertex[2].x * radius, (focused.corners[2] + vertex[2].y) * radius);
							glVertex2f(vertex[2].x * radius, (focused.corners[2] + vertex[2].y) * radius); glVertex2f(vertex[3].x * radius, (focused.corners[3] + vertex[3].y) * radius);
						}
						glEnd();
					}
					glPopMatrix();
					
					glColor3f(1, 1, 1);
				}
			}
			glPopMatrix();
			
			if(focused != null)
			{
				glBlendFunc(GL_ONE_MINUS_DST_COLOR, GL_ONE_MINUS_SRC_COLOR);
				renderString(focused.getRow() + ", " + focused.getCol() + " H: " + focused.getHeight(), 0, 0 + (6 * 16), 16);
				renderString(focused.corners[0] + " " + focused.corners[1] + " " + focused.corners[2] + " " + focused.corners[3], 0, 0 + (7 * 16), 16);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				//System.out.println(focused.getHeight());
			}
		}
	}
}
