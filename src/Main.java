import Expressions.Expression;
import Expressions.ExpressionEvaluator;
import Parsers.ExpressionParser;
import Numbers.ComplexNumbers;
import Operations.*;
import Parsers.ComplexParser;
import Validators.ExpressionArgumentsValidator;

public class Main {

    public static void main(String[] args) {

        OperatorRegistry registry = new OperatorRegistry();

        registry.register(new AddOperation());
        registry.register(new SubtractOperation());
        registry.register(new MultiplyOperation());
        registry.register(new DivideOperation());

        ComplexParser complexParser = new ComplexParser();

        ExpressionArgumentsValidator validator = new ExpressionArgumentsValidator();

        ExpressionParser parser =
                new ExpressionParser(
                        complexParser,
                        registry,
                        validator
                );

        ExpressionEvaluator evaluator =
                new ExpressionEvaluator();

        try {
            Expression expression =
                    parser.parse(args);

            ComplexNumbers result =
                    evaluator.evaluate(expression);

            System.out.println(result);

        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Expresie invalida: " + e.getMessage()
            );
        }
    }
}