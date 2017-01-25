package astrobleme;

/**
 * This class contains code that is used to detect whether a Matrix has any required
 * property, like being identity matrix, singular etc.
 *
 * @author Subhomoy Haldar
 * @version 2017.01.24
 */
class MatrixProperties {
    /**
     * Checks if the given Matrix is square and the elements above the diagonal
     * are all zero.
     *
     * @param matrix The Matrix to check.
     * @return {@code true} if the Matrix is a lower triangular matrix.
     */
    static boolean isLowerTriangular(Matrix matrix) {
        if (!matrix.isSquare()) return false;
        for (int j = 1; j < matrix.getCols(); j++) {
            for (int i = 0; i < j; i++) {
                if (!matrix.get(i, j).isZero()) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Checks if the given Matrix is square and the elements below the diagonal
     * are all zero.
     *
     * @param matrix The Matrix to check.
     * @return {@code true} if the Matrix is an upper triangular matrix.
     */
    static boolean isUpperTriangular(Matrix matrix) {
        if (!matrix.isSquare()) return false;
        for (int i = 1; i < matrix.getRows(); i++) {
            for (int j = 0; j < i; j++) {
                if (!matrix.get(i, j).isZero()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if the given matrix is an Identity Matrix, i.e. a diagonal Matrix
     * with all diagonal elements equal to 1.
     *
     * @param matrix The matrix to check.
     * @return {@code true}
     */
    static boolean isIdentity(Matrix matrix) {
        if (!matrix.isDiagonal()) {
            return false;
        }
        for (int i = 0; i < matrix.getRows(); i++) {
            if (!matrix.get(i, i).isOne()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the given Matrix is a permutation Matrix, i.e. it has only zeroes
     * and only one 1 per row and column.
     *
     * @param matrix The Matrix to check.
     * @return {@code true} if the given Matrix is a permutation Matrix.
     */
    static boolean isPermutation(Matrix matrix) {
        if (!matrix.isSquare()) {
            return false;
        }
        int size = matrix.getRows();
        for (int i = 0; i < size; i++) {
            boolean foundOneInRow = false;
            boolean foundOneInCol = false;
            // Check the row to ensure only one 1 is present
            for (int j = 0; j < size; j++) {
                Rational rowElement = matrix.get(i, j);
                Rational colElement = matrix.get(j, i);

                if (rowElement.isOne()) {
                    // check for duplicate and mark if unique
                    if (foundOneInRow) {
                        return false;
                    } else {
                        foundOneInRow = true;
                    }
                } else if (!rowElement.isZero()) {
                    // Only 0 and 1 are permitted. Anything else, and you're out!
                    return false;
                }
                if (colElement.isOne()) {
                    if (foundOneInCol) {
                        return false;
                    } else {
                        foundOneInCol = true;
                    }
                } else if (!rowElement.isZero()) {
                    return false;
                }
            }
            // Finally, ensure row and column is non-empty
            if (!foundOneInRow || !foundOneInCol) {
                return false;
            }
        }
        return true;
    }

}
