package org.example.functions.trig;

import org.example.test_helper.TestStubFunction;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CosFunctionTest {
    private static final double TEST_ACCURACY = 1.0E-12;

    @ParameterizedTest
    @MethodSource("baseValues")
    void baseValuesTest(double x) {
        double transformedX = Math.PI / 2.0 - x;
        CosFunction cosFunction = new CosFunction(
                new TestStubFunction(Map.of(transformedX, Math.sin(transformedX)))
        );

        double actual = cosFunction.calculate(x, TEST_ACCURACY);
        double expected = Math.cos(x);

        assertEquals(expected, actual, 1.0E-12);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0E-6, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void throwsIllegalArgumentExceptionForAccuracyTest(double accuracy) {
        CosFunction cosFunction = new CosFunction(new TestStubFunction(Map.of(Math.PI / 2.0, 1.0)));

        assertThrows(IllegalArgumentException.class, () -> cosFunction.calculate(0.0, accuracy));
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void throwsIllegalArgumentExceptionForInvalidXTest(double x) {
        CosFunction cosFunction = new CosFunction(new TestStubFunction(Map.of(Math.PI / 2.0, 1.0)));

        assertThrows(IllegalArgumentException.class, () -> cosFunction.calculate(x, TEST_ACCURACY));
    }

    @ParameterizedTest
    @MethodSource("baseValues")
    void baseValuesWithRealSinFunctionTest(double x) {
        CosFunction cosFunction = new CosFunction(new SinFunction());

        double actual = cosFunction.calculate(x, TEST_ACCURACY);
        double expected = Math.cos(x);

        assertEquals(expected, actual, 1.0E-9);
    }

    @ParameterizedTest
    @MethodSource("largeValues")
    void largeValuesNormalizationWithRealSinFunctionTest(double x) {
        CosFunction cosFunction = new CosFunction(new SinFunction());

        double actual = cosFunction.calculate(x, TEST_ACCURACY);
        double expected = Math.cos(x);

        assertEquals(expected, actual, 1.0E-8);
    }

    private static Stream<Double> baseValues() {
        return Stream.of(
                -Math.PI,
                -Math.PI / 2.0,
                -Math.PI / 3.0,
                0.0,
                Math.PI / 6.0,
                Math.PI / 3.0,
                Math.PI / 2.0,
                Math.PI
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
