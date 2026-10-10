package Parsers;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

import static Support.TestSupport.assertComplex;
import static org.junit.jupiter.api.Assertions.*;

class ComplexParserTest {
    private final ComplexParser parser = new ComplexParser();

    @ParameterizedTest
    @CsvSource({"'2+3i',2,3", "'4-5i',4,-5", "'-2+3i',-2,3", "'-2-3i',-2,-3",
            "'+2+i',2,1", "'1-i',1,-1", "'0+0i',0,0", "'-0-0i',0,0",
            "'0-7i',0,-7", "'7+0i',7,0", "'3+4*i',3,4", "'-3-i',-3,-1"})
    void parsesSupportedIntegerForms(String text, int real, int imaginary) {
        assertComplex(real, imaginary, assertDoesNotThrow(() -> parser.parse(text)));
    }

    static Stream<String> whitespaceVariants() {
        return Stream.of("  -2 + 3i  ", "\t-2\t+\t3*i\t", "\n-2 + 3 i\r\n");
    }

    @ParameterizedTest
    @MethodSource("whitespaceVariants")
    void ignoresWhitespaceWithinAnOperand(String text) {
        assertComplex(-2, 3, parser.parse(text));
    }

    static Stream<String> malformedNumbers() {
        return Stream.of("", " ", "3", "i", "-i", "2i", "2+3", "2++3i", "2+-3i",
                "2--3i", "2+3ii", "2+3I", "2+3j", "2.5+3i", "2+0.5i", "abc",
                "2+3i+4+5i");
    }

    @ParameterizedTest
    @MethodSource("malformedNumbers")
    void rejectsMalformedOrUnsupportedForms(String text) {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(text));
    }

    @ParameterizedTest
    @CsvSource({"'2147483648+0i'", "'0+2147483648i'", "'-2147483649+0i'"})
    void rejectsComponentsOutsideIntegerRange(String text) {
        assertThrows(NumberFormatException.class, () -> parser.parse(text));
    }
}
