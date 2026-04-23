"""
Maman 12 - Maximum Subarray Problem (פרוייקט הרצה)
Course: Data Structures and Introduction to Algorithms (20407)
Open University of Israel

THEORETICAL QUESTIONS & ANSWERS:

1. What is the recurrence relation of the Divide and Conquer algorithm, and what is its solution?
   Recurrence: T(n) = 2T(n/2) + Θ(n). 
   Solution: By the Master Theorem (Case 2), the solution is T(n) = Θ(n log n).

2. What algorithmic idea allows Kadane's algorithm to do only one pass?
   Dynamic Programming (Optimal Substructure). Kadane's algorithm works by deciding at each step 'i':
   "Is the maximum subarray ending at 'i' just A[i] on its own, or does it consist of A[i] appended 
   to the maximum subarray ending at 'i-1'?" By keeping track of only the optimal subarray 
   ending at the previous step, we remove the need for nested loops.

3. Is Kadane's always expected to be faster in practice than the recursive algorithm?
   Yes. Asymptotically, O(n) grows much slower than O(n log n). Practically, the iterative algorithm 
   completely avoids the massive overhead of recursive function calls, stack memory usage, and 
   continuous array-splitting math, resulting in significantly faster real-world clock times.

4. Why is Kadane's linear?
   It loops through the array exactly once (a single pass). For each element, it performs a 
   constant O(1) number of operations (simple additions and comparisons). For n elements, 
   the time complexity is exactly O(n).

5. Time and Space Complexity Analysis:
   - Divide and Conquer:
     Time: O(n log n)
     Space: O(log n) auxiliary space due to the depth of the recursion call stack.
   - Kadane's Algorithm:
     Time: O(n)
     Space: O(1) auxiliary space (it only stores a few tracking variables).
"""

def find_max_crossing_subarray(A, low, mid, high):
    """
    Helper function for Divide and Conquer. 
    Finds the maximum subarray that crosses the midpoint.
    """
    left_sum = float('-inf')
    total = 0
    max_left = mid
    
    # Traverse from middle to left
    for i in range(mid, low - 1, -1):
        total += A[i]
        if total > left_sum:
            left_sum = total
            max_left = i
            
    right_sum = float('-inf')
    total = 0
    max_right = mid + 1
    
    # Traverse from middle to right
    for j in range(mid + 1, high + 1):
        total += A[j]
        if total > right_sum:
            right_sum = total
            max_right = j
            
    return (max_left, max_right, left_sum + right_sum)


def _find_maximum_subarray_recursive(A, low, high):
    """
    Recursive core for the Divide and Conquer approach.
    """
    if high == low:
        # Base case: only one element
        return (low, high, A[low])
    
    mid = (low + high) // 2
    
    # Conquer left and right halves
    left_low, left_high, left_sum = _find_maximum_subarray_recursive(A, low, mid)
    right_low, right_high, right_sum = _find_maximum_subarray_recursive(A, mid + 1, high)
    
    # Find max crossing the midpoint
    cross_low, cross_high, cross_sum = find_max_crossing_subarray(A, low, mid, high)
    
    # Return the maximum of the three possibilities
    if left_sum >= right_sum and left_sum >= cross_sum:
        return (left_low, left_high, left_sum)
    elif right_sum >= left_sum and right_sum >= cross_sum:
        return (right_low, right_high, right_sum)
    else:
        return (cross_low, cross_high, cross_sum)


def max_subarray_divide_and_conquer(A):
    """
    O(n log n) Divide and Conquer Implementation.
    Returns: (max_sum, start_idx, end_idx, subarray)
    """
    if not A:
        return 0, -1, -1, []
        
    start, end, max_sum = _find_maximum_subarray_recursive(A, 0, len(A) - 1)
    return max_sum, start, end, A[start:end + 1]


def max_subarray_kadane(A):
    """
    O(n) Iterative Kadane's Algorithm Implementation.
    Returns: (max_sum, start_idx, end_idx, subarray)
    """
    if not A:
        return 0, -1, -1, []
        
    max_so_far = A[0]
    current_max = A[0]
    
    start = 0
    end = 0
    temp_start = 0
    
    for i in range(1, len(A)):
        # Decide: append A[i] to current sequence, or start a new sequence at A[i]?
        if A[i] > current_max + A[i]:
            current_max = A[i]
            temp_start = i
        else:
            current_max += A[i]
            
        # Update the overall max if the current sequence is strictly better
        if current_max > max_so_far:
            max_so_far = current_max
            start = temp_start
            end = i
            
    return max_so_far, start, end, A[start:end + 1]


if __name__ == "__main__":
    # Test cases to verify the logic
    test_arrays = {
        "Standard (Mixed)": [13, -3, -25, 20, -3, -16, -23, 18, 20, -7, 12, -5, -22, 15, -4, 7],
        "All Negatives": [-10, -5, -2, -1, -8],
        "All Positives": [1, 2, 3, 4, 5],
        "All Zeros": [0, 0, 0, 0],
        "Size 1": [42]
    }
    
    for name, arr in test_arrays.items():
        print(f"--- {name} ---")
        print(f"Array: {arr}")
        
        sum_dc, s_dc, e_dc, sub_dc = max_subarray_divide_and_conquer(arr)
        sum_k, s_k, e_k, sub_k = max_subarray_kadane(arr)
        
        print(f"Divide & Conquer -> Sum: {sum_dc} | Indices: ({s_dc}, {e_dc}) | Subarray: {sub_dc}")
        print(f"Kadane's Algo    -> Sum: {sum_k} | Indices: ({s_k}, {e_k}) | Subarray: {sub_k}")
        print()
