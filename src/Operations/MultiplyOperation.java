package Operations;

import Numbers.ComplexNumbers;

public class MultiplyOperation implements Operation{
    public ComplexNumbers apply(ComplexNumbers left, ComplexNumbers right)
    {
        return new ComplexNumbers(left.getRealPart() * right.getRealPart() - left.getImaginaryPart() * right.getImaginaryPart(),
                left.getRealPart() * right.getImaginaryPart() + left.getImaginaryPart() * right.getRealPart());
    }

    public String getSymbol()
    {
        return "*";
    }
    public int getPrecedence()
    {
        return 2;
    }
}
