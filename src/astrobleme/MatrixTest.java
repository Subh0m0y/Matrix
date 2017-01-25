package astrobleme;

import org.testng.annotations.Test;

import java.util.Random;

import static astrobleme.Rational.rationalize;
import static org.testng.Assert.*;

/**
 * @author Subhomoy Haldar
 * @version 2017.01.24
 */
public class MatrixTest {

    private static final int COUNT = 10;
    private static final int LIMIT = 100;
    private static final Random RANDOM = new Random();

    @org.testng.annotations.Test
    public void testIdentity() throws Exception {
        for (int i = 0; i < COUNT; i++) {
            int size = RANDOM.nextInt(LIMIT) + 1;
            double[][] a = new double[size][size];
            for (int j = 0; j < size; j++) {
                a[j][j] = 1;
            }
            Matrix matrix = new Matrix(a);
            assertEquals(true, matrix.isSquare());
            assertEquals(true, matrix.isLowerTriangular());
            assertEquals(true, matrix.isUpperTriangular());
            assertEquals(true, matrix.isIdentity());
        }
    }


    @org.testng.annotations.Test
    public void testGetRowsAndCols() throws Exception {
        for (int i = 0; i < COUNT; i++) {
            int rows = RANDOM.nextInt(LIMIT) + 1;
            int cols = RANDOM.nextInt(LIMIT) + 1;
            Matrix matrix = new Matrix(rows, cols);
            assertEquals(rows, matrix.getRows());
            assertEquals(cols, matrix.getCols());
            assertEquals(rows == cols, matrix.isSquare());
        }
    }

    @org.testng.annotations.Test
    public void testTransposeAndEquals() throws Exception {
        for (int counter = 0; counter < COUNT; counter++) {
            // Generate the two matrices
            int rows = RANDOM.nextInt(LIMIT) + 1;
            int cols = RANDOM.nextInt(LIMIT) + 1;
            Rational[][] a = new Rational[rows][cols];
            Rational[][] t = new Rational[cols][rows];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    a[i][j] = t[j][i] = randomRational();
                }
            }
            assertEquals(new Matrix(t), new Matrix(a).transpose());
        }
    }

    @org.testng.annotations.Test
    public void testToString() throws Exception {
        String expected = "" +
                "[1, 2, 3]\n" +
                "[4, 5, 6]\n" +
                "[7, 8, 9]\n" +
                "[10, 11, 12]";
        Matrix matrix = new Matrix(
                new double[][]{
                        {1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}
                }
        );
        assertEquals(expected, matrix.toString());
    }

    @Test
    public void testPermutationMatrix() throws Exception {
        Rational[][] a1 = rationalize(new double[][]{{1, 0, 0}, {0, 0, 1}, {0, 1, 0}});
        Rational[][] a2 = rationalize(new double[][]{{1, 1, 0}, {0, 0, 1}, {0, 1, 0}});
        Rational[][] a3 = rationalize(new double[][]{{1, 1, 7}, {1, 0, 1}, {0, 1, 0}});
        Rational[][] a4 = rationalize(new double[][]{{1, 0, 0}, {1, 0, 0}, {0, 1, 0}});
        assertEquals(true, new Matrix(a1).isPermutation());
        assertEquals(false, new Matrix(a2).isPermutation());
        assertEquals(false, new Matrix(a3).isPermutation());
        assertEquals(false, new Matrix(a4).isPermutation());
    }

    private static Rational randomRational() {
        return new Rational(RANDOM.nextDouble());
    }
}