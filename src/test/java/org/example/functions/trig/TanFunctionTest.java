package org.example.functions.trig;

import org.example.test_helper.TestStubFunction;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TanFunctionTest {
    private static final double TEST_ACCURACY = 1.0E-12;

    @ParameterizedTest
    @MethodSource("baseValues")
    void baseValuesTest(double x, double sinValue, double cosValue, double expected) {
        TanFunction tanFunction = new TanFunction(
                new TestStubFunction(Map.of(x, sinValue)),
                new TestStubFunction(Map.of(x, cosValue))
        );

        double actual = tanFunction.calculate(x, TEST_ACCURACY);

        assertEquals(expected, actual, 1.0E-12);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0E-6, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void throwsIllegalArgumentExceptionForAccuracyTest(double accuracy) {
        TanFunction tanFunction = new TanFunction(
                new TestStubFunction(Map.of(1.0, 3.0)),
                new TestStubFunction(Map.of(1.0, 4.0))
        );

        assertThrows(IllegalArgumentException.class, () -> tanFunction.calculate(1.0, accuracy));
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void throwsIllegalArgumentExceptionForInvalidXTest(double x) {
        TanFunction tanFunction = new TanFunction(
                new TestStubFunction(Map.of(1.0, 3.0)),
                new TestStubFunction(Map.of(1.0, 4.0))
        );

        assertThrows(IllegalArgumentException.class, () -> tanFunction.calculate(x, TEST_ACCURACY));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, Math.PI})
    void throwsArithmeticExceptionWhenCosIsZero(double x) {
        TanFunction tanFunction = new TanFunction(
                new TestStubFunction(Map.of(x, 1.0)),
                new TestStubFunction(Map.of(x, 0.0))
        );

        assertThrows(ArithmeticException.class, () -> tanFunction.calculate(x, TEST_ACCURACY));
    }

    @ParameterizedTest
    @MethodSource("realBaseValues")
    void baseValuesWithRealFunctionsTest(double x) {
        TanFunction tanFunction = new TanFunction(new SinFunction(), new CosFunction(new SinFunction()));

        double actual = tanFunction.calculate(x, TEST_ACCURACY);
        double expected = Math.tan(x);

        assertEquals(expected, actual, 1.0E-9);
    }

    @ParameterizedTest
    @MethodSource("largeValues")
    void largeValuesNormalizationWithRealFunctionsTest(double x) {
        TanFunction tanFunction = new TanFunction(new SinFunction(), new CosFunction(new SinFunction()));

        double actual = tanFunction.calculate(x, TEST_ACCURACY);
        double expected = Math.tan(x);

        assertEquals(expected, actual, 1.0E-8);
    }

    private static Stream<Arguments> baseValues() {
        return Stream.of(
                Arguments.of(1.0, 3.0, 4.0, 0.75),
                Arguments.of(2.0, -2.0, 5.0, -0.4)
        );
    }

    private static Stream<Double> realBaseValues() {
        return Stream.of(-Math.PI / 4.0, -Math.PI / 6.0, Math.PI / 6.0, Math.PI / 4.0);
    }

    private static Stream<Double> largeValues() {
        return Stream.of(
                1.0E6 * Math.PI + Math.PI / 6.0,
                -1.0E6 * Math.PI + Math.PI / 4.0,
                1234567.0 * Math.PI + Math.PI / 3.0
        );
    }
}
