import java.util.Scanner;

public class Main {
	public static Scanner in = new Scanner(System.in);

	public static void main(String[] args) {
		System.out.print("Select task num (1-5): ");
		int taskNum = in.nextInt();
		switch (taskNum) {
			case 1: 
				task1();
				break;
			case 2:
				task2();
				break;
			case 3:
				task3();
				break;
			case 4:
				task4();
				break;
			case 5:
				task5();
				break;
			
			default:
				System.out.println("Invalid task num");
				break;
		}
	
	}


	public static void task1() {
		int n = in.nextInt();
		int k = 0;

		if (n < 1) return;
		while (n != 1) {
			n = (n & 1) == 0 ? n / 2 : 3*n + 1;
			k++;
		}
		System.out.println(k);
	}

	public static void task2() {
		int n = in.nextInt();
		int sum = 0;
		boolean sign = false;
		
		for (int i = 0; i < n; ++i) {
			sum += sign ? -in.nextInt() : in.nextInt();
			sign = !sign;
		}
		
		System.out.println(sum);
	}
	
	public static void task3() {
		int x = in.nextInt();
		int y = in.nextInt();

		int curX = 0, curY = 0;
		int k = 0;
		String dir;
		while ( !(dir = in.next()).equals("стоп") ) {
			int steps = in.nextInt();

			if (dir.equals("север")) curY += steps;
			else if (dir.equals("юг")) curY -= steps;
			else if (dir.equals("запад")) curX -= steps;
			else if (dir.equals("восток")) curY += steps;

			if ((curX == x) && (curY == y)) {
				System.out.println(++k);
				return;
			}

			k++;
		}
	}

	public static void task4() {
		int roadsCount = in.nextInt();
		int maxHeight = 0;
		int roadNum = 0;

		for (int i = 0; i < roadsCount; ++i) {
			int tunnelCount = in.nextInt();
			int tunnelMinHeight = Integer.MAX_VALUE;
			for (int j = 0; j < tunnelCount; j++) {
				int height = in.nextInt();
				if (height < tunnelMinHeight) tunnelMinHeight = height;
			}
			
			if (maxHeight < tunnelMinHeight) {
				maxHeight = tunnelMinHeight;
				roadNum = i;
			}
		}

		System.out.printf("%d %d\n", roadNum + 1, maxHeight);
	}
	
	public static void task5() {
		int number = in.nextInt();
		if (number < 0) return;
		
		int digit1 = number % 10;
		int digit2 = (number / 10) % 10;
		int digit3 = (number / 100) % 10;
		
		if (((digit1 + digit2 + digit3) % 2 == 0) && (digit1 * digit2 * digit3 % 2 == 0) ) {
			System.out.println("true");
			return;
		}

		System.out.println("false");
	}
}
