import java.util.Scanner;

public class BoolAlg {
	public static void main(String args[]) {
		Scanner scan = new Scanner(System.in);
		while (true) {
			String input = null;
			System.out.println("Next: ");
			input = scan.nextLine();
			if (input.equalsIgnoreCase("done")) {
				break;
			}
			input = input.replaceAll(" ", "");
			input = input.replaceAll("a3", "a");
			input = input.replaceAll("a2", "b");
			input = input.replaceAll("a1", "c");
			input = input.replaceAll("a0", "d");
			while (input.contains("’")) {
				int num = input.indexOf("’");
				input = input.substring(0, num) + input.substring(num + 1, input.length());
				input = input.substring(0, num - 1) + "~" + input.substring(num - 1, input.length());
			}
			for(int i = 0; i < input.length() - 1; i++) {
				char letter = input.charAt(i);
				char next = input.charAt(i+1);
				if (letter != '(' && letter != '\'' && letter != '~' && letter != '+' && letter != '*') {
					if (next != '\'' && next != '+' && next != ')' && next != '*') {
						input = input.substring(0, i + 1) + "*" + input.substring(i + 1, input.length());
					}
				}
				else if (letter == ')' && next == '(') {
					input = input.substring(0, i + 1) + "*" + input.substring(i + 1, input.length());
				}
			}
			System.out.println(input);
		}
		scan.close();
	}
}
