package demo.bracelet.Util;

import com.google.gson.Gson;


public class jsonUtil {
		public jsonUtil() {
			// TODO Auto-generated constructor stub
		}
		/**
		 * @param key
		 *            ��ʾjson�ַ�����ͷ��Ϣ
		 * @param object
		 *            �ǶԽ����ļ��ϵ�����
		 * @return
		 */
		public static String createJsonString(Object value) {
			Gson gson = new Gson();
			String str = gson.toJson(value);
			return str;
		}
}
