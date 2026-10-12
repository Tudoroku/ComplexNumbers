package Expressions;

import Numbers.ComplexNumbers;
import Operations.AddOperation;
import Operations.DivideOperation;
import Operations.MultiplyOperation;
import Operations.OperatorRegistry;
import Operations.SubtractOperation;
import Parsers.ComplexParser;
import Parsers.ExpressionParser;
import Validators.ExpressionArgumentsValidator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExpressionEvaluatorTest {
    @Test
    void evaluatesRequiredExpressionThroughParser() {
        OperatorRegistry registry = new OperatorRegistry();
        registry.register(new MultiplyOperation());
        registry.register(new AddOperation());
        ExpressionParser parser = new ExpressionParser(
                new ComplexParser(), registry, new ExpressionArgumentsValidator());
        Expression expression = parser.parse(
                new String[]{"3+4i", "*", "2-5i", "+", "1+2i"});

        ComplexNumbers result = new ExpressionEvaluator().evaluate(expression);

        assertEquals(27f, result.getRealPart(), 0.0001f);
        assertEquals(-5f, result.getImaginaryPart(), 0.0001f);
    }

    @Test
    void multiplicationAndDivisionHavePriorityOverAdditionAndSubtraction() {
        // 20 - 3 * 4 + 8 / 2 = 12
        Expression expression = new Expression(
                List.of(new ComplexNumbers(20, 0), new ComplexNumbers(3, 0),
                        new ComplexNumbers(4, 0), new ComplexNumbers(8, 0),
                        new ComplexNumbers(2, 0)),
                List.of(new SubtractOperation(), new MultiplyOperation(),
                        new AddOperation(), new DivideOperation()));

        ComplexNumbers result = new ExpressionEvaluator().evaluate(expression);

        assertEquals(12f, result.getRealPart(), 0.0001f);
        assertEquals(0f, result.getImaginaryPart(), 0.0001f);
    }

    @Test
    void evaluatesSubtractionFromLeftToRight() {
        // (10 - 3) - 2 = 5
        Expression expression = new Expression(
                List.of(new ComplexNumbers(10, 0), new ComplexNumbers(3, 0),
                        new ComplexNumbers(2, 0)),
                List.of(new SubtractOperation(), new SubtractOperation()));

        ComplexNumbers result = new ExpressionEvaluator().evaluate(expression);

        assertEquals(5f, result.getRealPart(), 0.0001f);
        assertEquals(0f, result.getImaginaryPart(), 0.0001f);
    }

    @Test
    void evaluatesDivisionFromLeftToRight() {
        // (8 / 2) / 2 = 2
        Expression expression = new Expression(
                List.of(new ComplexNumbers(8, 0), new ComplexNumbers(2, 0),
                        new ComplexNumbers(2, 0)),
                List.of(new DivideOperation(), new DivideOperation()));

        ComplexNumbers result = new ExpressionEvaluator().evaluate(expression);

        assertEquals(2f, result.getRealPart(), 0.0001f);
        assertEquals(0f, result.getImaginaryPart(), 0.0001f);
    }
}
