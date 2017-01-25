package astrobleme;

import java.util.Arrays;

/**
 * @author Subhomoy Haldar
 * @version 2017.01.24
 */
public class MatrixOps {
    static Rational[][] add(Rational[][] a, Rational[][] b) {
        Rational[][] sum = new Rational[a.length][a[0].length];
        for (int i = 0; i < sum.length; i++) {
            for (int j = 0; j < sum[0].length; j++) {
                sum[i][j] = a[i][j].add(b[i][j]);
            }
        }
        return sum;
    }

    static Rational[][] subtract(Rational[][] a, Rational[][] b) {
        Rational[][] diff = new Rational[a.length][a[0].length];
        for (int i = 0; i < diff.length; i++) {
            for (int j = 0; j < diff[0].length; j++) {
                diff[i][j] = a[i][j].subtract(b[i][j]);
            }
        }
        return diff;
    }

    static Rational[][] multiply(Rational[][] a, Rational scalar) {
        Rational[][] b = new Rational[a.length][a[0].length];
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                b[i][j] = a[i][j].multiply(scalar);
            }
        }
        return b;
    }

    public static Rational[][] multiply(Rational[][] a, Rational[][] b) {
        Rational[][] c = new Rational[a.length][b[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                c[i][j] = Rational.ZERO;
                for (int k = 0; k < a[0].length; k++) {
                    c[i][j] = c[i][j].add(a[i][k].multiply(b[k][j]));
                }
            }
        }
        return c;
    }

    public static int[] compactify(Rational[][] p) {
        int[] compact = new int[p.length];
        for (int i = 0; i < p.length; i++) {
            for (int j = 0; j < p[0].length; j++) {
                if (p[i][j].isOne()) {
                    compact[i] = j;
                    break;
                }
            }
        }
        return compact;
    }

    public static Rational[][] permuteRows(Rational[][] array, int[] pCompact) {
        int n = pCompact.length;
        int m = array[0].length;
        Rational[][] newArray = new Rational[n][];
        for (int i = 0; i < n; i++) {
            newArray[i] = Arrays.copyOf(array[pCompact[i]], m);
        }
        return newArray;
    }

    public static Rational[][] permuteColumns(Rational[][] array, int[] pCompact) {
        int n = array.length;
        int m = pCompact.length;
        Rational[][] newArray = new Rational[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                newArray[i][j] = array[i][pCompact[j]];
            }
        }
        return newArray;
    }
}
