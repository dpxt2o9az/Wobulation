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
public class NewWobulation {

    private static final BigDecimal DEFAULT_BIN_SIZE = BigDecimal.valueOf(0.1);
    private static final BigDecimal BIG_DECIMAL_TWO = BigDecimal.valueOf(2L);
    private static final BigDecimal binWidth = DEFAULT_BIN_SIZE;

    private int lower = Integer.MAX_VALUE;
    private int upper = Integer.MIN_VALUE;
    private final MeanContainer mean;
    private final Map<BigDecimal, Integer> bins;
    private final double horizon;

    NewWobulation() {
        mean = new MeanContainer();
        bins = new TreeMap<>();
        horizon = 1.0;
    }

    void add(double value) {
        BigDecimal midPoint = midPointFor(value);
        bins.putIfAbsent(midPoint, 0);
        bins.put(midPoint, bins.get(midPoint) + 1);
        mean.updateMean(midPoint);
    }

    // assumptions:
    // - ranges start at 0.0
    // - bin-size is always 0.1
    BigDecimal midPointFor(double value) {
        final BigDecimal halfBinWidth = binWidth.divide(BIG_DECIMAL_TWO);
        BigDecimal midPoint = BigDecimal.valueOf(value);
        midPoint = midPoint.add(halfBinWidth);
        midPoint = midPoint.divide(binWidth);
        return midPoint;
    }

    double mean() {
        return mean.mean().doubleValue();
    }

    static class MeanContainer {

        private BigDecimal mean;
        private int N;

        public MeanContainer() {
            this.mean = BigDecimal.ZERO;
            N = 0;
        }

        void updateMean(BigDecimal value) {
            // ((mean * N) + value) / (N + 1);
            BigDecimal bdN = BigDecimal.valueOf(N);
            BigDecimal newMean = mean.multiply(bdN).add(value).divide(bdN.add(BigDecimal.ONE));
            mean = newMean;
            N++;
        }

        BigDecimal mean() {
            return mean;
        }

    }

}
