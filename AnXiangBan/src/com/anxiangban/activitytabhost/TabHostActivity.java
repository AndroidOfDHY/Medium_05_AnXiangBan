package com.anxiangban.activitytabhost;

import com.anxiangban.R;
import com.anxiangban.activitybaobeijiankang.BaoBeiJianKangActivity;
import com.anxiangban.activitysetcenter.SetCenterActivity;
import com.anxiangban.activitytimeline.TimeLineActivity;
import com.anxiangban.activityyuyinjiaoliu.YuYinJiaoLiuActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

@SuppressWarnings("deprecation")
public class TabHostActivity extends TabActivity implements OnCheckedChangeListener {
	FrameLayout fmpan;
	LayoutInflater inflater;
	private RadioGroup mainTab;
	private TabHost mTabHost;
	
	private static final int ITEM_1 = Menu.FIRST;
	private static final int ITEM_2 = Menu.FIRST + 1;
	private static final int ITEM_3 = Menu.FIRST + 2;
	
	private Intent GuiJiDiTuIntent;
	private Intent YuYinJiaoLiuIntent;
	private Intent SetCenterIntent;
	private Intent BaoBeiJianKangIntent;
	
	private final static String TAB_TAG_GUIJIDITU="tab_tag_guijiditu";
	private final static String TAB_TAG_YUYINJIAOLIU="tab_tag_yuyinjiaoliu";
	private final static String TAB_TAG_BAOBEIJIANKANG="tab_tag_baobeijiankang";
	private final static String TAB_TAG_SETCENTER="tab_tag_setcenter";
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tabhost);
        mainTab=(RadioGroup)findViewById(R.id.main_tab);
        mainTab.setOnCheckedChangeListener(this);
        prepareIntent();
        setupIntent();
        initView();
    }

	private void initView() {
		inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		fmpan=(FrameLayout)findViewById(R.id.tab1);
	}

	private void setupIntent() {
		this.mTabHost=getTabHost();
		TabHost localTabHost=this.mTabHost;
		localTabHost.addTab(buildTabSpec(TAB_TAG_GUIJIDITU, R.string.guijiditu, R.drawable.icon_1_n, GuiJiDiTuIntent));
		localTabHost.addTab(buildTabSpec(TAB_TAG_YUYINJIAOLIU, R.string.yuyinjiaoliu, R.drawable.icon_2_n, YuYinJiaoLiuIntent));
		localTabHost.addTab(buildTabSpec(TAB_TAG_BAOBEIJIANKANG, R.string.baobeijiankan, R.drawable.icon_3_n, BaoBeiJianKangIntent));
		localTabHost.addTab(buildTabSpec(TAB_TAG_SETCENTER, R.string.set, R.drawable.icon_4_n, SetCenterIntent));
		
	}
	private TabHost.TabSpec buildTabSpec(String tag, int resLabel, int resIcon,final Intent content) {
		return this.mTabHost.newTabSpec(tag).setIndicator(getString(resLabel),
				getResources().getDrawable(resIcon)).setContent(content);
	}
	private void prepareIntent() {
//		GuiJiDiTuIntent = new Intent(this, GuiJiDiTuActivity.class);
		GuiJiDiTuIntent = new Intent(this, TimeLineActivity.class);
		YuYinJiaoLiuIntent = new Intent(this, YuYinJiaoLiuActivity.class);
//		YuYinJiaoLiuIntent = new Intent(this, TimeLineActivity.class);
		BaoBeiJianKangIntent = new Intent(this, BaoBeiJianKangActivity.class);
//		GuiJiDiTuIntent = new Intent(this, SetCenterActivity.class);
		SetCenterIntent = new Intent(this, SetCenterActivity.class);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, ITEM_1, 0, "设置");
		menu.add(0, ITEM_2, 0, "切换账户");
		menu.add(0, ITEM_3, 0, "退出");
		return true;
	}

	@SuppressLint("ShowToast")
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ITEM_1:
			setTitle("单击了菜单子项1");
			break;
		case ITEM_2:

			break;
		case ITEM_3:
			@SuppressWarnings("unused")
			Dialog dialog = new AlertDialog.Builder(TabHostActivity.this)
					.setTitle("退出程序")
					.setMessage("退出后，你将收不到新的消息。确定要退出?")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Toast.makeText(TabHostActivity.this,
											"退出程序", Toast.LENGTH_LONG).show();
									TabHostActivity.this.finish();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Toast.makeText(TabHostActivity.this,
											"程序不退出", Toast.LENGTH_LONG);
								}
							}).show();
			break;
		}
		return true;

	}

	public void paiz() {
		Toast.makeText(TabHostActivity.this, "程序不退出", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch(checkedId){
		case R.id.radio_button0:
			this.mTabHost.setCurrentTabByTag(TAB_TAG_GUIJIDITU);
			break;
		case R.id.radio_button1:
			this.mTabHost.setCurrentTabByTag(TAB_TAG_YUYINJIAOLIU);
			break;
		case R.id.radio_button2:
			this.mTabHost.setCurrentTabByTag(TAB_TAG_BAOBEIJIANKANG);
			break;
		case R.id.radio_button3:
			this.mTabHost.setCurrentTabByTag(TAB_TAG_SETCENTER);
			break;
		}
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
		System.out.println("222__________________________222");
	}
}
