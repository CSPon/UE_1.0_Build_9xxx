package com.cs.ue.util.math.vector;

public class Vector2f extends Vector2d
{
	public float i, j, k;
	
	public Vector2f(){super(1, 1, 0);}
	
	public Vector2f(float i, float j, float k){super(i , j, k); this.i = i; this.j = j; this.k = k;}

	@Override
	public Vector2f getVector()
	{
		return (Vector2f) super.getVector();
	}

	@Override
	public void setVector(Vector vector)
	{
		super.setVector(vector);
		
		Vector2f v = (Vector2f) vector;
		this.i = v.i;
		this.j = v.j;
		this.k = v.k;
	}

	@Override
	public Vector2f crossVector(Vector vector)
	{
		return (Vector2f) super.crossVector(vector);
	}

	@Override
	public double dotVector(Vector vector)
	{
		return (float) super.dotVector(vector);
	}

	@Override
	public Vector2f unitVector()
	{
		return (Vector2f) super.unitVector();
	}
	
}
