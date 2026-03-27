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

class CscFunctionTest {
    private static final double TEST_ACCURACY = 1.0E-12;

    @ParameterizedTest
    @MethodSource("baseValues")
    void baseValuesTest(double x, double sinValue, double expected) {
        AbstractFunction sinFunction = mock(AbstractFunction.class);
        when(sinFunction.calculate(x, TEST_ACCURACY)).thenReturn(sinValue);
        CscFunction cscFunction = new CscFunction(sinFunction);

        double actual = cscFunction.calculate(x, TEST_ACCURACY);

        assertEquals(expected, actual, 1.0E-12);
        verify(sinFunction).calculate(x, TEST_ACCURACY);
        verifyNoMoreInteractions(sinFunction);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0E-6, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void throwsIllegalArgumentExceptionForAccuracyTest(double accuracy) {
        AbstractFunction sinFunction = mock(AbstractFunction.class);
        CscFunction cscFunction = new CscFunction(sinFunction);

        assertThrows(IllegalArgumentException.class, () -> cscFunction.calculate(1.0, accuracy));
        verifyNoInteractions(sinFunction);
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void throwsIllegalArgumentExceptionForInvalidXTest(double x) {
        AbstractFunction sinFunction = mock(AbstractFunction.class);
        CscFunction cscFunction = new CscFunction(sinFunction);

        assertThrows(IllegalArgumentException.class, () -> cscFunction.calculate(x, TEST_ACCURACY));
        verifyNoInteractions(sinFunction);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 3.141592653589793})
    void throwsArithmeticExceptionWhenSinIsZero(double x) {
        AbstractFunction sinFunction = mock(AbstractFunction.class);
        when(sinFunction.calculate(x, TEST_ACCURACY)).thenReturn(0.0);
        CscFunction cscFunction = new CscFunction(sinFunction);

        assertThrows(ArithmeticException.class, () -> cscFunction.calculate(x, TEST_ACCURACY));
        verify(sinFunction).calculate(x, TEST_ACCURACY);
        verifyNoMoreInteractions(sinFunction);
    }

    @ParameterizedTest
    @MethodSource("realBaseValues")
    void baseValuesWithRealFunctionsTest(double x, double expected) {
        CscFunction cscFunction = new CscFunction(new SinFunction());

        double actual = cscFunction.calculate(x, TEST_ACCURACY);

        assertEquals(expected, actual, 1.0E-9);
    }

    @ParameterizedTest
    @MethodSource("largeValues")
    void largeValuesNormalizationWithRealFunctionsTest(double x, double expected) {
        CscFunction cscFunction = new CscFunction(new SinFunction());

        double actual = cscFunction.calculate(x, TEST_ACCURACY);

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
                Arguments.of(-1.0471975511965976, -1.1547005383792517),
                Arguments.of(-0.7853981633974483, -1.4142135623730951),
                Arguments.of(0.7853981633974483, 1.4142135623730951),
                Arguments.of(1.0471975511965976, 1.1547005383792517)
        );
    }

    private static Stream<Arguments> largeValues() {
        return Stream.of(
                Arguments.of(3141593.1771885687, 2.00000000030563),
                Arguments.of(-3141591.8681916296, 1.4142135621004311),
                Arguments.of(3878507.664761941, -1.1547005387239148)
        );
    }
}
