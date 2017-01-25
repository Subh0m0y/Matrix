package astrobleme;

/**
 * @author Subhomoy Haldar
 * @version 2017.01.25
 */
public class LUPDecompose {
    /**
     * Returns the solution vector from the decomposed L and U matrices (2D arrays)
     * and the <strong>permuted</strong> "constants" vector: b.
     *
     * @param L The lower triangular matrix.
     * @param U The upper triangular matrix.
     * @param b The permuted vector.
     * @return The required solution vector.
     */
    public static Rational[] solve(Rational[][] L, Rational[][] U, Rational[] b) {
        final int n = L.length;
        Rational[] x = new Rational[n];
        Rational[] y = new Rational[n];
        // Forward substitution
        for (int i = 0; i < n; i++) {
            Rational sum = Rational.ZERO;
            for (int j = 0; j < i; j++) {
                sum = L[i][j].multiply(y[j]).add(sum);
            }
            y[i] = b[i].subtract(sum);
        }
        // Backward substitution
        for (int i = n - 1; i >= 0; i--) {
            Rational sum = Rational.ZERO;
            for (int j = i + 1; j < n; j++) {
                sum = U[i][j].multiply(x[j]).add(sum);
            }
            x[i] = y[i].subtract(sum).divide(U[i][i]);
        }

        return x;
    }
}
