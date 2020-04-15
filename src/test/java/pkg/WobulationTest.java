/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg;

import java.util.SortedSet;
import java.util.TreeSet;
import static junit.framework.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Brad
 */
public class WobulationTest {

    public WobulationTest() {
    }

    BinContainer initialBins() {
        BinContainer bins = new BinContainer();
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

    @Test
    public void testInitialMean() {

        BinContainer bins = initialBins();

        double mean = bins.calculateMean();
        System.out.println("mean: " + mean);

        assertEquals(1.061111, mean, 0.00001);
    }

    @Test
    public void testFinalMean() {

        BinContainer bins = finalBins();
        final double mean = bins.calculateMean();
        assertEquals(0.897205, mean, 0.00001);

    }

    private BinContainer finalBins() {
        BinContainer bins = initialBins();
        double mean = 0.0;
        for (int i = 0; i < 125; i++) {
            bins.bump(0.85);
            mean = bins.calculateMean();
            System.out.println("mean: " + mean);
        }
        return bins;
    }

    @Test
    public void testInitialClusterMinValue() {
        BinContainer bins = initialBins();
        assertEquals(0.7, bins.findClusterMin(), 0.00001);
    }

    @Test
    public void testInitialClusterMaxValue() {
        BinContainer bins = initialBins();
        assertEquals(1.4, bins.findClusterMax(), 0.00001);
    }
    
    @Test
    public void testFinalClusterMinValue() {
        BinContainer bins = finalBins();
        assertEquals(0.8, bins.findClusterMin(), 0.000001);
    }
    
    @Test
    public void testFinalClusterMaxValue() {
        BinContainer bins = finalBins();
        assertEquals(1.0, bins.findClusterMax(), 0.00001);
    }
    
    
    
    public static class BinContainer {

        private double binWidth = 0.1;
        private double p = 0.90;

        SortedSet<Bin> bins = new TreeSet<>();

        double calculateMean() {
            Integer divisor = bins.stream().map(b -> b.count).reduce(0, Integer::sum);
            double dividend = bins.stream().map(b -> b.count * b.midPoint).reduce(0.0, Double::sum);
            double mean = dividend / divisor;
            return mean;
        }

        void add(double midPoint, int initialCount) {
            bins.add(new Bin(midPoint, initialCount));
        }

        void bump(double value) {
            double midPoint = midPoint(value);
            Bin binFor = binFor(midPoint);
            binFor.bump();
        }

        private Bin binFor(double midPoint) {
            // todo: this is crap, but it might work
            System.out.println("binFor midPoint: " + midPoint);
            for (Bin b : bins) {
                final double difference = Math.abs(b.midPoint - midPoint);
                if (difference < 0.00000001) {
                    return b;
                }
            }
            return null;
        }

        double findClusterMin() {
            // 3. Update the cluster min to be the lower boundary of bin containing the (1-p)th percentile of the data below the new mean.
            // my assumptions:
            //   for this single "cluster" task, 100% of the data is all the data...
            // calculate the mean; we need N so, go ahead and copy-paste here for now
            Integer N = bins.stream().map(b -> b.count).reduce(0, Integer::sum);
            double dividend = bins.stream().map(b -> b.count * b.midPoint).reduce(0.0, Double::sum);
            double mean = dividend / N;

            Bin binFor = binFor(midPoint(mean));
            System.out.println("this succeeds");

            final double meanBinLowerContribution = binFor.count * ((mean - lower(mean)) / (binWidth));

            // sum up the all bin-counts lower than the mean
            double totalLowerContribution = meanBinLowerContribution;
            double midPoint = binFor.midPoint -= binWidth;
            while (binFor(midPoint) != null) {
                totalLowerContribution += binFor(midPoint).count;
                midPoint -= binWidth;
            }

            // now do it again, but only accumulate up to p
            double targetContribution = totalLowerContribution * (p);
            double contribution = meanBinLowerContribution;
            midPoint = midPoint(mean) - binWidth;
            while (contribution < targetContribution && binFor(midPoint) != null) {
                final int bin = binFor(midPoint).count;
                contribution += bin;
                midPoint -= binWidth;
            }

            return lower(midPoint + binWidth);
        }

        double findClusterMax() {
            // 4. Update the cluster max to be the upper boundary of the bin
            //    containing the pth percentile of the data above the new mean.
            // my assumptions:
            //   for this single "cluster" task, 100% of the data is all the data...

            // calculate the mean; we need N so, go ahead and copy-paste here for now
            Integer N = bins.stream().map(b -> b.count).reduce(0, Integer::sum);
            double dividend = bins.stream().map(b -> b.count * b.midPoint).reduce(0.0, Double::sum);
            double mean = dividend / N;

            Bin binFor = binFor(midPoint(mean));
            System.out.println("this does not");
            final double meanBinUpperContribution = binFor.count * ((upper(mean) - mean) / (binWidth));
            System.out.println("herp derp now it does!?!?");

            // sum up the all bin-counts lower than the mean
            double totalUpperContribution = meanBinUpperContribution;
            double midPoint = binFor.midPoint += binWidth;
            while (binFor(midPoint) != null) {
                totalUpperContribution += binFor(midPoint).count;
                midPoint += binWidth;
            }

            // now do it again, but only accumulate up to p
            double targetContribution = totalUpperContribution * (p);
            double contribution = meanBinUpperContribution;
            midPoint = midPoint(mean) + binWidth;
            while (contribution < targetContribution && binFor(midPoint) != null) {
                final int bin = binFor(midPoint).count;
                contribution += bin;
                midPoint += binWidth;
            }

            return upper(midPoint - binWidth);
        }

        double lower(double value) {
            // todo: this only works for bin-width of 0.1
            return Math.floor(value * 10) / 10;
        }

        double upper(double value) {
            // todo: this only works for bin-width of 0.1
            return Math.floor(value * 10) / 10 + binWidth;
        }

        double midPoint(double value) {
            return lower(value) + binWidth / 2.0;
        }

    }

    static class Bin implements Comparable<Bin> {

        double midPoint;
        int count;

        Bin(double midPoint) {
            this(midPoint, 1);
        }

        Bin(double midPoint, int initialCount) {
            super();
            this.midPoint = midPoint;
            this.count = initialCount;
        }

        void bump() {
            System.out.printf("bumping %f from %d", this.midPoint, this.count);
            this.count++;
            System.out.printf(" to %d\n", this.count);
        }

        @Override
        public int compareTo(Bin o) {
            return Double.compare(this.midPoint, o.midPoint);
        }

    }

}
