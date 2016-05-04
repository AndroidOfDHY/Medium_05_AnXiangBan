package com.anxiangban.activityqrcode;

import gson.gsonTool;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;
import Message.DefaultMessage;
import Message.MessageType;
import Util.NetConnect;
import Util.jsonUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.anxiangban.activitysetmima.SetMiMaActivity;
import com.anxiangban.myApplication.MyApplication;
import com.anxiangban.myView.ViewfinderView;
import com.anxiangban.thread.ClienIn;
import com.anxiangban.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import domain.Sign;


public class QRcodeActivity extends Activity implements Callback {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private boolean vibrate;
	private Builder update;
	public SharedPreferences preferences;
	private ProgressBar pg;
	private MyApplication app;
	private String ID;
	private String yanZhen;
	private EditText editText;
	private View layout;
	private TextView textView;
	private Handler socketHandler;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qrcode);
		editText = new EditText(this);
		app =(MyApplication) getApplication();
		socketHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// 如果发送的消息来自子线程
				if (msg.what == 0x123) {
					DefaultMessage message = gsonTool.getT(msg.obj.toString(),
							DefaultMessage.class);
					System.out.println(msg.obj.toString());
					play(message);
				}
			}

		};
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = preferences.edit();
		editor.putString("isFirst", "No");
		editor.commit();
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		initDialog();
	}
	private void initDialog(){
		LayoutInflater inflater = getLayoutInflater();
		layout = inflater.inflate(R.layout.update,(ViewGroup) findViewById(R.id.dialog));
		update = new AlertDialog.Builder(QRcodeActivity.this)
				.setTitle("验证：").setView(layout);
		textView = (TextView) layout.findViewById(R.id.head_tipsTextView);
	}
	private void success(){
		Editor editor = preferences.edit();
		editor.putString("sign", "Yes");
		editor.commit();
		Intent intent = new Intent(QRcodeActivity.this,SetMiMaActivity.class);
		startActivity(intent);
		QRcodeActivity.this.finish();
	}
	private void faile(){
		try {
			app.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Intent intent = new Intent(QRcodeActivity.this,QRcodeActivity.class);
		startActivity(intent);
		QRcodeActivity.this.finish();
	}
	/**
	 * Handler scan result
	 * 
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		// FIXME
		if (resultString.equals("")) {
			Toast.makeText(QRcodeActivity.this, "获取失败!", Toast.LENGTH_SHORT)
					.show();
		} else {
			if (pg != null && pg.isShown()) {
				pg.setVisibility(View.GONE);
			}
			ID = resultString;
			if(NetConnect.getNetworkConnectionType(app) == -1){
				Toast.makeText(QRcodeActivity.this, "无网络连接!", Toast.LENGTH_SHORT).show();
				QRcodeActivity.this.finish();
			}
			new AlertDialog.Builder(this)
					.setTitle("请输入包装上的校验码")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(editText)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@SuppressLint("NewApi")
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									yanZhen = editText.getText().toString();
									update.show();
									try {
										StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()       
								        .detectDiskReads()       
								        .detectDiskWrites()       
								        .detectNetwork()   // or .detectAll() for all detectable problems       
								        .penaltyLog()       
								        .build());       
								StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()       
								        .detectLeakedSqlLiteObjects()    
								        .penaltyLog()       
								        .penaltyDeath()       
								        .build());
//										Socket socket = new Socket("192.168.0.105", 11111);
//										Socket socket = new Socket("108.166.215.91", 11111);
										Socket socket = new Socket("125.65.110.148", 11111);
										app.socket = socket;
										initClien();
										PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "GBK")),true);
										Sign sign = new Sign();
										sign.setBraceletId(ID);
										sign.setIMSI(app.getIMSI());
										sign.setYanZhen(yanZhen);
										sign.setIsBracelet(false);
										DefaultMessage message = new DefaultMessage(MessageType.MESSAGE_SIGN_SERVER,jsonUtil.createJsonString(sign));
										out.println(jsonUtil.createJsonString(message));
									} catch (UnknownHostException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									QRcodeActivity.this.finish();
								}
							}).show();
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		vibrate = true;

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}
	private void initClien(){
		if (app.thread != null) {
			app.thread.handler = socketHandler;
		} else {
			try {
				ClienIn thread = new ClienIn(app.socket,socketHandler,app);
				thread.start();
				app.thread = thread;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private void play(DefaultMessage message){
		if(message.getFlag().equals(MessageType.MESSAGE_RETURN_SIGN_SUCC)){
			update.setTitle("数据同步验证：");
			textView.setText("正在同步数据");
			DefaultMessage message2 = new DefaultMessage(MessageType.MESSAGE_GET_ALL, "");
			app.thread.sendMessage(message2);
		}else if (message.getFlag().equals(MessageType.MESSAGE_RETURN_SIGN_BAD)) {
			Toast.makeText(QRcodeActivity.this, "验证码错误或二维码无效", Toast.LENGTH_LONG).show();
			faile();
		}else if (message.getFlag().equals(MessageType.MESSAGE_RETURN_OK)) {
			success();
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	     if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	         QRcodeActivity.this.finish();   
	         return true;   
	     }
	     return super.onKeyDown(keyCode, event);
	 }
}