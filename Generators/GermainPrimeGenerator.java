package Generators;

import org.apache.commons.lang3.tuple.Pair;

import java.math.BigInteger;
import java.util.Map;
import java.util.Random;

public class GermainPrimeGenerator {
    public static final Random RND = new Random();
    public static final BigInteger TWO = new BigInteger("2");

    /**
     * Method that generates a (probable) Germain prime
     *
     * @return a pair of BigIntegers, with p on the first position and q on the other one.
     */
    public Map.Entry<BigInteger, BigInteger> generateGermainPrime(int primeLength) {
        BigInteger p, q;
        do {
            q = BigInteger.probablePrime(primeLength, RND);
            p = TWO.multiply(q).add(BigInteger.ONE);

            //System.out.println("Another number: " + p);
        } while (!p.isProbablePrime(10));

        return Pair.of(p, q);
    }
}
