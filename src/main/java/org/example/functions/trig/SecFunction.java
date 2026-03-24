package org.example.functions.trig;

import org.example.functions.AbstractFunction;

import java.util.Objects;

public class SecFunction implements AbstractFunction {
    private final AbstractFunction cosFunction;

    public SecFunction(AbstractFunction cosFunction) {
        this.cosFunction = Objects.requireNonNull(cosFunction, "cosFunction must not be null.");
    }

    @Override
    public double calculate(double x, double accuracy) {
        if (accuracy <= 0.0 || Double.isNaN(accuracy) || Double.isInfinite(accuracy)) {
            throw new IllegalArgumentException("Accuracy must be a positive finite number.");
        }
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            throw new IllegalArgumentException("X must be a finite number.");
        }

        double cosValue = cosFunction.calculate(x, accuracy);
        if (Math.abs(cosValue) == 0.0) {
            throw new ArithmeticException("Secant is undefined when cos(x) is zero.");
        }

        return 1.0 / cosValue;
    }
}
