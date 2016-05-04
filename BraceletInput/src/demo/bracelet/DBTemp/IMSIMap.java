package demo.bracelet.DBTemp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class IMSIMap {
	public static Map<String, String> iMap= new HashMap<String, String>(); //家长端 对手环端
	public static Map<String, List<String>> idToImsiMap= new HashMap<String, List<String>>(); //手环端对家长端
	
	public static void show() {
		Set<Map.Entry<String, String>> allset = iMap.entrySet();
		Iterator<Map.Entry<String, String>> a = allset.iterator();
		while (a.hasNext()) {
			Entry<String, String> me = a.next();// 进行key和value分离
			System.out.println(me.getKey() + "--->" + me.getValue());// 输出关键字和内容
		}
		System.out.println("");
		Set<Map.Entry<String, List<String>>> allset1 = idToImsiMap.entrySet();
		Iterator<Map.Entry<String, List<String>>> a1 = allset1.iterator();
		while (a1.hasNext()) {
			Entry<String, List<String>> me = a1.next();// 进行key和value分离
			for(String key : me.getValue()){
				System.out.println(me.getKey() + "--->" + key);// 输出关键字和内容
			}
			
		}
	}
}
