/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg;

import java.io.BufferedReader;
import java.io.FileReader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import pkg.Wobulation.BinContainer;

/**
 *
 * @author Brad
 */
public class WobulationCase2Test {

    /*
    Process the attached data in the order provided.
    There are no pre-existing bin values, i.e., start with the first value and 
    establish bins appropriately. Since the first value is ~8.36, the initial 
    mean bin will be [8.3,8.4).

    Case 1.
        Use a "horizon" of 0.3 (i.e., 3 bins above and 3 bins below the "mean bin"
        Note: on first iteration there will be 7 bins:
        [8, 8.1), [8.1, 8.2), [8.2, 8.3), [8.3, 8.4). [8.4, 8.5), [8.5, 8.6), [8.6, 8.7) with bin counts: 0,0,0,1,0,0,0

        Expected Result is Cluster starts at 8.3-8.4 and never changes.

    Case 2:
        Use a "horizon of 1.0 (i.e., monitor 10 bins below and 10 bins above the "mean bin")
        Expected Results:
        Initial Cluster Range: 8.3 - 8.4
        Final Cluster Range: 9.3-10.9

        Detailed results w/transitions in 2nd attch. 
    
        Some parts of those calculations are tedious, so can't guarantee they 
        are perfect... If there are deviations in the results let me know 
        (prior to knocking yourself out trying to match) and I can recheck my 
        results for errors at the point of diversion.
     */
    public WobulationCase2Test() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    @Ignore
    public void testCase2() throws Exception {
        try (BufferedReader r = new BufferedReader(new FileReader("Wob_Valid_case2.csv"))) {
            String line;
            BinContainer bins = new BinContainer();
            while((line=r.readLine())!= null) {
                double value = Double.parseDouble(line);
                bins.bump(value);
                bins.dump();
            }
        }
            
    }
}
