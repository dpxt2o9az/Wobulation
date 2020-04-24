/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg;

import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 *
 * @author Brad
 */
public class FrequencyMap {
    public static final double SCALE = 1000000.0;

    SortedMap<Integer, AtomicInteger> freq = new TreeMap<>();

    private int remap(double key) {
        return (int) (key * SCALE);
    }

    public int put(double key, int value) {
        final int realkey = remap(key);
        freq.putIfAbsent(realkey, new AtomicInteger(value));
        AtomicInteger ai = freq.put(realkey, new AtomicInteger(value));
        return ai.get();
    }

    public int bump(double key) {
        final int realkey = remap(key);
        freq.putIfAbsent(realkey, new AtomicInteger(0));
        return freq.get(realkey).incrementAndGet();
    }

    public int size() {
        return freq.size();
    }

    public double mean() {
        int n = freq.entrySet().stream().map(b -> b.getValue().get()).reduce(0, Integer::sum);
        int dividend = freq.entrySet().stream().map(b -> b.getValue().get() * b.getKey()).reduce(0, Integer::sum);
        int mean = dividend / n;
        return mean / SCALE;
    }
    
    public Set<Entry<Double, Integer>> entrySet() {
        return freq.entrySet().stream().map(e-> new MyEntry(e.getKey() / SCALE, e.getValue().get())).collect(Collectors.toSet());
    }

    static class MyEntry implements Entry<Double, Integer> {

        private final double key;
        private int value;
        
        MyEntry(double key, int value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public Double getKey() {
            return key;
        }

        @Override
        public Integer getValue() {
            return value;
        }

        @Override
        public Integer setValue(Integer value) {
            int temp = this.value;
            this.value = value;
            return temp;
        }
        
    }
    
}
