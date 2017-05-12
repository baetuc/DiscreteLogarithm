package DiscreteLogarithm.Pollard;

import DiscreteLogarithm.DiscreteLogarithmSolver;
import DiscreteLogarithm.Pollard.PollardRho;
import Generators.GermainPrimeGenerator;
import Generators.PrimitiveRootSimpleGen;
import com.google.common.base.Stopwatch;
import org.apache.commons.lang3.Validate;

import java.math.BigInteger;
import java.util.Map;

public class TestDiscreteLogarithm {
    private static final int PRIME_LENGTH = 32;

    public static void main(String[] args) {
        DiscreteLogarithmSolver solver = new PollardRho();

        GermainPrimeGenerator primeGenerator = new GermainPrimeGenerator();
        PrimitiveRootSimpleGen primitiveGenerator = new PrimitiveRootSimpleGen();

        Stopwatch watch = Stopwatch.createStarted();
        Map.Entry<BigInteger, BigInteger> primes = primeGenerator.generateGermainPrime(PRIME_LENGTH);
        BigInteger p = primes.getKey();
        BigInteger q = primes.getValue();
        System.out.println("Time elapsed for generating Germain prime: " + watch + "\n");

        watch = Stopwatch.createStarted();
        BigInteger a = primitiveGenerator.generatePrimitiveRoot(p);
        System.out.println("Primitive root modulo p: " + a.toString());
        System.out.println("\nTime elapsed for generating the primitive root: " + watch + "\n");

        BigInteger b = PrimitiveRootSimpleGen.generateUniform(p);
        System.out.println("b = " + b);

        watch = Stopwatch.createStarted();
        BigInteger log = solver.logarithm(a, b, p);
        System.out.println("Discrete logarithm: " + log);
        System.out.println("\nTime elapsed for generating the discrete logarithm: " + watch + "\n");

        Validate.isTrue(a.modPow(log, p).compareTo(b) == 0, "Discrete logarithm is not computed correctly.");
    }
}
