package Generators;

import com.google.common.base.Stopwatch;
import org.apache.commons.lang3.Validate;

import java.math.BigInteger;
import java.util.Map;

public class TestPrimitiveGenerator {
    private static final int PRIME_LENGTH = 1024;
    public static final BigInteger TWO = new BigInteger("2");


    public static void main(String[] args) {
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
        System.out.println("\nTime elapsed for generating the primitive root: " + watch);

        Validate.isTrue(a.modPow(TWO, p).compareTo(BigInteger.ONE) != 0, "Generated primitive root is incorrect");
        Validate.isTrue(a.modPow(q, p).compareTo(BigInteger.ONE) != 0, "Generated primitive root is incorrect");
    }
}
