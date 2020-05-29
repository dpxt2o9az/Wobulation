/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Brad
 */
public class BinNumber {

    private static final double l = 0.0, u = 10.0;
    private static final int n = 1000;

    public static void main(String[] args) {
        Map<Integer, Integer> bins = new TreeMap<>();
        final double value = 0.80;
        int index = binNumberFor(value);
        System.out.println("index: " + index + ", value: " + value);
    }

    static int binNumberFor(double x) {
        return (int) map(x, l, u, 1, n);
    }

    static double map(double x, double dl, double du, double rl, double ru) {
        return rl + ((ru - rl) * (x - dl) / (du - dl));
    }

}
