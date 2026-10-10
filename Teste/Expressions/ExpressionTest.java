package Expressions;

import Numbers.ComplexNumbers;
import Operations.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static Support.TestSupport.assertComplex;
import static org.junit.jupiter.api.Assertions.*;

class ExpressionTest {
    @Test
    void constructorStoresOperandsAndOperationsInOrder() {
        ComplexNumbers first = new ComplexNumbers(2, 3);
        ComplexNumbers second = new ComplexNumbers(4, -5);
        ComplexNumbers third = new ComplexNumbers(0, 0);
        Operation add = new AddOperation();
        Operation subtract = new SubtractOperation();
        Expression expression = new Expression(List.of(first, second, third), List.of(add, subtract));
        assertEquals(List.of(first, second, third), expression.getOperands());
        assertEquals(List.of(add, subtract), expression.getOperations());
        assertEquals(expression.getOperations().size() + 1, expression.getOperands().size());
    }

    @Test
    void singleOperandNeedsNoOperations() {
        Expression expression = new Expression(List.of(new ComplexNumbers(-2, -3)), List.of());
        assertComplex(-2, -3, expression.getOperands().get(0));
        assertTrue(expression.getOperations().isEmpty());
    }

    @Test
    void retainsSuppliedListsRatherThanCopyingThem() {
        List<ComplexNumbers> operands = new ArrayList<>();
        List<Operation> operations = new ArrayList<>();
        Expression expression = new Expression(operands, operations);
        assertSame(operands, expression.getOperands());
        assertSame(operations, expression.getOperations());
        operands.add(new ComplexNumbers(1, 2));
        assertEquals(1, expression.getOperands().size());
    }

    @Test
    void emptyAndInconsistentStorageIsValidatedByEvaluator() {
        Expression empty = new Expression(List.of(), List.of());
        assertTrue(empty.getOperands().isEmpty());
        assertThrows(IllegalArgumentException.class, () -> new ExpressionEvaluator().evaluate(empty));
        Expression inconsistent = new Expression(
                List.of(new ComplexNumbers(1, 0), new ComplexNumbers(2, 0)), List.of());
        assertEquals(2, inconsistent.getOperands().size());
        assertThrows(IllegalArgumentException.class,
                () -> new ExpressionEvaluator().evaluate(inconsistent));
    }
}
