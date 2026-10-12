package Numbers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComplexNumbersTest {
    @Test
    void constructorStoresComponentsForGetters() {
        ComplexNumbers number = new ComplexNumbers(2.5f, -3.75f);

        assertEquals(2.5f, number.getRealPart(), 0.0001f);
        assertEquals(-3.75f, number.getImaginaryPart(), 0.0001f);
    }

    @Test
    void formatsPositiveImaginaryComponent() {
        ComplexNumbers number = new ComplexNumbers(2, 3);

        assertEquals("2.0 + 3.0i", number.toString());
    }

    @Test
    void formatsNegativeFractionalImaginaryComponent() {
        ComplexNumbers number = new ComplexNumbers(2.5f, -0.75f);

        assertEquals("2.5 - 0.75i", number.toString());
    }
}
