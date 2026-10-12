package Parsers;

import Expressions.Expression;
import Numbers.ComplexNumbers;
import Operations.Operation;
import Operations.OperatorRegistry;

import Validators.Validator;

import java.util.ArrayList;
import java.util.List;

public class ExpressionParser {

    private final ComplexParser complexParser;
    private final OperatorRegistry operatorRegistry;
    private final Validator<String[]> validator;

    public ExpressionParser(
            ComplexParser complexParser,
            OperatorRegistry operatorRegistry,
            Validator<String[]> validator) {

        this.complexParser = complexParser;
        this.operatorRegistry = operatorRegistry;
        this.validator = validator;
    }

    public Expression parse(String[] args) {

        validator.validate(args);

        if (args == null || args.length == 0) {
            throw new IllegalArgumentException(
                    "Expresia este goala."
            );
        }

        if (args.length % 2 == 0) {
            throw new IllegalArgumentException(
                    "Expresia trebuie sa aiba forma operand operator operand..."
            );
        }

        List<ComplexNumbers> operands =
                new ArrayList<>();

        List<Operation> operations =
                new ArrayList<>();

        for (int i = 0; i < args.length; i++) {

            if (i % 2 == 0) {
                operands.add(
                        complexParser.parse(args[i])
                );
            } else {

                String symbol = args[i].trim();

                if (!operatorRegistry.contains(symbol)) {
                    throw new IllegalArgumentException(
                            "Operator invalid: " + symbol
                    );
                }

                operations.add(
                        operatorRegistry.get(symbol)
                );
            }
        }

        return new Expression(
                operands,
                operations
        );
    }
}