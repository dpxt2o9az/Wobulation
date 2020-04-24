/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg;

import edu.princeton.cs32.Histogram;
import edu.princeton.cs32.StdDraw;
import edu.princeton.cs32.StdIn;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import org.p5.Calculation;
import pkg.Cluster.Bin;

/**
 *
 * @author Brad
 */
public class HistogramDrawer {

    public static void main(String[] args) throws Exception {
        caseTheSecond();
    }

    private static void caseTheSecond() throws NumberFormatException, IOException {
        Cluster cluster = readClusterDataFromFile();
        Set<Bin> bins = cluster.getBins();
        for (Bin b : bins) {
            System.out.printf("%d\t", b.count);
        }
        System.out.println("");
        for (Bin b : bins) {
            System.out.printf("%.1f\t", b.midPoint - 0.05);
        }
        System.out.println("");
//        drawBins(bins);
    }

    private static void caseTheFirst() {
        Cluster cluster = initialCluster();
        Set<Bin> bins = cluster.getBins();
        drawBins(bins);
        StdIn.readLine();
        cluster = finalCluster();
        bins = cluster.getBins();
        drawBins(bins);
    }

    private static Cluster readClusterDataFromFile() throws IOException, NumberFormatException {
        Cluster cluster = new Cluster();
        try (BufferedReader r = new BufferedReader(new FileReader("Wob_Valid_case2.csv"))) {
            String line;
            while ((line = r.readLine()) != null) {
                double value = Double.parseDouble(line);
                cluster.add(value);
                System.out.println(cluster.getMean());
//                Set<Bin> bins = cluster.getBins();
//                drawBins(bins);
//                StdIn.readLine();
            }
        }
        return cluster;
    }

    private static void drawBins(Set<Bin> bins) {
        double min = 10 * bins.stream().map(b -> b.midPoint).min(Double::compare).get() - 1;
        double max = 10 * bins.stream().map(b -> b.midPoint).max(Double::compare).get() + 1;
        int range = (int) (max - min);

        Histogram h = new Histogram(range);
        for (Bin b : bins) {
            for (int j = 0; j < b.count; j++) {
                int thing = (int) Calculation.map(b.midPoint * 10, min, max, 0, range, true);
                h.addDataPoint(thing);
            }
        }
        StdDraw.setCanvasSize(500, 500);
        h.draw();
    }

    private static Cluster initialCluster() {
        Cluster cluster = new Cluster();
        cluster.add(0.65, 1);
        cluster.add(0.75, 2);
        cluster.add(0.85, 3);
        cluster.add(0.95, 8);
        cluster.add(1.05, 8);
        cluster.add(1.15, 5);
        cluster.add(1.25, 5);
        cluster.add(1.35, 3);
        cluster.add(1.45, 1);
        return cluster;
    }

    private static Cluster finalCluster() {
        Cluster cluster = initialCluster();
        for (int i = 0; i < 125; i++) {
            cluster.add(0.85);
        }
        return cluster;
    }

}
