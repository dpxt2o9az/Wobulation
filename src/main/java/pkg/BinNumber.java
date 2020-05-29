/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Brad
 */
public class BinNumber {

    private static final double l = 0.0, u = 10.0;
    private static final int n = 100;
    private static final BigDecimal binWidth = BigDecimal.valueOf(2.0);

    public static void main(String[] args) {
        Map<Integer, Integer> bins = new TreeMap<>();
        final double value = 0.80;
        final BigDecimal bdValue = BigDecimal.valueOf(value);
        int index = binNumberFor(bdValue);
        BigDecimal midPoint = binMidPointFor(bdValue);
        System.out.printf("index: %d, bin midPoint: %.4f, value: %.4f%n", index, midPoint, value);
    }

    static int binNumberFor(BigDecimal x) {
        return (int) x.divide(binWidth).doubleValue();
    }
    
    static BigDecimal domainMinFor(BigDecimal x) {
        return x.divideToIntegralValue(binWidth).multiply(binWidth);
    }
    
    static BigDecimal binMidPointFor(BigDecimal x) {
        return domainMinFor(x).add(binWidth.divide(BigDecimal.valueOf(2L)));
    }

    static double map(double x, double dl, double du, double rl, double ru) {
        return rl + ((ru - rl) * (x - dl) / (du - dl));
    }

}
