package org.example.test_helper;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestStubFunctionTest {
    private static final double TEST_ACCURACY = 1.0E-12;

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 1.0, -1.0, Math.PI})
    void returnsConfiguredValue(double x) {
        TestStubFunction stubFunction = new TestStubFunction(Map.of(x, x * 2.0));

        double actual = stubFunction.calculate(x, TEST_ACCURACY);

        assertEquals(x * 2.0, actual);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0E-6, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void throwsIllegalArgumentExceptionForInvalidAccuracy(double accuracy) {
        TestStubFunction stubFunction = new TestStubFunction(Map.of(1.0, 2.0));

        assertThrows(IllegalArgumentException.class, () -> stubFunction.calculate(1.0, accuracy));
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void throwsIllegalArgumentExceptionForInvalidX(double x) {
        TestStubFunction stubFunction = new TestStubFunction(Map.of(1.0, 2.0));

        assertThrows(IllegalArgumentException.class, () -> stubFunction.calculate(x, TEST_ACCURACY));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 1.0, -1.0})
    void throwsIllegalArgumentExceptionWhenValueIsNotDefined(double x) {
        TestStubFunction stubFunction = new TestStubFunction(Map.of());

        assertThrows(IllegalArgumentException.class, () -> stubFunction.calculate(x, TEST_ACCURACY));
    }
}
