package Numbers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static Support.TestSupport.assertComplex;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ComplexNumbersTest {
    @ParameterizedTest
    @CsvSource({"0,0", "2,3", "-2,3", "2,-3", "-2,-3", "0,7", "7,0",
            "-2147483648,2147483647"})
    void constructorStoresBothComponents(int real, int imaginary) {
        assertComplex(real, imaginary, new ComplexNumbers(real, imaginary));
    }

    @ParameterizedTest
    @CsvSource({"2,3,'2 + 3i'", "2,-3,'2 - 3i'", "-2,3,'-2 + 3i'",
            "-2,-3,'-2 - 3i'", "0,0,'0 + 0i'", "0,-1,'0 - 1i'", "4,0,'4 + 0i'"})
    void stringRepresentsSignsAndZero(int real, int imaginary, String expected) {
        assertEquals(expected, new ComplexNumbers(real, imaginary).toString());
    }

    @Test
    void stringRepresentsMinimumIntegerImaginaryComponentWithoutOverflow() {
        assertEquals("0 - 2147483648i", new ComplexNumbers(0, Integer.MIN_VALUE).toString());
    }
}
