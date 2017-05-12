package DiscreteLogarithm.Pollard;

import DiscreteLogarithm.DiscreteLogarithmSolver;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.InvalidParameterException;
import java.util.List;

public class PollardRho implements DiscreteLogarithmSolver {
    private static final LinearCongruenceSolver LINEAR_SOLVER = new LinearCongruenceSolver();
    private static final HashFunction[] HASH_FUNCTIONS = {Hashing.murmur3_32(), Hashing.murmur3_128(), Hashing.sha256(), Hashing.sipHash24()};
    private static final int FIRST_BUCKET_UPPER_LIMIT = Integer.MAX_VALUE / 3;
    private static final int SECOND_BUCKET_UPPER_LIMIT = 2 * FIRST_BUCKET_UPPER_LIMIT;
    private static final BigInteger TWO = new BigInteger("2");


    @Override
    public BigInteger logarithm(BigInteger a, BigInteger b, BigInteger p) {
        for (HashFunction hashFunction : HASH_FUNCTIONS) {

            InfoPair pairIndexI = new InfoPair(BigInteger.ONE, BigInteger.ZERO, BigInteger.ZERO);

            pairIndexI = computeNextPair(pairIndexI, a, b, p, hashFunction);
            InfoPair pairIndex2I = computeNextPair(pairIndexI, a, b, p, hashFunction);

            while (pairIndexI.getX().compareTo(pairIndex2I.getX()) != 0) {
                pairIndexI = computeNextPair(pairIndexI, a, b, p, hashFunction);

                pairIndex2I = computeNextPair(pairIndex2I, a, b, p, hashFunction);
                pairIndex2I = computeNextPair(pairIndex2I, a, b, p, hashFunction);
            }

            // Now the collision is found out, now only have to solve the congruency linear equation:
            // x (delta_1 - delta_2) = (epsilon_2 - epsilon_1) mod (p-1).

            // We denote alpha = delta_1 - delta_2, beta = epsilon_2 - epsilon_1 and phi = p - 1.
            // Therefore, the congruence to solve is: alpha x = beta mod phi.
            // So, we must find gcd(alpha, phi) and verify if

            BigInteger phi = p.subtract(BigInteger.ONE);
            BigInteger alpha = pairIndexI.getDelta().subtract(pairIndex2I.getDelta()).mod(phi);
            BigInteger beta = pairIndex2I.getEpsilon().subtract(pairIndexI.getEpsilon()).mod(phi);

            try {
                List<BigInteger> solutions = LINEAR_SOLVER.solve(alpha, beta, phi);
                for (BigInteger solution : solutions) {
                    if (isCorrectLog(a, b, solution, p)) {
                        return solution.mod(phi);
                    }
                }
                throw new InvalidParameterException("Could not find Discrete Logarithm!");
            } catch (InvalidParameterException e) {
                //System.out.println(("Could not perform discrete logarithm with hash function " + hashFunction.toString()));
            }
        }
        throw new InvalidParameterException("Could not find Discrete Logarithm, did not find any hash function to result a solvable linear congruence!");
    }

    private InfoPair computeNextPair(InfoPair current, BigInteger a, BigInteger b, BigInteger p, HashFunction hashFunction) {
        int bucket = computeBucketIndex(current.getX(), hashFunction);

        return new InfoPair(
                computeNextX(current.getX(), a, b, p, bucket),
                computeNextEpsilon(current.getEpsilon(), p, bucket),
                computeNextDelta(current.getDelta(), p, bucket));
    }

    private BigInteger computeNextX(BigInteger currentX, BigInteger a, BigInteger b, BigInteger p, int bucket) {
        switch (bucket) {
            case 1:
                return currentX.multiply(b).mod(p);
            case 2:
                return currentX.multiply(currentX).mod(p);
            case 3:
                return currentX.multiply(a).mod(p);
            default:
                throw new InvalidParameterException("Invalid bucket index: " + bucket);
        }
    }

    private BigInteger computeNextEpsilon(BigInteger currentEpsilon, BigInteger p, int bucket) {
        BigInteger phi = p.subtract(BigInteger.ONE); // phi(p) = p - 1;
        switch (bucket) {
            case 1:
                return currentEpsilon;
            case 2:
                return currentEpsilon.multiply(TWO).mod(phi);
            case 3:
                return currentEpsilon.add(BigInteger.ONE).mod(phi);
            default:
                throw new InvalidParameterException("Invalid bucket index: " + bucket);
        }
    }

    private BigInteger computeNextDelta(BigInteger currentDelta, BigInteger p, int bucket) {
        BigInteger phi = p.subtract(BigInteger.ONE); // phi(p) = p - 1;
        switch (bucket) {
            case 1:
                return currentDelta.add(BigInteger.ONE).mod(phi);
            case 2:
                return currentDelta.multiply(TWO).mod(phi);
            case 3:
                return currentDelta;
            default:
                throw new InvalidParameterException("Invalid bucket index: " + bucket);
        }
    }

    boolean isCorrectLog(BigInteger a, BigInteger b, BigInteger x, BigInteger p) {
        return a.modPow(x, p).compareTo(b) == 0;
    }


    /**
     * Method that uniformly distributes a number to a bucket, using a hash function. The possible number of
     * buckets is 3. The same number will be every time assigned to the same bucket.
     *
     * @param number the number to be randomly assigned to a bucket.
     * @return the corresponding bucket, a value of 1, 2 or 3.
     */
    private int computeBucketIndex(BigInteger number, HashFunction hashFunction) {
        int hash = hashFunction.hashString(number.toString(), Charset.defaultCharset()).asInt();
        if (hash < 0) {
            hash *= -1;
        }

        if (hash < FIRST_BUCKET_UPPER_LIMIT) {
            return 1;
        } else if (hash < SECOND_BUCKET_UPPER_LIMIT) {
            return 2;
        }

        return 3;
    }

    private static final class InfoPair {
        private BigInteger x;
        private BigInteger epsilon;
        private BigInteger delta;

        public InfoPair(BigInteger x, BigInteger epsilon, BigInteger delta) {
            this.x = x;
            this.epsilon = epsilon;
            this.delta = delta;
        }

        public BigInteger getX() {
            return x;
        }

        public void setX(BigInteger x) {
            this.x = x;
        }

        public BigInteger getEpsilon() {
            return epsilon;
        }

        public void setEpsilon(BigInteger epsilon) {
            this.epsilon = epsilon;
        }

        public BigInteger getDelta() {
            return delta;
        }

        public void setDelta(BigInteger delta) {
            this.delta = delta;
        }
    }
}
