A = [1, 3, 5, 7, 9, 11, 2, 4, 6, 8, 10, 12]


# Textbook

def merge_sort(array, p, r):
    if p < r:
        q = (p + r) // 2
        merge_sort(array, p, q)
        merge_sort(array, q + 1, r)
        merge(array, p, q, r)


def merge(array, p, q, r):
    # Calculate sizes correctly for inclusive indices
    n1 = q - p + 1
    n2 = r - q

    left = []
    right = []

    # Fill left array: from p to q
    for i in range(n1):
        left.append(array[p + i])  # Removed the -1

    # Fill right array: from q+1 to r
    for j in range(n2):
        right.append(array[q + 1 + j])  # Start from q + 1

    # Sentinels
    left.append(float('inf'))
    right.append(float('inf'))

    i = 0
    j = 0
    # Loop from p to r INCLUSIVE
    for k in range(p, r + 1):
        if left[i] <= right[j]:
            array[k] = left[i]
            i += 1
        else:
            array[k] = right[j]
            j += 1


# Assignment 01 Question 3

def merge_sort_three(array, p, r):
    if p < r:
        q = p + (r - p) // 3
        s = p + 2 * (r - p) // 3
        merge_sort_three(array, p, q)
        merge_sort_three(array, q + 1, s)
        merge_sort_three(array, s + 1, r)
        merge_three(array, p, q, s, r)


def merge_three(array, p, q, s, r):
    # Calculate sizes correctly for inclusive indices
    n1 = q - p + 1
    n2 = s - q
    n3 = r - s

    left = []
    middle = []
    right = []

    # Fill left array: from p to q
    for i in range(n1):
        left.append(array[p + i])  # Removed the -1

    # Fill middle array: from q+1 to s
    for j in range(n2):
        middle.append(array[q + 1 + j])  # Start from q + 1

    # Fill right array: from s+1 to r
    for k in range(n3):
        right.append(array[s + 1 + k])  # Start from s + 1

    # Sentinels
    left.append(float('inf'))
    middle.append(float('inf'))
    right.append(float('inf'))

    # Loop from p to r INCLUSIVE
    i = 0
    j = 0
    k = 0
    for t in range(p, r + 1):
        if left[i] <= middle[k] and left[i] <= right[j]:
            array[t] = left[i]
            i += 1
        elif middle[k] <= left[i] and middle[k] <= right[j]:
            array[t] = middle[k]
            k += 1
        else:
            array[t] = right[j]
            j += 1


if __name__ == '__main__':
    print(f"{A=}")
    merge_sort_three(array=A, p=0, r=len(A) - 1)
    print(f"{A=}")
