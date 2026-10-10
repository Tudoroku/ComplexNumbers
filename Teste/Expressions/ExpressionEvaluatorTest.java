package Expressions;

import Numbers.ComplexNumbers;
import Operations.*;
import Parsers.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;
import java.util.stream.Stream;

import static Support.TestSupport.*;
import static org.junit.jupiter.api.Assertions.*;

class ExpressionEvaluatorTest {
    private final ExpressionEvaluator evaluator = new ExpressionEvaluator();
    private final ExpressionParser parser = new ExpressionParser(new ComplexParser(), registry());

    static Stream<Arguments> individualOperations() {
        return Stream.of(
                Arguments.of(new AddOperation(), 2, 3, 4, -5, 6, -2),
                Arguments.of(new SubtractOperation(), 3, 4, 3, 4, 0, 0),
                Arguments.of(new MultiplyOperation(), 1, 1, 1, -1, 2, 0),
                Arguments.of(new DivideOperation(), 3, 4, 1, -2, -1, 2));
    }

    @ParameterizedTest
    @MethodSource("individualOperations")
    void evaluatesDirectlyConstructedExpression(Operation operation, int ar, int ai,
                                               int br, int bi, int real, int imaginary) {
        Expression expression = new Expression(
                List.of(new ComplexNumbers(ar, ai), new ComplexNumbers(br, bi)), List.of(operation));
        assertComplex(real, imaginary, evaluator.evaluate(expression));
    }

    static Stream<Arguments> expressions() {
        // Each operand and operator is a separate command-line argument.
        return Stream.of(
                Arguments.of(new String[]{"2+3i", "+", "4-5i"}, 6, -2),
                Arguments.of(new String[]{"3+4i", "-", "3+4i"}, 0, 0),
                Arguments.of(new String[]{"1+i", "*", "1-i"}, 2, 0),
                Arguments.of(new String[]{"3+4i", "/", "1-2i"}, -1, 2),
                Arguments.of(new String[]{"3+4i", "*", "2-5i", "+", "1+2i"}, 27, -5),
                Arguments.of(new String[]{"1+2i", "+", "3+4i", "*", "2-5i"}, 27, -5),
                Arguments.of(new String[]{"10+0i", "-", "3+0i", "-", "2+0i"}, 5, 0),
                Arguments.of(new String[]{"10+0i", "-", "3+0i", "+", "2+0i"}, 9, 0),
                Arguments.of(new String[]{"1+i", "*", "1-i", "*", "0+i"}, 0, 2),
                Arguments.of(new String[]{"-2-3i", "+", "4-i", "-", "-1-2i"}, 3, -2),
                Arguments.of(new String[]{"0+0i", "*", "-5+2i", "+", "0+0i"}, 0, 0),
                Arguments.of(new String[]{"8+0i", "/", "2+0i", "/", "2+0i"}, 2, 0),
                Arguments.of(new String[]{"8+0i", "/", "2+0i", "*", "3+0i"}, 12, 0),
                Arguments.of(new String[]{"8+0i", "*", "2+0i", "/", "4+0i"}, 4, 0),
                Arguments.of(new String[]{"1+0i", "+", "8+0i", "/", "2+0i"}, 5, 0),
                Arguments.of(new String[]{"1+0i", "+", "6+0i", "*", "2+0i", "-", "8+0i", "/", "4+0i"}, 11, 0));
    }

    @ParameterizedTest
    @MethodSource("expressions")
    void integratesParserRegistryAndEvaluator(String[] tokens, int real, int imaginary) {
        assertComplex(real, imaginary, evaluator.evaluate(parser.parse(tokens)));
    }

    @Test
    void singleOperandIsReturnedUnchanged() {
        ComplexNumbers operand = new ComplexNumbers(-3, 4);
        assertSame(operand, evaluator.evaluate(new Expression(List.of(operand), List.of())));
    }

    @Test
    void evaluationIsRepeatableAndDoesNotConsumeExpression() {
        Expression expression = parser.parse(new String[]{"1+2i", "+", "3+4i", "*", "2-5i"});
        List<ComplexNumbers> operands = List.copyOf(expression.getOperands());
        List<Operation> operations = List.copyOf(expression.getOperations());
        assertComplex(27, -5, evaluator.evaluate(expression));
        assertComplex(27, -5, evaluator.evaluate(expression));
        assertEquals(operands, expression.getOperands());
        assertEquals(operations, expression.getOperations());
        assertComplex(1, 2, operands.get(0));
        assertComplex(3, 4, operands.get(1));
        assertComplex(2, -5, operands.get(2));
    }

    static Stream<Expression> inconsistentExpressions() {
        ComplexNumbers one = new ComplexNumbers(1, 0);
        return Stream.of(
                new Expression(List.of(), List.of()),
                new Expression(List.of(), List.of(new AddOperation())),
                new Expression(List.of(one, one), List.of()),
                new Expression(List.of(one), List.of(new AddOperation())),
                new Expression(List.of(one, one), List.of(new AddOperation(), new SubtractOperation())));
    }

    @ParameterizedTest
    @MethodSource("inconsistentExpressions")
    void rejectsIncorrectOperandOperatorCounts(Expression expression) {
        assertThrows(IllegalArgumentException.class, () -> evaluator.evaluate(expression));
    }

    @Test
    void divisionByZeroPropagatesFailureThroughIntegration() {
        // No exact exception type is documented; require an unchecked failure.
        assertThrows(RuntimeException.class,
                () -> evaluator.evaluate(parser.parse(new String[]{"3+4i", "/", "0+0i"})));
    }
}
