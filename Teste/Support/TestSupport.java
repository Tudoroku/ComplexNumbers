package Support;

import Numbers.ComplexNumbers;
import Operations.*;
import org.junit.jupiter.api.Assertions;

public final class TestSupport {
    private TestSupport() { }

    public static void assertComplex(int real, int imaginary, ComplexNumbers actual) {
        Assertions.assertNotNull(actual);
        Assertions.assertAll(
                () -> Assertions.assertEquals(real, actual.getRealPart(), "real component"),
                () -> Assertions.assertEquals(imaginary, actual.getImaginaryPart(), "imaginary component"));
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
