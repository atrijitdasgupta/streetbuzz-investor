package es.daedalus.textalytics.sempub.utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TermUtils {

	public static String remove(String s, String collection) {
		List<String> col = Arrays.asList(collection.split("\\|"));
		searchAndRemove(s, col);
		if(!col.isEmpty()) {
			StringBuffer buffer = new StringBuffer();
			Iterator<String> it = col.iterator();
			buffer.append(it.next().toString());
			while(it.hasNext()) {
				buffer.append('|').append(it.next());
			}
			return buffer.toString();
		} else {
			return "";
		}
	}
	
	public static void searchAndRemove(String s, List<String> collection) {
		for(String st : collection) {
			if(st.contains(s))
				collection.remove(st);
		}
	}
	
	public static String add(String s, String collection) {
		return collection + "|" + s;
	}
	
	
}
