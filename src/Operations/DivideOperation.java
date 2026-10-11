package Operations;

import Numbers.ComplexNumbers;

public class DivideOperation implements Operation{
    @Override
    public ComplexNumbers apply(ComplexNumbers left, ComplexNumbers right)
    {
        if(right.getRealPart() == 0 && right.getImaginaryPart() == 0)
        {
            throw new ArithmeticException("Division by zero");
        }
        return new ComplexNumbers((left.getRealPart() * right.getRealPart() + left.getImaginaryPart() * right.getImaginaryPart())
                / (right.getRealPart() * right.getRealPart() + right.getImaginaryPart() * right.getImaginaryPart()),
                (right.getRealPart() * left.getImaginaryPart() - left.getRealPart() * right.getImaginaryPart())
                / (right.getRealPart() * right.getRealPart() + right.getImaginaryPart() * right.getImaginaryPart()));
    }
    @Override
    public String getSymbol()
    {
        return "/";
    }
    @Override
    public int getPrecedence()
    {
        return 2;
    }
}
