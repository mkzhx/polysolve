package org.mkzh.polysolve.method.implementation;

import ch.obermuhlner.math.big.BigComplex;
import ch.obermuhlner.math.big.BigDecimalMath;
import org.mkzh.polysolve.method.Method;
import org.mkzh.polysolve.util.BigNumber;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cubic extends Method {
    @Override
    public List<BigDecimal> solve(List<BigDecimal> coefficients, MathContext mathContext) {
        return findRoots(coefficients.get(3), coefficients.get(2), coefficients.get(1), coefficients.get(0), mathContext);
    }

    private List<BigDecimal> findRoots(BigDecimal a, BigDecimal b, BigDecimal c, BigDecimal d, MathContext mathContext) {
        // n = 2b^3 - 9abc + 27(a^2)d
        BigDecimal n = b.pow(3).multiply(BigDecimal.valueOf(2))
                .subtract(a.multiply(b).multiply(c).multiply(BigDecimal.valueOf(9)))
                .add(a.pow(2).multiply(d).multiply(BigDecimal.valueOf(27)));
        // o = sqrt(n^2 - 4(b^2-3ac)^3)
        BigDecimal o = BigDecimalMath.sqrt(n.pow(2)
                .subtract(b.pow(2).subtract(a.multiply(c).multiply(BigDecimal.valueOf(3))).pow(3)
                        .multiply(BigDecimal.valueOf(4))), mathContext);
        // p = cbrt(0.5*(n+o))
        BigDecimal p = getCubeRootOfHalf(n.add(o), mathContext);
        //q = cbrt(0.5*(n-o))
        BigDecimal q = getCubeRootOfHalf(n.subtract(o), mathContext);
        // r = -b/(3*a)
        BigDecimal r = divideBy3a(b.negate(), a, mathContext);
        // s = 1/(3*a)
        BigDecimal s = divideBy3a(BigDecimal.valueOf(1), a, mathContext);
        // t = (1+i*sqrt(3))/6*a
        BigComplex t = getComplexPart(a, true, mathContext);
        // u = (1-i*sqrt(3))/6*a
        BigComplex u = getComplexPart(a, false, mathContext);

        List<BigDecimal> roots = new ArrayList<>();
        // real root = r - s*p - s*q
        roots.add(r.subtract(s.multiply(p)).subtract(s.multiply(q)));
        // possibly imaginary root 1 = r + t*p + u*q
        addBigComplexRoot(roots, t.multiply(p).add(u.multiply(q)).add(r));
        // possibly imaginary root 2 = r + u*p + t*q
        addBigComplexRoot(roots, u.multiply(p).add(t.multiply(q)).add(r));

        System.out.println(n);
        System.out.println(o);
        System.out.println(p);
        System.out.println(q);
        System.out.println(r);
        System.out.println(s);
        System.out.println(t);
        System.out.println(u);
        System.out.println(roots.get(0));
        return Arrays.asList(new BigDecimal("1"));
    }

    private void addBigComplexRoot(List<BigDecimal> roots, BigComplex root) {
        System.out.println(root);
    }

    // cbrt(0.5*val)
    private BigDecimal getCubeRootOfHalf(BigDecimal val, MathContext mathContext) {
        boolean neg = BigNumber.isNegative(val);
        BigDecimal res = BigDecimalMath.root((neg ? val.negate() : val).multiply(BigDecimal.valueOf(0.5)), BigDecimal.valueOf(3), mathContext);
        return neg ? res.negate() : res;
    }

    // val/3a
    private BigDecimal divideBy3a(BigDecimal val, BigDecimal a, MathContext mathContext) {
        return val.divide(a.multiply(BigDecimal.valueOf(3)), mathContext);
    }

    // (1 (+|-) i*sqrt(3))/6*a)
    private BigComplex getComplexPart(BigDecimal a, boolean addition, MathContext mathContext) {
        BigComplex numeratorComplex = BigComplex.I.multiply(BigDecimalMath.sqrt(BigDecimal.valueOf(3), mathContext))
                .multiply(addition ? BigDecimal.valueOf(1) : BigDecimal.valueOf(-1));
        return numeratorComplex.add(BigDecimal.valueOf(1)).divide(a.multiply(BigDecimal.valueOf(6)), mathContext);
    }

    public static void main(String[] args) {
        Cubic cubic = new Cubic();
        cubic.findRoots(new BigDecimal(3), new BigDecimal(4), new BigDecimal(5), new BigDecimal(6), new MathContext(24));

        cubic.findRoots(new BigDecimal(1), new BigDecimal(0), new BigDecimal(-2), new BigDecimal(0), new MathContext(24));
    }
}
