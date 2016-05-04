package com.anxiangban.activitysetcenter;

import java.io.File;
import com.anxiangban.R;
import com.anxiangban.activityresetmima.ResetMiMaActivity;
import com.anxiangban.activitysetalarm.SetAlarmActivity;
import com.anxiangban.activitysethomeyuyin.SetHomeYuYinActivity;
import com.anxiangban.activityshezhianquanqu.SheZhiAnQuanQuActivity;
import com.anxiangban.activityshezhiweixianqu.SheZhiWeiXianQuActivity;
import com.anxiangban.myView.SlipButton;
import com.anxiangban.myView.SlipButton.OnSwitchListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SetCenterActivity extends Activity {
	private RelativeLayout setSafe;
	private RelativeLayout setDanger;
	private RelativeLayout clearSaid;
	private RelativeLayout xiuGaiMiMa;
	private RelativeLayout setdingshi;
	private RelativeLayout setHome;
	private SlipButton moShiButton;
	public SharedPreferences preferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setcenter);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		initView();
	}

	private void initView() {
		setSafe = (RelativeLayout) findViewById(R.id.shezhianquanqu);
		setHome = (RelativeLayout) findViewById(R.id.sethomryuyin);
		setDanger = (RelativeLayout) findViewById(R.id.shezhiweixianqu);
		clearSaid = (RelativeLayout) findViewById(R.id.clearsaid);
		xiuGaiMiMa = (RelativeLayout) findViewById(R.id.xiugaimima);
		moShiButton = (SlipButton) findViewById(R.id.moshibutton);
		setdingshi = (RelativeLayout)findViewById(R.id.setdingshi);
		if(!preferences.getString("TuBiaoType", "").equals("TiWen")){
			moShiButton.setSwitchState(true);
		}else{
			moShiButton.setSwitchState(false);
		}
		
		setSafe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SetCenterActivity.this,
						SheZhiAnQuanQuActivity.class);
				startActivity(intent);
			}
		});
		setHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SetCenterActivity.this,
						SetHomeYuYinActivity.class);
				startActivity(intent);
			}
		});
		setdingshi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SetCenterActivity.this,
						SetAlarmActivity.class);
				startActivity(intent);
			}
		});
		setDanger.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SetCenterActivity.this,
						SheZhiWeiXianQuActivity.class);
				startActivity(intent);
			}
		});
		clearSaid.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(SetCenterActivity.this)
						.setTitle("清空缓存")
						.setMessage("是否删除所有的语音信息？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										delAllFile(Environment
												.getExternalStorageDirectory()
												+ "/AXB/voice/");
										Toast.makeText(SetCenterActivity.this,
												"缓存已清空", Toast.LENGTH_LONG)
												.show();
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

									}
								}).show();
			}
		});
		xiuGaiMiMa.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SetCenterActivity.this,
						ResetMiMaActivity.class);
				startActivity(intent);
			}
		});
		moShiButton.setOnSwitchListener(new OnSwitchListener() {

			public void onSwitched(boolean CheckState) {
				Editor editor = preferences.edit();
				if(CheckState){
					editor.putString("TuBiaoType", "XinLv");
					System.out.println("变为老人");
				}else{
					editor.putString("TuBiaoType", "TiWen");
					System.out.println("变为小孩");
				}
				editor.commit();
			}
		});
	}

	private void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			System.out.println("删除文件夹操作出错");
			e.printStackTrace();
		}
	}

	private void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	protected void onRestart() {
		super.onRestart();
	}

	protected void onResume() {
		super.onResume();
	}

	protected void onPause() {
		super.onPause();
	}

	protected void onStop() {
		super.onStop();
	}

	protected void onDestroy() {
		super.onDestroy();
	}
	
	
}
