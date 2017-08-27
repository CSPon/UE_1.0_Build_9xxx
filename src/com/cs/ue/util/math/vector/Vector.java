package com.cs.ue.util.math.vector;

public abstract class Vector implements _Vector
{
	protected double i, j, k;
	
	public Vector()
	{
		this.i = 1; this.j = 1; this.k = 1;
	}
	public Vector(double i, double j, double k)
	{
		this.i = i; this.j = j; this.k = k;
	}
	public Vector(float i, float j, float k)
	{
		this.i = i; this.j = j; this.k = k;
	}
	public Vector(int i, int j, int k)
	{
		this.i = i; this.j = j; this.k = k;
	}
	@Override
	public void setVector(double i, double j, double k)
	{
		this.i = i; this.j = j; this.k = k;
	}
	@Override
	public double getI()
	{
		return this.i;
	}
	@Override
	public double getJ()
	{
		return this.j;
	}
	@Override
	public double getK()
	{
		return this.k;
	}
	@Override
	public double magnitutdeVector()
	{
		return Math.sqrt(Math.pow(this.i, 2) + Math.pow(this.j, 2) + Math.pow(this.k, 2));
	}
}
