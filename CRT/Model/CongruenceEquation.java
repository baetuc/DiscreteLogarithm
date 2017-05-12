package CRT.Model;

import java.math.BigInteger;

/**
 * Class that represents a Congruence Equation, that takes the form x = b mod m, where b and m are the member
 * variables.
 */
public class CongruenceEquation {
    private final BigInteger b;
    private final BigInteger m;


    public CongruenceEquation(BigInteger b, BigInteger m) {
        this.b = b.mod(m);
        this.m = m;
    }

    public BigInteger getB() {
        return b;
    }

    public BigInteger getM() {
        return m;
    }
}
