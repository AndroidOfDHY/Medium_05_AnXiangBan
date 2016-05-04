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


// 窗口类
public class WindowInput {
	public  Frame f;  // 窗口主体
	public  Panel p1;  // 布局层1
	public  Panel p1_1;  // 布局层1的子层1
	public  Panel p1_2;  // 布局层1的子层2
	public  Panel p2;  // 布局层2
	public  TextField braceletInput;  // 输入文本框
	public  Button InPut;  // 输入按钮
	public  Label ShowCaptcha;  // 显示验证码的标签
	public BufferedImage ImageQRCode;  // 存储图片的对象
	public  JLabel ShowQRCode;  // 显示二维码的标签
	
	
	public void launch() throws FileNotFoundException, IOException {
		f = new Frame("手环ID输入");  // 创建窗口对象，并将标题作为参数传入
		f.setSize(400, 300);  // 设置窗口大小
		
		f.addWindowListener(new WindowAdapter() {  // 添加窗口监听
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(-1);  // 监听到关闭按钮后退出程序
			}
		});
		
		// 窗口布局
		f.setLayout(new GridLayout(2, 1));
		p1 = new Panel();
		p1.setLayout(new GridLayout(3, 1));
		p1_1 = new Panel();
		p1_1.setLayout(new FlowLayout());
		p1_2 = new Panel();
		p1_2.setLayout(new FlowLayout());
		p2 = new Panel();
		p2.setLayout(new FlowLayout(1));
		
		braceletInput = new TextField(20);  // 初始化文本框
		braceletInput.setFont(new Font("宋体", Font.BOLD, 25));  // 设置文本字体
		InPut = new Button("输入");  // 初始化按钮
		ShowCaptcha = new Label("验证码：                                ", Label.CENTER);  // 初始化占位，以显示验证码或提示
		ShowCaptcha.setFont(new Font("", Font.BOLD, 30));  // 设置显示的字体大小
		
		ImageQRCode = ImageIO.read(new FileInputStream("D:\\Axb\\0.png"));  // 初始读取的空白图片
		
		
		ShowQRCode = new JLabel();  // 用来显示图片的标签初始化
		ShowQRCode.setIcon(new ImageIcon(ImageQRCode));  // 设置图片显示
		
		
		p1_1.add(braceletInput);  // 子层1为Flow布局，加入文本框
		p1_1.add(InPut);  // 子层1为Flow布局，加入输入按钮
		p1_2.add(ShowCaptcha);  // 子层2为Flow布局，加入显示文本标签
		
		p1.add(p1_1);  // 加入子层1
		p1.add(p1_2);  // 加入子层2
		Label erweima = new Label("二维码：",Label.LEFT);
		erweima.setFont(new Font("", Font.BOLD, 30));
		
		p2.add(erweima);
		p2.add(ShowQRCode);  // 布局层2加入显示二维码的标签

		f.add(p1);  // 加入布局层1
		f.add(p2);  // 加入布局层2
		
		InPut.addActionListener(new MyInput(this));  // 设置按钮的监听
		
		f.pack();  // 更新
		f.setResizable(false);  // 设置窗口大小不可变
		f.setVisible(true);  // 设置显示窗口
	}
}
