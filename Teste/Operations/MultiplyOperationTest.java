package Operations;

import Numbers.ComplexNumbers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static Support.TestSupport.assertComplex;
import static org.junit.jupiter.api.Assertions.*;

class MultiplyOperationTest {
    private final Operation operation = new MultiplyOperation();

    @ParameterizedTest
    @CsvSource({"1,1,1,-1,2,0",
            "3,4,2,-5,26,-7",
            "0,0,-2,3,0,0",
            "-2,3,0,0,0,0",
            "-2,-3,-4,-5,-7,22",
            "0,1,0,1,-1,0",
            "-2,3,1,0,-2,3",
            "1.5,0.5,0.5,-1,1.25,-1.25",
            "0.1,0.2,0.3,-0.4,0.11,0.02"})
    void calculatesComponentsWithoutMutatingOperands(float ar, float ai, float br, float bi,
                                                     float real, float imaginary) {
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
        assertEquals("*", operation.getSymbol());
        assertEquals(2, operation.getPrecedence());
    }

}
