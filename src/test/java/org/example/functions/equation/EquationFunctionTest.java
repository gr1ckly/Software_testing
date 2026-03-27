package org.example.functions.equation;

import org.example.functions.AbstractFunction;
import org.example.functions.ln.LnFunction;
import org.example.functions.ln.LogFunction;
import org.example.functions.trig.CosFunction;
import org.example.functions.trig.CotFunction;
import org.example.functions.trig.CscFunction;
import org.example.functions.trig.SecFunction;
import org.example.functions.trig.SinFunction;
import org.example.functions.trig.TanFunction;
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

class EquationFunctionTest {
    private static final double TEST_ACCURACY = 1.0E-12;

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0E-6, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void throwsIllegalArgumentExceptionForAccuracyTest(double accuracy) {
        AbstractFunction sinFunction = mock(AbstractFunction.class);
        AbstractFunction cosFunction = mock(AbstractFunction.class);
        AbstractFunction tanFunction = mock(AbstractFunction.class);
        AbstractFunction cotFunction = mock(AbstractFunction.class);
        AbstractFunction secFunction = mock(AbstractFunction.class);
        AbstractFunction cscFunction = mock(AbstractFunction.class);
        AbstractFunction lnFunction = mock(AbstractFunction.class);
        AbstractFunction log2Function = mock(AbstractFunction.class);
        AbstractFunction log5Function = mock(AbstractFunction.class);
        AbstractFunction log10Function = mock(AbstractFunction.class);
        EquationFunction equationFunction = new EquationFunction(
                sinFunction,
                cosFunction,
                tanFunction,
                cotFunction,
                secFunction,
                cscFunction,
                lnFunction,
                log2Function,
                log5Function,
                log10Function
        );

        assertThrows(IllegalArgumentException.class, () -> equationFunction.calculate(1.0, accuracy));
        verifyNoInteractions(
                sinFunction, cosFunction, tanFunction, cotFunction, secFunction, cscFunction,
                lnFunction, log2Function, log5Function, log10Function
        );
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void throwsIllegalArgumentExceptionForInvalidXTest(double x) {
        AbstractFunction sinFunction = mock(AbstractFunction.class);
        AbstractFunction cosFunction = mock(AbstractFunction.class);
        AbstractFunction tanFunction = mock(AbstractFunction.class);
        AbstractFunction cotFunction = mock(AbstractFunction.class);
        AbstractFunction secFunction = mock(AbstractFunction.class);
        AbstractFunction cscFunction = mock(AbstractFunction.class);
        AbstractFunction lnFunction = mock(AbstractFunction.class);
        AbstractFunction log2Function = mock(AbstractFunction.class);
        AbstractFunction log5Function = mock(AbstractFunction.class);
        AbstractFunction log10Function = mock(AbstractFunction.class);
        EquationFunction equationFunction = new EquationFunction(
                sinFunction,
                cosFunction,
                tanFunction,
                cotFunction,
                secFunction,
                cscFunction,
                lnFunction,
                log2Function,
                log5Function,
                log10Function
        );

        assertThrows(IllegalArgumentException.class, () -> equationFunction.calculate(x, TEST_ACCURACY));
        verifyNoInteractions(
                sinFunction, cosFunction, tanFunction, cotFunction, secFunction, cscFunction,
                lnFunction, log2Function, log5Function, log10Function
        );
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.0})
    void throwsArithmeticExceptionWhenDenominatorIsZeroWithMocks(double x) {
        AbstractFunction sinFunction = mock(AbstractFunction.class);
        AbstractFunction cosFunction = mock(AbstractFunction.class);
        AbstractFunction tanFunction = mock(AbstractFunction.class);
        AbstractFunction cotFunction = mock(AbstractFunction.class);
        AbstractFunction secFunction = mock(AbstractFunction.class);
        AbstractFunction cscFunction = mock(AbstractFunction.class);
        when(sinFunction.calculate(x, TEST_ACCURACY)).thenReturn(1.0);
        when(cosFunction.calculate(x, TEST_ACCURACY)).thenReturn(1.0);
        when(tanFunction.calculate(x, TEST_ACCURACY)).thenReturn(1.0);
        when(cotFunction.calculate(x, TEST_ACCURACY)).thenReturn(2.0);
        when(secFunction.calculate(x, TEST_ACCURACY)).thenReturn(1.0);
        when(cscFunction.calculate(x, TEST_ACCURACY)).thenReturn(1.0);
        EquationFunction equationFunction = new EquationFunction(
                sinFunction,
                cosFunction,
                tanFunction,
                cotFunction,
                secFunction,
                cscFunction,
                mock(AbstractFunction.class),
                mock(AbstractFunction.class),
                mock(AbstractFunction.class),
                mock(AbstractFunction.class)
        );

        assertThrows(ArithmeticException.class, () -> equationFunction.calculate(x, TEST_ACCURACY));
        var inOrder = inOrder(sinFunction, cosFunction, tanFunction, cotFunction, secFunction, cscFunction);
        inOrder.verify(sinFunction).calculate(x, TEST_ACCURACY);
        inOrder.verify(cosFunction).calculate(x, TEST_ACCURACY);
        inOrder.verify(tanFunction).calculate(x, TEST_ACCURACY);
        inOrder.verify(cotFunction).calculate(x, TEST_ACCURACY);
        inOrder.verify(secFunction).calculate(x, TEST_ACCURACY);
        inOrder.verify(cscFunction).calculate(x, TEST_ACCURACY);
        verifyNoMoreInteractions(sinFunction, cosFunction, tanFunction, cotFunction, secFunction, cscFunction);
    }

    @ParameterizedTest
    @MethodSource("mockNonPositiveBranchValues")
    void baseValuesWithMocksForNonPositiveBranchTest(
            double x,
            double sin,
            double cos,
            double tan,
            double cot,
            double sec,
            double csc,
            double expected
    ) {
        AbstractFunction sinFunction = mock(AbstractFunction.class);
        AbstractFunction cosFunction = mock(AbstractFunction.class);
        AbstractFunction tanFunction = mock(AbstractFunction.class);
        AbstractFunction cotFunction = mock(AbstractFunction.class);
        AbstractFunction secFunction = mock(AbstractFunction.class);
        AbstractFunction cscFunction = mock(AbstractFunction.class);
        AbstractFunction lnFunction = mock(AbstractFunction.class);
        AbstractFunction log2Function = mock(AbstractFunction.class);
        AbstractFunction log5Function = mock(AbstractFunction.class);
        AbstractFunction log10Function = mock(AbstractFunction.class);
        when(sinFunction.calculate(x, TEST_ACCURACY)).thenReturn(sin);
        when(cosFunction.calculate(x, TEST_ACCURACY)).thenReturn(cos);
        when(tanFunction.calculate(x, TEST_ACCURACY)).thenReturn(tan);
        when(cotFunction.calculate(x, TEST_ACCURACY)).thenReturn(cot);
        when(secFunction.calculate(x, TEST_ACCURACY)).thenReturn(sec);
        when(cscFunction.calculate(x, TEST_ACCURACY)).thenReturn(csc);

        EquationFunction equationFunction = new EquationFunction(
                sinFunction,
                cosFunction,
                tanFunction,
                cotFunction,
                secFunction,
                cscFunction,
                lnFunction,
                log2Function,
                log5Function,
                log10Function
        );

        double actual = equationFunction.calculate(x, TEST_ACCURACY);

        assertEquals(expected, actual, 1.0E-12);
        var inOrder = inOrder(sinFunction, cosFunction, tanFunction, cotFunction, secFunction, cscFunction);
        inOrder.verify(sinFunction).calculate(x, TEST_ACCURACY);
        inOrder.verify(cosFunction).calculate(x, TEST_ACCURACY);
        inOrder.verify(tanFunction).calculate(x, TEST_ACCURACY);
        inOrder.verify(cotFunction).calculate(x, TEST_ACCURACY);
        inOrder.verify(secFunction).calculate(x, TEST_ACCURACY);
        inOrder.verify(cscFunction).calculate(x, TEST_ACCURACY);
        verifyNoInteractions(lnFunction, log2Function, log5Function, log10Function);
        verifyNoMoreInteractions(sinFunction, cosFunction, tanFunction, cotFunction, secFunction, cscFunction);
    }

    @ParameterizedTest
    @ValueSource(doubles = {1.0})
    void baseValuesWithMocksForPositiveBranchTest(double x) {
        AbstractFunction lnFunction = mock(AbstractFunction.class);
        AbstractFunction log2Function = mock(AbstractFunction.class);
        AbstractFunction log5Function = mock(AbstractFunction.class);
        AbstractFunction log10Function = mock(AbstractFunction.class);
        when(lnFunction.calculate(x, TEST_ACCURACY)).thenReturn(1.0);
        when(log2Function.calculate(x, TEST_ACCURACY)).thenReturn(2.0);
        when(log5Function.calculate(x, TEST_ACCURACY)).thenReturn(3.0);
        when(log10Function.calculate(x, TEST_ACCURACY)).thenReturn(4.0);

        EquationFunction equationFunction = new EquationFunction(
                mock(AbstractFunction.class),
                mock(AbstractFunction.class),
                mock(AbstractFunction.class),
                mock(AbstractFunction.class),
                mock(AbstractFunction.class),
                mock(AbstractFunction.class),
                lnFunction,
                log2Function,
                log5Function,
                log10Function
        );

        double actual = equationFunction.calculate(x, TEST_ACCURACY);

        assertEquals(9.0, actual, 1.0E-12);
        var inOrder = inOrder(lnFunction, log2Function, log5Function, log10Function);
        inOrder.verify(lnFunction).calculate(x, TEST_ACCURACY);
        inOrder.verify(log2Function).calculate(x, TEST_ACCURACY);
        inOrder.verify(log5Function).calculate(x, TEST_ACCURACY);
        inOrder.verify(log10Function).calculate(x, TEST_ACCURACY);
        verifyNoMoreInteractions(lnFunction, log2Function, log5Function, log10Function);
    }

    @ParameterizedTest
    @MethodSource("realNonPositiveBranchValues")
    void baseValuesWithRealFunctionsForNonPositiveBranchTest(double x, double expected) {
        SinFunction sinFunction = new SinFunction();
        CosFunction cosFunction = new CosFunction(sinFunction);
        LnFunction lnFunction = new LnFunction();
        EquationFunction equationFunction = new EquationFunction(
                sinFunction,
                cosFunction,
                new TanFunction(sinFunction, cosFunction),
                new CotFunction(sinFunction, cosFunction),
                new SecFunction(cosFunction),
                new CscFunction(sinFunction),
                lnFunction,
                new LogFunction(2.0, lnFunction),
                new LogFunction(5.0, lnFunction),
                new LogFunction(10.0, lnFunction)
        );

        double actual = equationFunction.calculate(x, TEST_ACCURACY);

        assertEquals(expected, actual, 1.0E-8);
    }

    @ParameterizedTest
    @MethodSource("realPositiveBranchValues")
    void baseValuesWithRealFunctionsForPositiveBranchTest(double x, double expected) {
        SinFunction sinFunction = new SinFunction();
        CosFunction cosFunction = new CosFunction(sinFunction);
        LnFunction lnFunction = new LnFunction();
        EquationFunction equationFunction = new EquationFunction(
                sinFunction,
                cosFunction,
                new TanFunction(sinFunction, cosFunction),
                new CotFunction(sinFunction, cosFunction),
                new SecFunction(cosFunction),
                new CscFunction(sinFunction),
                lnFunction,
                new LogFunction(2.0, lnFunction),
                new LogFunction(5.0, lnFunction),
                new LogFunction(10.0, lnFunction)
        );

        double actual = equationFunction.calculate(x, TEST_ACCURACY);

        assertEquals(expected, actual, 1.0E-8);
    }

    private static Stream<Arguments> mockNonPositiveBranchValues() {
        return Stream.of(
                Arguments.of(-1.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, -8606512.379562045)
        );
    }

    private static Stream<Arguments> realNonPositiveBranchValues() {
        return Stream.of(
                Arguments.of(-1.0, -44.648320763397706)
        );
    }

    private static Stream<Arguments> realPositiveBranchValues() {
        return Stream.of(
                Arguments.of(1.0, 0.0),
                Arguments.of(2.0, -0.6289808678171918),
                Arguments.of(5.0, -1.2634948572345677)
        );
    }
}
