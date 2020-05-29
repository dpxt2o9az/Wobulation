let data = [];
const bins = [];
const offset = 0.0;
const start = 0.65;
const step = 0.1;
const binWidth = 0.1;

function preload() {
  data = loadStrings('./data.txt', eachLine, error);
}

function eachLine() {}

function error() {
  console.log('error');
}

function mapToKey(value) {
  return Math.floor(value * 10) * 10;
}

function addEventToBins(value) {
  const idx = mapToKey(value);
  let stored = bins[idx] === undefined ? 0 : bins[idx];
  bins[idx] = stored + 1;
}

function setup() {

  createCanvas(400, 400);

  frameRate(30);
}

let dataIndex = 0;

function case2Step() {
  addEventToBins(float(data[dataIndex++]));
  if (dataIndex > data.length) {
    noLoop();
  }
}

function case1InitialBins() {
  bins.push(1);
  bins.push(2);
  bins.push(3);
  bins.push(8);
  bins.push(8);
  bins.push(5);
  bins.push(5);
  bins.push(3);
  bins.push(1);
}

function draw() {
  case2Step();
  // initial draw
  background(220);

  // find the maximum Y value
  let maxY = 0;
  for (let i = 0; i < bins.length; i++) {
    if (bins[i] > maxY) {
      maxY = bins[i];
    }
  }

  // draw the bins
  for (let i = 0; i < bins.length; i++) {
    if (bins[i] !== undefined) {
      let h = map(bins[i], 0, maxY, 0, height);
      fill('#1f77b4');
      rect(i * width / bins.length, height, (width / bins.length), -h);
    }
    //text("" + Math.round((start + (step * i)) * 100, 2) / 100, i * width / bins.length, height - 38);

  }

}