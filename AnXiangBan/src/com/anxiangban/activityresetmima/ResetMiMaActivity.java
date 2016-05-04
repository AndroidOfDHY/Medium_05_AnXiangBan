package com.anxiangban.activityresetmima;

import java.util.List;

import com.anxiangban.R;
import com.anxiangban.myView.LockPatternUtils;
import com.anxiangban.myView.LockPatternView;
import com.anxiangban.myView.LockPatternView.Cell;
import com.anxiangban.myView.LockPatternView.OnPatternListener;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;

public class ResetMiMaActivity extends Activity{
	private LockPatternView lockPatternView;
	private LockPatternUtils lockPatternUtils;
	private TextView tixin;
	private TextView title;
	public boolean opFLag = true;
	private int Flag;
	@SuppressLint("HandlerLeak")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mima);
		Flag = 0;
		lockPatternView = (LockPatternView) findViewById(R.id.lpv_lock);
		title = (TextView) findViewById(R.id.one);
		title.setText("修改手势密码");
		tixin = (TextView) findViewById(R.id.two);
		tixin.setText("输入原密码");
		lockPatternUtils = new LockPatternUtils(this);//工具
		lockPatternView.setOnPatternListener(new OnPatternListener() {//视图
			private String pString;
			public void onPatternStart() {}
			public void onPatternDetected(List<Cell> pattern) {
				if (opFLag) {	 //确认密码
					int result = lockPatternUtils.checkPattern(pattern);
					if (result == 1) {	//密码正确
						opFLag = false;
						tixin.setText("设置新密码");
						lockPatternView.clearPattern();
					}else{ 	//密码错误
						Toast.makeText(ResetMiMaActivity.this, "密码错误，请重新输入", Toast.LENGTH_LONG).show();
						lockPatternView.clearPattern();
					}
				}else{		//设置密码
					if(Flag == 0 ){
						pString = lockPatternUtils.getPatternToString(pattern);
						Flag++;
						tixin.setText("请在输一遍，确认密码");
						Toast.makeText(ResetMiMaActivity.this, "请在输一遍，确认密码", Toast.LENGTH_LONG).show();
						lockPatternView.clearPattern();
					}else if(Flag == 1){
						if (pString.equals(lockPatternUtils.getPatternToString(pattern))) {	//密码正确
							lockPatternUtils.saveLockPattern(pattern);
							Toast.makeText(ResetMiMaActivity.this, "密码设置成功", Toast.LENGTH_LONG).show();
							lockPatternView.clearPattern();
				            ResetMiMaActivity.this.finish();
						}else{ 	//密码错误
							Toast.makeText(ResetMiMaActivity.this, "输入密码与第一次输入的不同，请重新设置", Toast.LENGTH_LONG).show();
							lockPatternView.clearPattern();
							tixin.setText("请设置密码");
							Flag--;
						}
					}
				}
			}
			public void onPatternCleared() {}
			public void onPatternCellAdded(List<Cell> pattern) {}
		});
	}
	protected void onRestart() {
		super.onRestart();
		System.out.println("onRestart");
	}
}
