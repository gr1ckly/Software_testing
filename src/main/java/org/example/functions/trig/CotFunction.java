package org.example.functions.trig;

import org.example.functions.AbstractFunction;

import java.util.Objects;

public class CotFunction implements AbstractFunction {
    private final AbstractFunction sinFunction;
    private final AbstractFunction cosFunction;

    public CotFunction(AbstractFunction sinFunction, AbstractFunction cosFunction) {
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

        double sinValue = sinFunction.calculate(x, accuracy);
        if (Math.abs(sinValue) == 0.0) {
            throw new ArithmeticException("Cotangent is undefined when sin(x) is zero.");
        }

        double cosValue = cosFunction.calculate(x, accuracy);
        return cosValue / sinValue;
    }
}
