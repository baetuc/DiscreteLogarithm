package DiscreteLogarithm.SilverPohligHellman;

import CRT.Model.CongruenceSystem;
import CRT.Solver.CongruenceSystemSolver;
import CRT.Solver.GarnerSolver;
import DiscreteLogarithm.DiscreteLogarithmSolver;
import DiscreteLogarithm.Pollard.PollardRho;
import org.apache.commons.lang3.Validate;

import java.math.BigInteger;

public class SilverPohligHellman {
    private static final CongruenceSystemSolver CRT_SOLVER = new GarnerSolver();
    private static final DiscreteLogarithmSolver SIMPLE_DL_SOLVER = new PollardRho();
    private static final BigInteger MAX_INT = new BigInteger(String.valueOf(Integer.MAX_VALUE));

    public BigInteger logarithm(BigInteger a, BigInteger b, ParticularPrime p) {
        CongruenceSystem system = new CongruenceSystem();

        for (ParticularPrime.Factor factor : p.getPhiFactors()) {
            Validate.isTrue(factor.getPower().compareTo(MAX_INT) <= 0, "Power is too big: " + factor.getPower());

            int power = Integer.parseInt(factor.getPower().toString());
            system.addEquation(computeCongruence(a, b, factor, p.getValue()), factor.getPrime().pow(power));
            // Add equation x = x_i mod pi^ni
        }
        return CRT_SOLVER.solve(system);
    }

    /**
     * Method that computes x_i from th general Congruence System, following the notes from the class.
     */
    private BigInteger computeCongruence(BigInteger a, BigInteger b, ParticularPrime.Factor factor, BigInteger p) {
        int power = Integer.parseInt(factor.getPower().toString());

        BigInteger phi = p.subtract(BigInteger.ONE);
        BigInteger a_i = a.modPow(phi.divide(factor.getPrime()), p);
        BigInteger currentBasis = BigInteger.ONE; // currentBasis is pi^j
        BigInteger currentPower = phi.divide(factor.getPrime()); // currentPower is (p-1)/ (p_i^{j+1})

        BigInteger result = BigInteger.ZERO;

        for (int j = 0; j < power; ++j) {
            // We observe that the particular case j == 0 from the course can be generalized with the j^{th} step:

            BigInteger toExtract = b.multiply(a.modPow(result.negate(), p)).mod(p).modPow(currentPower, p);
            BigInteger currentB = SIMPLE_DL_SOLVER.logarithm(a_i, toExtract, p);

            result = result.add(currentB.multiply(currentBasis));
            currentBasis = currentBasis.multiply(factor.getPrime());
            currentPower = currentPower.divide(factor.getPrime());
        }

        return result;
    }


}
