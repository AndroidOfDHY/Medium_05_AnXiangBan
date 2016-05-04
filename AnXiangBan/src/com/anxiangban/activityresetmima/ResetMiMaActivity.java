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
		title.setText("�޸���������");
		tixin = (TextView) findViewById(R.id.two);
		tixin.setText("����ԭ����");
		lockPatternUtils = new LockPatternUtils(this);//����
		lockPatternView.setOnPatternListener(new OnPatternListener() {//��ͼ
			private String pString;
			public void onPatternStart() {}
			public void onPatternDetected(List<Cell> pattern) {
				if (opFLag) {	 //ȷ������
					int result = lockPatternUtils.checkPattern(pattern);
					if (result == 1) {	//������ȷ
						opFLag = false;
						tixin.setText("����������");
						lockPatternView.clearPattern();
					}else{ 	//�������
						Toast.makeText(ResetMiMaActivity.this, "�����������������", Toast.LENGTH_LONG).show();
						lockPatternView.clearPattern();
					}
				}else{		//��������
					if(Flag == 0 ){
						pString = lockPatternUtils.getPatternToString(pattern);
						Flag++;
						tixin.setText("������һ�飬ȷ������");
						Toast.makeText(ResetMiMaActivity.this, "������һ�飬ȷ������", Toast.LENGTH_LONG).show();
						lockPatternView.clearPattern();
					}else if(Flag == 1){
						if (pString.equals(lockPatternUtils.getPatternToString(pattern))) {	//������ȷ
							lockPatternUtils.saveLockPattern(pattern);
							Toast.makeText(ResetMiMaActivity.this, "�������óɹ�", Toast.LENGTH_LONG).show();
							lockPatternView.clearPattern();
				            ResetMiMaActivity.this.finish();
						}else{ 	//�������
							Toast.makeText(ResetMiMaActivity.this, "�����������һ������Ĳ�ͬ������������", Toast.LENGTH_LONG).show();
							lockPatternView.clearPattern();
							tixin.setText("����������");
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
