package Operations;

import Numbers.ComplexNumbers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OperationsTest {
    @Test
    void addsComplexNumbers() {
        ComplexNumbers result = new AddOperation().apply(
                new ComplexNumbers(2, 3), new ComplexNumbers(4, -5));

        assertEquals(6f, result.getRealPart(), 0.0001f);
        assertEquals(-2f, result.getImaginaryPart(), 0.0001f);
    }

    @Test
    void subtractsComplexNumbers() {
        ComplexNumbers result = new SubtractOperation().apply(
                new ComplexNumbers(2, 3), new ComplexNumbers(4, -5));

        assertEquals(-2f, result.getRealPart(), 0.0001f);
        assertEquals(8f, result.getImaginaryPart(), 0.0001f);
    }

    @Test
    void multipliesComplexNumbers() {
        ComplexNumbers result = new MultiplyOperation().apply(
                new ComplexNumbers(3, 4), new ComplexNumbers(2, -5));

        assertEquals(26f, result.getRealPart(), 0.0001f);
        assertEquals(-7f, result.getImaginaryPart(), 0.0001f);
    }

    @Test
    void dividesComplexNumbersWithFractionalResult() {
        ComplexNumbers result = new DivideOperation().apply(
                new ComplexNumbers(1, 1), new ComplexNumbers(2, -1));

        assertEquals(0.2f, result.getRealPart(), 0.0001f);
        assertEquals(0.6f, result.getImaginaryPart(), 0.0001f);
    }

    @Test
    void rejectsDivisionByZero() {
        assertThrows(ArithmeticException.class, () -> new DivideOperation().apply(
                new ComplexNumbers(3, 4), new ComplexNumbers(0, 0)));
    }
}
