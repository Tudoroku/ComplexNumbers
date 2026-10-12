package Parsers;

import Expressions.Expression;
import Expressions.ExpressionEvaluator;
import Operations.Operation;
import Operations.OperatorRegistry;
import Validators.ExpressionArgumentsValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.Arguments;
import java.util.stream.Stream;

import static Support.TestSupport.*;
import static org.junit.jupiter.api.Assertions.*;

class ExpressionParserTest {
    private final OperatorRegistry registry = registry();
    private final ExpressionArgumentsValidator validator = new ExpressionArgumentsValidator();
    private final ExpressionParser parser = new ExpressionParser(new ComplexParser(), registry, validator);

    @ParameterizedTest
    @ValueSource(strings = {"+", "-", "*", "/"})
    void storesOperandsAndRegisteredOperator(String symbol) {
        Expression expression = assertDoesNotThrow(
                () -> parser.parse(new String[]{"2+3i", symbol, "4-5i"}));
        assertEquals(2, expression.getOperands().size());
        assertEquals(1, expression.getOperations().size());
        assertComplex(2, 3, expression.getOperands().get(0));
        assertComplex(4, -5, expression.getOperands().get(1));
        assertSame(registry.get(symbol), expression.getOperations().get(0));
    }

    @Test
    void keepsMixedOperationsInInputOrderForEvaluatorToResolvePrecedence() {
        Expression expression = parser.parse(new String[]{
                "1+2i", "+", "3+4i", "*", "2-5i", "-", "-2-i", "/", "1+0i"});
        assertEquals(5, expression.getOperands().size());
        assertEquals(java.util.List.of("+", "*", "-", "/"),
                expression.getOperations().stream().map(Operation::getSymbol).toList());
        assertEquals(1, expression.getOperations().get(0).getPrecedence());
        assertEquals(2, expression.getOperations().get(1).getPrecedence());
        assertComplex(-2, -1, expression.getOperands().get(3));
    }

    @Test
    void parsesSingleOperandAndWhitespaceAroundTokens() {
        Expression single = parser.parse(new String[]{" \t-2 - i "});
        assertEquals(1, single.getOperands().size());
        assertTrue(single.getOperations().isEmpty());
        assertComplex(-2, -1, single.getOperands().get(0));
        Expression expression = parser.parse(new String[]{" 2 + 3 * i ", " \t+ ", " 4 - 5i "});
        assertComplex(6, -2, new ExpressionEvaluator().evaluate(expression));
    }

    static Stream<Arguments> invalidExpressions() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of((Object) new String[]{}),
                Arguments.of((Object) new String[]{""}),
                Arguments.of((Object) new String[]{"  "}),
                Arguments.of((Object) new String[]{"2+3i", "+"}),
                Arguments.of((Object) new String[]{"+", "2+3i"}),
                Arguments.of((Object) new String[]{"2+3i", "+", "4-5i", "*"}),
                Arguments.of((Object) new String[]{"2+3i", "^", "4-5i"}),
                Arguments.of((Object) new String[]{"2+3i", "%", "4-5i"}),
                Arguments.of((Object) new String[]{"2+3i", "", "4-5i"}),
                Arguments.of((Object) new String[]{"2+3i", "++", "4-5i"}),
                Arguments.of((Object) new String[]{"2+3i", "+", "-", "*", "4-5i"}),
                Arguments.of((Object) new String[]{"2+3i", "+", ""}),
                Arguments.of((Object) new String[]{"2+3i", "+", "4+-5i"}),
                Arguments.of((Object) new String[]{"2.5+3i", "+", "4-5i"}),
                Arguments.of((Object) new String[]{"2+3i+4-5i"}));
    }

    @ParameterizedTest
    @MethodSource("invalidExpressions")
    void rejectsEmptyMissingMalformedAndInvalidTokens(String[] args) {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(args));
    }

    @Test
    void acceptsOnlyOperatorsPresentInInjectedRegistry() {
        ExpressionParser restricted = new ExpressionParser(new ComplexParser(), new OperatorRegistry(), new ExpressionArgumentsValidator());
        assertThrows(IllegalArgumentException.class,
                () -> restricted.parse(new String[]{"1+0i", "+", "2+0i"}));
        assertDoesNotThrow(() -> restricted.parse(new String[]{"1+0i"}));
    }
}
