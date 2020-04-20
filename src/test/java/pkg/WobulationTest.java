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

    private BinContainer initialBins() {
        final MeanBinContributionCalculator meanBinContributionCalculator = new ProportionalMeanBinContributionCalculator();
        BinContainer bins = new BinContainer(meanBinContributionCalculator);
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
        double mean = 0.0;
        for (int i = 0; i < 125; i++) {
            bins.bump(0.85);
            mean = bins.calculateMean().mean;
            System.out.println("mean: " + mean);
        }
        return bins;
    }

    @Test
    public void testInitialBins() {
        BinContainer bins = initialBins();
        double mean = bins.calculateMean().mean;
        assertEquals(1.061111, mean, 0.00001);
        assertEquals(0.7, bins.findClusterMin(), 0.00001);
        assertEquals(1.4, bins.findClusterMax(), 0.00001);
    }

    @Test
    public void testFinalBins() {
        BinContainer bins = finalBins();
        final double mean = bins.calculateMean().mean;
        assertEquals(0.897205, mean, 0.00001);
        assertEquals(0.8, bins.findClusterMin(), 0.00001);
        assertEquals(1.4, bins.findClusterMax(), 0.00001);
    }

    public static class BinContainer {

        private final double binWidth = 0.1;
        private final double p = 0.90;
        private final MeanBinContributionCalculator meanBinCalculator;

        public BinContainer(MeanBinContributionCalculator calc) {
            this.meanBinCalculator = calc;
        }

        SortedSet<Bin> bins = new TreeSet<>();

        MeanContext calculateMean() {
            MeanContext mc = new MeanContext();
            mc.n = bins.stream().map(b -> b.count).reduce(0, Integer::sum);
            mc.dividend = bins.stream().map(b -> b.count * b.midPoint).reduce(0.0, Double::sum);
            mc.mean = mc.dividend / mc.n;
            return mc;
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
            MeanContext mc = calculateMean();

            Bin binFor = binFor(midPoint(mc.mean));

            final double meanBinLowerContribution = meanBinCalculator.calculateLowerContribution(binFor.count, lower(mc.mean), mc.mean, this.binWidth);

            // sum up the all bin-counts lower than the mean
            double totalLowerContribution = meanBinLowerContribution;
            double midPoint = binFor.midPoint;
            System.out.printf("lcf %f @ %f\n", totalLowerContribution, midPoint);
            midPoint -= (this.binWidth);

            while (binFor(midPoint) != null) {
                totalLowerContribution += binFor(midPoint).count;
                System.out.printf("lcf %f @ %f\n", totalLowerContribution, midPoint);
                midPoint -= (this.binWidth);
            }

            // now do it again, but only accumulate up to p
            double targetContribution = totalLowerContribution * (p);
            double contribution = meanBinLowerContribution;
            midPoint = midPoint(mc.mean) - (this.binWidth);
            while (contribution < targetContribution && binFor(midPoint) != null) {
                final int binCount = binFor(midPoint).count;
                contribution += binCount;
                midPoint -= (this.binWidth);
            }

            return lower(midPoint + this.binWidth);
        }

        double findClusterMax() {
            // 4. Update the cluster max to be the upper boundary of the bin
            //    containing the pth percentile of the data above the new mean.
            // my assumptions:
            //   for this single "cluster" task, 100% of the data is all the data...
            MeanContext mc = calculateMean();

            Bin binFor = binFor(midPoint(mc.mean));
            final double binWidth1 = binWidth;

            final double meanBinUpperContribution = meanBinCalculator.calculateUpperContribution(binFor.count, upper(mc.mean), mc.mean, binWidth1);

            // sum up the all bin-counts lower than the mean
            double totalUpperContribution = meanBinUpperContribution;
            double midPoint = binFor.midPoint;
            System.out.printf("ucf %f @ %f\n", totalUpperContribution, midPoint);
            midPoint += binWidth1;

            while (binFor(midPoint) != null) {
                totalUpperContribution += binFor(midPoint).count;
                System.out.printf("ucf %f @ %f\n", totalUpperContribution, midPoint);
                midPoint += binWidth1;
            }

            // now do it again, but only accumulate up to p
            double targetContribution = totalUpperContribution * (p);
            double contribution = meanBinUpperContribution;
            midPoint = midPoint(mc.mean) + binWidth1;
            while (contribution < targetContribution && binFor(midPoint) != null) {
                final int bin = binFor(midPoint).count;
                contribution += bin;
                midPoint += binWidth1;
            }

            return upper(midPoint - binWidth1);
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

    static class MeanContext {

        int n;
        double dividend;
        double mean;
    }

    static class Bin implements Comparable<Bin> {

        final double midPoint;
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
            this.count++;
        }

        @Override
        public int compareTo(Bin o) {
            return Double.compare(this.midPoint, o.midPoint);
        }

    }

    interface MeanBinContributionCalculator {

        double calculateLowerContribution(int count, double lower, double mean, double binWidth1);

        double calculateUpperContribution(int count, double upper, double mean, double binWidth1);
    }

    static class ProportionalMeanBinContributionCalculator implements MeanBinContributionCalculator {

        public double calculateLowerContribution(final int count, final double lower, final double mean, final double binWidth) {
            return count * ((mean - lower) / (binWidth));
        }

        public double calculateUpperContribution(final int count, final double upper, final double mean, final double binWidth) {
            return count * ((upper - mean) / (binWidth));
        }

    }

    static class HalfAssMeanBinContributionCalculator implements MeanBinContributionCalculator {

        public double calculateLowerContribution(final int count, final double lower, final double mean, final double binWidth) {
            return count / 2.0;
        }

        public double calculateUpperContribution(final int count, final double upper, final double mean, final double binWidth) {
            return count / 2.0;
        }
    }

}
