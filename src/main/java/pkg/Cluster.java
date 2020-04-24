/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg;

import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author Brad
 */
public class Cluster {

    double binWidth = 0.1;
    double horizon = 10 * binWidth;
//    double min, max, mean;  // cached? calculated? properties?
//    boolean dirty = true;  // need to calculate?
    SortedSet<Bin> bins = new TreeSet<>();

    void add(double value, int count) {
        for (int i = 0; i < count; i++) {
            add(value);
        }
    }

    void add(double value) {
        // find bin with midPoint of value's bin
        double midPoint = midPoint(value);
        Bin binFor = binFor(midPoint);

        // if not found, create it
        if (binFor == null) {
            binFor = new Bin(midPoint);
            bins.add(binFor);
        }
        // otherwise, bump its counter by one
        binFor.bump();

        adjustBins();
    }

    Set<Bin> getBins() {
        return Collections.unmodifiableSet(this.bins);
    }

    private double calculateMax() {
        return bins.last().midPoint;
    }

    private double calculateMin() {
        return bins.first().midPoint;
    }

    Bin binFor(double midPoint) {
        for (Bin b : bins) {
            if (Math.abs(b.midPoint - midPoint) < 0.00001) {
                return b;
            }
        }
        return null;
    }

    double getMean() {
        return calculateMean();
    }

    private double calculateMean() {
        long n = bins.stream().map(b -> (long) b.count).reduce(0L, Long::sum);
        double dividend = bins.stream().map(b -> b.count * b.midPoint).reduce(0.0, Double::sum);
        double mean = dividend / n;
        return mean;
    }

    double getMax() {
        double mean = calculateMean();
        double lastNonZeroMidPoint = bins.stream().filter(b -> b.count > 0).sorted((a, b) -> ((int) Math.signum(b.midPoint - a.midPoint))).findFirst().get().midPoint;
        double max = (mean + lastNonZeroMidPoint) / 2.0;
        return max;
    }

    double getMin() {
        double mean = calculateMean();
        double firstNonZeroMidPoint = bins.stream().filter(b -> b.count > 0).sorted((a, b) -> ((int) Math.signum(a.midPoint - b.midPoint))).findFirst().get().midPoint;
        double min = (mean + firstNonZeroMidPoint) / 2.0;
        return min;
    }

    private void adjustBins() {
        double lowest = midPoint(getMin() - horizon);
        double higher = midPoint(getMax() + horizon);
        for (double x = lowest; x <= higher; x += binWidth) {
            double newMidPoint = midPoint(x);
            if (binFor(newMidPoint) == null) {
                bins.add(new Bin(newMidPoint));
            }
        }
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
        return Math.floor(value / binWidth) * binWidth + binWidth / 2.0;
    }

    class Bin implements Comparable<Bin> {

        final double midPoint;
        int count;

        Bin(double midPoint) {
            this.midPoint = midPoint;
            this.count = 0;
        }

        void bump() {
            count++;
        }

        @Override
        public int compareTo(Bin o) {
            return (int) Math.signum(this.midPoint - o.midPoint);
        }

    }
}
