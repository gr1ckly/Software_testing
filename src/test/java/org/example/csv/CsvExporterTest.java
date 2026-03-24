package org.example.csv;

import org.example.functions.AbstractFunction;
import org.example.test_helper.TestStubFunction;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CsvExporterTest {
    private static final double TEST_ACCURACY = 1.0E-12;

    @Test
    void writeCsvWritesExpectedOutput() throws Exception {
        StringWriter writer = new StringWriter();
        CsvExporter csvExporter = new CsvExporter(writer, ";");
        AbstractFunction function = new TestStubFunction(Map.of(
                1.0, 10.5,
                2.0, -3.25,
                3.0, 0.0
        ));

        csvExporter.writeCsv(TEST_ACCURACY, 1.0, 3.0, 1.0, function);

        assertEquals(
                "1.0;10.5" + System.lineSeparator() +
                        "2.0;-3.25" + System.lineSeparator() +
                        "3.0;0.0" + System.lineSeparator(),
                writer.toString()
        );
    }

    @Test
    void writeCsvThrowsIllegalArgumentExceptionForInvalidAccuracy() {
        StringWriter writer = new StringWriter();
        CsvExporter csvExporter = new CsvExporter(writer, ";");
        AbstractFunction function = new TestStubFunction(Map.of(1.0, 2.0));

        assertThrows(IllegalArgumentException.class, () -> csvExporter.writeCsv(0.0, 1.0, 1.0, 1.0, function));
    }

    @Test
    void writeCsvThrowsIllegalArgumentExceptionForZeroStep() {
        StringWriter writer = new StringWriter();
        CsvExporter csvExporter = new CsvExporter(writer, ";");
        AbstractFunction function = new TestStubFunction(Map.of(1.0, 2.0));

        assertThrows(IllegalArgumentException.class, () -> csvExporter.writeCsv(TEST_ACCURACY, 1.0, 2.0, 0.0, function));
    }

    @Test
    void writeCsvThrowsIllegalArgumentExceptionForAscendingRangeWithNegativeStep() {
        StringWriter writer = new StringWriter();
        CsvExporter csvExporter = new CsvExporter(writer, ";");
        AbstractFunction function = new TestStubFunction(Map.of(1.0, 2.0));

        assertThrows(IllegalArgumentException.class, () -> csvExporter.writeCsv(TEST_ACCURACY, 1.0, 2.0, -1.0, function));
    }

    @Test
    void writeCsvThrowsIllegalArgumentExceptionForDescendingRangeWithPositiveStep() {
        StringWriter writer = new StringWriter();
        CsvExporter csvExporter = new CsvExporter(writer, ";");
        AbstractFunction function = new TestStubFunction(Map.of(2.0, 3.0));

        assertThrows(IllegalArgumentException.class, () -> csvExporter.writeCsv(TEST_ACCURACY, 2.0, 1.0, 1.0, function));
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
        AbstractFunction function = new TestStubFunction(Map.of(
                3.0, 9.0,
                2.0, 4.0,
                1.0, 1.0
        ));

        csvExporter.writeCsv(TEST_ACCURACY, 3.0, 1.0, -1.0, function);

        assertEquals(
                "3.0;9.0" + System.lineSeparator() +
                        "2.0;4.0" + System.lineSeparator() +
                        "1.0;1.0" + System.lineSeparator(),
                writer.toString()
        );
    }
}
