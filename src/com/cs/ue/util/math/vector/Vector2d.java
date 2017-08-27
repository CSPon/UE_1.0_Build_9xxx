package com.cs.ue.util.math.vector;

public class Vector2d extends Vector
{
	public Vector2d(){super(1, 1, 0);}
	
	public Vector2d(double i, double j, double k){super(i, j, k);}

	@Override
	public Vector2d getVector()
	{
		return this;
	}

	@Override
	public void setVector(Vector vector)
	{
		this.setVector(vector.i, vector.j, vector.k);
	}

	@Override
	public Vector2d crossVector(Vector vector)
	{
		Vector2d v = (Vector2d) vector;
		
		Vector2d resultant = new Vector2d();
		resultant.i = 0;
		resultant.j = 0;
		resultant.k = (this.i * v.j)- (v.i * this.j); 
		
		return resultant;
	}

	@Override
	public double dotVector(Vector vector)
	{
		Vector2d v = (Vector2d) vector;
		
		return (this.i * v.i)+ (this.j * v.j) + (this.k * v.k); 
	}

	@Override
	public Vector2d unitVector()
	{
		Vector2d u = new Vector2d();
		u.i = this.i / magnitutdeVector();
		u.j = this.j / magnitutdeVector();
		u.k = this.k / magnitutdeVector();
		
		return u;
	}
	
	
}
