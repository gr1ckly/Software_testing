package org.example.functions.ln;

import org.example.test_helper.TestStubFunction;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LogFunctionTest {
    private static final double TEST_ACCURACY = 1.0E-12;

    @ParameterizedTest
    @MethodSource("baseValues")
    void baseValuesTest(double base, double x, Map<Double, Double> lnValues, double expected) {
        LogFunction logFunction = new LogFunction(base, new TestStubFunction(lnValues));

        double actual = logFunction.calculate(x, TEST_ACCURACY);

        assertEquals(expected, actual, 1.0E-12);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0, 1.0, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void constructorThrowsIllegalArgumentExceptionForInvalidBase(double base) {
        assertThrows(IllegalArgumentException.class, () -> new LogFunction(base, new TestStubFunction(Map.of(2.0, Math.log(2.0)))));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0E-6, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void throwsIllegalArgumentExceptionForAccuracyTest(double accuracy) {
        LogFunction logFunction = new LogFunction(5.0, new TestStubFunction(Map.of(
                5.0, Math.log(5.0),
                25.0, Math.log(25.0)
        )));

        assertThrows(IllegalArgumentException.class, () -> logFunction.calculate(25.0, accuracy));
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void throwsIllegalArgumentExceptionForInvalidXTest(double x) {
        LogFunction logFunction = new LogFunction(5.0, new TestStubFunction(Map.of(
                5.0, Math.log(5.0),
                25.0, Math.log(25.0)
        )));

        assertThrows(IllegalArgumentException.class, () -> logFunction.calculate(x, TEST_ACCURACY));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0, -10.0})
    void throwsArithmeticExceptionForNonPositiveXTest(double x) {
        LogFunction logFunction = new LogFunction(5.0, new TestStubFunction(Map.of(
                5.0, Math.log(5.0),
                25.0, Math.log(25.0)
        )));

        assertThrows(ArithmeticException.class, () -> logFunction.calculate(x, TEST_ACCURACY));
    }

    @ParameterizedTest
    @MethodSource("realBaseValues")
    void baseValuesWithRealLnFunctionTest(double base, double x) {
        LogFunction logFunction = new LogFunction(base, new LnFunction());

        double actual = logFunction.calculate(x, TEST_ACCURACY);
        double expected = Math.log(x) / Math.log(base);

        assertEquals(expected, actual, 1.0E-9);
    }

    @ParameterizedTest
    @MethodSource("largeValues")
    void largeValuesWithRealLnFunctionTest(double base, double x) {
        LogFunction logFunction = new LogFunction(base, new LnFunction());

        double actual = logFunction.calculate(x, TEST_ACCURACY);
        double expected = Math.log(x) / Math.log(base);

        assertEquals(expected, actual, 1.0E-9);
    }

    private static Stream<Arguments> baseValues() {
        return Stream.of(
                Arguments.of(
                        5.0,
                        25.0,
                        Map.of(5.0, Math.log(5.0), 25.0, Math.log(25.0)),
                        Math.log(25.0) / Math.log(5.0)
                ),
                Arguments.of(
                        2.0,
                        8.0,
                        Map.of(2.0, Math.log(2.0), 8.0, Math.log(8.0)),
                        Math.log(8.0) / Math.log(2.0)
                )
        );
    }

    private static Stream<Arguments> realBaseValues() {
        return Stream.of(
                Arguments.of(2.0, 8.0),
                Arguments.of(5.0, 25.0),
                Arguments.of(10.0, 1000.0)
        );
    }

    private static Stream<Arguments> largeValues() {
        return Stream.of(
                Arguments.of(2.0, 1.0E12),
                Arguments.of(5.0, 1.0E100),
                Arguments.of(10.0, Double.MAX_VALUE / 2.0)
        );
    }
}
