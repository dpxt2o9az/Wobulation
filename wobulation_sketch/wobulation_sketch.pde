
void setup() {
  size(800, 640);

  Cluster bins = new Cluster();
  bins.add(0.75, 2);
  bins.add(0.85, 3);
  bins.add(0.95, 8);
  bins.add(1.05, 8);
  bins.add(1.15, 5);
  bins.add(1.25, 5);
  bins.add(1.35, 3);
  bins.add(1.45, 1);

  for (int xx = bins.minKey(); xx < bins.maxKey(); xx += 10) {
    println(xx + " => " + bins.get(xx));
  }
}

void draw() {
}
