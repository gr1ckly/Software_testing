package org.example.functions.ln;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LnFunctionTest {
    private static final double TEST_ACCURACY = 1.0E-12;
    private final LnFunction lnFunction = new LnFunction();

    @ParameterizedTest
    @MethodSource("baseValues")
    void baseValuesTest(double x) {
        double actual = lnFunction.calculate(x, TEST_ACCURACY);
        double expected = Math.log(x);

        assertEquals(expected, actual, 1.0E-9);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0E-6, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void throwsIllegalArgumentExceptionForAccuracyTest(double accuracy) {
        assertThrows(IllegalArgumentException.class, () -> lnFunction.calculate(2.0, accuracy));
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void throwsIllegalArgumentExceptionForNonFiniteXTest(double x) {
        assertThrows(IllegalArgumentException.class, () -> lnFunction.calculate(x, TEST_ACCURACY));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0, -10.0})
    void throwsArithmeticExceptionForNonPositiveXTest(double x) {
        assertThrows(ArithmeticException.class, () -> lnFunction.calculate(x, TEST_ACCURACY));
    }

    @ParameterizedTest
    @ValueSource(doubles = {1.9, 10.0})
    void checkAccuracyTest(double x) {
        double looseAccuracyResult = lnFunction.calculate(x, 1.0E-1);
        double tightAccuracyResult = lnFunction.calculate(x, 1.0E-12);
        double expected = Math.log(x);

        double looseError = Math.abs(looseAccuracyResult - expected);
        double tightError = Math.abs(tightAccuracyResult - expected);

        assertTrue(Math.abs(looseAccuracyResult - tightAccuracyResult) > 0.0);
        assertTrue(tightError < looseError);
    }

    @ParameterizedTest
    @MethodSource("largeValues")
    void largeValuesTest(double x) {
        double actual = lnFunction.calculate(x, TEST_ACCURACY);
        double expected = Math.log(x);

        assertEquals(expected, actual, 1.0E-9);
    }

    private static Stream<Double> baseValues() {
        return Stream.of(0.125, 0.5, 1.0, 1.5, 2.0, Math.E, 10.0, 1024.0);
    }

    private static Stream<Double> largeValues() {
        return Stream.of(1.0E6, 1.0E12, 1.0E100, Double.MAX_VALUE / 2.0);
    }
}
