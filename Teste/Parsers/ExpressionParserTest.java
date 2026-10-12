package Parsers;

import Expressions.Expression;
import Operations.AddOperation;
import Operations.OperatorRegistry;
import Validators.ExpressionArgumentsValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExpressionParserTest {
    @Test
    void buildsExpressionFromSeparateArguments() {
        OperatorRegistry registry = new OperatorRegistry();
        registry.register(new AddOperation());
        ExpressionParser parser = new ExpressionParser(
                new ComplexParser(), registry, new ExpressionArgumentsValidator());

        Expression expression = parser.parse(new String[]{"2+3i", " + ", "4-5i"});

        assertEquals(2, expression.getOperands().size());
        assertEquals(1, expression.getOperations().size());
        assertEquals(2f, expression.getOperands().get(0).getRealPart(), 0.0001f);
        assertEquals(3f, expression.getOperands().get(0).getImaginaryPart(), 0.0001f);
        assertEquals(4f, expression.getOperands().get(1).getRealPart(), 0.0001f);
        assertEquals(-5f, expression.getOperands().get(1).getImaginaryPart(), 0.0001f);
        assertEquals("+", expression.getOperations().get(0).getSymbol());
    }

    @Test
    void rejectsUnknownOperator() {
        ExpressionParser parser = new ExpressionParser(
                new ComplexParser(), new OperatorRegistry(), new ExpressionArgumentsValidator());

        assertThrows(IllegalArgumentException.class,
                () -> parser.parse(new String[]{"2+3i", "^", "4-5i"}));
    }

    @Test
    void rejectsMalformedOperand() {
        OperatorRegistry registry = new OperatorRegistry();
        registry.register(new AddOperation());
        ExpressionParser parser = new ExpressionParser(
                new ComplexParser(), registry, new ExpressionArgumentsValidator());

        assertThrows(IllegalArgumentException.class,
                () -> parser.parse(new String[]{"2+3i", "+", "invalid"}));
    }
}
