import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class StringReader {
	public static void main (String[] args) {
		File file = new File("C:\\Users\\Alex\\Desktop\\code.txt");
		BufferedWriter writer;
		try {
			Scanner scan = new Scanner(file);
			writer = new BufferedWriter(new FileWriter("C:\\Users\\Alex\\Desktop\\machine_code.txt"));
			while (scan.hasNext()) {
				String line = scan.nextLine();
				line = line.replace("\t", " ");
				String[] arguments = line.split(" ");
				System.out.println(line);
				String code = "";
				String type = "";
				if (!arguments[0].contains("-")) {
					switch (arguments[0]) {
					case "Add":
						code += "0000";
						type = "R";
						break;
					case "Load/Store":
						code += "0001";
						type = "R";
						break;
					case "XOR":
						code += "0010";
						type = "R";
						break;
					case "Branch":
						code += "0011";
						type = "B";
						break;
					case "Get/Set":
						code += "0100";
						type = "R";
						break;
					case "LSBtoLSB/MSB":
						code += "0101";
						type = "R";
						break;
					case "MSBtoLSB/MSB":
						code += "0110";
						type = "R";
						break;
					case "L/RShift":
						code += "0111";
						type = "R";
						break;
					case "SetAccum":
						code += "1000";
						type = "I";
						break;
					case "Equals/Not":
						code += "1001";
						type = "R";
						break;
					case "Equals/NotImm":
						code += "1010";
						type = "X";
						break;
					case "BranchReg":
						code += "1011";
						type = "R";
						break;
					case "FlipBit":
						code += "1100";
						type = "I";
						break;
					case "BranchRegOff":
						code += "1101";
						type = "R";
						break;
					case "LTE":
						code += "1110";
						type = "R";
						break;
					case "Reset":
						code += "1111";
						type = "R";
						break;
					default:
						code = "Error";
						break;
					}
					
					switch (type) {
					case "R":
						code += convertRegToDecimal(arguments[1]);
						code += arguments[2];
						break;
					case "I":
						code += convertToBinary(arguments[1], 5);
						break;
					case "B":
						if (arguments[1].charAt(0) == '-') {
							code += "1" + convertToBinary(arguments[1].substring(1), 4);
						}
						else {
							code += "0" + convertToBinary(arguments[1], 4);
						}
						break;
					case "X":
						code += convertToBinary(arguments[1], 4);
						code += arguments[2];
						break;
					default:
						code = "Error!! making sure writer sees";
						break;
					}
					
				}
				
				if (type != "") {
					if (code.length() != 9) {
						writer.append("Error at " + line + "\n");
					}
					else {
						writer.append(code + "\n");
					}
				}
			}
			writer.close();
			scan.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static String convertRegToDecimal(String reg) {
		reg = reg.substring(2);
		return convertToBinary(reg, 4);
	}
	static int convertToDecimalBranch(String binary) {
		int total = 0;
		char[] bins = binary.toCharArray();
		for (int i = 1; i < 5; i++) {
			total += ((int) bins[i]) * Math.pow(2, 4-i);
		}
		if (bins[0] == '1') {
			total = -total;
		}
		return total;
	}
	
	static String convertToBinary(String immediate, int length) {
		int num = Integer.parseInt(immediate);
		String binString = null;
		if (num < 0) {
			num = -num;
			binString = Integer.toBinaryString(num);
			while (binString.length() < length - 1) {
				binString = "0" + binString;
			}
			binString += "1";
		}
		else {
			binString = Integer.toBinaryString(num);
			while (binString.length() < length) {
				binString = "0" + binString;
			}
		}
		return binString;
	}
}