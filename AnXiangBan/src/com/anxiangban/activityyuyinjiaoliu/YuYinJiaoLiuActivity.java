package com.anxiangban.activityyuyinjiaoliu;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.anxiangban.R;
import com.anxiangban.myApplication.MyApplication;

import Message.DefaultMessage;
import Message.MessageType;
import Util.FileUtil;
import Util.MediaUtil;
import Util.jsonUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ListView;

public class YuYinJiaoLiuActivity extends Activity implements OnTouchListener {
	private Button btn_start;
	private YuYinJiaoLiuActivity mContext;
	private String newName;
	private ListView mListView;
	private YuYinViewAdapter mAdapter;
	private List<YuYinEntity> mDataArrays = new ArrayList<YuYinEntity>();
	private MediaUtil mediaUtil;
	private MyApplication app;
	private String temp = Environment.getExternalStorageDirectory() + "/AXB/voice";// 文件
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news);
		app = (MyApplication)getApplication();
		mediaUtil = new MediaUtil(YuYinJiaoLiuActivity.this);
		initView();
		mListView = (ListView) findViewById(R.id.listview);
		btn_start.setOnTouchListener(mContext);
		initData();
	}
	private void initView() {
		mContext = YuYinJiaoLiuActivity.this;
		btn_start = (Button) findViewById(R.id.btn_start);
	}

	protected void onRestart() {
		super.onRestart();
		System.out.println("onRestart");
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int id = event.getAction();
		switch (id) {
		case MotionEvent.ACTION_DOWN:
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd-HH-mm-ss");
			newName = format.format(new Date());
			mediaUtil.start(newName);
			mediaUtil.showVoiceDialog();
			break;
		case MotionEvent.ACTION_UP:
			mediaUtil.hideVoiceDialog();
			mediaUtil.stop();
			System.out.println("sendName" + newName);
			DefaultMessage message = new DefaultMessage();
			message.setFlag(MessageType.MESSAGE_SAID);
			message.setContext(jsonUtil.createJsonString(FileUtil.getUploadFile(newName+".amr")));
			if(app.thread != null){
				app.thread.sendMessage(message);
			}
			send();
			break;
		}
		return false;
	}

	private void send() {
		YuYinEntity entity = new YuYinEntity();
		entity.setDate(getDate());
		entity.setName("家长");
		entity.setText(newName+".amr");
		entity.setTime("11``");
		mDataArrays.add(entity);
		mAdapter.notifyDataSetChanged();
		mListView.setSelection(mListView.getCount() - 1);
	}

	private String getDate() { // 取时间
		Calendar c = Calendar.getInstance();
		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH));
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
		String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		String mins = String.valueOf(c.get(Calendar.MINUTE));
		StringBuffer sbBuffer = new StringBuffer();
		sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":"
				+ mins);
		return sbBuffer.toString();
	}
	
	private String nameToDate(String name){
		String temp = name.substring(0,name.lastIndexOf("."));
		String[] tempsplit = temp.split("-");
		String year = tempsplit[0];
		String month = tempsplit[1];
		String day = tempsplit[2];
		String hour = tempsplit[3];
		String mins = tempsplit[4];
		StringBuffer sbBuffer = new StringBuffer();
		sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":"
				+ mins);
		return sbBuffer.toString();
	}
	public void initData() {
		File file = new File(temp);
		File[] f = file.listFiles(new MusicFilter());
		int length = f.length;
		for (int i = 0; i < length; i++) {
			File ftemp = f[i];
			YuYinEntity entity = new YuYinEntity();
			entity.setDate(nameToDate(ftemp.getName()));
			entity.setName("家长");
			entity.setText(ftemp.getName());
			mDataArrays.add(entity);
		}
		mAdapter = new YuYinViewAdapter(this, mDataArrays);
		mListView.setAdapter(mAdapter);

	}
	class MusicFilter implements FilenameFilter {

		public boolean accept(File dir, String filename) {
			// TODO Auto-generated method stub
			return (filename.endsWith(".amr"));
		}

	}
}
