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

    public static SubarrayResult findMaximumSubarray(int[] A, int low, int high) {
        // Stop Condition: if there is only one element in the given array return it
        if (high == low) {
            return new SubarrayResult(low, high, A[low]);
        } else {
            // Divide: find the middle of the array and call function on left and right halves
            int mid = (low + high) / 2;
            SubarrayResult left = findMaximumSubarray(A, low, mid);
            SubarrayResult right = findMaximumSubarray(A, mid + 1, high);

            // Find the maximum subarray crossing between the two halves for comparison
            long leftSum = Integer.MIN_VALUE, rightSum = Integer.MIN_VALUE, sum = 0;
            int maxLeft = mid, maxRight = mid + 1;

            for (int i = mid; i >= low; i--) { // Find the maximum starting point
                sum += A[i];
                if (sum > leftSum) {
                    leftSum = sum;
                    maxLeft = i;
                }
            }

            sum = 0;
            for (int j = mid + 1; j <= high; j++) { // Find the maximum ending point
                sum += A[j];
                if (sum > rightSum) {
                    rightSum = sum;
                    maxRight = j;
                }
            }

            SubarrayResult cross = new SubarrayResult(maxLeft, maxRight, leftSum + rightSum);

            // Return the subarray with the greatest sum between the left half, the right half, and their crossing.
            if (left.sum >= right.sum && left.sum >= cross.sum) {
                return left;
            } else if (right.sum >= left.sum && right.sum >= cross.sum) {
                return right;
            } else {
                return cross;
            }
        }
    }

    public static SubarrayResult divideAndConquer(int[] A) {
        return findMaximumSubarray(A, 0, A.length - 1);
    }

    public static SubarrayResult kadane(int[] A) {
        // Check if there are any elements in the array.
        if (A == null || A.length == 0) return new SubarrayResult(-1, -1, 0);

        long maxSum = A[0], currentSum = A[0];
        int start = 0, end = 0, currentStart = 0;

        for (int i = 1; i < A.length; i++) {
            // Decide whether to append A[i] to current sequence, or start a new sequence at A[i] depending on
            // which is greater.
            if (A[i] > currentSum + A[i]) {
                currentSum = A[i];
                currentStart = i;
            } else {
                currentSum += A[i];
            }

            // Update the overall max if the current sequence is strictly better
            if (currentSum > maxSum) {
                maxSum = currentSum;
                start = currentStart;
                end = i;
            }
        }
        return new SubarrayResult(start, end, maxSum);
    }

    public static void main(String[] args) {

        System.out.println("Maman12 (Yonatan Bebchuk 209805233)");
        
        // 1. Test edge cases
        System.out.println("===============================================================");
        System.out.println("  Edge Cases");
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

        // 2. Run empirical experiment
        int[] sizes = {100, 1000, 10000, 100000};
        int iterations = 10;
        Random rand = new Random();

        System.out.println("===============================================================");
        System.out.println("  Empirical Experiment");
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
                divideAndConquer(A);
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
