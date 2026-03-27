package org.example.functions.trig;

import org.example.functions.AbstractFunction;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class SecFunctionTest {
    private static final double TEST_ACCURACY = 1.0E-12;

    @ParameterizedTest
    @MethodSource("baseValues")
    void baseValuesTest(double x, double cosValue, double expected) {
        AbstractFunction cosFunction = mock(AbstractFunction.class);
        when(cosFunction.calculate(x, TEST_ACCURACY)).thenReturn(cosValue);
        SecFunction secFunction = new SecFunction(cosFunction);

        double actual = secFunction.calculate(x, TEST_ACCURACY);

        assertEquals(expected, actual, 1.0E-12);
        verify(cosFunction).calculate(x, TEST_ACCURACY);
        verifyNoMoreInteractions(cosFunction);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0E-6, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void throwsIllegalArgumentExceptionForAccuracyTest(double accuracy) {
        AbstractFunction cosFunction = mock(AbstractFunction.class);
        SecFunction secFunction = new SecFunction(cosFunction);

        assertThrows(IllegalArgumentException.class, () -> secFunction.calculate(1.0, accuracy));
        verifyNoInteractions(cosFunction);
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void throwsIllegalArgumentExceptionForInvalidXTest(double x) {
        AbstractFunction cosFunction = mock(AbstractFunction.class);
        SecFunction secFunction = new SecFunction(cosFunction);

        assertThrows(IllegalArgumentException.class, () -> secFunction.calculate(x, TEST_ACCURACY));
        verifyNoInteractions(cosFunction);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 1.5707963267948966})
    void throwsArithmeticExceptionWhenCosIsZero(double x) {
        AbstractFunction cosFunction = mock(AbstractFunction.class);
        when(cosFunction.calculate(x, TEST_ACCURACY)).thenReturn(0.0);
        SecFunction secFunction = new SecFunction(cosFunction);

        assertThrows(ArithmeticException.class, () -> secFunction.calculate(x, TEST_ACCURACY));
        verify(cosFunction).calculate(x, TEST_ACCURACY);
        verifyNoMoreInteractions(cosFunction);
    }

    @ParameterizedTest
    @MethodSource("realBaseValues")
    void baseValuesWithRealFunctionsTest(double x, double expected) {
        SecFunction secFunction = new SecFunction(new CosFunction(new SinFunction()));

        double actual = secFunction.calculate(x, TEST_ACCURACY);

        assertEquals(expected, actual, 1.0E-9);
    }

    @ParameterizedTest
    @MethodSource("largeValues")
    void largeValuesNormalizationWithRealFunctionsTest(double x, double expected) {
        SecFunction secFunction = new SecFunction(new CosFunction(new SinFunction()));

        double actual = secFunction.calculate(x, TEST_ACCURACY);

        assertEquals(expected, actual, 1.0E-8);
    }

    private static Stream<Arguments> baseValues() {
        return Stream.of(
                Arguments.of(1.0, 0.5, 2.0),
                Arguments.of(2.0, -0.25, -4.0)
        );
    }

    private static Stream<Arguments> realBaseValues() {
        return Stream.of(
                Arguments.of(-1.0471975511965976, 1.9999999999999996),
                Arguments.of(-0.7853981633974483, 1.414213562373095),
                Arguments.of(0.7853981633974483, 1.414213562373095),
                Arguments.of(1.0471975511965976, 1.9999999999999996)
        );
    }

    private static Stream<Arguments> largeValues() {
        return Stream.of(
                Arguments.of(3141593.1771885687, 1.1547005383204202),
                Arguments.of(-3141591.8681916296, 1.4142135625752118),
                Arguments.of(3878507.664761941, -1.999999998209192)
        );
    }
}
