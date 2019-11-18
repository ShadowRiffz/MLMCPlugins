
public class XORCalculator {
	public static void main(String args[]) {
		boolean[] word 			= {true, false, true, false, true, false, true, false, true, false, true, false};
		boolean[] corruptWord 	= {true, false, true, false, true, false, true, false, true, false, true, false};
		reverse(word);
		reverse(corruptWord);
		
		boolean twoBitCorrupt = false;
		boolean oneBitCorrupt = true;
		
		boolean p8, p4, p2, p1, p16;
		boolean cp8, cp4, cp2, cp1, cp16;
		int rp8, rp4, rp2, rp1, rp16;
		
		// original word
		p8 = getP8(word);
		p4 = getP4(word);
		p2 = getP2(word);
		p1 = getP1(word);
		p16 = getP16(word, p8, p4, p2, p1);
		
		// corrupt word
		cp8 = getP8(corruptWord);
		cp4 = getP4(corruptWord);
		cp2 = getP2(corruptWord);
		cp1 = getP1(corruptWord);
		cp16 = getP16(corruptWord, p8, p4, p2, p1);
		
		if (oneBitCorrupt) {
			for (int i = 11; i >= 1; i--) {
				// Flip corrupt word bit
				corruptWord[i] = !corruptWord[i];
				// original word
				p8 = getP8(word);
				p4 = getP4(word);
				p2 = getP2(word);
				p1 = getP1(word);
				p16 = getP16(word, p8, p4, p2, p1);
				
				// corrupt word
				cp8 = getP8(corruptWord);
				cp4 = getP4(corruptWord);
				cp2 = getP2(corruptWord);
				cp1 = getP1(corruptWord);
				cp16 = getP16(corruptWord, p8, p4, p2, p1);
				
				rp8 = getBit(p8 ^ cp8);
				rp4 = getBit(p4 ^ cp4);
				rp2 = getBit(p2 ^ cp2);
				rp1 = getBit(p1 ^ cp1);
				rp16 = getBit(p16 ^ cp16);
				
				System.out.println("b[" + i + "]: " + rp8 + " " + rp4 + " " + rp2 + " " + rp1 + " " + rp16);
				corruptWord[i] = !corruptWord[i];
			}
		}
		
		if (twoBitCorrupt) {
			for (int i = 11; i >= 2; i--) {
				for (int j = i - 1; j >= 1; j--) {
					// Flip corrupt word bit
					corruptWord[i] = !corruptWord[i];
					corruptWord[j] = !corruptWord[j];
					// original word
					p8 = getP8(word);
					p4 = getP4(word);
					p2 = getP2(word);
					p1 = getP1(word);
					p16 = getP16(word, p8, p4, p2, p1);
					
					// corrupt word
					cp8 = getP8(corruptWord);
					cp4 = getP4(corruptWord);
					cp2 = getP2(corruptWord);
					cp1 = getP1(corruptWord);
					cp16 = getP16(corruptWord, p8, p4, p2, p1);
					
					rp8 = getBit(p8 ^ cp8);
					rp4 = getBit(p4 ^ cp4);
					rp2 = getBit(p2 ^ cp2);
					rp1 = getBit(p1 ^ cp1);
					rp16 = getBit(p16 ^ cp16);
					
					System.out.println("b[" + i + "], b[ " + j + "]: " + rp8 + " " + rp4 + " " + rp2 + " " + rp1 + " " + rp16);
					corruptWord[i] = !corruptWord[i];
					corruptWord[j] = !corruptWord[j];
				}
			}
		}
	}

	static void reverse(boolean a[]) {
		boolean temp = false;
		for (int i = 0; i < a.length/2; i++) {
			temp = a[i];
			a[i] = a[a.length - i - 1];
			a[a.length - i - 1] = temp;
		}
	}
	
	static boolean getP8(boolean b[]) {
		boolean p = b[11];
		for(int i = 10; i >= 5; i--) {
			p = p ^ b[i];
		}
		return p;
	}
	
	static boolean getP4(boolean b[]) {
		boolean p = b[11];
		for(int i = 10; i >= 8; i--) {
			p = p ^ b[i];
		}
		for(int i = 4; i >= 2; i--) {
			p = p ^ b[i];
		}
		return p;
	}
	
	static boolean getP2(boolean b[]) {
		boolean p = b[11];
		p = p ^ b[10];
		p = p ^ b[7];
		p = p ^ b[6];
		p = p ^ b[4];
		p = p ^ b[3];
		p = p ^ b[1];
		return p;
	}
	
	static boolean getP1(boolean b[]) {
		boolean p = b[11];
		p = p ^ b[9];
		p = p ^ b[7];
		p = p ^ b[5];
		p = p ^ b[4];
		p = p ^ b[2];
		p = p ^ b[1];
		return p;
	}

	static boolean getP16(boolean b[], boolean p8, boolean p4, boolean p2, boolean p1) {
		boolean p = b[11];
		for(int i = 10; i >= 1; i--) {
			p = p ^ b[i];
		}
		return p ^ p8 ^ p4 ^ p2 ^ p1;
	}
	
	static int getBit(boolean b) {
		return b ? 1 : 0;
	}
}
