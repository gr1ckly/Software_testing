package org.example.functions.trig;

import org.example.functions.AbstractFunction;

import java.util.Objects;

public class TanFunction implements AbstractFunction {
    private final AbstractFunction sinFunction;
    private final AbstractFunction cosFunction;

    public TanFunction(AbstractFunction sinFunction, AbstractFunction cosFunction) {
        this.sinFunction = Objects.requireNonNull(sinFunction, "sinFunction must not be null.");
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
            throw new ArithmeticException("Tangent is undefined when cos(x) is zero.");
        }

        double sinValue = sinFunction.calculate(x, accuracy);
        return sinValue / cosValue;
    }
}
