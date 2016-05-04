package demo.bracelet.myAction;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.marker.qrcode.QRCode;
import demo.bracelet.DBTemp.IMSIMap;
import demo.bracelet.DBdomain.BraceletData;
import demo.bracelet.MyWindows.WindowInput;
import demo.bracelet.Util.CMyEncrypt;
import demo.bracelet.dbHelp.BraceletSql;

public class MyInput implements java.awt.event.ActionListener {
	public WindowInput WInput;  // 窗口类对象
	
	public MyInput(WindowInput WInput) {  // 构造函数
		this.WInput = WInput;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String BraceletId = WInput.braceletInput.getText();  // 获取输入文本的字符串
		BraceletSql braceletSql = new BraceletSql();
		BraceletData data = new BraceletData();
		data.setBraceletId(BraceletId);// 判断输入错误
		if(BraceletId.length() < 15) {  // 判断字符长度
			WInput.ShowCaptcha.setText("输入错误，请重新输入！");
			WInput.ShowCaptcha.setFont(new Font("", Font.BOLD, 18));
			WInput.ShowCaptcha.setForeground(Color.RED);  // 设置为红色提醒
			WInput.braceletInput.setText("");  // 清空输入文本框
			try {
				WInput.ImageQRCode = ImageIO.read(new FileInputStream("D:\\Axb\\0.png"));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			WInput.ShowQRCode.setIcon(new ImageIcon(WInput.ImageQRCode));  // 将图片设置在标签中
		} else if(braceletSql.IsExist(data)) {  // 判断表内是否存在该ID
			WInput.ShowCaptcha.setText("该手环ID已存在！");
			WInput.ShowCaptcha.setFont(new Font("", Font.BOLD, 18));
			WInput.ShowCaptcha.setForeground(Color.RED);  // 设置为红色提醒
			WInput.braceletInput.setText("");  // 清空输入文本框
			try {
				WInput.ImageQRCode = ImageIO.read(new FileInputStream("D:\\Axb\\0.png"));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			WInput.ShowQRCode.setIcon(new ImageIcon(WInput.ImageQRCode));  // 将图片设置在标签中
			
		} else {  // 不存在该ID，则生成二维码，以及6位的验证码，并显示，以及插入数据库表中
			// 二维码生成部分  
			QRCode code = QRCode.getInstance();  // 说的二维码的一个对象
			code.encoder(BraceletId, "D:\\Axb\\" + BraceletId + ".png");  // 通过手环ID生成二维码，并保存在指定位置。
			
			try {
				WInput.ImageQRCode = ImageIO.read(new FileInputStream("D:\\Axb\\" + BraceletId + ".png"));  // 设置读取二维码图片
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			WInput.ShowQRCode.setIcon(new ImageIcon(WInput.ImageQRCode));  // 将图片设置在标签中
			// 生成验证码并显示
			CMyEncrypt cMyEncrypt = new CMyEncrypt();
			String aResult = cMyEncrypt.TheCaptcha(BraceletId);  // 生成6位的验证码
			WInput.ShowCaptcha.setText(" 验证码：" + aResult + " ");  // 设置显示文本标签为验证码
			WInput.ShowCaptcha.setFont(new Font("", Font.BOLD, 30));
			WInput.ShowCaptcha.setForeground(Color.BLACK);  // 设置为黑色
			// 插入手环id，以及验证码
			data.setCaptcha(aResult);
			braceletSql.BraceletInert(data);
			IMSIMap.idToImsiMap.put(BraceletId, new ArrayList<String>());
		}
	}	
}