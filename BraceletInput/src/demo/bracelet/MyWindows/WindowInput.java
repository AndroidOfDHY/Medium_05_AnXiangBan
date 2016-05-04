package demo.bracelet.MyWindows;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import demo.bracelet.myAction.MyInput;


// ������
public class WindowInput {
	public  Frame f;  // ��������
	public  Panel p1;  // ���ֲ�1
	public  Panel p1_1;  // ���ֲ�1���Ӳ�1
	public  Panel p1_2;  // ���ֲ�1���Ӳ�2
	public  Panel p2;  // ���ֲ�2
	public  TextField braceletInput;  // �����ı���
	public  Button InPut;  // ���밴ť
	public  Label ShowCaptcha;  // ��ʾ��֤��ı�ǩ
	public BufferedImage ImageQRCode;  // �洢ͼƬ�Ķ���
	public  JLabel ShowQRCode;  // ��ʾ��ά��ı�ǩ
	
	
	public void launch() throws FileNotFoundException, IOException {
		f = new Frame("�ֻ�ID����");  // �������ڶ��󣬲���������Ϊ��������
		f.setSize(400, 300);  // ���ô��ڴ�С
		
		f.addWindowListener(new WindowAdapter() {  // ��Ӵ��ڼ���
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(-1);  // �������رհ�ť���˳�����
			}
		});
		
		// ���ڲ���
		f.setLayout(new GridLayout(2, 1));
		p1 = new Panel();
		p1.setLayout(new GridLayout(3, 1));
		p1_1 = new Panel();
		p1_1.setLayout(new FlowLayout());
		p1_2 = new Panel();
		p1_2.setLayout(new FlowLayout());
		p2 = new Panel();
		p2.setLayout(new FlowLayout(1));
		
		braceletInput = new TextField(20);  // ��ʼ���ı���
		braceletInput.setFont(new Font("����", Font.BOLD, 25));  // �����ı�����
		InPut = new Button("����");  // ��ʼ����ť
		ShowCaptcha = new Label("��֤�룺                                ", Label.CENTER);  // ��ʼ��ռλ������ʾ��֤�����ʾ
		ShowCaptcha.setFont(new Font("", Font.BOLD, 30));  // ������ʾ�������С
		
		ImageQRCode = ImageIO.read(new FileInputStream("D:\\Axb\\0.png"));  // ��ʼ��ȡ�Ŀհ�ͼƬ
		
		
		ShowQRCode = new JLabel();  // ������ʾͼƬ�ı�ǩ��ʼ��
		ShowQRCode.setIcon(new ImageIcon(ImageQRCode));  // ����ͼƬ��ʾ
		
		
		p1_1.add(braceletInput);  // �Ӳ�1ΪFlow���֣������ı���
		p1_1.add(InPut);  // �Ӳ�1ΪFlow���֣��������밴ť
		p1_2.add(ShowCaptcha);  // �Ӳ�2ΪFlow���֣�������ʾ�ı���ǩ
		
		p1.add(p1_1);  // �����Ӳ�1
		p1.add(p1_2);  // �����Ӳ�2
		Label erweima = new Label("��ά�룺",Label.LEFT);
		erweima.setFont(new Font("", Font.BOLD, 30));
		
		p2.add(erweima);
		p2.add(ShowQRCode);  // ���ֲ�2������ʾ��ά��ı�ǩ

		f.add(p1);  // ���벼�ֲ�1
		f.add(p2);  // ���벼�ֲ�2
		
		InPut.addActionListener(new MyInput(this));  // ���ð�ť�ļ���
		
		f.pack();  // ����
		f.setResizable(false);  // ���ô��ڴ�С���ɱ�
		f.setVisible(true);  // ������ʾ����
	}
}
