package astrobleme;

import java.util.Arrays;
import java.util.StringJoiner;

/**
 * A simple matrix object that is basically a feature-rich wrapper
 * around a 2D matrix. The matrix is zero-indexed for simplicity of implementation,
 * i.e. the second element of the third row is available at (2, 1), NOT (3, 2) as
 * one might expect. Be careful about this feature.
 *
 * @author Subhomoy Haldar
 * @version 2017.01.24
 */
public class Matrix {
    private final Rational[][] a; // named for simplicity
    private final int rows;
    private final int cols;

    /**
     * Creates an empty (zero) matrix of the given dimension.
     *
     * @param rows The required number of rows.
     * @param cols The required number of columns.
     */
    public Matrix(final int rows, final int cols) {
        this.rows = rows;
        this.cols = cols;
        a = new Rational[rows][cols];
    }

    /**
     * Initializes the Matrix with a double[][] array. The elements are
     * converted to Rational.
     *
     * @param data The data to construct the Matrix from.
     */
    public Matrix(final double[][] data) {
        this(Rational.rationalize(data), false);
    }

    /**
     * Creates a new Matrix that is a deep copy of the given 2D array and hence,
     * independent.
     *
     * @param data The data to construct the Matrix from.
     */
    public Matrix(final Rational[][] data) {
        this(data, true);
    }

    /**
     * This constructor may simply assign the array or copy it, as per the
     * specification.
     *
     * @param data     The data to construct the Matrix from.
     * @param deepCopy Whether we need an independent copy.
     */
    private Matrix(final Rational[][] data, boolean deepCopy) {
        rows = data.length;
        cols = data[0].length;
        if (deepCopy) {
            a = new Rational[rows][cols];
            for (int i = 0; i < rows; i++) {
                System.arraycopy(data[i], 0, a[i], 0, cols);
            }
        } else {
            a = data;
        }
    }

    /**
     * @param i The row index.
     * @param j The column index.
     * @return The element at (i, j).
     * @throws IllegalArgumentException If i or j is out of permitted range.
     */
    public Rational get(int i, int j) {
        if (i < 0 || i >= rows) {
            throw new IllegalArgumentException("Invalid row index; must be in [0, rows)");
        }
        if (j < 0 || j >= cols) {
            throw new IllegalArgumentException("Invalid column index; must be in [0, cols)");
        }
        return a[i][j];
    }

    /**
     * @return The number of rows of the matrix.
     */
    public int getRows() {
        return rows;
    }

    /**
     * @return The number of columns of the matrix.
     */
    public int getCols() {
        return cols;
    }

    private Boolean isSquare = null;

    /**
     * @return {@code true} if the Matrix is a square matrix.
     */
    public boolean isSquare() {
        return isSquare == null ? isSquare = rows == cols : isSquare;
    }

    private Matrix transpose;

    /**
     * @return The transpose of the Matrix, by interchanging the rows and columns.
     */
    public Matrix transpose() {
        if (transpose != null) {
            return transpose;
        }
        Rational[][] t = new Rational[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                t[j][i] = a[i][j];
            }
        }
        transpose = new Matrix(t);
        transpose.transpose = this;
        return transpose;
    }

    /**
     * @return The String representation of the Matrix.
     */
    @Override
    public String toString() {
        StringJoiner rowJoiner = new StringJoiner("\n");
        for (Rational[] row : a) {
            StringJoiner elementJoiner = new StringJoiner(", ", "[", "]");
            for (Rational element : row) {
                elementJoiner.add(String.valueOf(element));
            }
            rowJoiner.add(elementJoiner.toString());
        }
        return rowJoiner.toString();
    }

    /**
     * @param other The other Matrix to check against.
     * @return {@code true} if both the matrices are same, i.e. their elements are equal.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Matrix)) return false;
        Matrix m = (Matrix) other;
        return rows == m.rows && cols == m.cols && Arrays.deepEquals(a, m.a);
    }

    /**
     * Compares two Matrices and checks if they are equal within a given tolerance.
     *
     * @param m         The other Matrix to compare with.
     * @param tolerance The required tolerance.
     * @return {@code true} of the two matrices are equal within a given tolerance.
     */
    public boolean approximatelyEquals(Matrix m, Rational tolerance) {
        if (rows != m.rows || cols != m.cols) {
            return false;
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Rational eps = a[i][j].subtract(m.a[i][j]).abs();
                if (eps.compareTo(tolerance) > 0) {
                    return false;
                }
            }
        }
        return true;
    }

    // Lazy initialization for certain properties.

    private Boolean isLowerTriangular = null;
    private Boolean isUpperTriangular = null;
    private Boolean isDiagonal = null;
    private Boolean isIdentity = null;
    private Boolean isSymmetric = null;
    private Boolean isPermutation = null;

    /**
     * @return {@code true} if get(i, j) = 0 for i < j.
     */
    public boolean isLowerTriangular() {
        return isLowerTriangular == null
                ? isLowerTriangular = MatrixProperties.isLowerTriangular(this)
                : isLowerTriangular;
    }

    /**
     * @return {@code true} if get(i, j) = 0 for j < i.
     */
    public boolean isUpperTriangular() {
        return isUpperTriangular == null
                ? isUpperTriangular = MatrixProperties.isUpperTriangular(this)
                : isUpperTriangular;
    }

    /**
     * @return {@code true} if get(i, j) = 0 for i &neq; j.
     */
    public boolean isDiagonal() {
        return isDiagonal == null
                ? isDiagonal = isLowerTriangular() && isUpperTriangular()
                : isDiagonal;
    }

    /**
     * @return {@code true} if this is an identity matrix.
     */
    public boolean isIdentity() {
        return isIdentity == null
                ? isIdentity = MatrixProperties.isIdentity(this)
                : isIdentity;
    }

    /**
     * @return {@code true} of transpose of this is equal to it.
     */
    public boolean isSymmetric() {
        return isSymmetric == null
                ? isSymmetric = this.equals(transpose())
                : isSymmetric;
    }

    /**
     * @return {@code true} if the Matrix is a Permutation Matrix.
     */
    public boolean isPermutation() {
        return isPermutation == null
                ? isPermutation = MatrixProperties.isPermutation(this)
                : isPermutation;
    }

    /**
     * @param m The matrix to add.
     * @return The sum of the two matrices.
     */
    public Matrix add(Matrix m) {
        if (rows != m.rows || cols != m.cols) {
            throw new IllegalArgumentException("Incompatible matrices.");
        }
        return new Matrix(MatrixOps.add(a, m.a), false);
    }

    /**
     * @param m The matrix to subtract.
     * @return The difference of the two matrices.
     */
    public Matrix subtract(Matrix m) {
        if (rows != m.rows || cols != m.cols) {
            throw new IllegalArgumentException("Incompatible matrices.");
        }
        return new Matrix(MatrixOps.subtract(a, m.a), false);
    }

    /**
     * @param scalar The factor to scale all the elements by.
     * @return The scaled matrix.
     */
    public Matrix multiply(Rational scalar) {
        return new Matrix(MatrixOps.multiply(a, scalar), false);
    }

    /**
     * @param m The matrix to multiply.
     * @return The product of this matrix and the given matrix.
     * @throws IllegalArgumentException If the matrices are not compatible.
     */
    public Matrix multiply(Matrix m) {
        if (cols != m.rows) {
            throw new IllegalArgumentException("Incompatible matrices.");
        }
        return new Matrix(MatrixOps.multiply(a, m.a), false);
    }

    /**
     * @param n The required size.
     * @return An identity matrix with the given size.
     */
    public static Matrix identity(final int n) {
        Rational[][] a = new Rational[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = i == j ? Rational.ONE : Rational.ZERO;
            }
        }
        return new Matrix(a, false);
    }
}
