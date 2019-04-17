import java.util.ArrayList;
import java.util.List;

public class Moebius {
	public static void main(String args[]) {
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		list.add(6);
		list.add(7);
		list.add(8);
		list.add(9);
		list.add(10);
		list.add(11);
		
		removeBigNumbers(list);
		System.out.println(list);
	}
		
	public static List<Integer> removeBigNumbers(List<Integer> data) {
		System.out.println(data);
		for (int i = 0; i < data.size(); i++) {
			if(data.get(i) > 10) {
				data.remove(i);
			}
		}
		return data;
	}
}
