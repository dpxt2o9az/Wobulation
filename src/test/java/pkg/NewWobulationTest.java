/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg;

import java.math.BigDecimal;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

/**
 *
 * @author Brad
 */
public class NewWobulationTest {

    @Test
    public void testMidPoint01() {
        NewWobulation w = new NewWobulation();
        BigDecimal m = w.midPointFor(0.80);
        final BigDecimal expected = BigDecimal.valueOf(0.85);
        assertEquals(expected, m);
        m = w.midPointFor(0.83);
        assertEquals(expected, m);
        m = w.midPointFor(0.86);
        assertEquals(expected, m);
        m = w.midPointFor(0.89);
        assertEquals(expected, m);
        m = w.midPointFor(0.79);
        assertNotEquals(expected, m);
        m = w.midPointFor(0.9);
        assertNotEquals(expected, m);
    }
    
    @Test
    public void logTest() {
        double binSize = 0.1;
        double multiplier = Math.log10(binSize);
        assertEquals(-1.0, multiplier, 0.000001);
        double value = 0.85;

    }
}
