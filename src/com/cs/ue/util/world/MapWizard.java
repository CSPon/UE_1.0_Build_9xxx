package com.cs.ue.util.world;

import java.util.Random;

public class MapWizard
{
	private Random seed;
	
	private float[][] lastSeed;
	private float[][] lastLeft, lastBottom;
	
	public MapWizard(final long seed, final int mapsize)
	{
		this.seed = new Random(seed);
		
		lastSeed = new float[mapsize][1];
		lastLeft = new float[mapsize][1];
		lastBottom = new float[1][mapsize];
		
		for(int i = 0; i < mapsize; i++)
		{
			lastLeft[i][0] = this.seed.nextFloat() % 1;
			lastBottom[0][i] = this.seed.nextFloat() % 1;
		}
	}
	
	public float[][] generate(int x, int y, float octave, float persistance)
	{
		float[][] base = generateNoise(0, x, y);
	 
	   return generate(base, octave, persistance);
	}
	
	public float[][] generateMap(int x, int y, float octave, float persistance)
	{
		// Generate noise with one extra colum on the left
		// Add previous float values into colum on the left
		// Smooth out the noise
		// If warping, then use previous edge data to make continuous wave.
		float[][] base = new float[x + 1][y + 1];
		
		for(int i = 1; i < base.length; i++)
			for(int j = 1; j < base[i].length; j++)
				base[i][j] = seed.nextFloat() % 1;
		
		for(int i = 0; i < lastLeft.length; i++)
		{
			base[i][0] = lastSeed[i][0];
			lastLeft[i][0] = base[i][base[0].length - 1];
		}
		for(int j = 0; j < lastBottom.length; j++)
		{
			base[0][j] = lastBottom[0][j];
			lastBottom[0][j] = base[base.length - 1][j];
		}
		
		float[][] map = generate(base, octave, persistance);
		float[][] trimmed = new float[x][y];
		
		for(int i = 1; i < map.length; i++)
			for(int j = 1; j < map[i].length; j++)
				trimmed[i - 1][j - 1] = map[i][j];
		
		return trimmed;
	}
	
	public float[][] generateWrap(float[] horizontal, float[] vertical, int x, int y, int octave, float persistance)
	{
		float[][] base = generateNoise(1, x, y);
		
		for(int i = 0; i < base.length; i++)
		{
			base[0][i] = horizontal[i];
			base[i][0] = vertical[i];
		}
		
		int width = base.length;
		int height = base[0].length;
	 
		float[][][] smoothNoise = new float[octave][][]; //an array of matrix
	 
		//generate smooth noise
		for(int i = 0; i < octave; i++)
		{
			smoothNoise[i] = generateSmoothNoise(base, i);
		}
	 
	    float[][] perlinNoise = new float[width][height];
	    float amplitude = 1.0f;
	    float totalAmplitude = 0.0f;
	 
	    //blend noise together
	    for(int pass = octave - 1; pass >= 0; pass--)
	    {
	       amplitude *= persistance;
	       totalAmplitude += amplitude;
	       
	       for (int i = 0; i < width; i++)
	       {
	          for (int j = 0; j < height; j++)
	          {
	             perlinNoise[i][j] += smoothNoise[pass][i][j] * amplitude;
	          }
	       }
	    }
	 
	   //Normalization
	   for (int i = 0; i < width; i++)
	   {
	      for (int j = 0; j < height; j++)
	      {
	         perlinNoise[i][j] /= totalAmplitude;
	      }
	   }
	 
	   return perlinNoise;
	}
	
	private float[][] generate(float[][] raw, float octave, float persistance)
	{	
	 
		float[][][] smoothNoise = new float[(int) octave][][]; //an array of matrix
	 
		//generate smooth noise
		for (int i = 0; i < octave; i++)
		{
			smoothNoise[i] = generateSmoothNoise(raw, i);
		}
	 
	    float[][] perlinNoise = new float[raw.length][raw[0].length];
	    float amplitude = 1.0f;
	    float totalAmplitude = 0.0f;
	 
	    //blend noise together
	    for (int pass = (int) octave - 1; pass >= 0; pass--)
	    {
	       amplitude *= persistance;
	       //amplitude = (float) Math.pow(persistance, pass);
	       totalAmplitude += amplitude;
	 
	       for (int i = 0; i < raw.length; i++)
	       {
	          for (int j = 0; j < raw[i].length; j++)
	          {
	             perlinNoise[i][j] += smoothNoise[pass][i][j] * amplitude;
	          }
	       }
	    }
	 
	   //Normalization
	    for (int i = 0; i < raw.length; i++)
	       {
	          for (int j = 0; j < raw[i].length; j++)
	          {
	         perlinNoise[i][j] /= totalAmplitude;
	      }
	   }
	 
	   return perlinNoise;
	}
	
	//First generates rough noise.
	private float[][] generateNoise(int start, int width, int height)
	{
		float[][] noise = new float[width][height];
		
		for(int x = start; x < noise.length; x++)
			for(int y = start; y < noise[x].length; y++)
			{
				noise[x][y] = seed.nextFloat() % 1;
			}
		return noise;
	}
	
	//Rough noise will be smoothed
	private float[][] generateSmoothNoise(float[][] base, int octave)
	{
		int width = base.length;
		int height = base[0].length;
		
		float[][] smoothnoise = new float[width][height];
		
		int sample_period = 1 << octave;
		float sample_freq = 1.0f / (float) sample_period;
		
		for(int x = 0;x < width; x++)
		{
			int sample_x0 = (int) (Math.floor(x / sample_period) * sample_period);
			int sample_x1 = (sample_x0 + sample_period) % width;
			float horiz_blend = (x - sample_x0) * sample_freq;
			
			for(int y = 0;y < height; y++)
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
}
