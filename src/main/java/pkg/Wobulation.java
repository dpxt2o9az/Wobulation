/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author Brad
 */
public class Wobulation {

    public static class BinContainer {

        private static final double DEFAULT_BIN_WIDTH = 0.1;
        private static final double DEFAULT_HORIZON = 1.0;
        private static final MeanBinContributionCalculator DEFAULT_CALC = new ProportionalMeanBinContributionCalculator();

        private final double binWidth;
        private final double p = 0.90;
        private final MeanBinContributionCalculator meanBinCalculator;

        BinContainer() {
            this(DEFAULT_BIN_WIDTH, DEFAULT_CALC);
        }

        BinContainer(double binWidth) {
            this(binWidth, DEFAULT_CALC);
        }

        BinContainer(MeanBinContributionCalculator calc) {
            this(DEFAULT_BIN_WIDTH, calc);
        }

        BinContainer(double binWidth, MeanBinContributionCalculator calc) {
            if (Math.abs(binWidth - DEFAULT_BIN_WIDTH) > 0.00001) {
                System.err.println("WARNING: Untested with anything other than binWidth: " + DEFAULT_BIN_WIDTH);
            }
            this.binWidth = binWidth;
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
            if (binFor == null) {
                binFor = new Bin(midPoint);
                bins.add(binFor);
            }
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
//            final Bin newBin = new Bin(midPoint);
//            bins.add(newBin);
//            return newBin;
            return null;
        }

        double calculateClusterMin() {
            // 3. Update the cluster min to be the lower boundary of bin containing the (1-p)th percentile of the data below the new mean.
            // my assumptions:
            //   for this single "cluster" task, 100% of the data is all the data...
            // calculate the mean; we need N so, go ahead and copy-paste here for now
            MeanContext mc = calculateMean();

            Bin binFor = binFor(midPoint(mc.mean));

            final double meanBinLowerContribution = meanBinCalculator.calculateLowerContribution(binFor.count, lower(mc.mean), mc.mean, this.binWidth);

            double totalLowerContribution = calculateTotalLowerContribution(meanBinLowerContribution, binFor.midPoint);

            // now do it again, but only accumulate up to p
            final double targetContribution = totalLowerContribution * (p);
            double contribution = meanBinLowerContribution;
            double midPoint = midPoint(mc.mean) - (this.binWidth);
            while (contribution < targetContribution && binFor(midPoint) != null) {
                final int binCount = binFor(midPoint).count;
                contribution += binCount;
                midPoint -= (this.binWidth);
            }

            return lower(midPoint + this.binWidth);
        }

        double calculateClusterMax() {
            // 4. Update the cluster max to be the upper boundary of the bin
            //    containing the pth percentile of the data above the new mean.
            // my assumptions:
            //   for this single "cluster" task, 100% of the data is all the data...
            MeanContext mc = calculateMean();

            Bin binFor = binFor(midPoint(mc.mean));

            final double meanBinUpperContribution = meanBinCalculator.calculateUpperContribution(binFor.count, upper(mc.mean), mc.mean, binWidth);

            double totalUpperContribution = calculateTotalUpperContribution(meanBinUpperContribution, binFor.midPoint);

            // now do it again, but only accumulate up to p
            double targetContribution = totalUpperContribution * (p);
            double contribution = meanBinUpperContribution;
            double midPoint = midPoint(mc.mean) + binWidth;
            while (contribution < targetContribution && binFor(midPoint) != null) {
                final int bin = binFor(midPoint).count;
                contribution += bin;
                midPoint += binWidth;
            }

            return upper(midPoint - binWidth);
        }

        private double calculateTotalLowerContribution(final double meanBinLowerContribution, final double startingMidPoint) {
            // sum up the all bin-counts lower than the mean
            double totalLowerContribution = meanBinLowerContribution;

            double midPoint = startingMidPoint;
            System.out.printf("lcf %f @ %f\n", totalLowerContribution, midPoint);
            midPoint -= this.binWidth;
            while (binFor(midPoint) != null) {
                totalLowerContribution += binFor(midPoint).count;
                System.out.printf("lcf %f @ %f\n", totalLowerContribution, midPoint);
                midPoint -= this.binWidth;
            }
            return totalLowerContribution;
        }

        private double calculateTotalUpperContribution(final double meanBinUpperContribution, final double startingMidPoint) {
            // sum up the all bin-counts lower than the mean
            double totalUpperContribution = meanBinUpperContribution;
            double midPoint = startingMidPoint;
            System.out.printf("ucf %f @ %f\n", totalUpperContribution, midPoint);
            midPoint += this.binWidth;
            while (binFor(midPoint) != null) {
                totalUpperContribution += binFor(midPoint).count;
                System.out.printf("ucf %f @ %f\n", totalUpperContribution, midPoint);
                midPoint += this.binWidth;
            }
            return totalUpperContribution;
        }

        double lower(double value) {
            // todo: this only works for bin-width of 0.1
            return Math.floor(value / binWidth) * binWidth;
        }

        double upper(double value) {
            // todo: this only works for bin-width of 0.1
            return (Math.floor(value / binWidth) * binWidth) + binWidth;
        }

        double midPoint(double value) {
            return lower(value) + binWidth / 2.0;
        }

        void dump() {
            for (Bin b : bins) {
                System.out.print(b + ", ");
            }
            System.out.printf("{ mean: %.4f, Cmin: $.4f, Cmax: %.4f }\n", calculateMean().mean, calculateClusterMin(), calculateClusterMax());
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
            this(midPoint, 0);
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

        @Override
        public String toString() {
            return String.format("{ midPoint: %.4f, count: %d }", midPoint, count);

        }

    }

    interface MeanBinContributionCalculator {

        double calculateLowerContribution(int count, double lower, double mean, double binWidth1);

        double calculateUpperContribution(int count, double upper, double mean, double binWidth1);
    }

    static class ProportionalMeanBinContributionCalculator implements MeanBinContributionCalculator {

        @Override
        public double calculateLowerContribution(final int count, final double lower, final double mean, final double binWidth) {
            return count * ((mean - lower) / (binWidth));
        }

        @Override
        public double calculateUpperContribution(final int count, final double upper, final double mean, final double binWidth) {
            return count * ((upper - mean) / (binWidth));
        }

    }

    static class HalfAssMeanBinContributionCalculator implements MeanBinContributionCalculator {

        @Override
        public double calculateLowerContribution(final int count, final double lower, final double mean, final double binWidth) {
            return count / 2.0;
        }

        @Override
        public double calculateUpperContribution(final int count, final double upper, final double mean, final double binWidth) {
            return count / 2.0;
        }
    }

}
