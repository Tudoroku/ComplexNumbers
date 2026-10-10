package Expressions;

import Numbers.ComplexNumbers;
import Operations.Operation;

import java.util.List;

public class Expression {
    private final List<ComplexNumbers> operands;
    private final List<Operation> operations;

    public Expression(List<ComplexNumbers> operands,
                      List<Operation> operations) {
        this.operands = operands;
        this.operations = operations;
    }

    public List<ComplexNumbers> getOperands() {
        return operands;
    }

    public List<Operation> getOperations() {
        return operations;
    }
}