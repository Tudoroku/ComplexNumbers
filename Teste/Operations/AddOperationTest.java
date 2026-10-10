package Operations;

import Numbers.ComplexNumbers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static Support.TestSupport.assertComplex;
import static org.junit.jupiter.api.Assertions.*;

class AddOperationTest {
    private final Operation operation = new AddOperation();

    @ParameterizedTest
    @CsvSource({"2,3,4,-5,6,-2",
            "0,0,0,0,0,0",
            "0,0,-2,3,-2,3",
            "-2,3,0,0,-2,3",
            "-2,-3,-4,-5,-6,-8",
            "2,3,-2,-3,0,0"})
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
        assertEquals("+", operation.getSymbol());
        assertEquals(1, operation.getPrecedence());
    }

}
