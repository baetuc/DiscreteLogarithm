package CRT.Model;


import org.apache.commons.lang3.Validate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a Congruence System.
 */
public class CongruenceSystem {
    private final List<CongruenceEquation> equations;

    public CongruenceSystem(List<CongruenceEquation> equations) {
        this.equations = equations;
    }

    public CongruenceSystem() {
        this(new ArrayList<>());
    }

    public void addEquation(CongruenceEquation equation) {
        for (CongruenceEquation oldEquation : equations) {
            Validate.isTrue(
                    equation.getM().gcd(oldEquation.getM()).equals(BigInteger.ONE),
                    "Added equation makes the system invalid.");
        }

        equations.add(equation);
    }

    public void addEquation(BigInteger b, BigInteger m) {
        addEquation(new CongruenceEquation(b, m));
    }

    public int size() {
        return equations.size();
    }

    public CongruenceEquation getEquation(int index) {
        return equations.get(index);
    }

}
