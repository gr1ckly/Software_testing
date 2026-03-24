package org.example.functions.equation;

import org.example.functions.AbstractFunction;

import java.util.Objects;

public class EquationFunction implements AbstractFunction {
    private final AbstractFunction sinFunction;
    private final AbstractFunction cosFunction;
    private final AbstractFunction tanFunction;
    private final AbstractFunction cotFunction;
    private final AbstractFunction secFunction;
    private final AbstractFunction cscFunction;
    private final AbstractFunction lnFunction;
    private final AbstractFunction log2Function;
    private final AbstractFunction log5Function;
    private final AbstractFunction log10Function;

    public EquationFunction(
            AbstractFunction sinFunction,
            AbstractFunction cosFunction,
            AbstractFunction tanFunction,
            AbstractFunction cotFunction,
            AbstractFunction secFunction,
            AbstractFunction cscFunction,
            AbstractFunction lnFunction,
            AbstractFunction log2Function,
            AbstractFunction log5Function,
            AbstractFunction log10Function
    ) {
        this.sinFunction = Objects.requireNonNull(sinFunction, "sinFunction must not be null.");
        this.cosFunction = Objects.requireNonNull(cosFunction, "cosFunction must not be null.");
        this.tanFunction = Objects.requireNonNull(tanFunction, "tanFunction must not be null.");
        this.cotFunction = Objects.requireNonNull(cotFunction, "cotFunction must not be null.");
        this.secFunction = Objects.requireNonNull(secFunction, "secFunction must not be null.");
        this.cscFunction = Objects.requireNonNull(cscFunction, "cscFunction must not be null.");
        this.lnFunction = Objects.requireNonNull(lnFunction, "lnFunction must not be null.");
        this.log2Function = Objects.requireNonNull(log2Function, "log2Function must not be null.");
        this.log5Function = Objects.requireNonNull(log5Function, "log5Function must not be null.");
        this.log10Function = Objects.requireNonNull(log10Function, "log10Function must not be null.");
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
            return calculateNonPositiveBranch(x, accuracy);
        }
        return calculatePositiveBranch(x, accuracy);
    }

    private double calculateNonPositiveBranch(double x, double accuracy) {
        double sin = sinFunction.calculate(x, accuracy);
        double cos = cosFunction.calculate(x, accuracy);
        double tan = tanFunction.calculate(x, accuracy);
        double cot = cotFunction.calculate(x, accuracy);
        double sec = secFunction.calculate(x, accuracy);
        double csc = cscFunction.calculate(x, accuracy);

        double leftFirst = Math.pow((((cot - sec) + (cot + csc)) * sin), 2.0);
        double leftSecond = (tan + csc) - (csc * cos);
        double leftThird = Math.pow(Math.pow(csc, 3.0), 3.0) - csc;

        double denominator = (((cot + cot) / tan) - (cot + (csc * ((cos + sin) * sec))));
        if (Math.abs(denominator) <= accuracy) {
            throw new ArithmeticException("Equation is undefined because the denominator is zero.");
        }

        double leftPart = ((leftFirst + leftSecond) * leftThird) / denominator;

        double rightFirst = ((tan / cos) + (cot + sin)) - cos;
        double rightSecond = (Math.pow(cos, 2.0) * (sin - sin)) - cot;
        double rightThird = (sin * csc) * csc;
        double rightPart = (rightFirst * rightSecond) + rightThird;

        return leftPart - rightPart;
    }

    private double calculatePositiveBranch(double x, double accuracy) {
        double ln = lnFunction.calculate(x, accuracy);
        double log2 = log2Function.calculate(x, accuracy);
        double log5 = log5Function.calculate(x, accuracy);
        double log10 = log10Function.calculate(x, accuracy);

        return (((((log5 - ln) - log2) * log5) + (log2 * (log2 + (log10 - ln)))) - ln);
    }
}
