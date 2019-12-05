
public class BitEncoder {
	public static void main(String args[]) {
		boolean[] ogMSW = {true, true, true, true, true, true, false, false};
		boolean[] ogLSW = {true, true, false, true, true, false, false, true};
		boolean[] MSW 	= {false, false, false, false, false, ogMSW[0], ogMSW[1], ogMSW[2]};
		boolean[] LSW 	= {ogMSW[3], ogMSW[4], ogMSW[5], ogMSW[6], ogLSW[0], ogLSW[1], ogLSW[2], ogLSW[4]};
		
		boolean p8, p4, p2, p1, p16, cp8, cp4, cp2, cp1, cp16;
		
		p8 = MSW[5] ^ MSW[6] ^ MSW[7] ^ LSW[0] ^ LSW[1] ^ LSW[2] ^ LSW[3];
		p4 = MSW[5] ^ MSW[6] ^ MSW[7] ^ LSW[0] ^ LSW[4] ^ LSW[5] ^ LSW[6];
		p2 = MSW[5] ^ MSW[6] ^ LSW[1] ^ LSW[2] ^ LSW[4] ^ LSW[5] ^ LSW[7];
		p1 = MSW[5] ^ MSW[7] ^ LSW[1] ^ LSW[3] ^ LSW[4] ^ LSW[6] ^ LSW[7];
		p16 = ogLSW[7] ^ ogMSW[7] ^ ogLSW[3] ^ ogLSW[5] ^ ogLSW[6] ^ p8 ^ p4 ^ p2 ^ p1;
		
		cp8 = p8 ^ ogMSW[7];
		cp4 = p4 ^ ogLSW[3];
		cp2 = p2 ^ ogLSW[5];
		cp1 = p1 ^ ogLSW[6];
		cp16 = p16 ^ ogLSW[7];
		
		boolean[] newMSW = {MSW[5], MSW[6], MSW[7], LSW[0], LSW[1], LSW[2], LSW[3], p8};
		boolean[] newLSW = {LSW[4], LSW[5], LSW[6], p4, LSW[7], p2, p1, p16};
		
		System.out.print("New MSW: ");
		for (boolean b : newMSW) {
			System.out.print(getBit(b) + " ");
		}
		System.out.println();
		System.out.print("New LSW: ");
		for (boolean b : newLSW) {
			System.out.print(getBit(b) + " ");
		}
		System.out.println();
		System.out.print("MSW: ");
		for (boolean b : MSW) {
			System.out.print(getBit(b) + " ");
		}
		System.out.println();
		System.out.print("LSW: ");
		for (boolean b : LSW) {
			System.out.print(getBit(b) + " ");
		}
		System.out.println();
		System.out.println("Old bits: " + getBit(ogMSW[7]) + " " + getBit(ogLSW[3]) + " " + getBit(ogLSW[5]) + " " + getBit(ogLSW[6]) + " " + getBit(ogLSW[7]));
		System.out.println("New bits: " + getBit(newMSW[7]) + " " + getBit(newLSW[3]) + " " + getBit(newLSW[5]) + " " + getBit(newLSW[6]) + " " + getBit(newLSW[7]));
		System.out.println("Difference bits: " + getBit(cp8) + " " + getBit(cp4) + " " + getBit(cp2) + " " + getBit(cp1) + " " + getBit(cp16));
	}
	
	static int getBit(boolean b) {
		return b ? 1 : 0;
	}
}
