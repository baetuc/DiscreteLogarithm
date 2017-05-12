package Generators;

import java.math.BigInteger;
import java.util.Random;

public class PrimitiveRootSimpleGen {
    public static final Random RND = new Random();
    public static final BigInteger TWO = new BigInteger("2");
    public static final BigInteger FOUR = new BigInteger("4");

    /**
     * Method that generates a primitive root modulo p, knowing that p = 2 * q + 1, where p and q are
     * odd primes. Using the theory, it is known that "a" is a primitive root modulo p iff a^{(p-1) / r} != 1,
     * \forall r - prime divisor of {p - 1}. In this case, "a" is primitive root modulo p iff a ^ {(p-1) / 2} != 1
     * and a ^ 2 != 1. Choosing a != 1 and a != (-1), the second condition is met (because p is prime, so Z_p does
     * not have zero divisors different than zero), so for a to be a primitive root, it is sufficient that the
     * relation (a / p) = -1 holds. Knowing that (-1 / p) = -1, it is obvious that (-a^2 / p) = -1,
     * \forall a \in Z_p. Choosing a != 1 and a != -1 => -a^2 != 1 and -a^2 != -1. Therefore, the number generated
     * as above is surely a primitive root modulo p.
     *
     * @param p the prime number which defines the computation group.
     */
    public BigInteger generatePrimitiveRoot(BigInteger p) {
        BigInteger a = generateUniform(p);
        return p.subtract(a.multiply(a).mod(p)); // p - a^2 = -a ^2
    }

    /**
     * Method that generates a number uniformly chosen between 2 and n - 2.
     */
    public static BigInteger generateUniform(BigInteger n) {
        n = n.subtract(FOUR);
        BigInteger a;
        do {
            a = new BigInteger(n.bitLength(), RND);
        } while (a.compareTo(n) >= 0);
        return a.add(TWO);
    }


}
