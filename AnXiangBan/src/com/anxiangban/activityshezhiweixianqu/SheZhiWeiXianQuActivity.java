package com.anxiangban.activityshezhiweixianqu;

import gson.gsonTool;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import Message.DefaultMessage;
import Message.MessageType;
import Util.jsonUtil;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.anxiangban.R;
import com.anxiangban.DBHelper.InsertHelp;
import com.anxiangban.DBHelper.MytabCursor;
import com.anxiangban.DBHelper.MytabOperate;
import com.anxiangban.DBHelper.domain.SafeDangerData;
import com.anxiangban.activitysetalarm.SetAlarmActivity;
import com.anxiangban.myApplication.MyApplication;
import com.anxiangban.thread.ClienIn;
import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MKMapTouchListener;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.Symbol;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * 演示MapView的基本用法
 */
public class SheZhiWeiXianQuActivity extends Activity {
	// 地图相关
	MapView mMapView = null;
	private GeoPoint[] Points = new GeoPoint[10];
	private Graphic formalGraphic = null;
	private int pointsum = 0;
	private MyOverlay mOverlay = null;
	private List<OverlayItem> mItems = new ArrayList<OverlayItem>();
	private Button reset;
	private Button save;
	private SafeDangerData safeData = new SafeDangerData();
	private MyApplication app;
	private SharedPreferences preferences;
	private GeoPoint home = new GeoPoint(0,0);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shezhiweixianqu);
		app = (MyApplication)getApplication();
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
		CharSequence titleLable = "自定义绘制功能";
		setTitle(titleLable);
		safeData.setIsSafe(0);
		initUI();
		initMap();
		initFormalOverlayGraphic();
		addFormalOverlay();
		mMapView.refresh();
		mMapView.regMapTouchListner(mapTouchListener);
		initClien();
	}

	/**
	 * 初始化UI
	 */
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

	/**
	 * 初始化地图
	 */
	private void initMap() {
		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapView.getController().setZoom(12.5f);
		mMapView.getController().enableClick(true);
		mMapView.setBuiltInZoomControls(true);
		mMapView.setSatellite(true);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		if (preferences.getInt("HomeLat", -1) != -1) {
			home.setLatitudeE6(preferences.getInt("HomeLat", -1));
			home.setLongitudeE6(preferences.getInt("HomeLon", -1));
			mMapView.getController().setCenter(home);
		}
	}

	/**
	 * 获取多边形覆盖物的坐标信息
	 * 
	 * @return 多边形覆盖物的坐标信息
	 */
	private GeoPoint[] getFormalOverlayData() {
		List<SafeDangerData> list = null;
		int biaozhi = 0;
		GeoPoint[] point = new GeoPoint[10];
		MytabCursor mtabReader = new MytabCursor(app.dbHelper.getReadableDatabase());

		list = mtabReader.SafeDangerFind();
		mtabReader.Stop();

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getIsSafe() == 1) {
				if (list.get(i).getLongitude1() != 0) {
					point[0] = new GeoPoint((int) (list.get(i).getLatitude1() * 1E6),
							(int) (list.get(i).getLongitude1() * 1E6));
					biaozhi = 1;
				}
				if (list.get(i).getLongitude2() != 0) {
					point[1] = new GeoPoint((int) (list.get(i).getLatitude2() * 1E6),
							(int) (list.get(i).getLongitude2() * 1E6));
					biaozhi = 2;
				}
				if (list.get(i).getLongitude3() != 0) {
					point[2] = new GeoPoint((int) (list.get(i).getLatitude3() * 1E6),
							(int) (list.get(i).getLongitude3() * 1E6));
					biaozhi = 3;
				}
				if (list.get(i).getLongitude4() != 0) {
					point[3] = new GeoPoint((int) (list.get(i).getLatitude4() * 1E6),
							(int) (list.get(i).getLongitude4() * 1E6));
					biaozhi = 4;
				}
				if (list.get(i).getLongitude5() != 0) {
					point[4] = new GeoPoint((int) (list.get(i).getLatitude5() * 1E6),
							(int) (list.get(i).getLongitude5() * 1E6));
					biaozhi = 5;
				}
				if (list.get(i).getLongitude6() != 0) {
					point[5] = new GeoPoint((int) (list.get(i).getLatitude6() * 1E6),
							(int) (list.get(i).getLongitude6() * 1E6));
					biaozhi = 6;
				}
				if (list.get(i).getLongitude7() != 0) {
					point[6] = new GeoPoint((int) (list.get(i).getLatitude7() * 1E6),
							(int) (list.get(i).getLongitude7() * 1E6));
					biaozhi = 7;
				}
				if (list.get(i).getLongitude8() != 0) {
					point[7] = new GeoPoint((int) (list.get(i).getLatitude8() * 1E6),
							(int) (list.get(i).getLongitude8() * 1E6));
					biaozhi = 8;
				}
				if (list.get(i).getLongitude9() != 0) {
					point[8] = new GeoPoint((int) (list.get(i).getLatitude9() * 1E6),
							(int) (list.get(i).getLongitude9() * 1E6));
					biaozhi = 9;
				}
				if (list.get(i).getLongitude10() != 0) {
					point[9] = new GeoPoint((int) (list.get(i).getLatitude10() * 1E6),
							(int) (list.get(i).getLongitude10() * 1E6));
					biaozhi = 10;
				}
			}
		}
		GeoPoint[] FormalPoints = new GeoPoint[biaozhi];
		for (int j = 0; j < biaozhi; j++) {
			FormalPoints[j] = point[j];
		}
		if (biaozhi == 0) {
			Toast.makeText(SheZhiWeiXianQuActivity.this, "安全区未设定",
					Toast.LENGTH_SHORT).show();
		}
		return FormalPoints;
	}

	/**
	 * 初始化永久显示的多边形覆盖物
	 */
	private void initFormalOverlayGraphic() {
		GeoPoint[] FormalPoints = getFormalOverlayData();
		System.out.println(FormalPoints.toString());
		Geometry formalGeometry = new Geometry();
		formalGeometry.setPolygon(FormalPoints);
		// 设置多边形样式
		Symbol polygonSymbol = new Symbol();
		Symbol.Color polygonColor = polygonSymbol.new Color();
		polygonColor.red = 0;
		polygonColor.green = 255;
		polygonColor.blue = 0;
		polygonColor.alpha = 126;
		polygonSymbol.setSurface(polygonColor, 1, 5);
		// 生成Graphic对象
		formalGraphic = new Graphic(formalGeometry, polygonSymbol);
	}

	/**
	 * 添加临时的图形覆盖物
	 * 
	 * @return 临时的图形覆盖物对象
	 */
	private Graphic GetTempOverlayGraphic() {
		GeoPoint[] tempPoints = new GeoPoint[pointsum];
		for (int i = 0; i < pointsum; i++) {
			tempPoints[i] = Points[i];
		}
		if (pointsum - 1 > 2) {
			tempPoints = paixu(tempPoints, pointsum - 1);
		}
		for (int i = 0; i < pointsum; i++) {
			Points[i] = tempPoints[i];
		}
		Geometry tempGeometry = new Geometry();
		tempGeometry.setPolygon(tempPoints);
		// 设置多边形样式
		Symbol polygonSymbol = new Symbol();
		Symbol.Color polygonColor = polygonSymbol.new Color();
		polygonColor.red = 255;
		polygonColor.green = 0;
		polygonColor.blue = 0;
		polygonColor.alpha = 126;
		polygonSymbol.setSurface(polygonColor, 1, 5);
		// 生成Graphic对象
		Graphic tempGraphic = new Graphic(tempGeometry, polygonSymbol);
		return tempGraphic;
	}

	/**
	 * 排序坐标
	 */
	public GeoPoint[] paixu(GeoPoint p[], int i) {
		int num = 0;
		double min = 0;
		GeoPoint point;
		min = DistanceUtil.getDistance(p[i], p[0]);
		for (int z = 0; z < i + 1; z++) {
			System.out.println(p[z].toString());

		}
		for (int k = 0; k < i; k++) {
			if (min > DistanceUtil.getDistance(p[i], p[k])) {
				min = DistanceUtil.getDistance(p[i], p[k]);
				num = k;
			}
		}
		double qian, hou;
		if (num == 0) {
			qian = DistanceUtil.getDistance(p[i], p[i - 1]);
			hou = DistanceUtil.getDistance(p[i], p[num + 1]);
			if (qian > hou) {
				num = num + 1;
			} else {
				num = i;
			}
		} else if (num == (i - 1)) {
			qian = DistanceUtil.getDistance(p[i], p[num - 1]);
			hou = DistanceUtil.getDistance(p[i], p[0]);
			if (qian > hou) {
				num = i;
			} else {
				num = i - 1;
			}
		} else {
			qian = DistanceUtil.getDistance(p[i], p[num - 1]);
			hou = DistanceUtil.getDistance(p[i], p[num + 1]);
			if (qian > hou) {
				num = num + 1;
			}
		}
		point = p[i];
		for (int l = i; l > num; l--) {
			p[l] = p[l - 1];
		}
		System.out.println(num);
		System.out.println(i);
		p[num] = point;
		for (int z = 0; z < i + 1; z++) {
			System.out.println(p[z].toString());

		}
		return p;
	}

	/**
	 * 添加Formal覆盖物到地图上
	 */
	public void addFormalOverlay() {
		GraphicsOverlay graphicsOverlay = new GraphicsOverlay(mMapView);
		mMapView.getOverlays().add(graphicsOverlay);
		graphicsOverlay.setData(formalGraphic);
	}

	/**
	 * 添加temp覆盖物到地图上
	 */
	public void addTempOverlay() {
		GraphicsOverlay graphicsOverlay = new GraphicsOverlay(mMapView);
		mMapView.getOverlays().add(graphicsOverlay);
		graphicsOverlay.setData(GetTempOverlayGraphic());
	}

	public void resetClick() {
		// 添加绘制元素
		mMapView.getOverlays().clear();
		pointsum = 0;
		mItems.clear();
		addFormalOverlay();
		mMapView.refresh();
	}

	public void saveClick() {
		// 清除所有图层
		switch (pointsum) {
		case 10:
			safeData.setLatitude10(((double) Points[0].getLatitudeE6()) / 1E6);
			safeData.setLongitude10(((double) Points[0].getLongitudeE6()) / 1E6);
		case 9:
			safeData.setLatitude9(((double) Points[8].getLatitudeE6()) / 1E6);
			safeData.setLongitude9(((double) Points[8].getLongitudeE6()) / 1E6);
		case 8:
			safeData.setLatitude8(((double) Points[7].getLatitudeE6()) / 1E6);
			safeData.setLongitude8(((double) Points[7].getLongitudeE6()) / 1E6);
		case 7:
			safeData.setLatitude7(((double) Points[6].getLatitudeE6()) / 1E6);
			safeData.setLongitude7(((double) Points[6].getLongitudeE6()) / 1E6);
		case 6:
			safeData.setLatitude6(((double) Points[5].getLatitudeE6()) / 1E6);
			safeData.setLongitude6(((double) Points[5].getLongitudeE6()) / 1E6);
		case 5:
			safeData.setLatitude5(((double) Points[4].getLatitudeE6()) / 1E6);
			safeData.setLongitude5(((double) Points[4].getLongitudeE6()) / 1E6);
		case 4:
			safeData.setLatitude4(((double) Points[3].getLatitudeE6()) / 1E6);
			safeData.setLongitude4(((double) Points[3].getLongitudeE6()) / 1E6);
		case 3:
			safeData.setLatitude3(((double) Points[2].getLatitudeE6()) / 1E6);
			safeData.setLongitude3(((double) Points[2].getLongitudeE6()) / 1E6);
		case 2:
			safeData.setLatitude2(((double) Points[1].getLatitudeE6()) / 1E6);
			safeData.setLongitude2(((double) Points[1].getLongitudeE6()) / 1E6);
		case 1:
			safeData.setLatitude1(((double) Points[0].getLatitudeE6()) / 1E6);
			safeData.setLongitude1(((double) Points[0].getLongitudeE6()) / 1E6);
			safeData.setBraceletId("");
			
			DefaultMessage message = new DefaultMessage();
			message.setFlag(MessageType.MESSAGE_SET_SAFE);
			message.setContext(jsonUtil.createJsonString(safeData));
			app.thread.sendMessage(message);
			
			break;
		default:
			Toast.makeText(SheZhiWeiXianQuActivity.this, "未设置点",Toast.LENGTH_SHORT).show();
			break;
		}
		
	}

	MKMapTouchListener mapTouchListener = new MKMapTouchListener() {
		@Override
		public void onMapClick(GeoPoint point) {
			// 在此处理地图单击事件

		}

		@Override
		public void onMapDoubleClick(GeoPoint point) {
			// 在此处理地图双击事件
		}

		@Override
		public void onMapLongClick(GeoPoint point) {
			// 在此处理地图长按事件
			if (pointsum < 10) {
				mMapView.getOverlays().clear();
				addFormalOverlay();
				mMapView.refresh();
				GeoPoint newPoint = new GeoPoint((int) (point.getLatitudeE6()),
						(int) (point.getLongitudeE6()));
				Points[pointsum] = newPoint;
				pointsum++;
				addTempOverlay();
				Drawable mark = getResources().getDrawable(
						R.drawable.icon_gcoding);
				OverlayItem item1 = new OverlayItem(newPoint, "polygon",
						"polygon");
				mItems.add(item1);
				mOverlay = new MyOverlay(mark, mMapView);
				mMapView.getOverlays().add(mOverlay);
				mOverlay.addItem(mItems);
				mMapView.refresh();
			} else {
				Toast.makeText(SheZhiWeiXianQuActivity.this, "区域不能超过十个点",
						Toast.LENGTH_SHORT).show();
			}
		}

	};
	private Handler socketHandler;

	public class MyOverlay extends ItemizedOverlay {

		public MyOverlay(Drawable defaultMarker, MapView mapView) {
			super(defaultMarker, mapView);
		}

		protected boolean onTap(int index) {
			// 在此处理item点击事件
			System.out.println("item onTap: " + index);
			return true;
		}

		public boolean onTap(GeoPoint pt, MapView mapView) {
			// 在此处理MapView的点击事件，当返回 true时
			super.onTap(pt, mapView);
			return false;
		}

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
			SheZhiWeiXianQuActivity.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	private void initClien(){
		if(app.socket == null){
			try {
				app.initSocket();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
		if(message.getFlag().equals(MessageType.MESSAGE_RETURN_OK)&&message.getContext().equals("OK")){	
			Toast.makeText(SheZhiWeiXianQuActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
			InsertHelp insertHelp = new InsertHelp(app);
			insertHelp.SafeDangerInsert(safeData);
		}else if(message.getFlag().equals(MessageType.MESSAGE_RETURN_OK)&&message.getContext().equals("UN")){
			Toast.makeText(SheZhiWeiXianQuActivity.this, "取消成功", Toast.LENGTH_SHORT).show();
		}
	}
}
