package CRT.Solver;

import CRT.Model.CongruenceSystem;

import java.math.BigInteger;

@FunctionalInterface
public interface CongruenceSystemSolver {
    BigInteger solve(CongruenceSystem system);
}
