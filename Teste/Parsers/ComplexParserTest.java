package Parsers;

import Numbers.ComplexNumbers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ComplexParserTest {
    @Test
    void parsesIntegerComponentsWithSigns() {
        ComplexNumbers number = new ComplexParser().parse("-2-3i");

        assertEquals(-2f, number.getRealPart(), 0.0001f);
        assertEquals(-3f, number.getImaginaryPart(), 0.0001f);
    }

    @Test
    void parsesImplicitImaginaryCoefficient() {
        ComplexNumbers number = new ComplexParser().parse("2+i");

        assertEquals(2f, number.getRealPart(), 0.0001f);
        assertEquals(1f, number.getImaginaryPart(), 0.0001f);
    }

    @Test
    void ignoresWhitespaceAndCoefficientAsterisk() {
        ComplexNumbers number = new ComplexParser().parse(" 2 + 3 * i ");

        assertEquals(2f, number.getRealPart(), 0.0001f);
        assertEquals(3f, number.getImaginaryPart(), 0.0001f);
    }

    @Test
    void rejectsMalformedComplexNumber() {
        assertThrows(IllegalArgumentException.class,
                () -> new ComplexParser().parse("2++3i"));
    }
}
