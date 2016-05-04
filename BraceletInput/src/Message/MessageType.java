package Message;

/**
 * @author xuweifeng
 *
 */
public class MessageType {
	/*
	 * 	MESSAGE_SIGN_SERVER		 	  向服务器发送验证信息
	 *  MESSAGE_LIANJIE_SERVER		  向服务器发送连接请求
	 * 
	 * 	 家长端发送消息类型定义
	 * 
	 *   MESSAGE_GET_GPS             获取手环端当前位置
	 *   MESSAGE_GET_GPS_LIST        获取手环端历史路径
	 *   MESSAGE_SAID				   发送给手环端的语音信息
	 *   MESSAGE_SET_ALARM_CLOCK	   发送给手环端的定时事件设置信息
	 *   MESSAGE_SET_DANGER			   发送给手环端的危险区设置信息
	 *   MESSAGE_SET_EARLY			   发送给手环端的预警区设置信息
	 *   MESSAGE_SET_SAFE			   发送给手环端的安全区设置信息
	 *   MESSAGE_GET_All             获取数据
	 *   
	 *   手环端发送消息类型定义
	 *   
	 *   MESSAGE_RETURN_GPS			   返回的手环端当前位置
	 *   MESSAGE_RETURN_HEALTH		   返回的手环端当前健康信息
	 *   MESSAGE_RETURN_OK			   返回的确认信息
	 *   MESSAGE_RETURN_GPS_LIST     返回的手环端历史路径
	 *   MESSAGE_WARN      	 		   获得的手环端警告信息
	 */
	
	public static final String MESSAGE_SIGN_SERVER = "SIGN_SERVER";
	public static final String MESSAGE_LIANJIE_SERVER = "LIANJIE_SERVER";
	
	public static final String MESSAGE_GET_GPS = "GPS";
	public static final String MESSAGE_GET_ALL = "All";
	public static final String MESSAGE_GET_GPS_LIST = "GPS_LIST";
	public static final String MESSAGE_GET_OK = "OK";
	public static final String MESSAGE_SAID = "SAID";
	public static final String MESSAGE_SET_ALARM_CLOCK = "SET_ALARM_CLOCK";
	public static final String MESSAGE_UN_ALARM_CLOCK = "UN_ALARM_CLOCK";
	public static final String MESSAGE_SET_DANGER = "SET_DANGER";
	public static final String MESSAGE_SET_EARLY = "SET_EARLY";
	public static final String MESSAGE_SET_SAFE = "SET_SAFE";
	public static final String MESSAGE_SET_HOME = "SET_HOME";
	public static final String MESSAGE_UN_HOME = "UN_HOME";
	
	
	public static final String MESSAGE_RETURN_GPS = "RETURN_GPS";
	public static final String MESSAGE_RETURN_OK = "RETURN_OK";
	public static final String MESSAGE_RETURN_ALL = "RETURN_All";
	public static final String MESSAGE_RETURN_GPS_LIST = "RETURN_GPS_LIST";
	public static final String MESSAGE_RETURN_HEALTH = "RETURN_HEALTH";
	public static final String MESSAGE_RETURN_HOME = "RETURN_HOME";
	public static final String MESSAGE_WARN = "WARN";
	public static final String MESSAGE_RETURN_SIGN_BAD = "RETURN_SIGN_BAD";
	public static final String MESSAGE_RETURN_SIGN_SUCC = "RETURN_SIGN_SUCC";
}
