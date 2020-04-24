/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Brad
 */
public class DoubleTest {
    public static void main(String[] args) {
        double value = 0.0;
        for (int i = 0; i< 100; i++) {
            value += 0.1;
            System.out.println(value);
        }
    }
}
