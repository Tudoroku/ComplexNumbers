package Operations;

import Numbers.ComplexNumbers;

public class AddOperation implements Operation{

    @Override
    public ComplexNumbers apply(ComplexNumbers left, ComplexNumbers right) {
        return new ComplexNumbers(left.getRealPart() + right.getRealPart(), left.getImaginaryPart() + right.getImaginaryPart());
    }
    @Override
    public String getSymbol() {
        return "+";
    }
    @Override
    public int getPrecedence() {
        return 1;
    }
}
