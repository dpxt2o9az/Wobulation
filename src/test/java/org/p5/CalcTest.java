/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.p5;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Brad
 */
public class CalcTest {
    
    public CalcTest() {
    }
    
     @Test
     public void hypotTest() {
         assertEquals(5.0, Calculation.hypot(3.0, 4.0), 0.000001);
         assertEquals(13.0, Calculation.hypot(5.0, 12.0), 0.000001);
     }
}
