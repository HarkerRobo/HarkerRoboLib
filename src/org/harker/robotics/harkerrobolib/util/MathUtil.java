package org.harker.robotics.harkerrobolib.util;

/**
 * Math IOUtil class for basic math functions, because Java 4 doesn't have 
 * arctan, arccos, and arcsin!
 * @author Manan Shah
 * @author neymikajain
 * @author Ben H.
 */
public class MathUtil {

    //answers are in radians
    public static double aTan(double x) {
	return x - (power(x, 3) / 3) + (power(x, 5) / 5) - (power(x, 7) / 7);
    }

    public static double aSin(double x) {
	return (x + power(x, 3) / 6 + 3 * power(x, 5) / 40 + 5 * power(x, 7) / 112);
    }

    public static double aCos(double x) {
	return (Math.PI / 2) - aSin(x);
    }

    public static double cos(double x) {
	return 1 - (power(x, 2) / 2) + (power(x, 4) / 24) - (power(x, 6) / 720) + (power(x, 8) / 40320);
    }

    public static double sin(double x) {
	return x - (power(x, 3) / 6) + power(x, 5) / 120 - power(x, 7) / 5040;
    }

    public static double tan(double x) {
	return x + power(x, 3) / 3 + 2 * power(x, 5) / 15 + 17 * power(x, 7) / 315;
    }
    /**
     * Returns x<sup>power</sup>, works for any values of x and power.
     * Written by Ben H.
     * @param x the base
     * @param power the exponent
     * @return x<sup>power</sup>
     */
    public static double power(double x, int power) {
        // This is a more efficient method of doing power than 1 at a time for large exponents
        // Runs in O(log2(power)) time  
        
        // If power is a multiple of 2 squares x and halves it. If not it decrements it and
        // stores what that 1 should have represented in this number. It is multiplied
        // back in at the end.
        double nonPowerOfTwoMultiplier = 1;
        while (power > 1) {
            if (power % 2 == 1) {
                nonPowerOfTwoMultiplier *= x;
                power--;
            }
            x*=x;
            power/=2;
        }
        return x * nonPowerOfTwoMultiplier;
    }
    
    public static double solveLinear(double xCoeff, double  constant) {
        // mx+b = 0 -> x = -b/m
        if (xCoeff == 0) {
            IOUtil.warn("Tried to solve " + constant + " == 0 for x");
            return 0;
        }
        return -constant / xCoeff;
    }
    
    public static double solveQuadraticHigh(double squareCoeff, double xCoeff, double constant) {
        // Uses quadratic formula; returns the higher one
        // x = ( -b + sqrt(b^2-4ac) ) / 2a
        if (squareCoeff == 0) {
            IOUtil.println("Tried to solve a quadratic with no square term. Using linear solver");
            return solveLinear(xCoeff, constant);
        }
        double determinant = power(xCoeff, 2) - (4 * squareCoeff * constant);
        if (determinant < 0) {
            IOUtil.warn("Tried to solve a quadratic with no real solution");
            return 0; // is it possible to return NaN?
        }
        double sqrtTerm = Math.sqrt(determinant);
        return ((sqrtTerm - xCoeff) / (2 * squareCoeff));
    }
    
    public static double solveQuadraticLow(double squareCoeff, double xCoeff, double constant) {
        // Uses quadratic formula; returns the lower one
        // x = ( -b - sqrt(b^2-4ac) ) / 2a
        if (squareCoeff == 0) {
            IOUtil.println("Tried to solve a quadratic with no square term. Using linear solver");
            return solveLinear(xCoeff, constant);
        }
        double determinant = power(xCoeff, 2) - (4 * squareCoeff * constant);
        if (determinant < 0) {
            IOUtil.warn("Tried to solve a quadratic with no real solution");
            return 0; // is it possible to return NaN?
        }
        double sqrtTerm = Math.sqrt(determinant);
        return ((-sqrtTerm - xCoeff) / (2 * squareCoeff));
    }
    
    /**
     * Returns the standard deviation of the data set
     * @param points
     * @return stdev of data
     */
    public static double stdev(double[] points)
    {
         double mean = mean(points);
         int sum = 0;
         for(int i=0; i<points.length; i++)
         {
             points[i] = power(points[i]-mean, 2);
             sum += points[i];
         }
         sum /= points.length;
         return Math.sqrt(sum);
    }
    
    /**
     * Returns the mean of the data set 
     * @param dataSet
     * @return the mean
     */
    public static double mean (double[] dataSet) {
        double sum = 0;
        for (int i = 0; i < dataSet.length; i++) {
            sum += dataSet[i];
        }
        return (sum/dataSet.length);
    }
    
    /**
     * Analyzes data and outputs an array of results
     *  Uses logic from the Princeton University Libraries:
     *  http://introcs.cs.princeton.edu/java/97data/LinearRegression.java.html
     * @param x the x coordinates of the data
     * @param y the y coordinates of the data
     * @return an array that contains
     *  Index 0: R^2 value
     *  Index 1: beta 1 error value
     *  Index 2: beta 0 error value
     *  Index 3: yybar value
     *  Index 4: residual sum of squares
     *  Index 5: regression sum of squares
     */
    public static double[] linReg(double[] x, double[] y)
    {
        int n = 0;
            // first pass: read in data, compute xbar and ybar
        double sumx = 0.0, sumy = 0.0, sumx2 = 0.0;
        double xbar = sumx / n;
        double ybar = sumy / n;

        // second pass: compute summary statistics
            double xxbar = 0.0, yybar = 0.0, xybar = 0.0;
            for (int i = 0; i < n; i++) 
            {
                    xxbar += (x[i] - xbar) * (x[i] - xbar);
                    yybar += (y[i] - ybar) * (y[i] - ybar);
                    xybar += (x[i] - xbar) * (y[i] - ybar);
            }
            double beta1 = xybar / xxbar;
            double beta0 = ybar - beta1 * xbar;

            // print results
            //System.out.println("y   = " + beta1 + " * x + " + beta0);

            // analyze results
            int df = n - 2;
            double rss = 0.0;      // residual sum of squares
            double ssr = 0.0;      // regression sum of squares
            for (int i = 0; i < n; i++) 
            {
                    double fit = beta1*x[i] + beta0;
                    rss += (fit - y[i]) * (fit - y[i]);
                    ssr += (fit - ybar) * (fit - ybar);
            }

            double R2    = ssr / yybar;
            double svar  = rss / df;
            double svar1 = svar / xxbar;
            double svar0 = svar/n + xbar*xbar*svar1;

            double[] arr = {R2, Math.sqrt(svar1), Math.sqrt(((svar *sumx2) / (n * xxbar))), yybar, rss, ssr}; 
            return arr;
     }
}
