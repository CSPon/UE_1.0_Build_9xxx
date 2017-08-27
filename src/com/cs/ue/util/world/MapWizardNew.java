package com.cs.ue.util.world;

import java.util.Random;

public class MapWizardNew
{	
	private Random baseSeed;
	
	public MapWizardNew(long seed)
	{
		baseSeed = new Random(seed);
	}
	
	public float[][] generateHeightMap(int row, int col, int size, int octave, float persistance)
	{
		float[][] heightMap = new float[size][size];
		float[][] base = getWhiteNoise(row, col, size);
		
		float[][][] smoothNoise = new float[(int) octave][][]; //an array of matrix
		 
		//generate smooth noise
		for(int i = 0; i < octave; i++)
			smoothNoise[i] = generateSmoothNoise(base, i);
		
		float amplitude = 1.0f;
	    float totalAmplitude = 0.0f;
		
		//blend noise together
	    for(int pass = octave - 1; pass >= 0; pass--)
	    {
	       amplitude *= persistance;
	       //amplitude = (float) Math.pow(persistance, pass);
	       totalAmplitude += amplitude;
	 
	       for(int i = 0; i < base.length; i++)
	          for(int j = 0; j < base[i].length; j++)
	             heightMap[i][j] += smoothNoise[pass][i][j] * amplitude;
	    }
	 
	   //Normalization
	   for(int i = 0; i < base.length; i++)
		   for(int j = 0; j < base[i].length; j++)
			   heightMap[i][j] /= totalAmplitude;
	   
	   return heightMap;
	}
	
	private float[][] getWhiteNoise(int row, int col, int size)
	{
		float[][] whiteNoise = new float[size][size];
		
		Random rnd1 = new Random(row);
		Random rnd2 = new Random(col);
		
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++)
			{
				float base = (float) Math.sin(baseSeed.nextFloat());
				float value1 = base * rnd1.nextFloat();
				float value2 = base * rnd2.nextFloat();
				whiteNoise[i][j] = (value1 + value2) % 1;
			}
		
		return whiteNoise;
	}
	
	//Rough noise will be smoothed
	private float[][] generateSmoothNoise(float[][] base, int octave)
	{
		int width = base.length;
		int height = base[0].length;
		
		float[][] smoothnoise = new float[width][height];
		
		int sample_period = 1 << octave;
		float sample_freq = 1.0f / (float) sample_period;
		
		for(int x = 0; x < width; x++)
		{
			int sample_x0 = (int) (Math.floor(x / sample_period) * sample_period);
			int sample_x1 = (sample_x0 + sample_period) % width;
			float horiz_blend = (x - sample_x0) * sample_freq;
			
			for(int y = 0; y < height; y++)
			{
				int sample_y0 = (int) (Math.floor(y / sample_period) * sample_period);
				int sample_y1 = (sample_y0 + sample_period) % height;
				float vert_blend = (y - sample_y0) * sample_freq;
				
				float top = interpolate(base[sample_x0][sample_y0],
			            base[sample_x1][sample_y0], horiz_blend);
				
				float bottom = interpolate(base[sample_x0][sample_y1],
			            base[sample_x1][sample_y1], horiz_blend);
				
				smoothnoise[x][y] = interpolate(top, bottom, vert_blend);
			}
		}
		
		return smoothnoise;
	}
	
	//This method will use two points to calculate slope.
	private float interpolate(float x0, float x1, float alpha)
	{
		return x0 * (1 - alpha) + alpha * x1;
	}
	
	public void levelRegion()
	{
		
	}
}
