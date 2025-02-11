import java.util.Arrays;

public class Main {
	public static void main(String[] args) {
		// 1
		{
			System.out.println("#1");
			String test = "babbadabcddsdasds";
			System.out.print("Input: ");
			System.out.println(test);

			System.out.print("Max substring: ");
			System.out.println(maxSubstring(test));
		}
		// 2
		{
			System.out.println("#2");
			int[] arr1 = {1, 3, 5, 7};
			int[] arr2 = {2, 4, 6, 8};
			System.out.print("A: ");
			System.out.println(Arrays.toString(arr1));
			System.out.print("B: ");
			System.out.println(Arrays.toString(arr2));
			System.out.print("C: ");
			System.out.println(Arrays.toString(mergeSortedArrays(arr1, arr2)));
		}
		// 3
		{
			System.out.println("#3");
			int[] arr = {1, 2, -4, 5, 6, -5};
			System.out.print("A: ");
			System.out.println(Arrays.toString(arr));
			System.out.print("MaxSum: ");
			System.out.println(maxSubarraySum(arr));
		}
		// 4
		{
			System.out.println("#4");
			int[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } };
			System.out.println("Arr: ");
			print2DArray(arr);
			System.out.println("Rotated: ");
			print2DArray(rotateArray90Clockwise(arr));
		}
		
		// 5
		{
			System.out.println("#5");
			int[] arr = {1, 2, -4, 5, 6, -5};
			int target = -9;
			System.out.print("Target sum: ");
			System.out.println(target);

			System.out.print("A: ");
			System.out.println(Arrays.toString(arr));
			
			System.out.println("Pair: ");
			System.out.println(Arrays.toString(targetSumPair(arr, target)));
		}
		
		// 6
		{
			System.out.println("#6");
			int[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } };
			System.out.println("Arr: ");
			print2DArray(arr);
			System.out.print("Sum: ");
			System.out.println(sum2DArray(arr));
		}
		// 7
		{
			System.out.println("#7");
			int[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } };
			System.out.println("Arr: ");
			print2DArray(arr);
			System.out.print("Max in rows: ");
			System.out.println(Arrays.toString(maxInRows(arr)));
		}
		// 8
		{
			System.out.println("#8");
			int[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } };
			System.out.println("Arr: ");
			print2DArray(arr);
			System.out.println("Rotated: ");
			print2DArray(rotateArray90NotClockwise(arr));
		}



	}
	
	public static void print2DArray(int[][] arr) {
		for (int i = 0; i < arr.length; i++) {
			System.out.println(Arrays.toString(arr[i]));
		}
	}


	// 1 
	public static String maxSubstring(String s) {
		if (s == null || s.isEmpty()) return "";

		int[] charIndex = new int[256];
		for (int i = 0; i < charIndex.length; i++) {
			charIndex[i] = -1;
		}
		
		int start = 0;
		int maxLength = 0;
		int maxStart = 0;


		for (int end = 0; end < s.length(); end++) {
			char curChar = s.charAt(end);
			int curIndex = (int) curChar;

			if (charIndex[curIndex] >= start) {
				start = charIndex[curIndex] + 1;
			}

			charIndex[curIndex] = end;

			if (end - start + 1 > maxLength) {
				maxLength = end - start + 1;
				maxStart = start;
			}
		}

		return s.substring(maxStart, maxStart + maxLength);
	}
	
	// 2
    public static int[] mergeSortedArrays(int[] array1, int[] array2) {
        int length1 = array1.length;
        int length2 = array2.length;
        int[] mergedArray = new int[length1 + length2];

        int i = 0;
        int j = 0;
        int k = 0; 

        // Merge
        while (i < length1 && j < length2) {
            if (array1[i] <= array2[j]) {
                mergedArray[k++] = array1[i++];
            } else {
                mergedArray[k++] = array2[j++];
            }
        }

        
        while (i < length1) {
            mergedArray[k++] = array1[i++];
        }

        while (j < length2) {
            mergedArray[k++] = array2[j++];
        }

        return mergedArray;
    }
	
	// 3
	public static int maxSubarraySum(int[] arr) {
		int maxSum = Integer.MIN_VALUE;
		int curSum = 0;

		for (int val: arr) {
			curSum += val;

			if (curSum > maxSum) maxSum = curSum;
			if (curSum < 0) curSum = 0;
		}

		return maxSum;
	}

	// 4
	public static int[][] rotateArray90Clockwise(int[][] arr) {
		int maxRow = arr[0].length;
		int maxCol = arr.length;
		
		int[][] rotated = new int[maxRow][maxCol];

		for (int i = 0; i <  maxRow; i++) {
			for (int j = 0; j < maxCol; j++) {
				rotated[i][j] = arr[maxCol-j-1][i];
			}
		}

		return rotated;
	}

	// 5
	public static int[] targetSumPair(int[] arr, int target) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = i + 1; j < arr.length; j++) {
				if ((arr[i] + arr[j]) == target) {
					int[] pair = {arr[i], arr[j]};
					return pair;
				}
			}
		}
		return null;
	}

	// 6
	public static int sum2DArray(int[][] arr) {
		int sum = 0;
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				sum += arr[i][j];
			}
		}
		return sum;
	}


	// 7
	public static int[] maxInRows(int[][] arr) {
		int[] maxVals = new int[arr.length];
		for (int i = 0; i < arr.length; i++) {
			int maxVal = arr[i][0];
			for (int val: arr[i]) {
				if (val > maxVal) maxVal = val;
			}
			maxVals[i] = maxVal;
		}
		return maxVals;
	}

	// 8
	public static int[][] rotateArray90NotClockwise(int[][] arr) {
		int maxRow = arr[0].length;
		int maxCol = arr.length;
		
		int[][] rotated = new int[maxRow][maxCol];

		for (int i = 0; i <  maxRow; i++) {
			for (int j = 0; j < maxCol; j++) {
				rotated[i][j] = arr[j][maxRow-i-1];
			}
		}

		return rotated;
	}
}
