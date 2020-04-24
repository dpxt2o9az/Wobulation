/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg;

import edu.princeton.cs32.Histogram;
import edu.princeton.cs32.StdDraw;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Comparator;
import java.util.Map.Entry;
import static junit.framework.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Brad
 */
public class FrequencyMapCase1Test {

    public FrequencyMapCase1Test() {
    }

    private FrequencyMap initialBins() {
        FrequencyMap bins = new FrequencyMap();
        bins.put(0.65, 1);
        bins.put(0.75, 2);
        bins.put(0.85, 3);
        bins.put(0.95, 8);
        bins.put(1.05, 8);
        bins.put(1.15, 5);
        bins.put(1.25, 5);
        bins.put(1.35, 3);
        bins.put(1.45, 1);
        return bins;
    }

    private FrequencyMap finalBins() {
        FrequencyMap bins = initialBins();
        double mean = 0.0;
        for (int i = 0; i < 125; i++) {
            bins.bump(0.85);
            mean = bins.mean();
            System.out.println("mean: " + mean);
        }
        return bins;
    }

    @Test
    public void testInitialBins() {
        FrequencyMap bins = initialBins();
        double mean = bins.mean();
        assertEquals(1.061111, mean, 10 / FrequencyMap.SCALE);
//        assertEquals(0.7, bins.calculateClusterMin(), 0.00001);
//        assertEquals(1.4, bins.calculateClusterMax(), 0.00001);
    }

    @Test
    public void testFinalBins() {
        FrequencyMap bins = finalBins();
        final double mean = bins.mean();
        assertEquals(0.897205, mean, 10 / FrequencyMap.SCALE);
//        assertEquals(0.8, bins.calculateClusterMin(), 0.00001);
//        assertEquals(1.4, bins.calculateClusterMax(), 0.00001);
    }

    @Test
    public void testEntrySet() throws Exception {
        FrequencyMap bins = new FrequencyMap();
        try (BufferedReader r = new BufferedReader(new FileReader("Wob_Valid_case2.csv"))) {
            String line;
            while ((line = r.readLine()) != null) {
                double value = Double.parseDouble(line);
                bins.bump(value);
            }
        }

        double min = bins.entrySet().stream().map(b -> b.getKey()*10).min(Double::compare).get();
        double max = bins.entrySet().stream().map(b -> b.getKey()*10).max(Double::compare).get();
        int range = (int) Math.abs(max - min) + 1;

        Histogram h = new Histogram(range);
        for (Entry<Double, Integer> b : bins.entrySet()) {
            for (int j = 0; j < b.getValue(); j++) {
                int thing = (int) (10 * b.getKey() - min);
                h.addDataPoint(thing);
            }
        }
        StdDraw.setCanvasSize(500, 500);
        h.draw();

    }

}
