import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Tester {
	public static void main(String args[]) {
		Calendar inst = Calendar.getInstance();
		inst.setTimeInMillis(System.currentTimeMillis() + 86400000);
		SimpleDateFormat format = new SimpleDateFormat("MMM dd hh:mm a z");
		System.out.println(format.format(inst.getTime()));
	}
}
