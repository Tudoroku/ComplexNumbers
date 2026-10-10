package Expressions;

import Numbers.ComplexNumbers;
import Operations.Operation;

import java.util.ArrayDeque;
import java.util.Deque;

public class ExpressionEvaluator {

    public ComplexNumbers evaluate(Expression expression) {

        if (expression.getOperands().isEmpty()) {
            throw new IllegalArgumentException("Expresia nu are operanzi.");
        }

        if (expression.getOperands().size()
                != expression.getOperations().size() + 1) {
            throw new IllegalArgumentException(
                    "Expresia are un numar invalid de operatori."
            );
        }

        Deque<ComplexNumbers> values = new ArrayDeque<>();
        Deque<Operation> operations = new ArrayDeque<>();

        values.push(expression.getOperands().get(0));

        for (int i = 0; i < expression.getOperations().size(); i++) {

            Operation currentOperation =
                    expression.getOperations().get(i);

            /*
             * Executam operatorii care au prioritate
             * mai mare sau egala cu operatorul curent.
             */
            while (!operations.isEmpty()
                    && operations.peek().getPrecedence()
                    >= currentOperation.getPrecedence()) {

                applyTopOperation(values, operations);
            }

            operations.push(currentOperation);

            values.push(
                    expression.getOperands().get(i + 1)
            );
        }

        // Executam operatorii ramasi
        while (!operations.isEmpty()) {
            applyTopOperation(values, operations);
        }

        return values.pop();
    }

    private void applyTopOperation(
            Deque<ComplexNumbers> values,
            Deque<Operation> operations) {

        if (values.size() < 2) {
            throw new IllegalStateException(
                    "Nu exista suficienti operanzi."
            );
        }

        Operation operation = operations.pop();

        /*
         * Stack-ul este LIFO!
         *
         * Pentru A - B:
         *
         * pop() -> B = right
         * pop() -> A = left
         */
        ComplexNumbers right = values.pop();
        ComplexNumbers left = values.pop();

        ComplexNumbers result =
                operation.apply(left, right);

        values.push(result);
    }
}