package org.mkzh.polysolve;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class MiscTest {
    @Test
    public void testMisc() {
        assertDoesNotThrow(() -> {
            Polysolve.solve("x^2x^3/x-(x^2+30)sin(3)/2-25 = x^3+3x^4", "x");
        });
    }
}
