import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Scanner;

public class MoneyCalculator {
	public static void main (String[] args) {
		File file = new File("C:\\Users\\Alex\\Desktop\\bugged acc.txt");
		String account = Files.readAllLines(file.toPath());
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
		System.out.println(data);
	}
}
