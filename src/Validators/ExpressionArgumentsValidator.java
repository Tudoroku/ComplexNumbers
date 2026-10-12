package Validators;

public class ExpressionArgumentsValidator implements Validator<String[]> {

    @Override
    public void validate(String[] args) {
        if(args == null || args.length == 0) {
            throw new IllegalArgumentException("Expression cannot be empty");
        }

        if(args.length % 2 == 0) {
            throw new IllegalArgumentException("Expected: operand opeartor operand operator ...");
        }

        for(int i = 1; i < args.length; i += 2) {
            if(args[i].isBlank())
            {
                throw new IllegalArgumentException("Operator cannot be empty");
            }
        }
    }


}
