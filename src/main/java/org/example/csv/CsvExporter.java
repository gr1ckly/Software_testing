package org.example.csv;

import org.example.functions.AbstractFunction;

import java.io.BufferedWriter;
import java.io.Writer;
import java.util.Objects;

public class CsvExporter {
    private final Writer writer;
    private final String delimiter;

    public CsvExporter(Writer writer, String delimiter) {
        this.writer = Objects.requireNonNull(writer, "writer must not be null.");
        this.delimiter = Objects.requireNonNull(delimiter, "delimiter must not be null.");
    }

    public void writeCsv(
            double accuracy,
            double fromX,
            double toX,
            double step,
            AbstractFunction func
    ) throws Exception {
        Objects.requireNonNull(func, "func must not be null.");
        if (accuracy <= 0.0 || Double.isNaN(accuracy) || Double.isInfinite(accuracy)) {
            throw new IllegalArgumentException("Accuracy must be a positive finite number.");
        }
        if (Double.isNaN(fromX) || Double.isInfinite(fromX)) {
            throw new IllegalArgumentException("From value must be a finite number.");
        }
        if (Double.isNaN(toX) || Double.isInfinite(toX)) {
            throw new IllegalArgumentException("To value must be a finite number.");
        }
        if (step == 0.0 || Double.isNaN(step) || Double.isInfinite(step)) {
            throw new IllegalArgumentException("Step must be a non-zero finite number.");
        }
        if (fromX < toX && step < 0.0) {
            throw new IllegalArgumentException("Step must be positive for an ascending range.");
        }
        if (fromX > toX && step > 0.0) {
            throw new IllegalArgumentException("Step must be negative for a descending range.");
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            for (double point = fromX; isWithinRange(point, toX, step); point += step) {
                double result = func.calculate(point, accuracy);
                bufferedWriter.write(point + delimiter + result);
                bufferedWriter.newLine();
            }
        }
    }

    private boolean isWithinRange(double point, double toX, double step) {
        return step > 0.0 ? point <= toX : point >= toX;
    }
}
