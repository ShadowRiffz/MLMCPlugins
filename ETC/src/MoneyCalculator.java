import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class MoneyCalculator {
	public static void main (String[] args) {
		File file = new File("C:\\Users\\Alex\\Desktop\\bug.txt");
		HashMap<String, Integer> players = new HashMap<String, Integer>();
		try {
			Scanner scan = new Scanner(file);
			while (scan.hasNext()) {
				String line = scan.nextLine();
				String[] arguments = line.split(" ");
				if (arguments[0].contains("/eco")) {
					String player = arguments[2];
					int amount = Integer.parseInt(arguments[3]);
					if (players.containsKey(player)) {
						int newTotal = players.get(player) + amount;
						players.put(player, newTotal);
					}
					else {
						players.put(player, amount);
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int count = 0;
		for (String p : players.keySet()) {
			if (players.get(p) > 5000) {
				System.out.println(p + ": " + players.get(p));
				count++;
			}
		}
		System.out.println("Total players: " + count);
		
		
	}
}
