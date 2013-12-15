package hsa.awp.usergui.util;

import java.util.Random;

public class RandomColor {
	
	private static char[] val = new char[] { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private static Random random = new Random();
	
	
	public static String getRandomHexColor(String cssAtt){
		String s = "#";
		while(s.length() < 7){
			s += val[random.nextInt(val.length)];
		}
		return cssAtt+": " + s;
	}
}
