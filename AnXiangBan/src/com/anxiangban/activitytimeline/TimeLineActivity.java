package com.anxiangban.activitytimeline;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.anxiangban.R;
import com.anxiangban.DBHelper.MytabCursor;
import com.anxiangban.DBHelper.MytabOperate;
import com.anxiangban.DBHelper.domain.PositionData;
import com.anxiangban.activityguijiditu.GuiJiDiTuActivity;
import com.anxiangban.myApplication.MyApplication;
import com.anxiangban.myView.MylistView;
import com.anxiangban.myView.MylistView.OnRefreshListener;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TimeLineActivity extends Activity {
	private MylistView mListView;
	private TimeLineViewAdapter mAdapter;
	private List<TimeLineEntity> mDataArrays = new ArrayList<TimeLineEntity>();
	private Button tomap;
	private double lat;
	private double lon;
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat positionFormat = new SimpleDateFormat("HH:mm");
	MKSearch mSearch = null;
	String place = "";
	PositionData positData = new PositionData();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		initView();
		lat = 29.892152;
		lon = 121.486738;
		MyApplication app = (MyApplication) this.getApplication();
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(getApplicationContext());
			app.mBMapManager.init(MyApplication.strKey,
					new MyApplication.MyGeneralListener());
		}
		mSearch = new MKSearch();
		mSearch.init(app.mBMapManager, new MKSearchListener() {
			@Override
			public void onGetPoiDetailSearchResult(int type, int error) {
			}

			@SuppressLint("DefaultLocale")
			public void onGetAddrResult(MKAddrInfo res, int error) {
				if (error != 0) {
					String str = String.format("错误号：%d", error);
					System.out.println("++++++++++++++++++" + str);
					return;
				}
				if (res.type == MKAddrInfo.MK_REVERSEGEOCODE) {
					// 反地理编码：通过坐标点检索详细地址及周边poi
					String strInfo = res.strAddr;
					place = strInfo;
					positData.setPlace(place);
					MytabOperate mytabOperate = new MytabOperate(
							((MyApplication) getApplication()).dbHelper
									.getWritableDatabase());
					mytabOperate.PositionInsert(positData);
					mytabOperate.Stop();
					System.out.println("位置：-------------->>" + place);
					// Toast.makeText(TimeLineActivity.this, strInfo,
					// Toast.LENGTH_LONG).show();
				}
			}

			public void onGetPoiResult(MKPoiResult res, int type, int error) {

			}

			public void onGetDrivingRouteResult(MKDrivingRouteResult res,
					int error) {
			}

			public void onGetTransitRouteResult(MKTransitRouteResult res,
					int error) {
			}

			public void onGetWalkingRouteResult(MKWalkingRouteResult res,
					int error) {
			}

			public void onGetBusDetailResult(MKBusLineResult result, int iError) {
			}

			@Override
			public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
			}

			@Override
			public void onGetShareUrlResult(MKShareUrlResult result, int type,
					int error) {
			}

		});
		 initData();
	}

	private void initView() {
		mListView = (MylistView) findViewById(R.id.listview);
		tomap = (Button) findViewById(R.id.tomap);
		tomap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TimeLineActivity.this,
						GuiJiDiTuActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("lat", (int) (lat * 1E6));
				bundle.putInt("lon", (int) (lon * 1E6));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		mListView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						initData();
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mAdapter.notifyDataSetChanged();
						mListView.onRefreshComplete();
					}

				}.execute();
			}
		});
	}

	public void initData() {
		mDataArrays.clear();
		List<PositionData> list = new ArrayList<PositionData>();
		mDataArrays.add(new TimeLineEntity(format.format(new Date()), "", 0, 0,
				-1));
		try {
			MytabCursor mtabReader = new MytabCursor(
				((MyApplication) getApplication()).dbHelper
				.getReadableDatabase());
			list = mtabReader.PositionFind();
			mtabReader.Stop();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < list.size(); i++) {
			PositionData positionData = list.get(i);
			TimeLineEntity entity = new TimeLineEntity();
			entity.setTime(positionFormat.format(positionData.getDay()));
			entity.setText(positionData.getPlace());
			entity.setLat(positionData.getLatitude());
			entity.setLon(positionData.getLongitude());
			mDataArrays.add(entity);
		}
		mDataArrays.add(new TimeLineEntity("", "", 0, 0, 1));
	}

	public void addToListView() {
		mAdapter = new TimeLineViewAdapter(this, mDataArrays);
		mListView.setAdapter(mAdapter);
	}

//	private void add() {
//		GeoPoint ptCenter = new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6));
//		positData.setDay(new Date());
//		positData.setBraceletId("123");
//		positData.setLatitude(lat);
//		positData.setLongitude(lon);
//		mSearch.reverseGeocode(ptCenter);
//
//	}

	protected void onRestart() {
		super.onRestart();
	}

	protected void onResume() {
		super.onResume();
//		add();
		addToListView();
	}

}
