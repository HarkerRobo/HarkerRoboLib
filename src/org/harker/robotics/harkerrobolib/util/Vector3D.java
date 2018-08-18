package org.harker.robotics.harkerrobolib.util;

/**
 * Represents a three-dimensional vector 
 * @author Manan Shah
 */
public class Vector3D
{
    /**
     * The X-position of the point.
     * This represents movement along the axis from one alliance side of the
     * field towards the other alliance side of the field.
     */
    private double X;
    public double getX() {return X;}

    /**
     * The Y-position of the point.
     * This represents movement along the axis from the sides of the field,
     * parallel to the alliance walls and game objectives.
     */
    private double Y;
    public double getY() {return Y;}

    /**
     * The Z-position of the point.
     * This represents movement up and down in the direction of gravity by the Earth.
     * Zero represents a ground-level object, while an increasing value would represent moving up.
     */
    private double Z;
    public double getZ() {return Z;}

    /**
     * The direction of the vector with respect to the origin. The angle is in radians.
     */
    private double direction;
    public double getDirection() {
        if (direction == 0) {
            //If the direction has not been initialized (i.e. it equals to zero), calculate the direction.
            //If X = 0, and thus (Y / X) would be undefined, set the angle to either (Pi / 2) or (3 Pi / 2)
            //depending on if Y is positive or negative. Otherwise take the arctangent of the ratio of
            //Y to X.
            //If the angle is negative (as the range of arctangent is (-Pi / 2, Pi / 2), calculate the
            //positive angle with the same heading as the one returned by arctangent.
            if (X == 0) {
                if (Y == 0)
                    throw new RuntimeException("Error: Tried to get direction of a zero vector");
                direction = (Y > 0) ? Math.PI / 2 : 3 * Math.PI / 2;
            } else {
                double theta = MathUtil.aTan(Y / X);
                direction = (theta >= 0) ? theta : Math.PI - theta;
            }
        }
        return direction;
    }

    /**
     * The magnitude of the vector with respect to the origin.
     */
    private double magnitude;
    public double getMagnitude() {
        if (magnitude == 0 && X != 0 && Y != 0 && Z != 0) {
            //If the magnitude has not been initialized (i.e. it equals to zero) and it is not a
            //zero vector, calculate the magnitude.
            magnitude = Math.sqrt(X * X + Y * Y + Z * Z);
        }
        return magnitude;
    }
	
    /**
     * Initializes a zero Vector3D
     */

    public Vector3D() {
        this(0.0, 0.0, 0.0);
    }

    /**
     * Initializes a new Vector3D with the given coordinates
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public Vector3D(double x, double y, double z) {
	this.X = x; 
	this.Y = y; 
	this.Z = z; 
    }
    
    /**
     * Constructs a 3D vector using magnitude/direction (with Z = 0)
     * @param magnitude the magnitude of the vector
     * @param direction the direction of the vector
     */
    public Vector3D(double magnitude, double direction) {
        this(MathUtil.cos(direction)*magnitude,
             MathUtil.sin(direction)*magnitude,
             0);
        this.magnitude = magnitude;
        this.direction = direction;
    }
    
    /** 
     * Adds this Vector3D with another specified Vector3D
     * @param v the vector to add
     * @return a new Vector3D that is the sum of the two vectors
     */
    public Vector3D add(Vector3D v) {
        return new Vector3D(this.X + v.X, this.Y + v.Y, this.Z + v.Z);
    }
    
    /**
     * Subtracts this Vector3D with another specified Vector3D
     * @param v the vector to subtract from this vector 
     * @return a new Vector3D that is the difference of the two vectors
     */
    public Vector3D subtract(Vector3D v) {
        return new Vector3D(this.X - v.X, this.Y - v.Y, this.Z - v.Z);
    }
    
    /**
     * Scales the given vector by performing scalar multiplication
     * @param d the getValue with which to scale this vector
     * @return a new Vector that is the original vector scaled by getValue d
     */
    public Vector3D scale(double d) {
        return new Vector3D(this.X * d, this.Y * d, this.Z * d);
    }
    
    /**
     * Gets the magnitude of this vector
     * @return the magnitude of this vector
     */
    public double magnitude() {
        return Math.sqrt(X * X + Y * Y + Z * Z);
    }
    
    /**
     * Gets the norm of this vector through (V / |V|)
     * @return The normalized form of the vector
     */
    public Vector3D normalize() {
        double mag = magnitude();
        return new Vector3D(
            X / mag,
            Y / mag,
            Z / mag
        );
    }
    
    /**
     * Calculates the dot product of this vector with the given vector
     * @param v the other vector
     * @return the dot product of the two vectors
     */
    public double dot(Vector3D v) {

        return X * v.X + Y * v.Y + Z * v.Z;
    }
    
    /**
     * Returns the cross product of this vector with the given vector
     * @param v the other vector
     * @return the cross product of the two vectors
     */
    public Vector3D cross(Vector3D v) {
        /* a cross b formula:
         * X = a2b3 - a3b2
         * Y = a3b1 - a1b3
         * Z = a1b2 - a2b1
         */
        
        double x = this.Y * v.Z - this.Z * v.Y;
        double y = this.Z * v.X - this.X * v.Z;
        double z = this.X * v.Y - this.Y * v.X;
        
        return new Vector3D(x,y,z);
    }
    
    /**
     * Returns the scalar triple of three vectors
     * @param v1 the first vector
     * @param v2 the second vector
     * @param v3 the third vector
     * @return the scalar triple
     */
    public double scalTrip(Vector3D v1, Vector3D v2, Vector3D v3)
    {
        //v1 dot (v2 cross v3)
        return v1.dot(v2.cross(v3));
    }
    
    /**
     * Returns the vector triple of three vectors
     * @param v1 the first vector
     * @param v2 the second vector
     * @param v3 the third vector
     * @return the vector triple
     */
    public Vector3D vectorTrip(Vector3D v1, Vector3D v2, Vector3D v3)
    {
        return v1.cross(v2.cross(v3));
    }
    
    /**
     * Creates a {@link Matrix} representing the Vector3D
     * @return A column matrix that represents the
     */
    public Matrix toMatrix() {
        return new Matrix(new double[][] {{this.X}, {this.Y}, {this.Z}});
    }
	
    /**
     * Returns a string representation of the Vector3D
     * @return A formatted string version of the vector
     */
    public String toString() 
    {
        return "<" + 
               IOUtil.padRight(Double.toString(X), 6).substring(0, 5) + ", " + 
               IOUtil.padRight(Double.toString(Y), 6).substring(0, 5) + ", " + 
               IOUtil.padRight(Double.toString(Z), 6).substring(0, 5) + ">";
    }
}
