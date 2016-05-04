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
	public WindowInput WInput;  // ���������
	
	public MyInput(WindowInput WInput) {  // ���캯��
		this.WInput = WInput;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String BraceletId = WInput.braceletInput.getText();  // ��ȡ�����ı����ַ���
		BraceletSql braceletSql = new BraceletSql();
		BraceletData data = new BraceletData();
		data.setBraceletId(BraceletId);// �ж��������
		if(BraceletId.length() < 15) {  // �ж��ַ�����
			WInput.ShowCaptcha.setText("����������������룡");
			WInput.ShowCaptcha.setFont(new Font("", Font.BOLD, 18));
			WInput.ShowCaptcha.setForeground(Color.RED);  // ����Ϊ��ɫ����
			WInput.braceletInput.setText("");  // ��������ı���
			try {
				WInput.ImageQRCode = ImageIO.read(new FileInputStream("D:\\Axb\\0.png"));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			WInput.ShowQRCode.setIcon(new ImageIcon(WInput.ImageQRCode));  // ��ͼƬ�����ڱ�ǩ��
		} else if(braceletSql.IsExist(data)) {  // �жϱ����Ƿ���ڸ�ID
			WInput.ShowCaptcha.setText("���ֻ�ID�Ѵ��ڣ�");
			WInput.ShowCaptcha.setFont(new Font("", Font.BOLD, 18));
			WInput.ShowCaptcha.setForeground(Color.RED);  // ����Ϊ��ɫ����
			WInput.braceletInput.setText("");  // ��������ı���
			try {
				WInput.ImageQRCode = ImageIO.read(new FileInputStream("D:\\Axb\\0.png"));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			WInput.ShowQRCode.setIcon(new ImageIcon(WInput.ImageQRCode));  // ��ͼƬ�����ڱ�ǩ��
			
		} else {  // �����ڸ�ID�������ɶ�ά�룬�Լ�6λ����֤�룬����ʾ���Լ��������ݿ����
			// ��ά�����ɲ���  
			QRCode code = QRCode.getInstance();  // ˵�Ķ�ά���һ������
			code.encoder(BraceletId, "D:\\Axb\\" + BraceletId + ".png");  // ͨ���ֻ�ID���ɶ�ά�룬��������ָ��λ�á�
			
			try {
				WInput.ImageQRCode = ImageIO.read(new FileInputStream("D:\\Axb\\" + BraceletId + ".png"));  // ���ö�ȡ��ά��ͼƬ
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			WInput.ShowQRCode.setIcon(new ImageIcon(WInput.ImageQRCode));  // ��ͼƬ�����ڱ�ǩ��
			// ������֤�벢��ʾ
			CMyEncrypt cMyEncrypt = new CMyEncrypt();
			String aResult = cMyEncrypt.TheCaptcha(BraceletId);  // ����6λ����֤��
			WInput.ShowCaptcha.setText(" ��֤�룺" + aResult + " ");  // ������ʾ�ı���ǩΪ��֤��
			WInput.ShowCaptcha.setFont(new Font("", Font.BOLD, 30));
			WInput.ShowCaptcha.setForeground(Color.BLACK);  // ����Ϊ��ɫ
			// �����ֻ�id���Լ���֤��
			data.setCaptcha(aResult);
			braceletSql.BraceletInert(data);
			IMSIMap.idToImsiMap.put(BraceletId, new ArrayList<String>());
		}
	}	
}