package org.harker.robotics.harkerrobolib.util;

/**
 * IO util library with some utility methods for writing to console and strings in general
 * @author Ben Huchley
 * @author Manan Shah
 * @author Andrew Tierno
 */
public class IOUtil {
    
    /** 
      * Call when something minor goes wrong that shouldn't crash the robot.
      * @param s a descriptive string about the problem
      */
     public static void warn(String s) {
         System.err.print("Warn: ");
         System.err.println(s);
     }
 
    /**
     * Pads the given string on the right with spaces
     * @param s The string which we are padding
     * @param length the desired final length of our string.
     * @return The padded string
     */
    public static String padRight(String s, int length) {
        StringBuffer sb = new StringBuffer();
        sb.append(s);
        for(int i = s.length(); i < length; i++)
            sb.append(" ");
        return sb.toString();
    }
    
    /**
     * Pads the given string on the left with spaces
     * @param s The string which we are padding
     * @param length the desired final length of our string.
     * @return The padded string
     */
    public static String padLeft(String s, int length) {
            StringBuffer sb = new StringBuffer();
            for(int i = s.length(); i < length; i++)
                sb.append(" ");
            sb.append(s);
            return sb.toString();
    }
    
    /** 
     * Prints a line.
     * This is really simple, it exists in case we need to change this later
     * so that we can change it everywhere easily
     * @param obj the line to print
     */
    public static void println(Object obj) {
        System.out.println(obj);
    }
    
    public static void println(boolean b) {
	println(String.valueOf(b));
    }
    
    public static void println(char c) {
	println(String.valueOf(c));
    }
    
    public static void println(char[] s) {
	println(String.valueOf(s));
    }
    
    public static void println(double d) {
	println(String.valueOf(d));
    }
    
    public static void println(float f) {
	println(String.valueOf(f));
    }
    
    public static void println(int i) {
	println(String.valueOf(i));
    }
    
    public static void println(long l) {
	println(String.valueOf(l));
    }
    
}
