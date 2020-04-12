import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class PlayerCommander {
	public static void main (String[] args) {
		File file = new File("C:\\Users\\Alex\\Desktop\\100.txt");
		ArrayList<String> players = new ArrayList<String>();
		try {
			Scanner scan = new Scanner(file);
			while (scan.hasNext()) {
					players.add(scan.next());
			}
			scan.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println();
		for (String p : players) {
			System.out.println("  - /lp user " + p + " permission set deluxetags.tag.100");
		}
		
		
	}
}
