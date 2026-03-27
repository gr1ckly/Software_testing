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

class CosFunctionTest {
    private static final double TEST_ACCURACY = 1.0E-12;

    @ParameterizedTest
    @MethodSource("baseValues")
    void baseValuesTest(double x, double transformedX, double transformedSin, double expected) {
        AbstractFunction sinFunction = mock(AbstractFunction.class);
        when(sinFunction.calculate(transformedX, TEST_ACCURACY)).thenReturn(transformedSin);
        CosFunction cosFunction = new CosFunction(sinFunction);

        double actual = cosFunction.calculate(x, TEST_ACCURACY);

        assertEquals(expected, actual, 1.0E-12);
        verify(sinFunction).calculate(transformedX, TEST_ACCURACY);
        verifyNoMoreInteractions(sinFunction);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0E-6, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void throwsIllegalArgumentExceptionForAccuracyTest(double accuracy) {
        AbstractFunction sinFunction = mock(AbstractFunction.class);
        CosFunction cosFunction = new CosFunction(sinFunction);

        assertThrows(IllegalArgumentException.class, () -> cosFunction.calculate(0.0, accuracy));
        verifyNoInteractions(sinFunction);
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void throwsIllegalArgumentExceptionForInvalidXTest(double x) {
        AbstractFunction sinFunction = mock(AbstractFunction.class);
        CosFunction cosFunction = new CosFunction(sinFunction);

        assertThrows(IllegalArgumentException.class, () -> cosFunction.calculate(x, TEST_ACCURACY));
        verifyNoInteractions(sinFunction);
    }

    @ParameterizedTest
    @MethodSource("baseValues")
    void baseValuesWithRealSinFunctionTest(double x, double transformedX, double transformedSin, double expected) {
        CosFunction cosFunction = new CosFunction(new SinFunction());

        double actual = cosFunction.calculate(x, TEST_ACCURACY);

        assertEquals(expected, actual, 1.0E-9);
    }

    @ParameterizedTest
    @MethodSource("largeValues")
    void largeValuesNormalizationWithRealSinFunctionTest(double x, double expected) {
        CosFunction cosFunction = new CosFunction(new SinFunction());

        double actual = cosFunction.calculate(x, TEST_ACCURACY);

        assertEquals(expected, actual, 1.0E-8);
    }

    private static Stream<Arguments> baseValues() {
        return Stream.of(
                Arguments.of(-3.141592653589793, 4.71238898038469, -1.0, -1.0),
                Arguments.of(-1.5707963267948966, 3.141592653589793, 1.2246467991473532E-16, 6.123233995736766E-17),
                Arguments.of(-1.0471975511965976, 2.617993877991494, 0.5000000000000003, 0.5000000000000001),
                Arguments.of(0.0, 1.5707963267948966, 1.0, 1.0),
                Arguments.of(0.5235987755982988, 1.0471975511965979, 0.8660254037844387, 0.8660254037844387),
                Arguments.of(1.0471975511965976, 0.5235987755982989, 0.5, 0.5000000000000001),
                Arguments.of(1.5707963267948966, 0.0, 0.0, 6.123233995736766E-17),
                Arguments.of(3.141592653589793, -1.5707963267948966, -1.0, -1.0)
        );
    }

    private static Stream<Arguments> largeValues() {
        return Stream.of(
                Arguments.of(3141593.1771885687, 0.8660254038285525),
                Arguments.of(-3141591.8681916296, 0.7071067810502133),
                Arguments.of(3878507.664761941, -0.500000000447702)
        );
    }
}
