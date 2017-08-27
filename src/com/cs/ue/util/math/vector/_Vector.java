package com.cs.ue.util.math.vector;

/**
 * Vector 
 * @author Charlie Shin
 *
 */
public interface _Vector
{
	Vector getVector();
	
	void setVector(Vector vector);
	
	void setVector(double i, double j, double k);
	
	double getI();
	double getJ();
	double getK();
	
	/**
	 * Returns [A] X [B]<br>
	 * If crossed with 2 dimensional vectors, it will return K coefficient<br>
	 * @param vector target vector to cross with.
	 * @return Vector of crossed vectors.
	 */
	Vector crossVector(Vector vector);
	/**
	 * Returns [A] * [B]<br>
	 * Returns scalar value of two vectors
	 * @param vector target vector to cross with.
	 * @return
	 */
	double dotVector(Vector vector);
	/**
	 * Returns unit vector of self<br>
	 * @return unit vector of self.
	 */
	Vector unitVector();
	/**
	 * Returns magnitude of self<br>
	 * @return magnitude of self.
	 */
	double magnitutdeVector();
}
