import org.junit.Before;
import org.junit.Test;
import shape.Rect;

import static org.junit.Assert.*;

public class RectTest {
    private Rect rA;
    private Rect rB;
    private Rect rC;

    @Before
    public void setup() {
        rA = new Rect(0, 0, 1, 1);
        rB = Rect.getRectByCenterSize(0.5, 0.5, 1, 1);
        rC = Rect.getRectByCornerSize(0, 0, 1, 1);
    }

    @Test
    public void testInitializationSameness() {
        for (double i = -1; i < 2; i += 0.01) {
            for (double j = -1; j < 2; j += 0.01) {
                assertTrue(rA.inBounds(i, j) == rB.inBounds(i, j)
                    && rA.inBounds(i, j) == rC.inBounds(i, j)
                    && rB.inBounds(i, j) == rC.inBounds(i, j));

                assertTrue(rC.inBounds(i, j) == rB.inBounds(i, j)
                        && rC.inBounds(i, j) == rA.inBounds(i, j)
                        && rB.inBounds(i, j) == rA.inBounds(i, j));
            }
        }
    }

    @Test
    public void testInBounds() {
        for (double i = -1; i < 2; i += 0.01) {
            for (double j = -1; j < 2; j += 0.01) {
                if (i < 0 || i > 1 || j < 0 || j > 1) {
                    assertFalse(rA.inBounds(i, j));
                } else {
                    assertTrue(rA.inBounds(i, j));
                }
            }
        }
    }

    @Test
    public void testIntersects() {
        for (double i = -1.1; i < 2.1; i += 0.01) {
            Rect t = Rect.getRectByCornerSize(i, 0, 1, 1);
            if (i < -1 || i > 1) {
                assertFalse(rA.intersects(t));
            } else {
                assertTrue(rA.intersects(t));
            }
        }
    }
}
