package org.example.functions.trig;

import org.example.functions.AbstractFunction;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class TanFunctionTest {
    private static final double TEST_ACCURACY = 1.0E-12;

    @ParameterizedTest
    @MethodSource("baseValues")
    void baseValuesTest(double x, double sinValue, double cosValue, double expected) {
        AbstractFunction sinFunction = mock(AbstractFunction.class);
        AbstractFunction cosFunction = mock(AbstractFunction.class);
        when(sinFunction.calculate(x, TEST_ACCURACY)).thenReturn(sinValue);
        when(cosFunction.calculate(x, TEST_ACCURACY)).thenReturn(cosValue);
        TanFunction tanFunction = new TanFunction(sinFunction, cosFunction);

        double actual = tanFunction.calculate(x, TEST_ACCURACY);

        assertEquals(expected, actual, 1.0E-12);
        var inOrder = inOrder(cosFunction, sinFunction);
        inOrder.verify(cosFunction).calculate(x, TEST_ACCURACY);
        inOrder.verify(sinFunction).calculate(x, TEST_ACCURACY);
        verifyNoMoreInteractions(cosFunction, sinFunction);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0E-6, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void throwsIllegalArgumentExceptionForAccuracyTest(double accuracy) {
        AbstractFunction sinFunction = mock(AbstractFunction.class);
        AbstractFunction cosFunction = mock(AbstractFunction.class);
        TanFunction tanFunction = new TanFunction(sinFunction, cosFunction);

        assertThrows(IllegalArgumentException.class, () -> tanFunction.calculate(1.0, accuracy));
        verifyNoInteractions(sinFunction, cosFunction);
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void throwsIllegalArgumentExceptionForInvalidXTest(double x) {
        AbstractFunction sinFunction = mock(AbstractFunction.class);
        AbstractFunction cosFunction = mock(AbstractFunction.class);
        TanFunction tanFunction = new TanFunction(sinFunction, cosFunction);

        assertThrows(IllegalArgumentException.class, () -> tanFunction.calculate(x, TEST_ACCURACY));
        verifyNoInteractions(sinFunction, cosFunction);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 3.141592653589793})
    void throwsArithmeticExceptionWhenCosIsZero(double x) {
        AbstractFunction sinFunction = mock(AbstractFunction.class);
        AbstractFunction cosFunction = mock(AbstractFunction.class);
        when(cosFunction.calculate(x, TEST_ACCURACY)).thenReturn(0.0);
        TanFunction tanFunction = new TanFunction(sinFunction, cosFunction);

        assertThrows(ArithmeticException.class, () -> tanFunction.calculate(x, TEST_ACCURACY));
        var inOrder = inOrder(cosFunction, sinFunction);
        inOrder.verify(cosFunction).calculate(x, TEST_ACCURACY);
        verifyNoInteractions(sinFunction);
        verifyNoMoreInteractions(cosFunction, sinFunction);
    }

    @ParameterizedTest
    @MethodSource("realBaseValues")
    void baseValuesWithRealFunctionsTest(double x, double expected) {
        TanFunction tanFunction = new TanFunction(new SinFunction(), new CosFunction(new SinFunction()));

        double actual = tanFunction.calculate(x, TEST_ACCURACY);

        assertEquals(expected, actual, 1.0E-9);
    }

    @ParameterizedTest
    @MethodSource("largeValues")
    void largeValuesNormalizationWithRealFunctionsTest(double x, double expected) {
        TanFunction tanFunction = new TanFunction(new SinFunction(), new CosFunction(new SinFunction()));

        double actual = tanFunction.calculate(x, TEST_ACCURACY);

        assertEquals(expected, actual, 1.0E-8);
    }

    private static Stream<Arguments> baseValues() {
        return Stream.of(
                Arguments.of(1.0, 3.0, 4.0, 0.75),
                Arguments.of(2.0, -2.0, 5.0, -0.4)
        );
    }

    private static Stream<Arguments> realBaseValues() {
        return Stream.of(
                Arguments.of(-0.7853981633974483, -0.9999999999999999),
                Arguments.of(-0.5235987755982988, -0.5773502691896256),
                Arguments.of(0.5235987755982988, 0.5773502691896256),
                Arguments.of(0.7853981633974483, 0.9999999999999999)
        );
    }

    private static Stream<Arguments> largeValues() {
        return Stream.of(
                Arguments.of(3141593.1771885687, 0.5773502691806923),
                Arguments.of(-3141591.8681916296, 1.0000000003856128),
                Arguments.of(3878507.664761941, 1.732050805992277)
        );
    }
}
