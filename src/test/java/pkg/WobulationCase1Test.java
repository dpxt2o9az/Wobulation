/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg;

import static junit.framework.Assert.assertEquals;
import org.junit.Test;
import pkg.Wobulation.BinContainer;
import pkg.Wobulation.MeanBinContributionCalculator;
import pkg.Wobulation.ProportionalMeanBinContributionCalculator;

/**
 *
 * @author Brad
 */
public class WobulationCase1Test {

    private static final double FP_ACCURACY = 0.00001;

    public WobulationCase1Test() {
    }

    private BinContainer initialBins() {
        final MeanBinContributionCalculator meanBinContributionCalculator = new ProportionalMeanBinContributionCalculator();
        BinContainer bins = new BinContainer(0.1, meanBinContributionCalculator);
        bins.add(0.65, 1);
        bins.add(0.75, 2);
        bins.add(0.85, 3);
        bins.add(0.95, 8);
        bins.add(1.05, 8);
        bins.add(1.15, 5);
        bins.add(1.25, 5);
        bins.add(1.35, 3);
        bins.add(1.45, 1);
        return bins;
    }

    private BinContainer finalBins() {
        BinContainer bins = initialBins();
        for (int i = 0; i < 125; i++) {
            bins.bump(0.85);
            double mean = bins.calculateMean().mean;
            System.out.println("mean: " + mean);
        }
        return bins;
    }

    @Test
    public void testInitialBins() {
        BinContainer bins = initialBins();
        double mean = bins.calculateMean().mean;
        assertEquals(1.061111, mean, FP_ACCURACY);
        assertEquals(0.7, bins.calculateClusterMin(), FP_ACCURACY);
        assertEquals(1.4, bins.calculateClusterMax(), FP_ACCURACY);
    }

    @Test
    public void testFinalBins() {
        BinContainer bins = finalBins();
        final double mean = bins.calculateMean().mean;
        assertEquals(0.897205, mean, FP_ACCURACY);
        assertEquals(0.8, bins.calculateClusterMin(), FP_ACCURACY);
        assertEquals(1.4, bins.calculateClusterMax(), FP_ACCURACY);
    }

}
