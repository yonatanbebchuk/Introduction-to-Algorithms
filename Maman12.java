import java.util.Random;
import java.util.Arrays;

public class Maman12 {

    public static class SubarrayResult {
        public int low;
        public int high;
        public long sum;

        public SubarrayResult(int low, int high, long sum) {
            this.low = low;
            this.high = high;
            this.sum = sum;
        }
        
        @Override
        public String toString() {
            return "Sum: " + sum + " | Range: [" + low + ", " + high + "]";
        }
    }

    private static SubarrayResult findMaxCrossingSubarray(int[] A, int low, int mid, int high) {
        long leftSum = Integer.MIN_VALUE;
        long sum = 0;
        int maxLeft = mid;
        
        for (int i = mid; i >= low; i--) {
            sum += A[i];
            if (sum > leftSum) {
                leftSum = sum;
                maxLeft = i;
            }
        }

        long rightSum = Integer.MIN_VALUE;
        sum = 0;
        int maxRight = mid + 1;
        
        for (int j = mid + 1; j <= high; j++) {
            sum += A[j];
            if (sum > rightSum) {
                rightSum = sum;
                maxRight = j;
            }
        }
        
        return new SubarrayResult(maxLeft, maxRight, leftSum + rightSum);
    }

    public static SubarrayResult findMaximumSubarray(int[] A, int low, int high) {
        if (high == low) {
            return new SubarrayResult(low, high, A[low]);
        } else {
            int mid = (low + high) / 2;
            SubarrayResult left = findMaximumSubarray(A, low, mid);
            SubarrayResult right = findMaximumSubarray(A, mid + 1, high);
            SubarrayResult cross = findMaxCrossingSubarray(A, low, mid, high);

            if (left.sum >= right.sum && left.sum >= cross.sum) {
                return left;
            } else if (right.sum >= left.sum && right.sum >= cross.sum) {
                return right;
            } else {
                return cross;
            }
        }
    }

    public static SubarrayResult kadane(int[] A) {
        if (A == null || A.length == 0) return new SubarrayResult(-1, -1, 0);

        long maxSoFar = A[0];
        long currentMax = A[0];
        int start = 0, end = 0, tempStart = 0;

        for (int i = 1; i < A.length; i++) {
            if (A[i] > currentMax + A[i]) {
                currentMax = A[i];
                tempStart = i;
            } else {
                currentMax += A[i];
            }

            if (currentMax > maxSoFar) {
                maxSoFar = currentMax;
                start = tempStart;
                end = i;
            }
        }
        return new SubarrayResult(start, end, maxSoFar);
    }

    public static void testEdgeCases() {
        System.out.println("===============================================================");
        System.out.println("  Maman 12 Explicit Edge Cases Testing");
        System.out.println("===============================================================");
        
        int[][] cases = {
            {-5, -2, -9, -1, -3}, // All negative
            {1, 2, 3, 4, 5},      // All positive
            {0, 0, 0, 0, 0},      // All zeros
            {42},                 // Size 1
            {2, -5, 2, -5, 2}     // Multiple sub-arrays with the same max sum
        };
        String[] names = {
            "All Negatives", 
            "All Positives", 
            "All Zeros", 
            "Size 1", 
            "Multiple Max Sums (Sum is 2)"
        };
        
        for (int i = 0; i < cases.length; i++) {
            System.out.println(names[i] + ": " + Arrays.toString(cases[i]));
            SubarrayResult dc = findMaximumSubarray(cases[i], 0, cases[i].length - 1);
            SubarrayResult k = kadane(cases[i]);
            System.out.println("  D&C    -> " + dc);
            System.out.println("  Kadane -> " + k);
            System.out.println();
        }
    }

    // Bug fix for Programiz: No space between main and (String[] args)
    public static void main(String[] args) {
        
        // 1. Run mandatory edge cases
        testEdgeCases();

        // 2. Run empirical time tests
        int[] sizes = {100, 1000, 10000, 100000};
        int iterations = 10;
        Random rand = new Random();

        System.out.println("===============================================================");
        System.out.println("  Maman 12 Empirical Experiment Results");
        System.out.println("===============================================================");
        System.out.printf("%-10s | %-20s | %-20s\n", "Size (n)", "D&C Avg Time (ns)", "Kadane Avg Time (ns)");
        System.out.println("---------------------------------------------------------------");

        for (int size : sizes) {
            long totalTimeDC = 0;
            long totalTimeKadane = 0;

            for (int i = 0; i < iterations; i++) {
                int[] A = new int[size];
                for (int j = 0; j < size; j++) {
                    A[j] = rand.nextInt(201) - 100;
                }

                long startDC = System.nanoTime();
                findMaximumSubarray(A, 0, size - 1);
                long endDC = System.nanoTime();
                totalTimeDC += (endDC - startDC);

                long startKadane = System.nanoTime();
                kadane(A);
                long endKadane = System.nanoTime();
                totalTimeKadane += (endKadane - startKadane);
            }

            long avgDC = totalTimeDC / iterations;
            long avgKadane = totalTimeKadane / iterations;
            
            System.out.printf("%-10d | %-20d | %-20d\n", size, avgDC, avgKadane);
        }
        System.out.println("===============================================================");
    }
}
