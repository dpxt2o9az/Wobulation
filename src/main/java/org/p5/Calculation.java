package org.p5;

public class Calculation {

    public static double abs(double value) {
        return Math.abs(value);
    }

    public static double floor(double value) {
        return Math.floor(value);
    }

    public static double ceil(double value) {
        return Math.ceil(value);
    }

    public static double exp(double value) {
        return Math.exp(value);
    }

    public static double lerp(double start, double stop, double amt) {
        return amt * (stop - start) + start;
    }

    public static double constrain(double n, double low, double high) {
        return Math.max(Math.min(n, high), low);
    }

    public static double dist(double... args) {
        double value = Double.NEGATIVE_INFINITY;
        if (args.length == 4) {
            //2D
            value = hypot(args[2] - args[0], args[3] - args[1]);
        } else if (args.length == 6) {
            //3D
            value = hypot(args[3] - args[0], args[4] - args[1], args[5] - args[2]);
        }
        return value;
    }

    public static double hypot(double... arguments) {

        // Otherwise use the V8 implementation
        // https://github.com/v8/v8/blob/8cd3cf297287e581a49e487067f5cbd991b27123/src/js/math.js#L217
        int length = arguments.length;
        double[] args = new double[length];
        double max = 0;
        for (int i = 0; i < length; i++) {
            double n = arguments[i];
            n = +n;
            if (Double.isInfinite(n)) {
                return Double.POSITIVE_INFINITY;
            }
            n = Math.abs(n);
            if (n > max) {
                max = n;
            }
            args[i] = n;
        }

        if (max == 0) {
            max = 1;
        }
        double sum = 0;
        double compensation = 0;
        for (int j = 0; j < length; j++) {
            final double m = args[j] / max;
            final double summand = m * m - compensation;
            final double preliminary = sum + summand;
            compensation = preliminary - sum - summand;
            sum = preliminary;
        }
        return Math.sqrt(sum) * max;
    }

    public static double map(double n, double start1, double stop1, double start2, double stop2, boolean withinBounds) {
        final double newval = (n - start1) / (stop1 - start1) * (stop2 - start2) + start2;

        if (!withinBounds) {
            return newval;
        }
        if (start2 < stop2) {
            return constrain(newval, start2, stop2);
        } else {
            return constrain(newval, stop2, start2);
        }

    }
}
//p5.prototype.log  = Math.log;
//    p5.prototype.mag  = function(x, y)
//
//    {
//        p5._validateParameters('mag', arguments
//        );
//  return hypot(x, y);
//    }
//    ;
//
///**
// * Re-maps a number from one range to another.
// * <br><br>
// * In the first example above, the number 25 is converted from a value in the
// * range of 0 to 100 into a value that ranges from the left edge of the
// * window (0) to the right edge (width).
// *
// * @method map
// * @param  {Number} value  the incoming value to be converted
// * @param  {Number} start1 lower bound of the value's current range
// * @param  {Number} stop1  upper bound of the value's current range
// * @param  {Number} start2 lower bound of the value's target range
// * @param  {Number} stop2  upper bound of the value's target range
// * @param  {Boolean} [withinBounds] constrain the value to the newly mapped range
// * @return {Number}        remapped number
// * @example
// *   <div><code>
// * let value = 25;
// * let m = map(value, 0, 100, 0, width);
// * ellipse(m, 50, 10, 10);
//</code></div>
// *
// *   <div><code>
// * function setup() {
// *   noStroke();
// * }
// *
// * function draw() {
// *   background(204);
// *   let x1 = map(mouseX, 0, width, 25, 75);
// *   ellipse(x1, 25, 25, 25);
// *   //This ellipse is constrained to the 0-100 range
// *   //after setting withinBounds to true
// *   let x2 = map(mouseX, 0, width, 0, 100, true);
// *   ellipse(x2, 75, 25, 25);
// * }
//</code></div>
// *
// * @alt
// * 10 by 10 white ellipse with in mid left canvas
// * 2 25 by 25 white ellipses move with mouse x. Bottom has more range from X
// *
// */
//p5.prototype.map  = function(n, start1, stop1, start2, stop2, withinBounds)
//
//    {
//        p5._validateParameters('map', arguments
//    
//    
//    
//    
//    );
//  const newval  = (n - start1) / (stop1 - start1) * (stop2 - start2) + start2;
//    if (!withinBounds
//
//    
//        ) {
//    return newval;
//    }
//    if (start2< stop2
//
//    
//        ) {
//    return this.constrain(newval, start2, stop2);
//    }
//
//    
//        else {
//    return this.constrain(newval, stop2, start2);
//    }
//};
//
///**
// * Determines the largest value in a sequence of numbers, and then returns that
// * value. <a href="#/p5/max">max()</a> accepts any number of Number parameters,
// * or an Array of any length.
// *
// * @method max
// * @param {Number} n0 Number to compare
// * @param {Number} n1 Number to compare
// * @return {Number} maximum Number
// * @example
// * <div><code>
// * function setup() {
// *   // Change the elements in the array and run the sketch
// *   // to show how max() works!
// *   let numArray = [2, 1, 5, 4, 8, 9];
// *   fill(0);
// *   noStroke();
// *   text('Array Elements', 0, 10);
// *   // Draw all numbers in the array
// *   let spacing = 15;
// *   let elemsY = 25;
// *   for (let i = 0; i < numArray.length; i++) {
// *     text(numArray[i], i * spacing, elemsY);
// *   }
// *   let maxX = 33;
// *   let maxY = 80;
// *   // Draw the Maximum value in the array.
// *   textSize(32);
// *   text(max(numArray), maxX, maxY);
// * }
// * </code></div>
// *
// * @alt Small text at top reads: Array Elements 2 1 5 4 8 9. Large text at
// * center: 9
// *
// */
///**
// * @method max
// * @param {Number[]} nums Numbers to compare
// * @return {Number}
// */
//p5.prototype.max = function(...args) {
//  p5._validateParameters('max', args);
//  if (args[0] instanceof Array) {
//    return Math.max.apply(null, args[0]);
//  } else {
//    return Math.max.apply(null, args);
//  }
//};
//
///**
// * Determines the smallest value in a sequence of numbers, and then returns
// * that value. <a href="#/p5/min">min()</a> accepts any number of Number parameters, or an Array
// * of any length.
// *
// * @method min
// * @param  {Number} n0 Number to compare
// * @param  {Number} n1 Number to compare
// * @return {Number}             minimum Number
// * @example
// * <div><code>
// * function setup() {
// *   // Change the elements in the array and run the sketch
// *   // to show how min() works!
// *   let numArray = [2, 1, 5, 4, 8, 9];
// *   fill(0);
// *   noStroke();
// *   text('Array Elements', 0, 10);
// *   // Draw all numbers in the array
// *   let spacing = 15;
// *   let elemsY = 25;
// *   for (let i = 0; i < numArray.length; i++) {
// *     text(numArray[i], i * spacing, elemsY);
// *   }
// *   let maxX = 33;
// *   let maxY = 80;
// *   // Draw the Minimum value in the array.
// *   textSize(32);
// *   text(min(numArray), maxX, maxY);
// * }
// * </code></div>
// *
// * @alt
// * Small text at top reads: Array Elements 2 1 5 4 8 9. Large text at center: 1
// *
// */
///**
// * @method min
// * @param  {Number[]} nums Numbers to compare
// * @return {Number}
// */
//p5.prototype.min = function(...args) {
//  p5._validateParameters('min', args);
//  if (args[0] instanceof Array) {
//    return Math.min.apply(null, args[0]);
//  } else {
//    return Math.min.apply(null, args);
//  }
//};
//
///**
// * Normalizes a number from another range into a value between 0 and 1.
// * Identical to map(value, low, high, 0, 1).
// * Numbers outside of the range are not clamped to 0 and 1, because
// * out-of-range values are often intentional and useful. (See the example above.)
// *
// * @method norm
// * @param  {Number} value incoming value to be normalized
// * @param  {Number} start lower bound of the value's current range
// * @param  {Number} stop  upper bound of the value's current range
// * @return {Number}       normalized number
// * @example
// * <div><code>
// * function draw() {
// *   background(200);
// *   let currentNum = mouseX;
// *   let lowerBound = 0;
// *   let upperBound = width; //100;
// *   let normalized = norm(currentNum, lowerBound, upperBound);
// *   let lineY = 70;
// *   stroke(3);
// *   line(0, lineY, width, lineY);
// *   //Draw an ellipse mapped to the non-normalized value.
// *   noStroke();
// *   fill(50);
// *   let s = 7; // ellipse size
// *   ellipse(currentNum, lineY, s, s);
// *
// *   // Draw the guide
// *   let guideY = lineY + 15;
// *   text('0', 0, guideY);
// *   textAlign(RIGHT);
// *   text('100', width, guideY);
// *
// *   // Draw the normalized value
// *   textAlign(LEFT);
// *   fill(0);
// *   textSize(32);
// *   let normalY = 40;
// *   let normalX = 20;
// *   text(normalized, normalX, normalY);
// * }
// * </code></div>
// *
// * @alt
// * ellipse moves with mouse. 0 shown left & 100 right and updating values center
// *
// */
//p5.prototype.norm = function(n, start, stop) {
//  p5._validateParameters('norm', arguments);
//  return this.map(n, start, stop, 0, 1);
//};
//
///**
// * Facilitates exponential expressions. The <a href="#/p5/pow">pow()</a> function is an efficient
// * way of multiplying numbers by themselves (or their reciprocals) in large
// * quantities. For example, pow(3, 5) is equivalent to the expression
// * 3 &times; 3 &times; 3 &times; 3 &times; 3 and pow(3, -5) is equivalent to 1 /
// * 3 &times; 3 &times; 3 &times; 3 &times; 3. Maps to
// * Math.pow().
// *
// * @method pow
// * @param  {Number} n base of the exponential expression
// * @param  {Number} e power by which to raise the base
// * @return {Number}   n^e
// * @example
// * <div><code>
// * function setup() {
// *   //Exponentially increase the size of an ellipse.
// *   let eSize = 3; // Original Size
// *   let eLoc = 10; // Original Location
// *
// *   ellipse(eLoc, eLoc, eSize, eSize);
// *
// *   ellipse(eLoc * 2, eLoc * 2, pow(eSize, 2), pow(eSize, 2));
// *
// *   ellipse(eLoc * 4, eLoc * 4, pow(eSize, 3), pow(eSize, 3));
// *
// *   ellipse(eLoc * 8, eLoc * 8, pow(eSize, 4), pow(eSize, 4));
// * }
// * </code></div>
// *
// * @alt
// * small to large ellipses radiating from top left of canvas
// *
// */
//p5.prototype.pow = Math.pow;
//
///**
// * Calculates the integer closest to the n parameter. For example,
// * round(133.8) returns the value 134. Maps to Math.round().
// *
// * @method round
// * @param  {Number} n number to round
// * @param  {Number} [decimals] number of decimal places to round to, default is 0
// * @return {Integer}  rounded number
// * @example
// * <div><code>
// * let x = round(3.7);
// * text(x, width / 2, height / 2);
// * </code></div>
// * <div><code>
// * let x = round(12.782383, 2);
// * text(x, width / 2, height / 2);
// * </code></div>
// * <div><code>
// * function draw() {
// *   background(200);
// *   //map, mouseX between 0 and 5.
// *   let ax = map(mouseX, 0, 100, 0, 5);
// *   let ay = 66;
// *
// *   // Round the mapped number.
// *   let bx = round(map(mouseX, 0, 100, 0, 5));
// *   let by = 33;
// *
// *   // Multiply the mapped numbers by 20 to more easily
// *   // see the changes.
// *   stroke(0);
// *   fill(0);
// *   line(0, ay, ax * 20, ay);
// *   line(0, by, bx * 20, by);
// *
// *   // Reformat the float returned by map and draw it.
// *   noStroke();
// *   text(nfc(ax, 2), ax, ay - 5);
// *   text(nfc(bx, 1), bx, by - 5);
// * }
// * </code></div>
// *
// * @alt
// * "3" written in middle of canvas
// * "12.78" written in middle of canvas
// * horizontal center line squared values displayed on top and regular on bottom.
// *
// */
//p5.prototype.round = function(n, decimals) {
//  if (!decimals) {
//    return Math.round(n);
//  }
//  return Number(Math.round(n + 'e' + decimals) + 'e-' + decimals);
//};
//
///**
// * Squares a number (multiplies a number by itself). The result is always a
// * positive number, as multiplying two negative numbers always yields a
// * positive result. For example, -1 * -1 = 1.
// *
// * @method sq
// * @param  {Number} n number to square
// * @return {Number}   squared number
// * @example
// * <div><code>
// * function draw() {
// *   background(200);
// *   let eSize = 7;
// *   let x1 = map(mouseX, 0, width, 0, 10);
// *   let y1 = 80;
// *   let x2 = sq(x1);
// *   let y2 = 20;
// *
// *   // Draw the non-squared.
// *   line(0, y1, width, y1);
// *   ellipse(x1, y1, eSize, eSize);
// *
// *   // Draw the squared.
// *   line(0, y2, width, y2);
// *   ellipse(x2, y2, eSize, eSize);
// *
// *   // Draw dividing line.
// *   stroke(100);
// *   line(0, height / 2, width, height / 2);
// *
// *   // Draw text.
// *   let spacing = 15;
// *   noStroke();
// *   fill(0);
// *   text('x = ' + x1, 0, y1 + spacing);
// *   text('sq(x) = ' + x2, 0, y2 + spacing);
// * }
// * </code></div>
// *
// * @alt
// * horizontal center line squared values displayed on top and regular on bottom.
// *
// */
//p5.prototype.sq = n => n * n;
//
///**
// * Calculates the square root of a number. The square root of a number is
// * always positive, even though there may be a valid negative root. The
// * square root s of number a is such that s*s = a. It is the opposite of
// * squaring. Maps to Math.sqrt().
// *
// * @method sqrt
// * @param  {Number} n non-negative number to square root
// * @return {Number}   square root of number
// * @example
// * <div><code>
// * function draw() {
// *   background(200);
// *   let eSize = 7;
// *   let x1 = mouseX;
// *   let y1 = 80;
// *   let x2 = sqrt(x1);
// *   let y2 = 20;
// *
// *   // Draw the non-squared.
// *   line(0, y1, width, y1);
// *   ellipse(x1, y1, eSize, eSize);
// *
// *   // Draw the squared.
// *   line(0, y2, width, y2);
// *   ellipse(x2, y2, eSize, eSize);
// *
// *   // Draw dividing line.
// *   stroke(100);
// *   line(0, height / 2, width, height / 2);
// *
// *   // Draw text.
// *   noStroke();
// *   fill(0);
// *   let spacing = 15;
// *   text('x = ' + x1, 0, y1 + spacing);
// *   text('sqrt(x) = ' + x2, 0, y2 + spacing);
// * }
// * </code></div>
// *
// * @alt
// * horizontal center line squareroot values displayed on top and regular on bottom.
// *
// */
//p5.prototype.sqrt = Math.sqrt;
//
//// Calculate the length of the hypotenuse of a right triangle
//// This won't under- or overflow in intermediate steps
//// https://en.wikipedia.org/wiki/Hypot
//
///**
// * Calculates the fractional part of a number.
// *
// * @method fract
// * @param {Number} num Number whose fractional part needs to be found out
// * @returns {Number} fractional part of x, i.e, {x}
// * @example
// * <div>
// * <code>
// * function setup() {
// *   createCanvas(windowWidth, windowHeight);
// *   fill(0);
// *   text(7345.73472742, 0, 50);
// *   text(fract(7345.73472742), 0, 100);
// *   text(1.4215e-15, 150, 50);
// *   text(fract(1.4215e-15), 150, 100);
// * }
// * </code>
// * </div>
// * @alt
// * 2 rows of numbers, the first row having 8 numbers and the second having the fractional parts of those numbers.
// */
//p5.prototype.fract = function(toConvert) {
//  p5._validateParameters('fract', arguments);
//  let sign = 0;
//  let num = Number(toConvert);
//  if (isNaN(num) || Math.abs(num) === Infinity) {
//    return num;
//  } else if (num < 0) {
//    num = -num;
//    sign = 1;
//  }
//  if (String(num).includes('.') && !String(num).includes('e')) {
//    let toFract = String(num);
//    toFract = Number('0' + toFract.slice(toFract.indexOf('.')));
//    return Math.abs(sign - toFract);
//  } else if (num < 1) {
//    return Math.abs(sign - num);
//  } else {
//    return 0;
//  }
//};
//
//export default p5;
