# Assignment 01 Question 2

A = [1, 2, 3, 4, 5, 6]


def find_peak(array: list) -> int:
    """Divide & conquer algorithm for finding a peak in an array."""
    return find_peak_rec(array=array, left=0, right=len(array))


def find_peak_rec(array: list, left: int, right: int) -> int:
    """The recursive part of the divide & conquer algorithm."""
    middle = (left + right) // 2

    # The middle is a peak if:
    #   It is larger than it's neighbors
    #   It is the left edge and larger than it's right neighbor
    #   It is the right edge and larger than it's left neighbor
    if (middle == 0 or array[middle - 1] < array[middle]) and (
            middle == len(array) - 1 or array[middle] > array[middle + 1]):
        return middle

    # If the neighbor to the middle's left is larger there must be a peak to the left
    if array[middle - 1] > array[middle]:
        print("")
        return find_peak_rec(array, left, middle)

    # If the neighbor to the middle's right is larger there must be a peak to the right
    if array[middle] < array[middle + 1]:
        return find_peak_rec(array, middle, right)

    # This is impossible since if the two previous if statements fail then the
    # first if statement must have succeeded. I am adding this for typing purposes.
    return -1


if __name__ == '__main__':
    peak_index = find_peak(array=A)
    print(f"{A[peak_index]=}")
