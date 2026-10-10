package Operations;

import java.util.HashMap;
import java.util.Map;

public class OperatorRegistry {

    private final Map<String, Operation> operations =
            new HashMap<>();

    public void register(Operation operation) {
        operations.put(
                operation.getSymbol(),
                operation
        );
    }

    public boolean contains(String symbol) {
        return operations.containsKey(symbol);
    }

    public Operation get(String symbol) {
        Operation operation = operations.get(symbol);

        if (operation == null) {
            throw new IllegalArgumentException(
                    "Operator necunoscut: " + symbol
            );
        }

        return operation;
    }
}