package demo.bracelet.DBTemp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class IMSIMap {
	public static Map<String, String> iMap= new HashMap<String, String>(); //�ҳ��� ���ֻ���
	public static Map<String, List<String>> idToImsiMap= new HashMap<String, List<String>>(); //�ֻ��˶Լҳ���
	
	public static void show() {
		Set<Map.Entry<String, String>> allset = iMap.entrySet();
		Iterator<Map.Entry<String, String>> a = allset.iterator();
		while (a.hasNext()) {
			Entry<String, String> me = a.next();// ����key��value����
			System.out.println(me.getKey() + "--->" + me.getValue());// ����ؼ��ֺ�����
		}
		System.out.println("");
		Set<Map.Entry<String, List<String>>> allset1 = idToImsiMap.entrySet();
		Iterator<Map.Entry<String, List<String>>> a1 = allset1.iterator();
		while (a1.hasNext()) {
			Entry<String, List<String>> me = a1.next();// ����key��value����
			for(String key : me.getValue()){
				System.out.println(me.getKey() + "--->" + key);// ����ؼ��ֺ�����
			}
			
		}
	}
}
