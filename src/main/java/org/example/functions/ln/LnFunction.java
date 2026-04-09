package org.example.functions.ln;

import org.example.functions.AbstractFunction;

public class LnFunction implements AbstractFunction {
    private static final double LN_2 = 0.6931471805599453;

    @Override
    public double calculate(double x, double accuracy) {
        if (accuracy <= 0.0 || Double.isNaN(accuracy) || Double.isInfinite(accuracy)) {
            throw new IllegalArgumentException("Accuracy must be a positive finite number.");
        }
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            throw new IllegalArgumentException("X must be a finite number.");
        }
        if (x <= 0.0) {
            throw new ArithmeticException("Natural logarithm is undefined for x <= 0.");
        }

        int exponent = Math.getExponent(x);
        double mantissa = x / Math.scalb(1.0, exponent);
        while (mantissa < 1.0) {
            mantissa *= 2.0;
            exponent--;
        }

        double y = (mantissa - 1.0) / (mantissa + 1.0);
        double y2 = y * y;

        double term = y;
        int denominator = 1;
        double sum = 0.0;

        while (true) {
            sum += term / denominator;

            double nextTerm = term * y2;
            int nextDenominator = denominator + 2;

            double tailBound = 2.0 * Math.abs(nextTerm) / (nextDenominator * (1.0 - y2));
            if (tailBound <= accuracy) {
                break;
            }

            term = nextTerm;
            denominator = nextDenominator;
        }

        return 2.0 * sum + exponent * LN_2;
    }
}
