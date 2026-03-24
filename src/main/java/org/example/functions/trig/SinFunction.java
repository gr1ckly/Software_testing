package org.example.functions.trig;

import org.example.functions.AbstractFunction;

public class SinFunction implements AbstractFunction {
    @Override
    public double calculate(double x, double accuracy) {
        if (accuracy <= 0.0 || Double.isNaN(accuracy) || Double.isInfinite(accuracy)) {
            throw new IllegalArgumentException("Accuracy must be a positive finite number.");
        }
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            throw new IllegalArgumentException("X must be a finite number.");
        }

        double normalizedX = normalizeAngle(x);
        double term = normalizedX;
        double sum = term;
        int n = 1;

        while (Math.abs(term) > accuracy) {
            term *= -normalizedX * normalizedX / ((2.0 * n) * (2.0 * n + 1.0));
            sum += term;
            n++;
        }

        return sum;
    }

    private static double normalizeAngle(double x) {
        double twoPi = 2.0 * Math.PI;
        double normalized = x % twoPi;
        if (normalized > Math.PI) {
            normalized -= twoPi;
        } else if (normalized < -Math.PI) {
            normalized += twoPi;
        }
        return normalized;
    }
}
