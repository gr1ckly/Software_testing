package org.example.functions.ln;

import org.example.functions.AbstractFunction;

import java.util.Objects;

public class LogFunction implements AbstractFunction {
    private final double base;
    private final AbstractFunction lnFunction;

    public LogFunction(double base, AbstractFunction lnFunction) {
        if (Double.isNaN(base) || Double.isInfinite(base) || base <= 0.0 || base == 1.0) {
            throw new IllegalArgumentException("Logarithm base must be positive, finite, and not equal to 1.");
        }
        this.base = base;
        this.lnFunction = Objects.requireNonNull(lnFunction, "lnFunction must not be null.");
    }

    @Override
    public double calculate(double x, double accuracy) {
        if (accuracy <= 0.0 || Double.isNaN(accuracy) || Double.isInfinite(accuracy)) {
            throw new IllegalArgumentException("Accuracy must be a positive finite number.");
        }
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            throw new IllegalArgumentException("X must be a finite number.");
        }
        if (x <= 0.0) {
            throw new ArithmeticException("Logarithm is undefined for x <= 0.");
        }

        double lnX = lnFunction.calculate(x, accuracy);
        double lnBase = lnFunction.calculate(base, accuracy);
        return lnX / lnBase;
    }
}
