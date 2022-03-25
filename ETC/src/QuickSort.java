public class QuickSort {

    public static void main(String args[]) {
        int quickArray[] = new int[10];
        System.out.println("Quicksort Unsorted Array");
        QuickFillArray(quickArray);

        System.out.println("Quicksort Sorted Array");
        int quickArrayLeft = quickArray[0];
        int quickArrayRight = quickArray[9];
        //QuickSort(quickArray, 0, 9);
        QuickSortMedian(quickArray, quickArrayLeft, quickArrayRight);

        for (int i = 0; i < 10; i++) {
            System.out.println(quickArray[i] + " ");
        }
    }

    // fill the array for quicksort
    public static void QuickFillArray(int[] quickArray) {
        for (int i = 0; i < 10; i++) {
            quickArray[i] = (int) (Math.random() * 30);
            System.out.println(quickArray[i] + " ");
        }
    }

    public static void QuickSortRandom() {

    }

    public static void QuickSortFirst(int[] quickArray, int quickArrayLeft, int quickArrayRight) {
        if (quickArrayLeft < quickArrayRight) {
            int pi = FirstPartition(quickArray, quickArrayLeft, quickArrayRight);
            QuickSortFirst(quickArray, quickArrayLeft, pi - 1);
            QuickSortFirst(quickArray, pi + 1, quickArrayRight);
        }
    }

    public static int FirstPartition(int[] quickArray, int quickArrayLeft, int quickArrayRight) {
        int pivot = quickArray[quickArrayLeft];
        int i = (quickArrayRight + 1);
        for (int j = quickArrayRight; j > quickArrayLeft; j--) {
            if (quickArray[j] > pivot) {
                i--;
                // swap array i j
                Swap(quickArray, quickArray[i], quickArray[j]);
            }
        }
        int temp = quickArray[i - 1];
        quickArray[i - 1] = quickArray[quickArrayLeft];
        quickArray[quickArrayLeft] = temp;
        return (i - 1);
    }

    // sorting the array if median of 3
    public static int QuickSortMedian(int[] quickArray, int quickArrayLeft, int quickArrayRight) {
        int center = ((quickArrayLeft + quickArrayRight) / 2);
        if (quickArray[center] < quickArray[quickArrayLeft]) {
            Swap(quickArray, quickArrayLeft, center);
        }
        if (quickArray[quickArrayRight] < quickArray[quickArrayLeft]) {
            Swap(quickArray, quickArrayLeft, quickArrayRight);
        }
        if (quickArray[quickArrayRight] < quickArray[center]) {
            Swap(quickArray, quickArrayRight, center);
        }
        Swap(quickArray, center, quickArrayRight - 1);

        return quickArray[quickArrayRight - 1];
    }

    // partitioning when pivot is center
    public static void Partition(int[] quickArray, int quickArrayLeft, int quickArrayRight, int pivot) {
        int i = quickArrayLeft, j = quickArrayRight - 1;
        if (quickArrayLeft <= quickArrayRight) {
            return;
        }
        for (;;) {
            while (quickArray[i++] < pivot) {
            }
            while (pivot < quickArray[j--]) {
            }
            if (i < j) {
                Swap(quickArray, i, j);
            } else {
                break;
            }
        }
        Swap(quickArray, i, quickArrayRight);
        QuickSortMedian(quickArray, quickArrayLeft, i-1);
        QuickSortMedian(quickArray, i+1, quickArrayRight);
    }
    
    // swap locations i and j
    public static void Swap(int[] quickArray, int i, int j) {
        int temp = quickArray[i];
        quickArray[i] = quickArray[j];
        quickArray[j] = temp;
    }
}
