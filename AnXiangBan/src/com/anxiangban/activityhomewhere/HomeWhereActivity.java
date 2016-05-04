package com.anxiangban.activityhomewhere;

import com.anxiangban.R;
import com.anxiangban.activityguijiditu.OverlayTest;
import com.anxiangban.activitytabhost.TabHostActivity;
import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.MKMapTouchListener;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.Symbol;
import com.baidu.mapapi.map.Symbol.Stroke;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class HomeWhereActivity extends Activity {
	// ��ͼ���
	MapView mMapView = null;
	GraphicsOverlay palaceOverlay;
	OverlayTest itemOverlay;
	private Button reset;
	private Button save;
	private GeoPoint home = new GeoPoint(0,0);
	public SharedPreferences preferences;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homewhere);
		CharSequence titleLable = "�Զ�����ƹ���";
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		setTitle(titleLable);
		initUI();
		initMap();
		if (preferences.getInt("HomeLat", -1) != -1) {
			home.setLatitudeE6(preferences.getInt("HomeLat", -1));
			home.setLongitudeE6(preferences.getInt("HomeLon", -1));
			mMapView.getController().setCenter(home);
			yuan(mMapView,home,100);
		}
		adddianji();
	}
	private void initUI() {
		reset = (Button) findViewById(R.id.reset);
		save = (Button) findViewById(R.id.save);
		reset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				resetClick();
			}
		});
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saveClick();
			}
		});
	}
	public void resetClick() {
		// ��ӻ���Ԫ��
		mMapView.getOverlays().remove(itemOverlay);
		mMapView.getOverlays().remove(palaceOverlay);
		mMapView.refresh();
	}
	public void saveClick() {
		// ��ӻ���Ԫ��
		if (preferences.getInt("HomeLat", -1) == -1) {
			Editor editor = preferences.edit();
			editor.putInt("HomeLat", home.getLatitudeE6());
			editor.putInt("HomeLon", home.getLongitudeE6());
			editor.commit();
			Intent intent = new Intent (HomeWhereActivity.this,TabHostActivity.class);
			startActivity(intent);	
			HomeWhereActivity.this.finish();
		}else{
			Editor editor = preferences.edit();
			editor.putInt("HomeLat", home.getLatitudeE6());
			editor.putInt("HomeLon", home.getLongitudeE6());
			editor.commit();
			mMapView.getOverlays().clear();
			HomeWhereActivity.this.finish();
		}

	}
	/**
	 * ��ʼ����ͼ
	 */
	private void initMap() {
		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapView.getController().setZoom(16f);
		mMapView.setSatellite(true);
		mMapView.setBuiltInZoomControls(true);
		mMapView.getController().enableClick(true);
	}
	private void adddianji() {
		MKMapTouchListener mapTouchListener = new MKMapTouchListener() {
			@Override
			public void onMapClick(GeoPoint point) {
				// �ڴ˴����ͼ�����¼�
			}

			@Override
			public void onMapDoubleClick(GeoPoint point) {
				// �ڴ˴����ͼ˫���¼�
			}

			@Override
			public void onMapLongClick(GeoPoint point) {
				// �ڴ˴����ͼ�����¼�
				System.out.println(point.getLatitudeE6());
				home = point;
				// ����һ�����ǹʹ���Χ�ľ���
				if(itemOverlay!=null)
				{
					mMapView.getOverlays().remove(itemOverlay);
					mMapView.refresh();
				}
				if(palaceOverlay!=null)
				{
					mMapView.getOverlays().remove(palaceOverlay);
					mMapView.refresh();
				}
				
				GeoPoint p1 = new GeoPoint((int) (point.getLatitudeE6()),
						(int) (point.getLongitudeE6()));
				Drawable mark = getResources().getDrawable(
						R.drawable.icon_gcoding);
				OverlayItem item1 = new OverlayItem(p1, "item1", "item1");
				itemOverlay = new OverlayTest(mark, mMapView);
				mMapView.getOverlays().add(itemOverlay);
				itemOverlay.addItem(item1);
				mMapView.refresh();
				yuan(mMapView,p1,100);
			}

		};
		mMapView.regMapTouchListner(mapTouchListener);
		mMapView.refresh();

	}

	public void yuan(MapView mMapView,GeoPoint p,int r)
	{
	      //����һ�����ǹʹ���Χ�ľ���  
		  Geometry palaceGeometry = new Geometry();  
	      palaceGeometry.setCircle(p,r);
	      Symbol lineSymbol = new Symbol();//������ʽ  
	      Symbol.Color palaceColor = lineSymbol.new Color();//������ɫ  
	      palaceColor.red = 0;//������ɫ�ĺ�ɫ����  
	      palaceColor.green = 255;//������ɫ����ɫ����  
	      palaceColor.blue = 0;//������ɫ����ɫ����  
	      palaceColor.alpha = 126;//������ɫ��alphaֵ  
	      // ����һ���߿����  
	      // ���� - 5�� �߿���߿�  
	      // ���� - polygonSymbol.new Color(0xffff0000)���߿����ɫ 
	     
	      Stroke stroke = new Stroke(0,lineSymbol.new Color(0xffffffff)); 
	      lineSymbol.setSurface(palaceColor,1,3,stroke);//������ʽ��������ɫ��palaceColor�Ƿ������Σ����� 
	      Graphic palaceGraphic = new Graphic(palaceGeometry, lineSymbol); 
	      palaceOverlay = new GraphicsOverlay(mMapView);  
	      /** 
	      * ��GraphicsOverlay���Graphic 
	      *һ��GraphicsOVerlay����Ӷ��Graphic 
	      *GraphicsOVerlay��remove�ӿ�ʹ��setData()���ص�ID���Ƴ�ָ��Graphic 
	      */  
	      long palaceId = palaceOverlay.setData(palaceGraphic);  
	      //��overlay��ӵ�mapview��  
	      mMapView.getOverlays().add(palaceOverlay);  
	      //ˢ�µ�ͼʹ����ӵ�overlay��Ч  
	      mMapView.refresh(); 
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mMapView.destroy();
		super.onDestroy();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			mMapView.getOverlays().clear();
			HomeWhereActivity.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
