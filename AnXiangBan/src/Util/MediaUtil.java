package Util;

import java.io.File;
import java.io.IOException;
import com.anxiangban.R;
import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.SystemClock;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;

/**
 * ¼��������
 */
public class MediaUtil {
	private MediaRecorder mMediaRecorder = null;
	private Dialog dialog;
	private Chronometer timer;
	private File path = null;
	private String voice = Environment.getExternalStorageDirectory() + "/AXB/voice/";// �ļ�
	private String alarm = Environment.getExternalStorageDirectory() + "/AXB/alarm/alarm.amr";// �ļ�
	private Context context;

	public MediaUtil(Context context){
		this.context = context;
	}
	// ��Ƶ��ȡԴ
	public void start(String fileName) {
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // check
		if (sdCardExist) {
			try {
				path = new File(voice+fileName+".amr");
				System.out.println(path.getAbsolutePath());
				mMediaRecorder = new MediaRecorder();
				mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// ����������Դ����˷�
				mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);// ���ø�ʽ
				mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);// ���ñ���
				mMediaRecorder.setOutputFile(path.getAbsolutePath());// ��������ļ�·��
				mMediaRecorder.prepare();
				mMediaRecorder.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void start() {
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // check
		if (sdCardExist) {
			try {
				path = new File(getAlarm());
				System.out.println(path.getAbsolutePath());
				mMediaRecorder = new MediaRecorder();
				mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// ����������Դ����˷�
				mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);// ���ø�ʽ
				mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);// ���ñ���
				mMediaRecorder.setOutputFile(path.getAbsolutePath());// ��������ļ�·��
				mMediaRecorder.prepare();
				mMediaRecorder.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void stop() {
		if(mMediaRecorder!=null){
			mMediaRecorder.stop();
			mMediaRecorder.release();
			mMediaRecorder = null;
		}
	}

	// ¼��ʱ��ʾDialog
	public void showVoiceDialog() {
		dialog = new Dialog(context, R.style.DialogStyle);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.record_dialog);
		timer = (Chronometer) dialog.findViewById(R.id.chronometer);
		dialog.show();
		 startTimer();
	}
	public void hideVoiceDialog() {
		stopTimer();
		dialog.dismiss();
	}
	public void startTimer() {
		timer.setBase(SystemClock.elapsedRealtime());
		timer.start();
	}

	public void stopTimer() {
		if (timer != null) {
			timer.stop();
			timer = null;
		}
	}
	public void playMedia(String name) {
		MediaPlayer mediaPlayer = null;
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(voice +name);
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (Exception e) {

		}
	}
	public void playMedia() {
		MediaPlayer mediaPlayer = null;
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(alarm);
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (Exception e) {

		}
	}
	public void openSound(String name, AssetManager am) {
		MediaPlayer mediaPlayer = null;
		AssetFileDescriptor afd =null;
		try {
			afd = am.openFd(name);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(afd.getFileDescriptor(),
					afd.getStartOffset(), afd.getLength());
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (Exception e) {

		}
	}
	public String getAlarm() {
		return alarm;
	}
}