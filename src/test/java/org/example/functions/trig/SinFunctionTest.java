package org.example.functions.trig;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
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
    void baseValuesTest(double x, double expected) {
        double actual = sinFunction.calculate(x, TEST_ACCURACY);

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
    @MethodSource("accuracyValues")
    void accuracyTest(double x, double expected) {
        double looseAccuracyResult = sinFunction.calculate(x, 1.0E-1);
        double tightAccuracyResult = sinFunction.calculate(x, 1.0E-12);

        double looseError = Math.abs(looseAccuracyResult - expected);
        double tightError = Math.abs(tightAccuracyResult - expected);

        assertTrue(Math.abs(looseAccuracyResult - tightAccuracyResult) > 0.0);
        assertTrue(tightError < looseError);
    }

    @ParameterizedTest
    @MethodSource("largeValues")
    void largeValuesNormalizationTest(double x, double expected) {
        double actual = sinFunction.calculate(x, TEST_ACCURACY);

        assertEquals(expected, actual, 1.0E-8);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.125, -0.125})
    void returnsInitialTermWhenAbsoluteXEqualsAccuracy(double x) {
        double actual = sinFunction.calculate(x, Math.abs(x));

        assertEquals(x, actual, 0.0);
    }

    private static Stream<Arguments> baseValues() {
        return Stream.of(
                Arguments.of(-6.283185307179586, 2.4492935982947064E-16),
                Arguments.of(-3.141592653589793, -1.2246467991473532E-16),
                Arguments.of(-1.5707963267948966, -1.0),
                Arguments.of(-1.0471975511965976, -0.8660254037844386),
                Arguments.of(-0.5235987755982988, -0.49999999999999994),
                Arguments.of(0.0, 0.0),
                Arguments.of(0.5235987755982988, 0.49999999999999994),
                Arguments.of(1.0471975511965976, 0.8660254037844386),
                Arguments.of(1.5707963267948966, 1.0),
                Arguments.of(3.141592653589793, 1.2246467991473532E-16),
                Arguments.of(6.283185307179586, -2.4492935982947064E-16),
                Arguments.of(10.0, -0.5440211108893698)
        );
    }

    private static Stream<Arguments> accuracyValues() {
        return Stream.of(
                Arguments.of(1.0, 0.8414709848078965),
                Arguments.of(2.5, 0.5984721441039564)
        );
    }

    private static Stream<Arguments> largeValues() {
        return Stream.of(
                Arguments.of(3141593.1771885687, 0.4999999999235925),
                Arguments.of(-3141591.8681916296, 0.7071067813228817),
                Arguments.of(3878507.664761941, -0.8660254035259578)
        );
    }
}
