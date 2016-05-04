package demo.bracelet.main;

import gson.gsonTool;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Message.DefaultMessage;
import Message.MessageType;
import demo.bracelet.DBTemp.IMSIMap;
import demo.bracelet.DBdomain.BraceletData;
import demo.bracelet.DBdomain.PhoneData;
import demo.bracelet.MyWindows.WindowInput;
import demo.bracelet.Thread.ServerThread;
import demo.bracelet.Util.jsonUtil;
import demo.bracelet.dbHelp.BraceletSql;
import demo.bracelet.dbHelp.PhoneSql;
import domain.Sign;

public class MyServer {
	public static Map<String, ServerThread> socketList = new HashMap<String, ServerThread>();

	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		WindowInput WInput = new WindowInput();
		WInput.launch();
		initMap();
		IMSIMap.show();
		@SuppressWarnings("resource")
		ServerSocket serverSocket = new ServerSocket(11111);
		while (true) {
			Socket socket = serverSocket.accept();
			PrintWriter outputStream = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream(), "GBK")),
					true);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(socket.getInputStream(), "GBK"));
			String jString = bufferedReader.readLine();
			DefaultMessage message = gsonTool.getT(jString,
					DefaultMessage.class);
			if (message.getFlag().equals(MessageType.MESSAGE_SIGN_SERVER)) {
				Sign sign = gsonTool.getT(message.getContext(), Sign.class);
				if (!sign.getIsBracelet()) {
					// 验证是否存在
					if (isSign(sign)) {
						saveSign(sign);
						DefaultMessage returnMessage = new DefaultMessage(
								MessageType.MESSAGE_RETURN_SIGN_SUCC, "success");
						String context = jsonUtil
								.createJsonString(returnMessage);
						outputStream.println(context);
						new Thread(new ServerThread(socket, sign.getIMSI(),
								sign.getIsBracelet())).start();
					} else {
						DefaultMessage returnMessage = new DefaultMessage(
								MessageType.MESSAGE_RETURN_SIGN_BAD, "faile");
						String context = jsonUtil
								.createJsonString(returnMessage);
						outputStream.println(context);
						outputStream.close();
						bufferedReader.close();
						socket.close();
					}
				} else {
					new Thread(new ServerThread(socket, sign.getBraceletId(),
							sign.getIsBracelet())).start();
				}
			} else if (message.getFlag().equals(
					MessageType.MESSAGE_LIANJIE_SERVER)) {
				Sign sign = gsonTool.getT(message.getContext(), Sign.class);
				System.out.println("IMSI="+sign.getIMSI());
				new Thread(new ServerThread(socket, sign.getIMSI(),
						sign.getIsBracelet())).start();
			} else {
				socket.close();
			}
		}
	}

	private static void initMap() {
		PhoneSql phoneSql = new PhoneSql();
		BraceletSql braceletSql = new BraceletSql();
		List<PhoneData> phoneDatas = phoneSql.PhoneFindAll();
		List<BraceletData> braceletDatas = braceletSql.BraceletFindAll();
		for (PhoneData data : phoneDatas) {
			IMSIMap.iMap.put(data.getIMSI(), data.getBraceletid());
		}
		for (BraceletData data : braceletDatas) {
			List<String> list = phoneSql.PhoneFindIMSI(data.getBraceletId());
			IMSIMap.idToImsiMap.put(data.getBraceletId(), list);
		}
	}

	private static boolean isSign(Sign sign) {
		boolean flag = true;
		if (IMSIMap.idToImsiMap.containsKey(sign.getBraceletId())) {
			BraceletSql braceletSql = new BraceletSql();
			if (braceletSql.BraceletFindCaptcha(sign.getBraceletId()).equals(
					sign.getYanZhen())) {
				flag = true;
			} else {
				flag = false;
			}
		} else {
			flag = false;
		}
		return flag;
	}

	private static void saveSign(Sign sign) {
		PhoneSql phoneSql = new PhoneSql();
		String id = null;
		if (!IMSIMap.iMap.containsKey(sign.getIMSI())) {
			// 不存在
			phoneSql.PhoneInert(new PhoneData(sign.getIMSI(), sign
					.getBraceletId())); // 添加
			IMSIMap.iMap.put(sign.getIMSI(), sign.getBraceletId());
		} else {
			// 存在
			id = IMSIMap.iMap.get(sign.getIMSI());
			if (id != sign.getBraceletId()) {
				phoneSql.PhoneUpdate(sign.getIMSI(), sign.getBraceletId()); // 更新
				IMSIMap.idToImsiMap.get(id).remove(sign.getIMSI());
				IMSIMap.idToImsiMap.get(sign.getBraceletId()).add(sign.getIMSI());
				IMSIMap.iMap.put(sign.getIMSI(), sign.getBraceletId());
			}
		}
		IMSIMap.show();
	}
}
