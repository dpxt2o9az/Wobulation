
class Bin implements Comparable<Bin> {
  
  private final int key;
  private int count;
  
  public Bin(int key) {
    this.key = key;
    this.count = 0;
  }
  
  public void inc() {
    this.count++;
  }
  
  public void add(int count) { 
    this.count += count;
  }
  
  public Integer getCount() {
    return this.count;
  }
  
  public Integer getKey() {
    return this.key;
  }
  
  public Integer setCount(Integer newCount) {
    int replacedValue = this.count;
    this.count = newCount;
    return replacedValue;
  }
  
  @Override
  public int compareTo(Bin other) {
    return this.key - other.key;
  }
  
}
