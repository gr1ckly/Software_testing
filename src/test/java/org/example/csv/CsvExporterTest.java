package org.example.csv;

import org.example.functions.AbstractFunction;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class CsvExporterTest {
    private static final double TEST_ACCURACY = 1.0E-12;

    @Test
    void writeCsvWritesExpectedOutput() throws Exception {
        StringWriter writer = new StringWriter();
        CsvExporter csvExporter = new CsvExporter(writer, ";");
        AbstractFunction function = mock(AbstractFunction.class);
        when(function.calculate(1.0, TEST_ACCURACY)).thenReturn(10.5);
        when(function.calculate(2.0, TEST_ACCURACY)).thenReturn(-3.25);
        when(function.calculate(3.0, TEST_ACCURACY)).thenReturn(0.0);

        csvExporter.writeCsv(TEST_ACCURACY, 1.0, 3.0, 1.0, function);

        assertEquals(
                "1.0;10.5" + System.lineSeparator() +
                        "2.0;-3.25" + System.lineSeparator() +
                        "3.0;0.0" + System.lineSeparator(),
                writer.toString()
        );
        var inOrder = inOrder(function);
        inOrder.verify(function).calculate(1.0, TEST_ACCURACY);
        inOrder.verify(function).calculate(2.0, TEST_ACCURACY);
        inOrder.verify(function).calculate(3.0, TEST_ACCURACY);
        verifyNoMoreInteractions(function);
    }

    @Test
    void writeCsvThrowsIllegalArgumentExceptionForInvalidAccuracy() {
        StringWriter writer = new StringWriter();
        CsvExporter csvExporter = new CsvExporter(writer, ";");
        AbstractFunction function = mock(AbstractFunction.class);

        assertThrows(IllegalArgumentException.class, () -> csvExporter.writeCsv(0.0, 1.0, 1.0, 1.0, function));
        verifyNoInteractions(function);
    }

    @Test
    void writeCsvThrowsIllegalArgumentExceptionForZeroStep() {
        StringWriter writer = new StringWriter();
        CsvExporter csvExporter = new CsvExporter(writer, ";");
        AbstractFunction function = mock(AbstractFunction.class);

        assertThrows(IllegalArgumentException.class, () -> csvExporter.writeCsv(TEST_ACCURACY, 1.0, 2.0, 0.0, function));
        verifyNoInteractions(function);
    }

    @Test
    void writeCsvThrowsIllegalArgumentExceptionForAscendingRangeWithNegativeStep() {
        StringWriter writer = new StringWriter();
        CsvExporter csvExporter = new CsvExporter(writer, ";");
        AbstractFunction function = mock(AbstractFunction.class);

        assertThrows(IllegalArgumentException.class, () -> csvExporter.writeCsv(TEST_ACCURACY, 1.0, 2.0, -1.0, function));
        verifyNoInteractions(function);
    }

    @Test
    void writeCsvThrowsIllegalArgumentExceptionForDescendingRangeWithPositiveStep() {
        StringWriter writer = new StringWriter();
        CsvExporter csvExporter = new CsvExporter(writer, ";");
        AbstractFunction function = mock(AbstractFunction.class);

        assertThrows(IllegalArgumentException.class, () -> csvExporter.writeCsv(TEST_ACCURACY, 2.0, 1.0, 1.0, function));
        verifyNoInteractions(function);
    }

    @Test
    void writeCsvThrowsNullPointerExceptionForNullFunction() {
        StringWriter writer = new StringWriter();
        CsvExporter csvExporter = new CsvExporter(writer, ";");

        assertThrows(NullPointerException.class, () -> csvExporter.writeCsv(TEST_ACCURACY, 1.0, 1.0, 1.0, null));
    }

    @Test
    void writeCsvWritesExpectedOutputForDescendingRange() throws Exception {
        StringWriter writer = new StringWriter();
        CsvExporter csvExporter = new CsvExporter(writer, ";");
        AbstractFunction function = mock(AbstractFunction.class);
        when(function.calculate(3.0, TEST_ACCURACY)).thenReturn(9.0);
        when(function.calculate(2.0, TEST_ACCURACY)).thenReturn(4.0);
        when(function.calculate(1.0, TEST_ACCURACY)).thenReturn(1.0);

        csvExporter.writeCsv(TEST_ACCURACY, 3.0, 1.0, -1.0, function);

        assertEquals(
                "3.0;9.0" + System.lineSeparator() +
                        "2.0;4.0" + System.lineSeparator() +
                        "1.0;1.0" + System.lineSeparator(),
                writer.toString()
        );
        var inOrder = inOrder(function);
        inOrder.verify(function).calculate(3.0, TEST_ACCURACY);
        inOrder.verify(function).calculate(2.0, TEST_ACCURACY);
        inOrder.verify(function).calculate(1.0, TEST_ACCURACY);
        verifyNoMoreInteractions(function);
    }

    @Test
    void writeCsvPropagatesFunctionException() {
        StringWriter writer = new StringWriter();
        CsvExporter csvExporter = new CsvExporter(writer, ";");
        AbstractFunction function = mock(AbstractFunction.class);
        doThrow(new IllegalArgumentException("boom")).when(function).calculate(1.0, TEST_ACCURACY);

        assertThrows(IllegalArgumentException.class, () -> csvExporter.writeCsv(TEST_ACCURACY, 1.0, 3.0, 1.0, function));
        var inOrder = inOrder(function);
        inOrder.verify(function).calculate(1.0, TEST_ACCURACY);
        verifyNoMoreInteractions(function);
    }
}
