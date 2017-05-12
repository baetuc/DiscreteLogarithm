package DiscreteLogarithm.SilverPohligHellman;

import Generators.PrimitiveRootSimpleGen;
import com.google.common.base.Stopwatch;
import org.apache.commons.lang3.Validate;

import java.math.BigInteger;

public class TestSilverPohligHellman {
    public static void main(String[] args) {
        Stopwatch watch = Stopwatch.createStarted();
        ParticularPrime prime = ParticularPrime.generate();
        System.out.println("Generated particular prime in: " + watch);
        System.out.println("Particular prime factors of phi: " + prime.getPhiFactors());

        BigInteger a = prime.generatePrimitiveRoot();
        BigInteger b = PrimitiveRootSimpleGen.generateUniform(prime.getValue());
        SilverPohligHellman dlSolver = new SilverPohligHellman();

        watch = Stopwatch.createStarted();
        BigInteger log = dlSolver.logarithm(a, b, prime);
        System.out.println("Solved discrete logarithm in: " + watch);

        System.out.println("Primitive root: " + a);
        System.out.println("b: " + b);
        System.out.println("p: " + prime.getValue());
        System.out.println("Discrete logarithm: " + log);

        Validate.isTrue(a.modPow(log, prime.getValue()).compareTo(b) == 0, "Generated logarithm is incorrect!");

    }
}
