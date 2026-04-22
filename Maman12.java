import java.util.Random;

/**
 * Maman 12 - Maximum Subarray Problem (פרוייקט הרצה)
 * Course: Data Structures and Introduction to Algorithms (20407)
 * 
 * Includes both Divide & Conquer (O(n log n)) and Kadane's Algorithm (O(n)).
 * The main method runs the required empirical experiment comparing runtimes.
 */
public class Maman12 {

    // Helper class to store the results
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

    /**
     * Helper for Divide and Conquer: Finds the max subarray crossing the midpoint.
     */
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

    /**
     * 1. Divide and Conquer Approach - O(n log n)
     */
    public static SubarrayResult findMaximumSubarray(int[] A, int low, int high) {
        if (high == low) {
            return new SubarrayResult(low, high, A[low]); // Base case: only one element
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

    /**
     * 2. Kadane's Algorithm - O(n)
     */
    public static SubarrayResult kadane(int[] A) {
        if (A == null || A.length == 0) return new SubarrayResult(-1, -1, 0);

        long maxSoFar = A[0];
        long currentMax = A[0];
        int start = 0, end = 0, tempStart = 0;

        for (int i = 1; i < A.length; i++) {
            // Decide whether to start a new sequence at i, or append to the current one
            if (A[i] > currentMax + A[i]) {
                currentMax = A[i];
                tempStart = i;
            } else {
                currentMax += A[i];
            }

            // Update global maximum if we found a better sequence
            if (currentMax > maxSoFar) {
                maxSoFar = currentMax;
                start = tempStart;
                end = i;
            }
        }
        return new SubarrayResult(start, end, maxSoFar);
    }

    /**
     * Main Execution: Runs the Empirical Experiment required by the assignment.
     */
    public static void main(String[] args) {
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
                    A[j] = rand.nextInt(201) - 100; // Random integers from -100 to 100
                }

                // --- Benchmark Divide & Conquer ---
                long startDC = System.nanoTime();
                findMaximumSubarray(A, 0, size - 1);
                long endDC = System.nanoTime();
                totalTimeDC += (endDC - startDC);

                // --- Benchmark Kadane's ---
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
        
        System.out.println("\nNote: Theoretical questions and answers can be found in the README/accompanying document.");
    }
}
