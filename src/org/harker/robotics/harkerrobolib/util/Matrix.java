package org.harker.robotics.harkerrobolib.util;

/**
 * An immutable implementation of a Matrix in Java
 * Primary logic and Matrix code derived and/or reproduced from
 *  <a href="http://introcs.cs.princeton.edu/java/95linear/Matrix.java.html">
 *      Princeton Introduction to Programming in Java</a>
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Manan
 */
public class Matrix {
    /**
     * The number of rows.
     */
    private final int rows;

    /**
     * The number of columns
     */
    private final int cols;

    /**
     * A 2-dimensional array containing the elements of the matrix.
     */
    private final double[][] data;

    /**
     * Create an M-by-N matrix of zeros
     * @param M The number of rows
     * @param N The number of columns
     */
    public Matrix(int M, int N) {
        this.rows = M;
        this.cols = N;
        data = new double[M][N];
    }

    /**
     * Create a matrix based on a 2-dimensional array
     * @param data The array to create a matrix of
     */
    public Matrix(double[][] data) {
        rows = data.length;
        cols = data[0].length;
        this.data = new double[rows][cols];
        for (int i = 0; i < rows; i++)
            System.arraycopy(data[i], 0, this.data[i], 0, cols);
    }

    /**
     * Create a new matrix that is a copy of another one
     * @param A The matrix to copy
     */
    public Matrix(Matrix A) { this(A.data); }


    /**
     * Gets the value of an element in the matrix
     * @param x The row number
     * @param y The column number
     * @return The value at index (x,y)
     */
    public double getValue(int x, int y) {
    	return data[x][y];
    }

    /**
     * Sets the value of an element in the matrix
     * @param x The row number
     * @param y The column number
     * @param val The value to set index (x,y) to
     */
    public void setValue(int x, int y, double val) {
    	data[x][y] = val;
    }

    /**
     * Create a new N-by-N identity matrix
     * @param N The size of the matrix
     * @return An identity matrix of size N
     */
    public static Matrix identity(int N) {
        Matrix I = new Matrix(N, N);
        for (int i = 0; i < N; i++)
            I.data[i][i] = 1;
        return I;
    }

    /**
     * Swap rows i and j
     * @param i The first row to swap
     * @param j The row to swap with
     */
    private void swap(int i, int j) {
        double[] temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    /**
     * Creates the transpose matrix, A<sup>T</sup>
     * @return The transpose of a given matrix
     */
    public Matrix transpose() {
        Matrix A = new Matrix(cols, rows);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                A.data[j][i] = data[i][j];
        return A;
    }

    /**
     * Adds the matrix to another one
     * @param B The matrix to add to
     * @return The sum of the two matrices
     */
    public Matrix plus(Matrix B) {
        Matrix A = this;
        if (B.rows != A.rows || B.cols != A.cols) throw new RuntimeException("Illegal matrix dimensions.");
        Matrix C = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                C.data[i][j] = A.data[i][j] + B.data[i][j];
        return C;
    }


    /**
     * Takes the difference of the matrix and another one
     * @param B The matrix to subtract this one from
     * @return The difference of the two matrices
     */
    public Matrix minus(Matrix B) {
        Matrix A = this;
        if (B.rows != A.rows || B.cols != A.cols) throw new RuntimeException("Illegal matrix dimensions.");
        Matrix C = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                C.data[i][j] = A.data[i][j] - B.data[i][j];
        return C;
    }

    /**
     * Takes the product of this matrix and another
     * @param B The matrix to multiply by
     * @return The product of the matrix and the other
     */
    public Matrix mult(Matrix B) {
        Matrix A = this;
        if (A.cols != B.rows) throw new RuntimeException("Illegal matrix dimensions.");
        Matrix C = new Matrix(A.rows, B.cols);
        for (int i = 0; i < C.rows; i++)
            for (int j = 0; j < C.cols; j++)
                for (int k = 0; k < A.cols; k++)
                    C.data[i][j] += (A.data[i][k] * B.data[k][j]);
        return C;
    }

    /**
     * Solves the equation A * x = b for x
     * @param rhs The right hand side of the equation, b.
     * @return x = A<sup>-1</sup> * b, assuming A is square and has full rank
     */
    public Matrix solve(Matrix rhs) {
        if (rows != cols || rhs.rows != cols)// || rhs.cols != 1)
            throw new RuntimeException("Illegal matrix dimensions.");

        // create copies of the data
        Matrix A = new Matrix(this);
        Matrix b = new Matrix(rhs);

        // Gaussian elimination with partial pivoting
        for (int i = 0; i < cols; i++) {
            int max = i;
            for (int j = i + 1; j < cols; j++)
                if (Math.abs(A.data[j][i]) > Math.abs(A.data[max][i]))
                    max = j;
            A.swap(i, max);
            b.swap(i, max);

            // singular
            if (A.data[i][i] == 0.0) throw new RuntimeException("Matrix is singular.");

            // pivot within b
            for (int j = i + 1; j < cols; j++)
                b.data[j][0] -= b.data[i][0] * A.data[j][i] / A.data[i][i];

            // pivot within A
            for (int j = i + 1; j < cols; j++) {
                double m = A.data[j][i] / A.data[i][i];
                for (int k = i+1; k < cols; k++) {
                    A.data[j][k] -= A.data[i][k] * m;
                }
                A.data[j][i] = 0.0;
            }
        }

        // back substitution
        Matrix x = new Matrix(cols, 1);
        for (int j = cols - 1; j >= 0; j--) {
            double t = 0.0;
            for (int k = j + 1; k < cols; k++)
                t += A.data[j][k] * x.data[k][0];
            x.data[j][0] = (b.data[j][0] - t) / A.data[j][j];
        }
        return x;
    }

    /**
     * Creates a new {@link Vector3D} with the first column of the matrix.
     * @return A new {@link Vector3D} based on the first column of the matrix.
     */
    public Vector3D ToVector3DByCol() {
    	return new Vector3D(getValue(0, 0), getValue(1, 0), getValue(2, 0));
    }

    /**
     * Creates a new {@link Vector3D} with a specified column of the matrix.
     * @param col The column to create a vector of.
     * @return A new {@link Vector3D} based on the given column of the matrix.
     */
    public Vector3D ToVector3DByCol(int col) {
        return new Vector3D(getValue(0, col), getValue(1, col), getValue(2, col));
    }

    /**
     * Creates a new {@link Vector3D} with the first column of the matrix, scaled by the fourth element in that column.
     * @return A new {@link Vector3D} based on the given column of the matrix and its scale factor.
     */
    public Vector3D ToVector3DByColWithScale() {
    	return new Vector3D(getValue(0, 0) / getValue(3, 0), getValue(1, 0) / getValue(3, 0), getValue(2, 0) / getValue(3, 0));
    }

    /**
     * Creates a new {@link Vector3D} with the first row of the matrix.
     * @return A new {@link Vector3D} based on the first row of the matrix.
     */
    public Vector3D ToVector3DByRow() {
    	return new Vector3D(getValue(0, 0), getValue(0, 1), getValue(0, 2));
    }

    /**
     * Creates a new {@link Vector3D} with a specified row of the matrix.
     * @param row The row to create a vector of.
     * @return A new {@link Vector3D} based on the given row of the matrix.
     */
    public Vector3D ToVector3DByRow(int row) {

    	return new Vector3D(getValue(row, 0), getValue(row, 1), getValue(row, 2));
    }

    /**
     * Creates a new matrix from a Vector3D
     * @deprecated Instead, use {@link Vector3D#toMatrix()}
     * @param v The vector to create a matrix from
     * @return The new matrix
     */
    public static Matrix FromVector3D(Vector3D v) {
    	return new Matrix(new double[][] { {v.getX()}, {v.getY()}, {v.getZ()}, {1}});
    }

    /**
     * Checks if two matrices are equivalent
     * @param B The matrix to compare to
     * @return <code>true</code> if the dimensions match and the individual elements are the same.
     */
    public boolean equals(Matrix B) {
        Matrix A = this;
        if (B.rows != A.rows || B.cols != A.cols) throw new RuntimeException("Illegal matrix dimensions.");
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                if (A.data[i][j] != B.data[i][j]) return false;
        return true;
    }

    /**
     * Returns a printable form of the matrix.
     * @return The formatted string version of the matrix
     */
    public String toString() {
        String s = "";
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++)
                s += data[i][j] + " ";
            s += "\n";
        }
        return s;
    }
}
