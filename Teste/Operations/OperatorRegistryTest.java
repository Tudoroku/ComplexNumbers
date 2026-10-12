package Operations;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OperatorRegistryTest {
    @Test
    void registersAndFindsAnOperator() {
        OperatorRegistry registry = new OperatorRegistry();
        registry.register(new AddOperation());

        assertTrue(registry.contains("+"));
        assertEquals("+", registry.get("+").getSymbol());
    }

    @Test
    void rejectsAnUnknownOperator() {
        OperatorRegistry registry = new OperatorRegistry();

        assertFalse(registry.contains("^"));
        assertThrows(IllegalArgumentException.class, () -> registry.get("^"));
    }
}
