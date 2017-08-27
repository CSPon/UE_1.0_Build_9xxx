package com.cs.ue.util.math.vector;

public class Vector3d extends Vector
{
	public Vector3d()
	{
		super(1, 1, 1);
	}
	
	public Vector3d(double i, double j, double k)
	{
		super(i, j, k);
	}

	@Override
	public Vector3d getVector()
	{
		return this;
	}

	@Override
	public void setVector(Vector vector)
	{
		setVector(vector.i, vector.j, vector.k);
	}

	@Override
	public Vector3d crossVector(Vector vector)
	{
		Vector3d v = (Vector3d) vector;
		
		Vector3d resultant = new Vector3d();
		resultant.i = ((this.j * v.k)- (this.k * v.j));
		resultant.j = -((this.i * v.k) - (this.k * v.i));
		resultant.k = ((this.i * v.j) - (v.i * this.j));
		
		return resultant;
	}

	@Override
	public double dotVector(Vector vector)
	{
		Vector3d v = (Vector3d) vector;
		return (this.i * v.i) + (this.j * v.j) + (this.k * v.k);
	}

	@Override
	public Vector3d unitVector()
	{
		Vector3d u = new Vector3d();
		u.i = this.i / magnitutdeVector();
		u.j = this.j / magnitutdeVector();
		u.k = this.k / magnitutdeVector();
		
		return u;
	}
}
