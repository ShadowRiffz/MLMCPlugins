import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindReplaceRecursive {
	private static final String PATH = "C:\\Users\\Alex\\Downloads\\";
	private static final String DIRECTORY = "gear";
	private static final Pattern REPLACE_OLD = Pattern.compile("display: ");
	private static final String REPLACE_NEW = "type: ";
	public static HashMap<File, String> pendingChanges = new HashMap<File, String>();
	
	public static int totalChanges = 0;
	public static int numFiles = 0;
	
	public static void main(String args[]) {
		File top = new File(PATH + DIRECTORY);
		recurseMatch(top);
		Scanner scan = new Scanner(System.in);
		System.out.println("Found a total of " + totalChanges + " changes in " + numFiles + " files.");
		System.out.println("Continue? (Y)");
		if (scan.next().equalsIgnoreCase("y")) {
			System.out.println("Modifying files.");
			recurseWrite(top);
		}
		else {
			System.out.println("Replace cancelled.");
		}
		scan.close();
	}
	
	private static void recurseMatch(File dir) {
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				recurseMatch(file);
			}
			else {
				numFiles++;
				String oldContent = "";
				try {
					BufferedReader reader = new BufferedReader(new FileReader(file));
					String line = reader.readLine();
					while (line != null) {
						oldContent += line + System.lineSeparator();
						line = reader.readLine();
					}
					reader.close();
					
					// Matching and replacing
					StringBuffer sb = new StringBuffer();
					Matcher m = REPLACE_OLD.matcher(oldContent);
					int count = 0;
					while (m.find()) {
						count++;
						m.appendReplacement(sb, REPLACE_NEW);
					}
					System.out.println(count + " changes: " + file.getName());
					totalChanges += count;
					m.appendTail(sb);
					String newContent = sb.toString();
					pendingChanges.put(file, newContent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static void recurseWrite(File dir) {
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				recurseWrite(file);
			}
			else {
				FileWriter writer;
				try {
					writer = new FileWriter(file);
					writer.write(pendingChanges.get(file));
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
