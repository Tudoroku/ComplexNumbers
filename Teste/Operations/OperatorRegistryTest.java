package Operations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static Support.TestSupport.registry;
import static org.junit.jupiter.api.Assertions.*;

class OperatorRegistryTest {
    @ParameterizedTest
    @ValueSource(strings = {"+", "-", "*", "/"})
    void registeredOperatorsCanBeLookedUp(String symbol) {
        OperatorRegistry registry = registry();
        assertTrue(registry.contains(symbol));
        assertEquals(symbol, assertDoesNotThrow(() -> registry.get(symbol)).getSymbol());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "^", "%", " + ", "unknown"})
    void unknownSymbolsAreRejectedWithoutChangingRegistry(String symbol) {
        OperatorRegistry registry = registry();
        assertFalse(registry.contains(symbol));
        assertThrows(IllegalArgumentException.class, () -> registry.get(symbol));
        assertTrue(registry.contains("+"));
    }

    @Test
    void newRegistryIsEmptyAndRegistrationPreservesInstance() {
        OperatorRegistry registry = new OperatorRegistry();
        assertFalse(registry.contains("+"));
        assertThrows(IllegalArgumentException.class, () -> registry.get("+"));
        Operation operation = new AddOperation();
        registry.register(operation);
        assertSame(operation, registry.get("+"));
    }

    @Test
    void registeringSameSymbolReplacesPreviousInstance() {
        OperatorRegistry registry = registry();
        Operation replacement = new AddOperation();
        registry.register(replacement);
        assertSame(replacement, registry.get("+"));
        assertEquals("*", registry.get("*").getSymbol());
    }
}
