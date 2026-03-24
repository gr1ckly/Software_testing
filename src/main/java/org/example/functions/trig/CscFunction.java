package org.example.functions.trig;

import org.example.functions.AbstractFunction;

import java.util.Objects;

public class CscFunction implements AbstractFunction {
    private final AbstractFunction sinFunction;

    public CscFunction(AbstractFunction sinFunction) {
        this.sinFunction = Objects.requireNonNull(sinFunction, "sinFunction must not be null.");
    }

    @Override
    public double calculate(double x, double accuracy) {
        if (accuracy <= 0.0 || Double.isNaN(accuracy) || Double.isInfinite(accuracy)) {
            throw new IllegalArgumentException("Accuracy must be a positive finite number.");
        }
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            throw new IllegalArgumentException("X must be a finite number.");
        }

        double sinValue = sinFunction.calculate(x, accuracy);
        if (Math.abs(sinValue) == 0.0) {
            throw new ArithmeticException("Cosecant is undefined when sin(x) is zero.");
        }

        return 1.0 / sinValue;
    }
}
