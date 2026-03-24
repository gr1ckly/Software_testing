package org.example.functions.equation;

import org.example.functions.AbstractFunction;
import org.example.functions.ln.LnFunction;
import org.example.functions.ln.LogFunction;
import org.example.functions.trig.CosFunction;
import org.example.functions.trig.CotFunction;
import org.example.functions.trig.CscFunction;
import org.example.functions.trig.SecFunction;
import org.example.functions.trig.SinFunction;
import org.example.functions.trig.TanFunction;
import org.example.test_helper.TestStubFunction;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EquationFunctionTest {
    private static final double TEST_ACCURACY = 1.0E-12;

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0E-6, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void throwsIllegalArgumentExceptionForAccuracyTest(double accuracy) {
        EquationFunction equationFunction = new EquationFunction(
                stubAt(1.0, 0.0),
                stubAt(1.0, 0.0),
                stubAt(1.0, 0.0),
                stubAt(1.0, 0.0),
                stubAt(1.0, 0.0),
                stubAt(1.0, 0.0),
                stubAt(1.0, 0.0),
                stubAt(1.0, 0.0),
                stubAt(1.0, 0.0),
                stubAt(1.0, 0.0)
        );

        assertThrows(IllegalArgumentException.class, () -> equationFunction.calculate(1.0, accuracy));
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, 0.0, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void throwsIllegalArgumentExceptionForInvalidXTest(double x) {
        EquationFunction equationFunction = new EquationFunction(
                stubAt(1.0, 0.0),
                stubAt(1.0, 0.0),
                stubAt(1.0, 0.0),
                stubAt(1.0, 0.0),
                stubAt(1.0, 0.0),
                stubAt(1.0, 0.0),
                stubAt(1.0, 0.0),
                stubAt(1.0, 0.0),
                stubAt(1.0, 0.0),
                stubAt(1.0, 0.0)
        );

        assertThrows(IllegalArgumentException.class, () -> equationFunction.calculate(x, TEST_ACCURACY));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.0})
    void throwsArithmeticExceptionWhenDenominatorIsZeroWithStubs(double x) {
        EquationFunction equationFunction = new EquationFunction(
                stubAt(x, 1.0),
                stubAt(x, 1.0),
                stubAt(x, 1.0),
                stubAt(x, 2.0),
                stubAt(x, 1.0),
                stubAt(x, 1.0),
                stubAt(1.0, 0.0),
                stubAt(1.0, 0.0),
                stubAt(1.0, 0.0),
                stubAt(1.0, 0.0)
        );

        assertThrows(ArithmeticException.class, () -> equationFunction.calculate(x, TEST_ACCURACY));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.0})
    void baseValuesWithStubsForNonPositiveBranchTest(double x) {
        double sin = Math.sin(x);
        double cos = Math.cos(x);
        double tan = Math.tan(x);
        double cot = 1 / tan;
        double sec = 1 / sin;
        double csc = 1 / cos;

        EquationFunction equationFunction = new EquationFunction(
                stubAt(x, sin),
                stubAt(x, cos),
                stubAt(x, tan),
                stubAt(x, cot),
                stubAt(x, sec),
                stubAt(x, csc),
                stubAt(1.0, 0.0),
                stubAt(1.0, 0.0),
                stubAt(1.0, 0.0),
                stubAt(1.0, 0.0)
        );

        double actual = equationFunction.calculate(x, TEST_ACCURACY);
        double expected = calculateExpectedNonPositiveBranch(sin, cos, tan, cot, sec, csc);

        assertEquals(expected, actual, 1.0E-12);
    }

    @ParameterizedTest
    @ValueSource(doubles = {2.0})
    void baseValuesWithStubsForPositiveBranchTest(double x) {
        double ln = 1.0;
        double log2 = 2.0;
        double log5 = 3.0;
        double log10 = 4.0;

        EquationFunction equationFunction = new EquationFunction(
                stubAt(-1.0, 0.0),
                stubAt(-1.0, 0.0),
                stubAt(-1.0, 0.0),
                stubAt(-1.0, 0.0),
                stubAt(-1.0, 0.0),
                stubAt(-1.0, 0.0),
                stubAt(x, ln),
                stubAt(x, log2),
                stubAt(x, log5),
                stubAt(x, log10)
        );

        double actual = equationFunction.calculate(x, TEST_ACCURACY);
        double expected = calculateExpectedPositiveBranch(ln, log2, log5, log10);

        assertEquals(expected, actual, 1.0E-12);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-Math.PI / 4.0})
    void baseValuesWithRealFunctionsForNonPositiveBranchTest(double x) {
        SinFunction sinFunction = new SinFunction();
        CosFunction cosFunction = new CosFunction(sinFunction);
        LnFunction lnFunction = new LnFunction();
        EquationFunction equationFunction = new EquationFunction(
                sinFunction,
                cosFunction,
                new TanFunction(sinFunction, cosFunction),
                new CotFunction(sinFunction, cosFunction),
                new SecFunction(cosFunction),
                new CscFunction(sinFunction),
                lnFunction,
                new LogFunction(2.0, lnFunction),
                new LogFunction(5.0, lnFunction),
                new LogFunction(10.0, lnFunction)
        );

        double actual = equationFunction.calculate(x, TEST_ACCURACY);

        double sin = Math.sin(x);
        double cos = Math.cos(x);
        double tan = Math.tan(x);
        double cot = cos / sin;
        double sec = 1.0 / cos;
        double csc = 1.0 / sin;
        double expected = calculateExpectedNonPositiveBranch(sin, cos, tan, cot, sec, csc);

        assertEquals(expected, actual, 1.0E-8);
    }

    @ParameterizedTest
    @ValueSource(doubles = {2.0, 10.0})
    void baseValuesWithRealFunctionsForPositiveBranchTest(double x) {
        SinFunction sinFunction = new SinFunction();
        CosFunction cosFunction = new CosFunction(sinFunction);
        LnFunction lnFunction = new LnFunction();
        EquationFunction equationFunction = new EquationFunction(
                sinFunction,
                cosFunction,
                new TanFunction(sinFunction, cosFunction),
                new CotFunction(sinFunction, cosFunction),
                new SecFunction(cosFunction),
                new CscFunction(sinFunction),
                lnFunction,
                new LogFunction(2.0, lnFunction),
                new LogFunction(5.0, lnFunction),
                new LogFunction(10.0, lnFunction)
        );

        double actual = equationFunction.calculate(x, TEST_ACCURACY);

        double ln = Math.log(x);
        double log2 = Math.log(x) / Math.log(2.0);
        double log5 = Math.log(x) / Math.log(5.0);
        double log10 = Math.log(x) / Math.log(10.0);
        double expected = calculateExpectedPositiveBranch(ln, log2, log5, log10);

        assertEquals(expected, actual, 1.0E-8);
    }

    private static AbstractFunction stubAt(double x, double value) {
        return new TestStubFunction(Map.of(x, value));
    }

    private static double calculateExpectedNonPositiveBranch(
            double sin,
            double cos,
            double tan,
            double cot,
            double sec,
            double csc
    ) {
        double leftFirst = Math.pow((((cot - sec) + (cot + csc)) * sin), 2.0);
        double leftSecond = (tan + csc) - (csc * cos);
        double leftThird = Math.pow(Math.pow(csc, 3.0), 3.0) - csc;
        double denominator = (((cot + cot) / tan) - (cot + (csc * ((cos + sin) * sec))));
        double leftPart = ((leftFirst + leftSecond) * leftThird) / denominator;

        double rightFirst = ((tan / cos) + (cot + sin)) - cos;
        double rightSecond = (Math.pow(cos, 2.0) * (sin - sin)) - cot;
        double rightThird = (sin * csc) * csc;
        double rightPart = (rightFirst * rightSecond) + rightThird;

        return leftPart - rightPart;
    }

    private static double calculateExpectedPositiveBranch(
            double ln,
            double log2,
            double log5,
            double log10
    ) {
        return (((((log5 - ln) - log2) * log5) + (log2 * (log2 + (log10 - ln)))) - ln);
    }
}
