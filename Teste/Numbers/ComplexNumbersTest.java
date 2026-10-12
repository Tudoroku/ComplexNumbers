package Numbers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static Support.TestSupport.assertComplex;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ComplexNumbersTest {
    @ParameterizedTest
    @CsvSource({"0,0", "2,3", "-2,3", "2,-3", "-2,-3", "0,7", "7,0",
            "-2.5,3.75", "0.125,-0.0625", "0.1,0.2", "-2147483648,2147483648"})
    void constructorStoresBothComponents(float real, float imaginary) {
        assertComplex(real, imaginary, new ComplexNumbers(real, imaginary));
    }

    @ParameterizedTest
    @CsvSource({"2,3,'2.0 + 3.0i'", "2,-3,'2.0 - 3.0i'", "-2,3,'-2.0 + 3.0i'",
            "-2,-3,'-2.0 - 3.0i'", "0,0,'0.0 + 0.0i'", "0,-1,'0.0 - 1.0i'",
            "4,0,'4.0 + 0.0i'", "2.5,0.75,'2.5 + 0.75i'"})
    void stringRepresentsSignsAndZero(float real, float imaginary, String expected) {
        assertEquals(expected, new ComplexNumbers(real, imaginary).toString());
    }

    @ParameterizedTest
    @CsvSource({"2.5,-0.75,'2.5 - 0.75i'", "-2.5,-3.75,'-2.5 - 3.75i'"})
    void stringPreservesNegativeFractionalImaginaryComponent(float real, float imaginary, String expected) {
        assertEquals(expected, new ComplexNumbers(real, imaginary).toString());
    }

    @Test
    void constructorStoresFiniteFloatExtremes() {
        assertComplex(Float.MAX_VALUE, -Float.MAX_VALUE,
                new ComplexNumbers(Float.MAX_VALUE, -Float.MAX_VALUE));
        // An exact check is necessary here: the arithmetic tolerance exceeds
        // the smallest positive float and would also accept a stored zero.
        ComplexNumbers tiny = new ComplexNumbers(Float.MIN_VALUE, -Float.MIN_VALUE);
        assertEquals(Float.MIN_VALUE, tiny.getRealPart());
        assertEquals(-Float.MIN_VALUE, tiny.getImaginaryPart());
    }
}
