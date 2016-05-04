package demo.bracelet.Util;

import com.google.gson.Gson;


public class jsonUtil {
		public jsonUtil() {
			// TODO Auto-generated constructor stub
		}
		/**
		 * @param key
		 *            表示json字符串的头信息
		 * @param object
		 *            是对解析的集合的类型
		 * @return
		 */
		public static String createJsonString(Object value) {
			Gson gson = new Gson();
			String str = gson.toJson(value);
			return str;
		}
}
