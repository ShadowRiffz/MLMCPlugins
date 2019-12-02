import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CodeDecompiler {
	public static void main (String[] args) {
		File file = new File("C:\\Users\\Alex\\Desktop\\machine_code.txt");
		BufferedWriter writer;
		try {
			Scanner scan = new Scanner(file);
			writer = new BufferedWriter(new FileWriter("C:\\Users\\Alex\\Desktop\\decompiled.txt"));
			while (scan.hasNext()) {
				String line = scan.nextLine();
				String opcode = line.substring(0, 4);
				String code = "";
				String type = "";
				switch (opcode) {
				case "0000":
					code += "Add ";
					type = "R";
					break;
				case "0001":
					code += "Load/Store ";
					type = "R";
					break;
				case "0010":
					code += "XOR ";
					type = "R";
					break;
				case "0011":
					code += "Branch ";
					type = "B";
					break;
				case "0100":
					code += "Get/Set ";
					type = "R";
					break;
				case "0101":
					code += "LSBtoLSB/MSB ";
					type = "R";
					break;
				case "0110":
					code += "MSBtoLSB/MSB ";
					type = "R";
					break;
				case "0111":
					code += "L/RShift ";
					type = "R";
					break;
				case "1000":
					code += "SetAccum ";
					type = "I";
					break;
				case "1001":
					code += "Equals/Not ";
					type = "R";
					break;
				case "1010":
					code += "Equals/NotImm ";
					type = "X";
					break;
				case "1011":
					code += "BranchReg ";
					type = "R";
					break;
				case "1111":
					code += "Reset ";
					type = "R";
					break;
				default:
					code = "Error ";
					break;
				}
				
				switch (type) {
				case "R":
					String reg = line.substring(4, 8);
					code += binaryToDecimal(reg) + " ";
					code += line.charAt(8);
					break;
				case "I":
					String imm1 = line.substring(4, 9);
					code += binaryToDecimal(imm1);
					break;
				case "B":
					String imm2 = line.substring(5, 9);
					if (line.charAt(4) == '1') {
						code += -binaryToDecimal(imm2);
					}
					else {
						code += binaryToDecimal(imm2);
					}
					break;
				case "X":
					String imm = line.substring(4, 8);
					code += binaryToDecimal(imm) + " ";
					code += line.charAt(8);
					break;
				}
				
				
				if (type != "") {
					writer.append(code + "\n");
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
	static int binaryToDecimal(String bin) {
		char[] bins = bin.toCharArray();
		int dec = 0;
		for (int i = 0; i < bins.length; i++) {
			dec += Character.getNumericValue(bins[i]) * (Math.pow(2, bins.length - i - 1));
		}
		return dec;
	}
}