package astrobleme;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * An immutable, arbitrary-precision Rational class for precise, numerically stable
 * calculations.
 *
 * @author Subhomoy Haldar
 * @version 2017.01.25
 */
public class Rational implements Comparable<Rational> {
    /*
     * Publicly visible constants. These are the one most commonly needed.
     */
    public static final Rational ZERO = new Rational(0);
    public static final Rational ONE = new Rational(1);
    public static final Rational HALF = new Rational(1, 2);

    private final BigInteger num;
    private final BigInteger den;

    /**
     * Creates a new Rational from a primitive (long) integer.
     *
     * @param integer The value to wrap.
     */
    public Rational(final long integer) {
        this(BigInteger.valueOf(integer), BigInteger.ONE, false);
    }

    /**
     * Creates a new Rational from a BigInteger.
     *
     * @param integer The value to wrap.
     */
    public Rational(final BigInteger integer) {
        this(integer, BigInteger.ONE, false);
    }

    /**
     * Creates a new Rational with the given longs: numerator and denominator.
     *
     * @param numerator   The required numerator.
     * @param denominator The desired, non-zero denominator.
     * @throws IllegalArgumentException If the denominator supplied is zero.
     */
    public Rational(final long numerator, final long denominator)
            throws IllegalArgumentException {
        this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator), true);
    }

    /**
     * Creates a new Rational with the given {@link BigInteger}s: numerator and denominator.
     *
     * @param numerator   The required numerator.
     * @param denominator The desired, non-zero denominator.
     * @throws IllegalArgumentException If the denominator supplied is zero.
     */
    public Rational(final BigInteger numerator, final BigInteger denominator)
            throws IllegalArgumentException {
        this(numerator, denominator, true);
    }

    /**
     * Private constructor for either verified initialization or quick bypass.
     *
     * @param numerator   The required numerator.
     * @param denominator The desired, non-zero denominator.
     * @param verify
     * @throws IllegalArgumentException If verification process finds a zero denominator.
     */
    private Rational(final BigInteger numerator, final BigInteger denominator,
                     boolean verify) {
        // Quick creation for verified, trusted Rationals
        if (!verify) {
            num = numerator;
            den = denominator;
            return;
        }

        /* Thorough checking of the arguments */

        if (denominator.signum() == 0) {
            throw new IllegalArgumentException("Denominator must be non-zero.");
        }
        BigInteger n = numerator;
        BigInteger d = denominator;

        // Ensure that only the numerator has the sign.
        if (d.signum() < 0) {
            n = n.negate();
            d = d.negate();
        }

        // Reduce to its lowest terms.
        BigInteger gcd = n.gcd(d);
        num = n.divide(gcd);
        den = d.divide(gcd);
    }

    /**
     * Creates a new Rational from a double. For preserving as much accuracy as
     * possible, the value is converted to a String and then processed. In fact,
     * the latter is the encouraged approach.
     *
     * @param fraction The double to wrap.
     */
    public Rational(double fraction) {
        this(String.valueOf(fraction));
    }

    /**
     * Creates a new Rational from it's String representation, or in decimal form,
     * i.e. with the point. It is the recommended approach for processing fractions
     * with decimal points accurately.
     *
     * @param fraction The String to parse.
     * @throws NumberFormatException If the String is not in the proper format.
     */
    public Rational(String fraction) throws NumberFormatException {
        BigInteger n, d;

        // Check if '/' is present
        int pos = fraction.indexOf('/');
        if (pos > -1) {
            n = new BigInteger(fraction.substring(0, pos));
            d = new BigInteger(fraction.substring(pos + 1));
        } else {
            // Use BigDecimal to generate our numerator and denominator.
            BigDecimal decimal = new BigDecimal(fraction);
            n = decimal.unscaledValue();
            int scale = decimal.scale();
            BigInteger power = BigInteger.TEN.pow(scale);
            if (scale < 0) {
                n = n.multiply(power);
                d = BigInteger.ONE;
            } else {
                d = power;
            }
        }
        // Reduce to lowest terms
        BigInteger gcd = n.gcd(d);
        num = n.divide(gcd);
        den = d.divide(gcd);
    }

    /**
     * @param term The Rational to add.
     * @return The sum of the two rationals.
     */
    public Rational add(Rational term) {
        BigInteger a = this.num;
        BigInteger b = this.den;
        BigInteger c = term.num;
        BigInteger d = term.den;

        BigInteger d1 = b.gcd(d);
        if (d1.equals(BigInteger.ONE)) {
            // Knuth argues that this occurs approximately 61% of the time.
            BigInteger num = a.multiply(d).add(b.multiply(c));
            BigInteger den = b.multiply(d);
            return new Rational(num, den);
        } else {
            BigInteger common = b.divide(d1);
            BigInteger t = a.multiply(d.divide(d1)).add(common.multiply(c));
            BigInteger d2 = t.gcd(d1);

            BigInteger num = t.divide(d2);
            BigInteger den = common.multiply(d.divide(d2));

            return new Rational(num, den);
        }
    }

    /**
     * @param term The Rational to multiply.
     * @return The product of the two Rationals.
     */
    public Rational multiply(Rational term) {
        BigInteger a = this.num;
        BigInteger b = this.den;
        BigInteger c = term.num;
        BigInteger d = term.den;

        BigInteger d1 = a.gcd(d);
        BigInteger d2 = b.gcd(c);

        BigInteger num = a.divide(d1).multiply(c.divide(d2));
        BigInteger den = b.divide(d2).multiply(d.divide(d1));

        return new Rational(num, den);
    }

    /**
     * @return The additive inverse of this Rational.
     */
    public Rational negate() {
        return new Rational(num.negate(), den, false);
    }

    /**
     * @return The multiplicative inverse of this Rational.
     * @throws ArithmeticException If it cannot be reciprocated, when zero.
     */
    public Rational reciprocal()
            throws ArithmeticException {
        if (isZero()) {
            throw new ArithmeticException("Zero has no reciprocal.");
        }
        return new Rational(den, num, false);
    }

    /**
     * @param term The Rational to subtract.
     * @return The difference of the two Rationals.
     */
    public Rational subtract(Rational term) {
        return add(term.negate());
    }

    /**
     * @param term The non-zero divisor.
     * @return The quotient of the operation.
     * @throws ArithmeticException If the divisor is zero.
     */
    public Rational divide(Rational term)
            throws ArithmeticException {
        return multiply(term.reciprocal());
    }

    /**
     * @return {@code true} if it is zero.
     */
    public boolean isZero() {
        return num.signum() == 0;
    }

    /**
     * @return {@code true} if it is zero.
     */
    public boolean isOne() {
        return num.equals(den);
    }

    /**
     * @param value The integer to compare it with.
     * @return {@code true} if it is indeed equal to {@code value}.
     */
    public boolean is(final long value) {
        BigInteger n = BigInteger.valueOf(value);
        return den.equals(BigInteger.ONE) && n.equals(num);
    }

    /**
     * @param fraction The fraction to compare it with.
     * @return {@code true} if it is indeed equal to {@code fraction}.
     */
    public boolean is(final String fraction) {
        return equals(new Rational(fraction));
    }

    /**
     * Compares the Rational with the given double and checks if it is within tolerance.
     *
     * @param fraction  The fraction to compare with.
     * @param tolerance The allowed deviation.
     * @return {@code true} if this is within tolerance of fraction.
     */
    public boolean isApproximately(final double fraction, final double tolerance) {
        double value = num.doubleValue() / den.doubleValue();
        return Math.abs(fraction - value) <= tolerance;
    }

    /**
     * @return -1 if this &lt; 0, 0 if this = 0 and +1 if this &gt; 0
     */
    public int signum() {
        return num.signum();
    }

    /**
     * @return The absolute value of this Rational.
     */
    public Rational abs() {
        return signum() >= 0 ? this : negate();
    }

    /**
     * @param term The term to compare the Rational with.
     * @return -1, 0 or 1 as this Rational is numerically less than, equal
     * to, or greater than {@code term}.
     */
    @Override
    public int compareTo(Rational term) {
        BigInteger x = this.num.multiply(term.den);
        BigInteger y = term.num.multiply(this.den);
        return x.compareTo(y);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Rational)) {
            return false;
        }
        Rational r = (Rational) other;
        return num.equals(r.num) && den.equals(r.den);
    }

    @Override
    public String toString() {
        String representation = num.toString();
        if (!den.equals(BigInteger.ONE)) {
            representation += "/" + den.toString();
        }
        return representation;
    }

    public static Rational[] rationalize(double[] items) {
        Rational[] rationals = new Rational[items.length];
        for (int i = 0; i < rationals.length; i++) {
            rationals[i] = new Rational(items[i]);
        }
        return rationals;
    }

    public static Rational[][] rationalize(double[][] items) {
        Rational[][] rationals = new Rational[items.length][];
        for (int i = 0; i < rationals.length; i++) {
            rationals[i] = rationalize(items[i]);
        }
        return rationals;
    }

}
