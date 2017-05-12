package DiscreteLogarithm;

import java.math.BigInteger;

@FunctionalInterface
public interface DiscreteLogarithmSolver {
    /**
     * Method that computes the discrete logarithm of b in the base a, modulo p. Note that a must be a primitive
     * root modulo p.
     *
     * @param a the base of the logarithm
     * @param b the number to be extracted logarithm from
     * @param p the index of the computation field
     * @return the DL of b in base a, modulo p.
     */
    BigInteger logarithm(BigInteger a, BigInteger b, BigInteger p);
}
