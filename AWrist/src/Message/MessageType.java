package Message;

/**
 * @author xuweifeng
 *
 */
public class MessageType {
	/*
	 * 	MESSAGE_SIGN_SERVER		 	  �������������֤��Ϣ
	 *  MESSAGE_LIANJIE_SERVER		  �������������������
	 * 
	 * 	 �ҳ��˷�����Ϣ���Ͷ���
	 * 
	 *   MESSAGE_GET_GPS             ��ȡ�ֻ��˵�ǰλ��
	 *   MESSAGE_GET_GPS_LIST        ��ȡ�ֻ�����ʷ·��
	 *   MESSAGE_SAID				   ���͸��ֻ��˵�������Ϣ
	 *   MESSAGE_SET_ALARM_CLOCK	   ���͸��ֻ��˵Ķ�ʱ�¼�������Ϣ
	 *   MESSAGE_SET_DANGER			   ���͸��ֻ��˵�Σ����������Ϣ
	 *   MESSAGE_SET_EARLY			   ���͸��ֻ��˵�Ԥ����������Ϣ
	 *   MESSAGE_SET_SAFE			   ���͸��ֻ��˵İ�ȫ��������Ϣ
	 *   MESSAGE_GET_All             ��ȡ����
	 *   
	 *   �ֻ��˷�����Ϣ���Ͷ���
	 *   
	 *   MESSAGE_RETURN_GPS			   ���ص��ֻ��˵�ǰλ��
	 *   MESSAGE_RETURN_HEALTH		   ���ص��ֻ��˵�ǰ������Ϣ
	 *   MESSAGE_RETURN_OK			   ���ص�ȷ����Ϣ
	 *   MESSAGE_RETURN_GPS_LIST     ���ص��ֻ�����ʷ·��
	 *   MESSAGE_WARN      	 		   ��õ��ֻ��˾�����Ϣ
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
