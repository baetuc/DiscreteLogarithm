package DiscreteLogarithm.Pollard;

import org.bouncycastle.pqc.math.ntru.euclid.BigIntEuclidean;

import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.List;

public class LinearCongruenceSolver {
    /**
     * Method that solves the equation alpha x = beta mod phi.
     */
    public List<BigInteger> solve(BigInteger alpha, BigInteger beta, BigInteger phi) {
        BigIntEuclidean result = BigIntEuclidean.calculate(alpha, phi);
        if (beta.mod(result.gcd).compareTo(BigInteger.ZERO) != 0) {
            throw new InvalidParameterException("Could not perform discrete logarithm with hash function ");
        }

        beta = beta.divide(result.gcd);
        phi = phi.divide(result.gcd);

        List<BigInteger> solutions = new LinkedList<>();
        BigInteger finalResult = result.x.multiply(beta).mod(phi);
        for (BigInteger t = BigInteger.ZERO; t.compareTo(result.gcd) < 0; t = t.add(BigInteger.ONE)) {
            solutions.add(finalResult);
            finalResult = finalResult.add(phi);
        }

        return solutions;
    }
}
