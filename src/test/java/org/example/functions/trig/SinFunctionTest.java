package org.example.functions.trig;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SinFunctionTest {
    private static final double TEST_ACCURACY = 1.0E-12;
    private final SinFunction sinFunction = new SinFunction();

    @ParameterizedTest
    @MethodSource("baseValues")
    void baseValuesTest(double x) {
        double actual = sinFunction.calculate(x, TEST_ACCURACY);
        double expected = Math.sin(x);

        assertEquals(expected, actual, 1.0E-9);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0E-6, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void throwsIllegalArgumentExceptionForAccuracyTest(double accuracy) {
        assertThrows(IllegalArgumentException.class, () -> sinFunction.calculate(1.0, accuracy));
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void throwsIllegalArgumentExceptionForInvalidXTest(double x) {
        assertThrows(IllegalArgumentException.class, () -> sinFunction.calculate(x, TEST_ACCURACY));
    }

    @ParameterizedTest
    @ValueSource(doubles = {1.0, 2.5})
    void accuracyTest(double x) {
        double looseAccuracyResult = sinFunction.calculate(x, 1.0E-1);
        double tightAccuracyResult = sinFunction.calculate(x, 1.0E-12);
        double expected = Math.sin(x);

        double looseError = Math.abs(looseAccuracyResult - expected);
        double tightError = Math.abs(tightAccuracyResult - expected);

        assertTrue(Math.abs(looseAccuracyResult - tightAccuracyResult) > 0.0);
        assertTrue(tightError < looseError);
    }

    @ParameterizedTest
    @MethodSource("largeValues")
    void largeValuesNormalizationTest(double x) {
        double actual = sinFunction.calculate(x, TEST_ACCURACY);
        double expected = Math.sin(x);

        assertEquals(expected, actual, 1.0E-8);
    }

    private static Stream<Double> baseValues() {
        return Stream.of(
                -2.0 * Math.PI,
                -Math.PI,
                -Math.PI / 2.0,
                -Math.PI / 3.0,
                -Math.PI / 6.0,
                0.0,
                Math.PI / 6.0,
                Math.PI / 3.0,
                Math.PI / 2.0,
                Math.PI,
                2.0 * Math.PI,
                10.0
        );
    }

    private static Stream<Double> largeValues() {
        return Stream.of(
                1.0E6 * Math.PI + Math.PI / 6.0,
                -1.0E6 * Math.PI + Math.PI / 4.0,
                1234567.0 * Math.PI + Math.PI / 3.0
        );
    }
}
