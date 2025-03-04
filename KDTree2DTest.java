import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.geometry.Point2D;

public class KDTree2DTest {
    private static String LINE_SEPERATOR = System.lineSeparator();

    public static void main(String[] args) {
        
        System.out.println("Testing KDTree2D methods...\n");

        boolean insertTestResult = testInsert();
        System.out.println("Insert method test " + (insertTestResult ? "passed." : "failed."));

        boolean insertSamePointTestResult = testInsertSamePoint();
        System.out.println("Insert same point test " + (insertSamePointTestResult ? "passed." : "failed."));

        boolean searchTestResult = testSearch();
        System.out.println("Search method test " + (searchTestResult ? "passed." : "failed."));

        boolean removeTestResult = testRemove();
        System.out.println("Remove method test " + (removeTestResult ? "passed." : "failed."));

        boolean removeComplexTestResult = testRemoveComplex();
        System.out.println("Complex remove method test " +
            (removeComplexTestResult ? "passed." : "failed."));

        boolean findMinTestResult = testFindMin();
        System.out.println("FindMin method test " + (findMinTestResult ? "passed." : "failed."));

        boolean findMaxTestResult = testFindMax();
        System.out.println("FindMax method test " + (findMaxTestResult ? "passed." : "failed."));

        boolean rectRangeTestResult = testPrintRectangularRange();
        System.out.println("Rectangular range search test " + (rectRangeTestResult ? "passed." : "failed."));

        boolean circRangeTestResult = testPrintCircularRange();
        System.out.println("Circular range search test " + (circRangeTestResult ? "passed." : "failed."));

        boolean circularRangeNegativeRadiusTestResult = testPrintCircularRangeNegativeRadius();
        System.out.println("printCircularRange negative radius test " +
            (circularRangeNegativeRadiusTestResult ? "passed." : "failed."));

        boolean findMinNegativeDimensionTestResult = testFindMinNegativeDimension();
        System.out.println("findMin negative dimension test " +
            (findMinNegativeDimensionTestResult ? "passed." : "failed."));

        boolean findMaxNegativeDimensionTestResult = testFindMaxNegativeDimension();
        System.out.println("findMax negative dimension test " +
            (findMaxNegativeDimensionTestResult ? "passed." : "failed."));
    }

    // After adding final result is shown via display method, that output should match with the expected output
    public static boolean testInsert() {
        KDTree2D tree = new KDTree2D();

        // Insert points as per the given example
        Point2D[] pointsToInsert = {
            new Point2D(30, 40),
            new Point2D(5, 25),
            new Point2D(18, 10)
        };

        for (Point2D p : pointsToInsert) {
            tree.insert(p);
        }

        // Capture the output of displayTree
        String actualOutput = captureDisplayTreeOutput(tree);

        // Expected output
        String expectedOutput = 
            "(30.0, 40.0)" + LINE_SEPERATOR +
            ". (5.0, 25.0)" + LINE_SEPERATOR +
            ". . (18.0, 10.0)" + LINE_SEPERATOR;

        // Compare actual output with expected output
        boolean outputMatches = actualOutput.equals(expectedOutput);

        if (!outputMatches) {
            System.out.println("Insert test failed. Expected output:\n" + expectedOutput);
            System.out.println("Actual output:\n" + actualOutput);
        }

        return outputMatches;
    }

    // Test if insert return null when one tries to insert same point twice
    public static boolean testInsertSamePoint(){
        KDTree2D tree = new KDTree2D();
        Point2D[] pointsToInsert = {
            new Point2D(30, 40),
            new Point2D(5, 25),
            new Point2D(18, 10),
            new Point2D(80, 90),
            new Point2D(15, 70),
            new Point2D(60, 20)
        };

        boolean allInserted = true;

        for (Point2D p : pointsToInsert) {
            tree.insert(p);
        }

        // Attempt to insert a duplicate point
        Point2D duplicatePoint = new Point2D(30, 40);
        Point2D result = tree.insert(duplicatePoint);
        if (result != null) {
            System.out.println("Duplicate insertion did not return null for " + duplicatePoint);
            allInserted = false;
        }

        return allInserted;
    }

    // After removing a point, final result is shown via display method, that output should match with the expected output
    public static boolean testRemove() {
        KDTree2D tree = new KDTree2D();

        // Insert points
        Point2D[] pointsToInsert = {
            new Point2D(30, 40),
            new Point2D(5, 25),
            new Point2D(18, 10),
            new Point2D(80, 90),
            new Point2D(15, 70),
            new Point2D(60, 20)
        };

        for (Point2D p : pointsToInsert) {
            tree.insert(p);
        }

        // Remove a point
        tree.remove(new Point2D(5, 25));

        // Capture the output of displayTree after removal
        String actualOutput = captureDisplayTreeOutput(tree);

        // Expected output after removal of (5,25)
        String expectedOutput = 
            "(30.0, 40.0)" + LINE_SEPERATOR + 
            ". (15.0, 70.0)" + LINE_SEPERATOR +
            ". . (18.0, 10.0)" + LINE_SEPERATOR +
            ". (80.0, 90.0)" + LINE_SEPERATOR +
            ". . (60.0, 20.0)" + LINE_SEPERATOR;

        // Compare actual output with expected output
        boolean outputMatches = actualOutput.equals(expectedOutput);

        if (!outputMatches) {
            System.out.println("Remove test failed. Expected output:\n" + expectedOutput);
            System.out.println("Actual output:\n" + actualOutput);
        }

        return outputMatches;
    }

    public static boolean testRemoveComplex() {
        KDTree2D tree = new KDTree2D();

        // Insert points to create a tree with depth at least 4
        Point2D[] pointsToInsert = {
            new Point2D(30, 40),
            new Point2D(5, 25),
            new Point2D(70, 70),
            new Point2D(2, 20),
            new Point2D(40, 30),
            new Point2D(80, 80),
            new Point2D(1, 15),
            new Point2D(3, 10),
            new Point2D(35, 35)
        };

        for (Point2D p : pointsToInsert) {
            tree.insert(p);
        }

        // Capture the output of displayTree before removal
        String initialOutput = captureDisplayTreeOutput(tree);

        // Expected output before removal
        String expectedInitialOutput = 
            "(30.0, 40.0)" + LINE_SEPERATOR + 
            ". (5.0, 25.0)" + LINE_SEPERATOR + 
            ". . (2.0, 20.0)" + LINE_SEPERATOR +
            ". . . (1.0, 15.0)" + LINE_SEPERATOR +
            ". . . (3.0, 10.0)" + LINE_SEPERATOR +
            ". (70.0, 70.0)" + LINE_SEPERATOR +
            ". . (40.0, 30.0)" + LINE_SEPERATOR +
            ". . . (35.0, 35.0)" + LINE_SEPERATOR +
            ". . (80.0, 80.0)"+ LINE_SEPERATOR;

        // Check if initial tree is correct
        boolean initialOutputMatches = initialOutput.equals(expectedInitialOutput);

        if (!initialOutputMatches) {
            System.out.println("Initial tree does not match expected output.");
            System.out.println("Expected initial output:\n" + expectedInitialOutput);
            System.out.println("Actual initial output:\n" + initialOutput);
            return false;
        }

        // Remove a node with two children (e.g., (2, 20))
        tree.remove(new Point2D(2, 20));

        // Capture the output of displayTree after removal
        String actualOutput = captureDisplayTreeOutput(tree);

        // Expected output after removal of (2,20)
        String expectedOutput = 
            "(30.0, 40.0)" + LINE_SEPERATOR +
            ". (5.0, 25.0)" + LINE_SEPERATOR +
            ". . (3.0, 10.0)" + LINE_SEPERATOR +
            ". . . (1.0, 15.0)" + LINE_SEPERATOR +
            ". (70.0, 70.0)" + LINE_SEPERATOR +
            ". . (40.0, 30.0)" + LINE_SEPERATOR +
            ". . . (35.0, 35.0)" + LINE_SEPERATOR +
            ". . (80.0, 80.0)"+ LINE_SEPERATOR;

        // Compare actual output with expected output
        boolean outputMatches = actualOutput.equals(expectedOutput);

        if (!outputMatches) {
            System.out.println("Complex remove test failed.");
            System.out.println("Expected output after removal:\n" + expectedOutput);
            System.out.println("Actual output after removal:\n" + actualOutput);
        }

        return outputMatches;
    }

    // Helper method to capture displayTree output
    private static String captureDisplayTreeOutput(KDTree2D tree) {
        // Save the old System.out
        PrintStream originalOut = System.out;

        // Create a ByteArrayOutputStream to capture output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);

        // Call displayTree
        tree.displayTree();

        // Restore the original System.out
        System.out.flush();
        System.setOut(originalOut);

        // Get the output as a string
        return baos.toString();
    }

    //test method for search
    public static boolean testSearch() {
        KDTree2D tree = new KDTree2D();
        Point2D[] pointsToInsert = {
            new Point2D(30, 40),
            new Point2D(5, 25),
            new Point2D(18, 10)
        };
        for (Point2D p : pointsToInsert) {
            tree.insert(p);
        }

        // Test searching for existing points
        boolean searchSuccess = true;
        for (Point2D p : pointsToInsert) {
            Point2D result = tree.search(p);
            if (result == null || !result.equals(p)) {
                System.out.println("Failed to find inserted point " + p);
                searchSuccess = false;
            }
        }

        // Test searching for a non-existing point
        Point2D nonExistingPoint = new Point2D(50, 50);
        Point2D result = tree.search(nonExistingPoint);
        if (result != null) {
            System.out.println("Found non-existing point " + nonExistingPoint);
            searchSuccess = false;
        }

        return searchSuccess;
    }

    //test method for findMin
    public static boolean testFindMin() {
        KDTree2D tree = new KDTree2D();
        Point2D[] pointsToInsert = {
            new Point2D(30, 40),
            new Point2D(5, 25),    // Min x
            new Point2D(18, 10),   // Min y
            new Point2D(80, 90),
            new Point2D(15, 70),
            new Point2D(60, 20)
        };
        for (Point2D p : pointsToInsert) {
            tree.insert(p);
        }

        Point2D expectedMinX = new Point2D(5, 25);
        Point2D expectedMinY = new Point2D(18, 10);

        Point2D actualMinX = tree.findMin(0);
        Point2D actualMinY = tree.findMin(1);

        boolean minXCorrect = actualMinX != null && actualMinX.equals(expectedMinX);
        boolean minYCorrect = actualMinY != null && actualMinY.equals(expectedMinY);

        if (!minXCorrect) {
            System.out.println("Expected min x-coordinate point: " + expectedMinX + ", but got: " + actualMinX);
        }

        if (!minYCorrect) {
            System.out.println("Expected min y-coordinate point: " + expectedMinY + ", but got: " + actualMinY);
        }

        return minXCorrect && minYCorrect;
    }

    //test method for findMax
    public static boolean testFindMax() {
        KDTree2D tree = new KDTree2D();
        Point2D[] pointsToInsert = {
            new Point2D(30, 40),
            new Point2D(5, 25),
            new Point2D(18, 10),
            new Point2D(80, 90),   // Max x and y
            new Point2D(15, 70),
            new Point2D(60, 20)
        };
        for (Point2D p : pointsToInsert) {
            tree.insert(p);
        }

        Point2D expectedMaxX = new Point2D(80, 90);
        Point2D expectedMaxY = new Point2D(80, 90);

        Point2D actualMaxX = tree.findMax(0);
        Point2D actualMaxY = tree.findMax(1);

        boolean maxXCorrect = actualMaxX != null && actualMaxX.equals(expectedMaxX);
        boolean maxYCorrect = actualMaxY != null && actualMaxY.equals(expectedMaxY);

        if (!maxXCorrect) {
            System.out.println("Expected max x-coordinate point: " + expectedMaxX + ", but got: " + actualMaxX);
        }

        if (!maxYCorrect) {
            System.out.println("Expected max y-coordinate point: " + expectedMaxY + ", but got: " + actualMaxY);
        }

        return maxXCorrect && maxYCorrect;
    }

    //test method for printRectangularRange
    public static boolean testPrintRectangularRange() {
        KDTree2D tree = new KDTree2D();
        Point2D[] pointsToInsert = {
            new Point2D(30, 40),   // Inside
            new Point2D(5, 25),    // Outside
            new Point2D(18, 10),   // Inside
            new Point2D(80, 90),   // Outside
            new Point2D(15, 70),   // Inside
            new Point2D(60, 20)    // Inside
        };
        for (Point2D p : pointsToInsert) {
            tree.insert(p);
        }

        Point2D ll = new Point2D(10, 10);
        Point2D ur = new Point2D(70, 70);

        List<Point2D> expectedPoints = Arrays.asList(
            new Point2D(30, 40),
            new Point2D(18, 10),
            new Point2D(15, 70),
            new Point2D(60, 20)
        );

        Iterable<Point2D> actualPointsIterable = tree.printRectangularRange(ll, ur);
        List<Point2D> actualPoints = new ArrayList<>();
        for (Point2D p : actualPointsIterable) {
            actualPoints.add(p);
        }

        boolean rangeCorrect = actualPoints.containsAll(expectedPoints) && expectedPoints.containsAll(actualPoints);

        if (!rangeCorrect) {
            System.out.println("Expected points in rectangular range: " + expectedPoints);
            System.out.println("Actual points in rectangular range: " + actualPoints);
        }

        return rangeCorrect;
    }

    //test method for printCircularRange
    public static boolean testPrintCircularRange() {
        KDTree2D tree = new KDTree2D();
        Point2D[] pointsToInsert = {
            new Point2D(30, 40),   // Inside
            new Point2D(5, 25),    // Outside
            new Point2D(18, 10),   // Outside
            new Point2D(80, 90),   // Outside
            new Point2D(15, 70),   // Outside
            new Point2D(60, 20)    // Inside
        };
        for (Point2D p : pointsToInsert) {
            tree.insert(p);
        }

        Point2D center = new Point2D(40, 40);
        double radius = 30;

        List<Point2D> expectedPoints = Arrays.asList(
            new Point2D(30, 40),
            new Point2D(60, 20)
        );

        Iterable<Point2D> actualPointsIterable = tree.printCircularRange(center, radius);
        List<Point2D> actualPoints = new ArrayList<>();
        for (Point2D p : actualPointsIterable) {
            actualPoints.add(p);
        }

        boolean rangeCorrect = actualPoints.containsAll(expectedPoints) && expectedPoints.containsAll(actualPoints);

        if (!rangeCorrect) {
            System.out.println("Expected points in circular range: " + expectedPoints);
            System.out.println("Actual points in circular range: " + actualPoints);
        }

        return rangeCorrect;
    }

    //illegal argument test case
    // PrintCircularRange method is given negative radius, method should throw illegal argument exception
    public static boolean testPrintCircularRangeNegativeRadius() {
        KDTree2D tree = new KDTree2D();

        // Insert some points
        tree.insert(new Point2D(30, 40));
        tree.insert(new Point2D(5, 25));

        boolean exceptionThrown = false;

        try {
            // Call printCircularRange with negative radius
            tree.printCircularRange(new Point2D(20, 20), -10);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        } catch (Exception e) {
            // If any other exception is thrown, the test fails
            System.out.println("An unexpected exception was thrown: " + e);
            return false;
        }

        return exceptionThrown;
    }

    // illegal argument test case
    // FindMin method is given negative radius, method should throw illegal argument exception
    public static boolean testFindMinNegativeDimension() {
        KDTree2D tree = new KDTree2D();

        // Insert some points
        tree.insert(new Point2D(30, 40));
        tree.insert(new Point2D(5, 25));

        boolean exceptionThrown = false;
        try {
            // Call findMin with negative dimension
            tree.findMin(-1);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        } catch (Exception e) {
            // If any other exception is thrown, the test fails
            System.out.println("An unexpected exception was thrown: " + e);
            return false;
        }
        return exceptionThrown;
    }

    // illegal argument test case
    // FindMax method is given negative radius, method should throw illegal argument exception
    public static boolean testFindMaxNegativeDimension() {
        KDTree2D tree = new KDTree2D();

        // Insert some points
        tree.insert(new Point2D(30, 40));
        tree.insert(new Point2D(5, 25));

        boolean exceptionThrown = false;

        try {
            // Call findMax with negative dimension
            tree.findMax(-2);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        } catch (Exception e) {
            // If any other exception is thrown, the test fails
            System.out.println("An unexpected exception was thrown: " + e);
            return false;
        }

        return exceptionThrown;
    }
}
