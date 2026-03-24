package org.example.test_helper;

import org.example.functions.AbstractFunction;

import java.util.Map;
import java.util.Objects;

public class TestStubFunction implements AbstractFunction {
    private final Map<Double, Double> values;

    public TestStubFunction(Map<Double, Double> values) {
        this.values = Map.copyOf(Objects.requireNonNull(values, "values must not be null."));
    }

    @Override
    public double calculate(double x, double accuracy) {
        if (accuracy <= 0.0 || Double.isNaN(accuracy) || Double.isInfinite(accuracy)) {
            throw new IllegalArgumentException("Accuracy must be a positive finite number.");
        }
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            throw new IllegalArgumentException("X must be a finite number.");
        }
        if (!values.containsKey(x)) {
            throw new IllegalArgumentException("No stubbed value defined for x = " + x + ".");
        }

        return values.get(x);
    }
}
