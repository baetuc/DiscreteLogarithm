package DiscreteLogarithm.SilverPohligHellman;

import Generators.PrimitiveRootSimpleGen;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ParticularPrime {
    private static final BigInteger TWO = new BigInteger("2");
    private static final Random RND = new Random();

    private final List<Factor> phiFactors;
    private final  BigInteger value;

    private ParticularPrime(List<Factor> factors, BigInteger value) {
        this.phiFactors = factors;
        this.value = value;
    }

    public static ParticularPrime generate() {
        List<Factor> factors = new LinkedList<>();

        BigInteger currentPhi = TWO;
        boolean foundSolution = false;

        while(!foundSolution) {
            factors.clear();
            factors.add(new Factor(TWO, BigInteger.ONE));

            int numberOfFactors = (((RND.nextInt() % 7) + 7) % 7) + 2; // between 3 and 10 phiFactors
            for (int i = 0; i < numberOfFactors; ++i) {
                factors.add(new Factor(BigInteger.probablePrime(16, RND), BigInteger.ZERO));
            }

            currentPhi = TWO;

            while (currentPhi.bitCount() < 1000 || !currentPhi.add(BigInteger.ONE).isProbablePrime(15)) {
                int factorIndex = ((RND.nextInt() % factors.size()) + factors.size()) % factors.size();
                factors.get(factorIndex).incrementPower();
                currentPhi = currentPhi.multiply(factors.get(factorIndex).getPrime());

                if (currentPhi.bitLength() > 2000) {
                    break;
                }
            }

            if (currentPhi.add(BigInteger.ONE).isProbablePrime(15)) {
                foundSolution = true;
            }
        }

        return new ParticularPrime(factors, currentPhi.add(BigInteger.ONE));
    }

    public BigInteger generatePrimitiveRoot() {
        boolean foundSolution = false;
        BigInteger supposedSolution = null;

        while(!foundSolution) {
            foundSolution = true;
            supposedSolution = PrimitiveRootSimpleGen.generateUniform(value);

            BigInteger phi = value.subtract(BigInteger.ONE);

            for(Factor factor : phiFactors) {
                BigInteger power = phi.divide(factor.getPrime());
                if (supposedSolution.modPow(power, value).compareTo(BigInteger.ONE) == 0) {
                    foundSolution = false;
                    break;
                }
            }
        }

        return supposedSolution;
    }

    public BigInteger getValue() {
        return value;
    }

    public List<Factor> getPhiFactors() {
        return phiFactors;
    }

    public static final class Factor {
        BigInteger prime;
        BigInteger power;

        public Factor(BigInteger prime, BigInteger power) {
            this.prime = prime;
            this.power = power;
        }

        public BigInteger getPrime() {
            return prime;
        }

        public void setPrime(BigInteger prime) {
            this.prime = prime;
        }

        public BigInteger getPower() {
            return power;
        }

        public void setPower(BigInteger power) {
            this.power = power;
        }

        public void incrementPower() {
            this.power = this.power.add(BigInteger.ONE);
        }

        @Override
        public String toString() {
            return "(" + prime + ", " + power + ")";
        }
    }

    public static void main(String[] args) {

        ParticularPrime prime = ParticularPrime.generate();

        System.out.println(prime.getValue());
        System.out.println(prime.getValue().bitLength());

        System.out.println(prime.getPhiFactors());

        BigInteger primitiveRoot = prime.generatePrimitiveRoot();
        System.out.println(primitiveRoot);
        System.out.println(primitiveRoot.bitLength());
    }
}
