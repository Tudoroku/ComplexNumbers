package Operations;

import Numbers.ComplexNumbers;

public interface Operation {
    public ComplexNumbers apply(ComplexNumbers left, ComplexNumbers right);
    String getSymbol();
    int getPrecedence();
}
