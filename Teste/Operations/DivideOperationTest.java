package Operations;

import Numbers.ComplexNumbers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static Support.TestSupport.assertComplex;
import static org.junit.jupiter.api.Assertions.*;

class DivideOperationTest {
    private final Operation operation = new DivideOperation();

    @ParameterizedTest
    @CsvSource({"3,4,1,-2,-1,2",
            "0,0,2,3,0,0",
            "-2,-3,1,0,-2,-3",
            "3,4,3,4,1,0",
            "0,6,0,2,3,0",
            "-6,2,-2,0,3,-1",
            "2,4,2,0,1,2"})
    void calculatesComponentsWithoutMutatingOperands(int ar, int ai, int br, int bi,
                                                     int real, int imaginary) {
        ComplexNumbers left = new ComplexNumbers(ar, ai);
        ComplexNumbers right = new ComplexNumbers(br, bi);
        ComplexNumbers result = operation.apply(left, right);
        assertComplex(real, imaginary, result);
        assertComplex(ar, ai, left);
        assertComplex(br, bi, right);
        assertNotSame(left, result);
        assertNotSame(right, result);
    }

    @Test
    void exposesSymbolAndPrecedenceThroughOperationInterface() {
        assertEquals("/", operation.getSymbol());
        assertEquals(2, operation.getPrecedence());
    }

    @ParameterizedTest
    @CsvSource({"3,4", "0,0", "-2,-3"})
    void rejectsZeroDivisor(int real, int imaginary) {
        // The implementation has no documented exception contract.
        assertThrows(RuntimeException.class, () -> operation.apply(
                new ComplexNumbers(real, imaginary), new ComplexNumbers(0, 0)));
    }

}
