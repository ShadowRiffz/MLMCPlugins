import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class MoneyCalculator {
	public static void main (String[] args) {
		
		double total = 0;
		double price = 1000;
		for (int i = 1; i <= 500; i++) {
			total += price;
			price *= 1.005;
		}
		System.out.println("Price: " + price + ", total: " + total);
	}
}
