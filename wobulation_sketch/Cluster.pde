import java.util.*;

class Cluster {

  private static final double BIN_WIDTH = 0.1; 
  private final SortedSet<Bin> data = new TreeSet<Bin>();

  private int keyFor(double value) {
    return (int) ((Math.floor(value * 10.0) + (BIN_WIDTH / 2.0))*10);
  }

  public void add(double k, int count) {
    println("adding " + k + " " + count);
    int kk = keyFor(k);
    if (Collections.binarySearch(data, kk); 
    data.putIfAbsent(kk, 0);
    data.put(kk, data.get(kk) + count);
  }

  public void put(double k, int count) {
    int kk = keyFor(k);
    data.put(kk, count);
  }

  public int get(double k) {
    final int kk = keyFor(k); 
    return data.get(kk) != null ? data.get(kk) : 0;
  }

  public int minKey() {
    int mink = Integer.MAX_VALUE;
    for (int k : data.keySet()) {
      if (k < mink) {
        mink = k;
      }
    }
    return mink;
  }

  public int maxKey() {
    int maxk = Integer.MIN_VALUE;
    for (int k : data.keySet()) {
      if (k > maxk) {
        maxk = k;
      }
    }
    return maxk;
  }

  public int maxValue() {
    int maxValue = Integer.MIN_VALUE;
    for (Integer value : data.values()) {
      if (value > maxValue) {
        maxValue = value;
      }
    }
    return maxValue;
  }
}
