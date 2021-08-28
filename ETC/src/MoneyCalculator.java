import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class MoneyCalculator {
	public static void main (String[] args) {
		File file = new File("C:\\Users\\Alex\\Desktop\\bugged acc.txt");
		String account = new String();
		try {
			Scanner scan = new Scanner(file);
			while (scan.hasNext()) {
				account += scan.nextLine();
				if (scan.hasNext()) {
					account += "\n";
				}
				
			}
			scan.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(account);
		DataSection data = YAMLParser.parseText(account);
		System.out.println("Stop here");
		System.out.println(data.getDouble("hunger"));
		System.out.println(data.getSection("accounts").getSection("acc1").getSection("classes").getSection("Blacksmith").getInt("level"));
	}
}
