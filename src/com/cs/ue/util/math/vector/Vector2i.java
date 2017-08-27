package com.cs.ue.util.math.vector;

public class Vector2i extends Vector2f
{
	public int i, j, k;
	
	public Vector2i(){super();}
	
	public Vector2i(int i, int j, int k){super(i, j, k); this.i = i; this.j = j; this.k = k;}

	@Override
	public Vector2i getVector()
	{
		return (Vector2i) super.getVector();
	}

	@Override
	public void setVector(Vector vector)
	{
		super.setVector(vector);
		
		Vector2i v = (Vector2i) vector;
		this.i = v.i;
		this.j = v.j;
		this.k = v.k;
	}

	@Override
	public Vector2i crossVector(Vector vector)
	{
		return (Vector2i) super.crossVector(vector);
	}

	@Override
	public double dotVector(Vector vector)
	{
		return (int) super.dotVector(vector);
	}

	@Override
	public Vector2f unitVector()
	{
		return super.unitVector();
	}
	
}
