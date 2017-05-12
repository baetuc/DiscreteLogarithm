package CRT.Solver;

import CRT.Model.CongruenceEquation;
import CRT.Model.CongruenceSystem;
import org.apache.commons.lang3.Validate;

import java.math.BigInteger;

public class GarnerSolver implements CongruenceSystemSolver {

    @Override
    public BigInteger solve(CongruenceSystem system) {
        Validate.notNull(system, "System given as parameter must be not null.");
        Validate.isTrue(system.size() > 0, "Congruence system does not contain any equation.");

        BigInteger solution = system.getEquation(0).getB();

        for (int i = 1; i < system.size(); ++i) {
            solution = constructNewSolution(solution, system, i);
        }

        return solution;
    }

    /**
     * Method that consructs a new solution for the CongruenceSystem, based on the previous solution and the
     * new congruence equation given as parameters, using Garner's algorithm.
     *
     * @param oldSolution      the solution for the previous equations
     * @param system           the Congruence System that must be solved by the Algorithm
     * @param newEquationIndex the index of the new equation that the newly created solution must hold for
     * @return the new constructed solution, that holds for both old system equations and new system equation
     */
    private BigInteger constructNewSolution(BigInteger oldSolution, CongruenceSystem system, int newEquationIndex) {
        CongruenceEquation newEquation = system.getEquation(newEquationIndex);
        BigInteger mProduct = system.getEquation(0).getM();

        for (int i = 1; i < newEquationIndex; ++i) {
            mProduct = mProduct.multiply(system.getEquation(i).getM());
        }
        // (m_0 * m_1 * ... * m_{i-1}) ^ (-1) mod m_i
        BigInteger mProductInverse = mProduct.mod(newEquation.getM()).modInverse(newEquation.getM());
        // b_i - x_{i - 1} mod m_i
        BigInteger rhs = newEquation.getB().subtract(oldSolution).mod(newEquation.getM());
        BigInteger alpha = mProductInverse.multiply(rhs).mod(newEquation.getM());

        // x_i = x_{i-1} + alpha * m_0 * m_1 *... * m_{i-1} mod m_i
        return oldSolution.add(alpha.multiply(mProduct));
    }
}
