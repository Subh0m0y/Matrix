import astrobleme.LUPDecompose;
import astrobleme.MatrixOps;
import astrobleme.Rational;

import java.util.Arrays;

/**
 * @author Subhomoy Haldar
 * @version 2017.01.24
 */
public class Sandbox {
    public static void main(String[] args) {
        Rational[][] L = Rational.rationalize(new double[][]{{1, 0, 0}, {0.2, 1, 0}, {0.6, 0.5, 1}});
        Rational[][] U = Rational.rationalize(new double[][]{{5, 6, 3}, {0, 0.8, -0.6}, {0, 0, 2.5}});
        Rational[][] P = Rational.rationalize(new double[][]{{0, 0, 1}, {1, 0, 0}, {0, 1, 0}});
        int[] pCompact = MatrixOps.compactify(P);

        Rational[][] b = Rational.rationalize(new double[][]{{3, 7, 8}});
        System.out.println(Arrays.deepToString(b));
        b = MatrixOps.permuteColumns(b, pCompact);
        System.out.println(Arrays.deepToString(b));
        //Rational[] B = {b[0][0], b[1][0], b[2][0]};
        //Rational[] x = LUPDecompose.solve(L, U, B);
        Rational[] x = LUPDecompose.solve(L, U, b[0]);
        System.out.println(Arrays.toString(x));
    }

}
