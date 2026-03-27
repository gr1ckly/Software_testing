package org.example.functions.ln;

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

class LogFunctionTest {
    private static final double TEST_ACCURACY = 1.0E-12;

    @ParameterizedTest
    @MethodSource("baseValues")
    void baseValuesTest(double base, double x, double lnBase, double lnX, double expected) {
        AbstractFunction lnFunction = mock(AbstractFunction.class);
        when(lnFunction.calculate(base, TEST_ACCURACY)).thenReturn(lnBase);
        when(lnFunction.calculate(x, TEST_ACCURACY)).thenReturn(lnX);
        LogFunction logFunction = new LogFunction(base, lnFunction);

        double actual = logFunction.calculate(x, TEST_ACCURACY);

        assertEquals(expected, actual, 1.0E-12);
        var inOrder = inOrder(lnFunction);
        inOrder.verify(lnFunction).calculate(x, TEST_ACCURACY);
        inOrder.verify(lnFunction).calculate(base, TEST_ACCURACY);
        verifyNoMoreInteractions(lnFunction);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0, 1.0, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void constructorThrowsIllegalArgumentExceptionForInvalidBase(double base) {
        assertThrows(IllegalArgumentException.class, () -> new LogFunction(base, mock(AbstractFunction.class)));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0E-6, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void throwsIllegalArgumentExceptionForAccuracyTest(double accuracy) {
        AbstractFunction lnFunction = mock(AbstractFunction.class);
        LogFunction logFunction = new LogFunction(5.0, lnFunction);

        assertThrows(IllegalArgumentException.class, () -> logFunction.calculate(25.0, accuracy));
        verifyNoInteractions(lnFunction);
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void throwsIllegalArgumentExceptionForInvalidXTest(double x) {
        AbstractFunction lnFunction = mock(AbstractFunction.class);
        LogFunction logFunction = new LogFunction(5.0, lnFunction);

        assertThrows(IllegalArgumentException.class, () -> logFunction.calculate(x, TEST_ACCURACY));
        verifyNoInteractions(lnFunction);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0, -10.0})
    void throwsArithmeticExceptionForNonPositiveXTest(double x) {
        AbstractFunction lnFunction = mock(AbstractFunction.class);
        LogFunction logFunction = new LogFunction(5.0, lnFunction);

        assertThrows(ArithmeticException.class, () -> logFunction.calculate(x, TEST_ACCURACY));
        verifyNoInteractions(lnFunction);
    }

    @ParameterizedTest
    @MethodSource("realBaseValues")
    void baseValuesWithRealLnFunctionTest(double base, double x, double expected) {
        LogFunction logFunction = new LogFunction(base, new LnFunction());

        double actual = logFunction.calculate(x, TEST_ACCURACY);

        assertEquals(expected, actual, 1.0E-9);
    }

    @ParameterizedTest
    @MethodSource("largeValues")
    void largeValuesWithRealLnFunctionTest(double base, double x, double expected) {
        LogFunction logFunction = new LogFunction(base, new LnFunction());

        double actual = logFunction.calculate(x, TEST_ACCURACY);

        assertEquals(expected, actual, 1.0E-9);
    }

    private static Stream<Arguments> baseValues() {
        return Stream.of(
                Arguments.of(5.0, 25.0, 1.6094379124341003, 3.2188758248682006, 2.0),
                Arguments.of(2.0, 8.0, 0.6931471805599453, 2.0794415416798357, 3.0)
        );
    }

    private static Stream<Arguments> realBaseValues() {
        return Stream.of(
                Arguments.of(2.0, 8.0, 3.0),
                Arguments.of(5.0, 25.0, 2.0),
                Arguments.of(10.0, 1000.0, 2.9999999999999996)
        );
    }

    private static Stream<Arguments> largeValues() {
        return Stream.of(
                Arguments.of(2.0, 1.0E12, 39.86313713864835),
                Arguments.of(5.0, 1.0E100, 143.06765580733932),
                Arguments.of(10.0, 8.988465674311579E307, 307.95368556425274)
        );
    }
}
