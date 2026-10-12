package Validators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ExpressionArgumentsValidatorTest {
    @Test
    void acceptsOperandOperatorOperandStructure() {
        new ExpressionArgumentsValidator().validate(
                new String[]{"2+3i", "+", "4-5i"});
    }

    @Test
    void rejectsMissingArguments() {
        ExpressionArgumentsValidator validator = new ExpressionArgumentsValidator();

        assertThrows(IllegalArgumentException.class, () -> validator.validate(null));
        assertThrows(IllegalArgumentException.class, () -> validator.validate(new String[]{}));
    }

    @Test
    void rejectsEvenArgumentCount() {
        assertThrows(IllegalArgumentException.class,
                () -> new ExpressionArgumentsValidator().validate(new String[]{"2+3i", "+"}));
    }

    @Test
    void rejectsBlankOperator() {
        assertThrows(IllegalArgumentException.class,
                () -> new ExpressionArgumentsValidator().validate(
                        new String[]{"2+3i", "  ", "4-5i"}));
    }
}
