package org.example.functions.ln;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
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
    void baseValuesTest(double x, double expected) {
        double actual = lnFunction.calculate(x, TEST_ACCURACY);

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
    @MethodSource("accuracyValues")
    void checkAccuracyTest(double x, double expected) {
        double looseAccuracyResult = lnFunction.calculate(x, 1.0E-1);
        double tightAccuracyResult = lnFunction.calculate(x, 1.0E-12);

        double looseError = Math.abs(looseAccuracyResult - expected);
        double tightError = Math.abs(tightAccuracyResult - expected);

        assertTrue(Math.abs(looseAccuracyResult - tightAccuracyResult) > 0.0);
        assertTrue(tightError < looseError);
    }

    @ParameterizedTest
    @MethodSource("largeValues")
    void largeValuesTest(double x, double expected) {
        double actual = lnFunction.calculate(x, TEST_ACCURACY);

        assertEquals(expected, actual, 1.0E-9);
    }

    private static Stream<Arguments> baseValues() {
        return Stream.of(
                Arguments.of(0.125, -2.0794415416798357),
                Arguments.of(0.5, -0.6931471805599453),
                Arguments.of(1.0, 0.0),
                Arguments.of(1.5, 0.4054651081081644),
                Arguments.of(2.0, 0.6931471805599453),
                Arguments.of(2.718281828459045, 1.0),
                Arguments.of(10.0, 2.302585092994046),
                Arguments.of(1024.0, 6.931471805599453)
        );
    }

    private static Stream<Arguments> accuracyValues() {
        return Stream.of(
                Arguments.of(1.9, 0.6418538861723947),
                Arguments.of(10.0, 2.302585092994046)
        );
    }

    private static Stream<Arguments> largeValues() {
        return Stream.of(
                Arguments.of(1.0E6, 13.815510557964274),
                Arguments.of(1.0E12, 27.631021115928547),
                Arguments.of(1.0E100, 230.25850929940458),
                Arguments.of(8.988465674311579E307, 709.0895657128241)
        );
    }
}
