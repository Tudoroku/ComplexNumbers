package Support;

import Numbers.ComplexNumbers;
import Operations.*;
import org.junit.jupiter.api.Assertions;

public final class TestSupport {
    private TestSupport() { }

    // Float arithmetic has about seven significant decimal digits. Use an
    // absolute tolerance near zero and a relative tolerance for larger values.
    private static final float TOLERANCE = 1e-6f;

    private static float tolerance(float expected) {
        return Math.max(TOLERANCE, Math.abs(expected) * TOLERANCE);
    }

    public static void assertComplex(float real, float imaginary, ComplexNumbers actual) {
        Assertions.assertNotNull(actual);
        Assertions.assertAll(
                () -> Assertions.assertEquals(real, actual.getRealPart(), tolerance(real), "real component"),
                () -> Assertions.assertEquals(imaginary, actual.getImaginaryPart(), tolerance(imaginary), "imaginary component"));
    }

    public static OperatorRegistry registry() {
        OperatorRegistry registry = new OperatorRegistry();
        registry.register(new AddOperation());
        registry.register(new SubtractOperation());
        registry.register(new MultiplyOperation());
        registry.register(new DivideOperation());
        return registry;
    }
}
