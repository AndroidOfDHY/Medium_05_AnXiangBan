package com.anxiangban.activityguijiditu;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.anxiangban.R;
import com.anxiangban.DBHelper.MytabCursor;
import com.anxiangban.DBHelper.domain.PositionData;
import com.anxiangban.DBHelper.domain.SafeDangerData;
import com.anxiangban.myApplication.MyApplication;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapTouchListener;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.map.Symbol;
import com.baidu.mapapi.map.Symbol.Stroke;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * 演示MapView的基本用法
 */
public class GuiJiDiTuActivity extends Activity {

	final static String TAG = "MainActivity";
	private MapView mMapView = null;
	private MapController mMapController = null;
	MKMapViewListener mMapListener = null;
	private int lat;
	private int lon;
	private TextView popupText = null;// 泡泡view
	private View viewCache = null;
	GeoPoint p, p1, jiazhang, haizi, p4;
	List<PositionData> list = null;
	MyLocationOverlay myLocationOverlay2;
	OverlayTest itemOverlay1;
	private Graphic formalGraphic = null;
	ImageButton imageButton1, imageButton2, imageButton3;
	int tu = 1;
	private MyOverlay mOverlay = null;
	private PopupOverlay pop = null;
	private ArrayList<OverlayItem> mItems = null;
	private Button button = null;
	private MapView.LayoutParams layoutParam = null;
	private OverlayItem mCurItem = null;
	@SuppressLint("SimpleDateFormat")
	SimpleDateFormat format = new SimpleDateFormat("HH:mm");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication app = (MyApplication) this.getApplication();
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(this);
			app.mBMapManager.init(MyApplication.strKey,
					new MyApplication.MyGeneralListener());
		}
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		setContentView(R.layout.activity_guijiditu);
		mMapView = (MapView) findViewById(R.id.bmapView);
		myLocationOverlay2 = new MyLocationOverlay(mMapView);
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		mLocationClient.setAK("WOWuz9LlgpjFkrbVbEugXzFu");
		setOption();
		viewCache = getLayoutInflater()
				.inflate(R.layout.custom_text_view, null);
		popupText = (TextView) viewCache.findViewById(R.id.textcache);
		mMapController = mMapView.getController();
		mMapController.enableClick(true);
		mMapController.setZoom(12);
		mMapView.setBuiltInZoomControls(true);
		mMapView.setTraffic(true);
		initFormalOverlayGraphic(0,255,0,0);
		addFormalOverlay();
		initFormalOverlayGraphic(1,0,255,0);
		addFormalOverlay();
		initUI();
		p4 = new GeoPoint(b.getInt("lat"), b.getInt("lon"));
		System.out.println(p4.toString());
		addguijidian();
		adddianji();
		addzhongdian();
		haizi = new GeoPoint(
				(int) (list.get(list.size() - 1).getLatitude() * 1E6),
				(int) (list.get(list.size() - 1).getLongitude() * 1E6));

		MyLocationOverlay myLocationOverlay3 = new MyLocationOverlay(mMapView);

		LocationData locData3 = new LocationData();
		locData3.latitude = ((double) haizi.getLatitudeE6()) / 1E6;
		locData3.longitude = ((double) haizi.getLongitudeE6()) / 1E6;
		locData3.direction = 1.0f;

		myLocationOverlay3.setData(locData3);

		mMapView.getOverlays().add(myLocationOverlay3);
		mMapView.refresh();
		Drawable mark = getResources().getDrawable(R.drawable.icon_gcoding1);
		OverlayItem item1 = new OverlayItem(haizi, "item1", "item1");
		OverlayTest itemOverlay = new OverlayTest(mark, mMapView);
		mMapView.getOverlays().add(itemOverlay);
		itemOverlay.addItem(item1);
		mMapView.refresh();

	}

	private void initUI() {
		imageButton1 = (ImageButton) findViewById(R.id.ImageButton03);
		imageButton2 = (ImageButton) findViewById(R.id.ImageButton01);
		imageButton3 = (ImageButton) findViewById(R.id.ImageButton02);
		imageButton1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (tu % 2 == 0) {
					mMapView.setTraffic(true);
					mMapView.setSatellite(false);
					mMapView.refresh();

				} else {
					mMapView.setSatellite(true);
					mMapView.setTraffic(false);
					mMapView.refresh();
				}
				tu++;
			}
		});
		imageButton2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mMapController.setCenter(jiazhang);
			}
		});
		imageButton3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mMapController.setCenter(haizi);
			}
		});
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
	 * 添加轨迹点
	 */
	public void addguijidian() {
		MytabCursor mtabReader = new MytabCursor(
				((MyApplication) getApplication()).dbHelper
						.getReadableDatabase());
		try {
			list = mtabReader.PositionFind();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			mtabReader.Stop();
		}
		GeoPoint last = null;
		GeoPoint[] drawline = new GeoPoint[2];
		OverlayItem[] item = new OverlayItem[list.size()];
		System.out.println(list.size());
		for (int i = 0; i < list.size(); i++) {
			p = new GeoPoint((int) (list.get(i).getLatitude() * 1E6),
					(int) (list.get(i).getLongitude() * 1E6));
			mOverlay = new MyOverlay(getResources().getDrawable(
					R.drawable.icon_gcoding), mMapView);
			if(i!=list.size()-1)
			{
				item[i] = new OverlayItem(p, "覆盖物4", "");
				item[i].setMarker(getResources().getDrawable(
						R.drawable.icon_gcoding));
				mOverlay.addItem(item[i]);
				mItems = new ArrayList<OverlayItem>();
				mItems.addAll(mOverlay.getAllItem());
				mMapView.getOverlays().add(mOverlay);
				mMapView.refresh();
				button = new Button(this);
				button.setBackgroundResource(R.drawable.popup);
			}

			if (i != 0) {
				drawline[0] = last;
				drawline[1] = p;
				line(mMapView, drawline);
			}
			last = p;
		}

		viewCache = getLayoutInflater()
				.inflate(R.layout.custom_text_view, null);
		popupText = (TextView) viewCache.findViewById(R.id.textcache);
		PopupClickListener popListener = new PopupClickListener() {
			@Override
			public void onClickedPopup(int index) {
			}
		};
		pop = new PopupOverlay(mMapView, popListener);

	}

	public void addzhongdian() {
		GeoPoint zhongdian;
		int zhong = 0;
		for (int i = 0; i < list.size(); i++) {
			zhongdian = new GeoPoint((int) (list.get(i).getLatitude() * 1E6),
					(int) (list.get(i).getLongitude() * 1E6));
			if (zhongdian.equals(p4)) {
				zhong = i;
				System.out.println("hgygj");
			}
		}
		mMapController.setCenter(p4);
		PopupOverlay pop = new PopupOverlay(mMapView, new PopupClickListener() {
			@Override
			public void onClickedPopup(int index) {
				Log.v("click", "clickapoapo");
				// 在此处理pop点击事件，index为点击区域索引,点击区域最多可有三个
			}
		});

		popupText.setText(format.format(list.get(zhong).getDay()) + "\n" + "在"
				+ list.get(zhong).getPlace());
		pop.showPopup(BMapUtil.getBitmapFromView(popupText), p4, 8);
	}

	/**
	 * /** 初始化永久显示的多边形覆盖物
	 */
	private void initFormalOverlayGraphic(int anwei,int r,int g,int b) {
		GeoPoint[] FormalPoints = getFormalOverlayData(anwei);
		System.out.println(FormalPoints.toString());
		Geometry formalGeometry = new Geometry();
		formalGeometry.setPolygon(FormalPoints);
		// 设置多边形样式
		Symbol polygonSymbol = new Symbol();
		Symbol.Color polygonColor = polygonSymbol.new Color();
		polygonColor.red = r;
		polygonColor.green = g;
		polygonColor.blue = b;
		polygonColor.alpha = 126;
		polygonSymbol.setSurface(polygonColor, 1, 5);
		// 生成Graphic对象
		formalGraphic = new Graphic(formalGeometry, polygonSymbol);
	}

	public void adddianji() {
		MKMapTouchListener mapTouchListener = new MKMapTouchListener() {
			@Override
			public void onMapClick(GeoPoint point) {
				for (int i = 0; i < list.size(); i++) {
					p1 = new GeoPoint((int) (list.get(i).getLatitude() * 1E6)
							+ 10000 * i,
							(int) (list.get(i).getLongitude() * 1E6) + 10000
									* i);
					if (Math.abs(p1.getLatitudeE6() - point.getLatitudeE6()) < 8000
							&& Math.abs(p1.getLongitudeE6()
									- point.getLongitudeE6()) < 8000) {
						PopupOverlay pop = new PopupOverlay(mMapView,
								new PopupClickListener() {
									@Override
									public void onClickedPopup(int index) {
										Log.v("click", "clickapoapo");
										// 在此处理pop点击事件，index为点击区域索引,点击区域最多可有三个
									}
								});
						popupText.setText(format.format(list.get(i).getDay())
								+ "\n" + "在" + list.get(i).getPlace());
						pop.showPopup(BMapUtil.getBitmapFromView(popupText),
								p1, 8);
					}
				}
			}

			@Override
			public void onMapDoubleClick(GeoPoint point) {
				// 在此处理地图双击事件
			}

			@Override
			public void onMapLongClick(GeoPoint point) {
				// 在此处理地图长按事件
			}
		};
		mMapView.regMapTouchListner(mapTouchListener);
	}

	public void line(MapView mMapView, GeoPoint[] line)//
	{
		// 创建一个覆盖故宫范围的距形
		Geometry palaceGeometry = new Geometry();
		palaceGeometry.setPolyLine(line);
		;
		Symbol lineSymbol = new Symbol();// 创建样式
		Symbol.Color palaceColor = lineSymbol.new Color();// 创建颜色
		palaceColor.red = 0;// 设置颜色的红色分量
		palaceColor.green = 0;// 设置颜色的绿色分量
		palaceColor.blue = 255;// 设置颜色的蓝色分量
		palaceColor.alpha = 126;// 设置颜色的alpha值
		// 创建一个边框对象
		// 参数 - 5： 边框的线宽
		// 参数 - polygonSymbol.new Color(0xffff0000)：边框的颜色

		Stroke stroke = new Stroke(0, lineSymbol.new Color(0xffffffff));
		lineSymbol.setSurface(palaceColor, 1, 3, stroke);// 设置样式参数，颜色：palaceColor是否填充距形：是线
		Graphic palaceGraphic = new Graphic(palaceGeometry, lineSymbol);
		GraphicsOverlay palaceOverlay = new GraphicsOverlay(mMapView);
		/**
		 * 向GraphicsOverlay添加Graphic 一个GraphicsOVerlay可添加多个Graphic
		 * GraphicsOVerlay的remove接口使用setData()返回的ID来移除指定Graphic
		 */
		long palaceId = palaceOverlay.setData(palaceGraphic);
		// 将overlay添加到mapview中
		mMapView.getOverlays().add(palaceOverlay);
		// 刷新地图使新添加的overlay生效
		mMapView.refresh();
	}

	/**
	 * 获取多边形覆盖物的坐标信息
	 * 
	 * @return 多边形覆盖物的坐标信息
	 */
	private GeoPoint[] getFormalOverlayData(int anwei) {
		int biaozhi = 0;
		List<SafeDangerData> list = null;
		GeoPoint[] point = new GeoPoint[10];
		MytabCursor mtabReader = new MytabCursor(
				((MyApplication) getApplication()).dbHelper
						.getReadableDatabase());
		list = mtabReader.SafeDangerFind();
		mtabReader.Stop();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getIsSafe() == anwei) {
				if (list.get(i).getLongitude1() != 0) {
					point[0] = new GeoPoint(
							(int) (list.get(i).getLatitude1() * 1E6),
							(int) (list.get(i).getLongitude1() * 1E6));
					biaozhi = 1;
				}
				if (list.get(i).getLongitude2() != 0) {
					point[1] = new GeoPoint(
							(int) (list.get(i).getLatitude2() * 1E6),
							(int) (list.get(i).getLongitude2() * 1E6));
					biaozhi = 2;
				}
				if (list.get(i).getLongitude3() != 0) {
					point[2] = new GeoPoint(
							(int) (list.get(i).getLatitude3() * 1E6),
							(int) (list.get(i).getLongitude3() * 1E6));
					biaozhi = 3;
				}
				if (list.get(i).getLongitude4() != 0) {
					point[3] = new GeoPoint(
							(int) (list.get(i).getLatitude4() * 1E6),
							(int) (list.get(i).getLongitude4() * 1E6));
					biaozhi = 4;
				}
				if (list.get(i).getLongitude5() != 0) {
					point[4] = new GeoPoint(
							(int) (list.get(i).getLatitude5() * 1E6),
							(int) (list.get(i).getLongitude5() * 1E6));
					biaozhi = 5;
				}
				if (list.get(i).getLongitude6() != 0) {
					point[5] = new GeoPoint(
							(int) (list.get(i).getLatitude6() * 1E6),
							(int) (list.get(i).getLongitude6() * 1E6));
					biaozhi = 6;
				}
				if (list.get(i).getLongitude7() != 0) {
					point[6] = new GeoPoint(
							(int) (list.get(i).getLatitude7() * 1E6),
							(int) (list.get(i).getLongitude7() * 1E6));
					biaozhi = 7;
				}
				if (list.get(i).getLongitude8() != 0) {
					point[7] = new GeoPoint(
							(int) (list.get(i).getLatitude8() * 1E6),
							(int) (list.get(i).getLongitude8() * 1E6));
					biaozhi = 8;
				}
				if (list.get(i).getLongitude9() != 0) {
					point[8] = new GeoPoint(
							(int) (list.get(i).getLatitude9() * 1E6),
							(int) (list.get(i).getLongitude9() * 1E6));
					biaozhi = 9;
				}
				if (list.get(i).getLongitude10() != 0) {
					point[9] = new GeoPoint(
							(int) (list.get(i).getLatitude10() * 1E6),
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
			if (anwei == 0) {
				Toast.makeText(GuiJiDiTuActivity.this, "危险区未设定",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(GuiJiDiTuActivity.this, "安全区未设定",
						Toast.LENGTH_SHORT).show();
			}
		}
		return FormalPoints;

	}

	@Override
	protected void onPause() {
		stopLocation();
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		startLocation();
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		stopLocation();
		mMapView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}

	public class MyOverlay extends ItemizedOverlay {

		public MyOverlay(Drawable defaultMarker, MapView mapView) {
			super(defaultMarker, mapView);
		}

		@Override
		public boolean onTap(int index) {

			return true;
		}

		@Override
		public boolean onTap(GeoPoint pt, MapView mMapView) {
			if (pop != null) {
				pop.hidePop();
				mMapView.removeView(button);
			}
			return false;
		}

	}

	public BDLocationListener myListener = new MyLocationListener();
	public LocationClient mLocationClient = null;

	public void setOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setPriority(LocationClientOption.GpsFirst);
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5 * 1000);// 设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);// 禁止启用缓存定位
		option.setPoiNumber(5); // 最多返回POI个数
		option.setPoiDistance(1000); // poi查询距离
		option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
		mLocationClient.setLocOption(option);
	}

	public void startLocation() {
		mLocationClient.start();
		if (mLocationClient == null)
			System.out.println("locClient is null ");
		else if (mLocationClient.isStarted()) {
			System.out.println("locClient is not started");
		} else {
			System.out.println("1111");
			mLocationClient.requestLocation();
		}
	}

	public void stopLocation() {
		mLocationClient.stop();
	}

	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				return;
			}
			if (myLocationOverlay2 != null) {
				mMapView.getOverlays().remove(itemOverlay1);
				mMapView.getOverlays().remove(myLocationOverlay2);
				mMapView.refresh();
			}
			jiazhang = new GeoPoint((int) (location.getLatitude() * 1E6),
					(int) (location.getLongitude() * 1E6));
			LocationData locData2 = new LocationData();
			locData2.latitude = location.getLatitude();
			locData2.longitude = location.getLongitude();
			locData2.direction = 1.0f;
			myLocationOverlay2.setData(locData2);
			mMapView.getOverlays().add(myLocationOverlay2);
			mMapView.refresh();
			Drawable mark1 = getResources().getDrawable(R.drawable.jiazhang);

			OverlayItem item2 = new OverlayItem(jiazhang, "item2", "item2");
			itemOverlay1 = new OverlayTest(mark1, mMapView);
			mMapView.getOverlays().add(itemOverlay1);
			itemOverlay1.addItem(item2);
			mMapView.refresh();

		}

		public void onReceivePoi(BDLocation poiLocation) {
			System.out.println("33");
		}
	}

}
