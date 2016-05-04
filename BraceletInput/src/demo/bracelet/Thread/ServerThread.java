package demo.bracelet.Thread;

import gson.gsonTool;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import demo.bracelet.DBTemp.IMSIMap;
import demo.bracelet.DBdomain.HealthData;
import demo.bracelet.DBdomain.PositionData;
import demo.bracelet.DBdomain.SafeDangerData;
import demo.bracelet.Util.jsonUtil;
import demo.bracelet.dbHelp.HealthSql;
import demo.bracelet.dbHelp.PositionSql;
import demo.bracelet.dbHelp.SafeDangerSql;
import demo.bracelet.dbHelp.SelectHelp;
import demo.bracelet.main.MyServer;
import domain.All;
import Message.DefaultMessage;
import Message.MessageType;

public class ServerThread implements Runnable {

	private Socket socket = null;
	private BufferedReader bufferedReader = null;
	private PrintWriter outputStream;
	private String keyString = null;
	private boolean IsBracelet;

	public ServerThread(Socket socket,String keyString,boolean isIsBracelet) throws IOException {
		this.socket = socket;
		this.bufferedReader = new BufferedReader(new InputStreamReader(
				socket.getInputStream(), "GBK"));
		outputStream = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(this.socket.getOutputStream(), "GBK")),
				true);
		this.keyString = keyString;
		MyServer.socketList.put(keyString, this);
		this.IsBracelet = isIsBracelet;
		System.out.println(keyString);
	}

	@Override
	public void run() {
		DefaultMessage message = null;
		while ((message = readFromClient()) != null) {
			
			if(message.getFlag().equals(MessageType.MESSAGE_GET_ALL)){
				SelectHelp selectHelp = new SelectHelp(keyString);
				All all = selectHelp.getAll();
				DefaultMessage message2 = new DefaultMessage(MessageType.MESSAGE_RETURN_ALL, jsonUtil.createJsonString(all));
				sendMes(message2);
			}else if (message.getFlag().equals(MessageType.MESSAGE_GET_OK)) {
				DefaultMessage message2 = new DefaultMessage(MessageType.MESSAGE_RETURN_OK, "");
				sendMes(message2);
			}else if (message.getFlag().equals(MessageType.MESSAGE_RETURN_HEALTH)) {
				send(message);
				addHealth(message);
			}else if (message.getFlag().equals(MessageType.MESSAGE_RETURN_GPS)) {
				send(message);
				addPosition(message);
			}else if (message.getFlag().equals(MessageType.MESSAGE_SET_SAFE)) {
				send(message);
				addSafeDanger(message);
			}else if (message.getFlag().equals(MessageType.MESSAGE_SET_DANGER)) {
				send(message);
				addSafeDanger(message);
			}else {
				send(message);
			}
		}
	}
	public void send(DefaultMessage message){
		System.out.println("类型--->>"+message.getFlag());
		if(IsBracelet){
			List<String> list = IMSIMap.idToImsiMap.get(keyString);
			for(String duiyingIM : list){
//				System.out.println(duiyingIM);
				if(MyServer.socketList.containsKey(duiyingIM)){System.out.println("--->"+duiyingIM);
					MyServer.socketList.get(duiyingIM).sendMes(message);
				}
			}
		}else{
			String duiyingIM = IMSIMap.iMap.get(keyString);
			if (duiyingIM == null) {
				System.out.println("未找到对应的手机，请修改IMSIMap类的map 为对应手机的IMSI");
			} else {
				if(MyServer.socketList.containsKey(duiyingIM)){
					MyServer.socketList.get(duiyingIM).sendMes(message);
				}
			}
		}
	}
	public void sendMes(DefaultMessage message) {
		String context = jsonUtil.createJsonString(message);
		System.out.println(context);
		outputStream.println(context);
	}

	private DefaultMessage readFromClient() {
		DefaultMessage message = null;
		String jsonString = null;
		try {
			jsonString = bufferedReader.readLine();
			message = gsonTool.getT(jsonString, DefaultMessage.class);
			return message;
		}
		catch (IOException e) {
			MyServer.socketList.remove(keyString);
		}
		return null;
	}
	private void addPosition(DefaultMessage message){
		PositionSql positionSql = new PositionSql();
		PositionData positionData = gsonTool.getT(message.getContext(), PositionData.class);
		positionSql.PositionInsert(positionData);
	}
	private void addHealth(DefaultMessage message){
		HealthSql healthSql = new HealthSql();
		HealthData healthData = gsonTool.getT(message.getContext(), HealthData.class);		
		healthSql.HealthInsert(keyString, healthData);
	}
	private void addSafeDanger(DefaultMessage message){
		SafeDangerSql safeDangerSql = new SafeDangerSql();
		SafeDangerData safeDangerData = gsonTool.getT(message.getContext(), SafeDangerData.class);
		safeDangerData.setBraceletId(IMSIMap.iMap.get(keyString));
		if(safeDangerSql.isExistSafeDanger(safeDangerData)){
			safeDangerSql.SafeDangerUpdate(safeDangerData);
		}else{
			safeDangerSql.SafeDangerInert(safeDangerData);
		}
	}
}
