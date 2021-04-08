import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;

public class Moebius {
	public static void main(String args[]) {
		int exp = 0;
		int level = 5;
		HashMap<Integer, Integer> levelup = new HashMap<Integer, Integer>();
		System.out.println("Exp gain:");
		Scanner scan = new Scanner(System.in);
		
		levelup.put(5, 1);
		levelup.put(6, 1);
		levelup.put(7, 1);
		levelup.put(8, 1);
		levelup.put(9, 1);
		levelup.put(10, 3);
		levelup.put(11, 4);
		levelup.put(12, 5);
		levelup.put(13, 6);
		levelup.put(14, 7);
		levelup.put(15, 9);
		levelup.put(16, 11);
		levelup.put(17, 13);
		levelup.put(18, 15);
		levelup.put(19, 17);
		levelup.put(20, 19);
		levelup.put(21, 21);
		levelup.put(22, 23);
		levelup.put(23, 25);
		levelup.put(24, 27);
		levelup.put(25, 29);
		levelup.put(26, 31);
		levelup.put(27, 33);
		levelup.put(28, 35);
		levelup.put(29, 37);
		levelup.put(30, 40);
		levelup.put(31, 50);
		levelup.put(32, 60);
		levelup.put(33, 70);
		levelup.put(34, 80);
		levelup.put(35, 90);
		levelup.put(36, 100);
		levelup.put(37, 110);
		levelup.put(38, 120);
		levelup.put(39, 130);
		levelup.put(40, 140);
		levelup.put(41, 150);
		levelup.put(42, 175);
		levelup.put(43, 200);
		levelup.put(44, 225);
		levelup.put(45, 250);
		levelup.put(46, 275);
		levelup.put(47, 300);
		levelup.put(48, 325);
		levelup.put(49, 350);
		levelup.put(50, 400);
		levelup.put(51, 450);
		levelup.put(52, 500);
		levelup.put(53, 550);
		levelup.put(54, 600);
		levelup.put(55, 650);
		levelup.put(56, 700);
		levelup.put(57, 750);
		levelup.put(58, 800);
		levelup.put(59, 850);
		levelup.put(60, 900);
		levelup.put(61, 1000);
		levelup.put(62, 1100);
		levelup.put(63, 1200);
		levelup.put(64, 1300);
		levelup.put(65, 1400);
		levelup.put(66, 1500);
		levelup.put(67, 1600);
		while (scan.hasNext()) {
			String line = scan.next();
			if (line.equals("reset")) {
				level = 5;
				exp = 0;
				System.out.println("Level is " + level + ", exp is " + exp + " / " + levelup.get(level));
			}
			else if (line.equals("done")) {
				System.out.println("End program.");
				return;
			}
			else {
				exp += Integer.parseInt(line);
				
				// De-level
				boolean changed = false;
				while (exp < 0) {
					level--;
					exp += levelup.get(level);
					System.out.println("Level down to " + level + ", exp is " + exp + " / " + levelup.get(level));
					changed = true;
				}
				
				while (levelup.containsKey(level) && exp >= levelup.get(level)) {
					exp -= levelup.get(level);
					level++;
					System.out.println("Level up to " + level + ", exp is " + exp + " / " + levelup.get(level));
					changed = true;
				}
				if (!changed) System.out.println("Level is " + level + ", exp is " + exp + " / " + levelup.get(level));
			}
		}
	}
}
