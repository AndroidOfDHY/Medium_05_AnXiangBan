package com.anxiangban.activitybaobeijiankang;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.tools.ZoomEvent;
import org.achartengine.tools.ZoomListener;
import com.anxiangban.R;
import com.anxiangban.DBHelper.MytabCursor;
import com.anxiangban.DBHelper.domain.HealthData;
import com.anxiangban.myApplication.MyApplication;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class BaoBeiJianKangActivity extends Activity {
	private String type = "TiWen";
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	private XYSeriesRenderer mCurrentRenderer;
	private String mDateFormat;
	private GraphicalView mChartView;
	private TimeSeries mCurrentSeries;
	private List<Double> listY = new ArrayList<Double>();
	private List<Date> listX = new ArrayList<Date>();
	private SharedPreferences preferences;
	private MytabCursor mtabReader;
	@SuppressWarnings("unused")
	private Bundle bundle;
	private XYSeriesRenderer renderer = new XYSeriesRenderer();// 定义XYSeriesRenderer
	@SuppressLint("SimpleDateFormat")
	SimpleDateFormat format = new SimpleDateFormat("MM-dd a");
	private Button updateButton;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tubiao);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);	
		renderer.setColor(Color.BLUE);
		renderer.setPointStyle(PointStyle.CIRCLE);// 点的类型是圆形
		renderer.setFillPoints(true);// 设置点是否实心
		mRenderer.addSeriesRenderer(renderer);// 将单个XYSeriesRenderer增加到XYMultipleSeriesRenderer
		updateButton = (Button) findViewById(R.id.button_refresh);
		updateButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setTuBiaoType();
				setChartOption();
				if (type.equals("TiWen")) {
					mRenderer.setYAxisMin(36.0);
					mRenderer.setYAxisMax(41.6);
				} else {
					mRenderer.setYAxisMin(65.0);
					mRenderer.setYAxisMax(120.6);
				}
				if (mDataset != null) {
					mDataset.clear();
				}
				
				listX.clear();
				listY.clear();
				huatu();
				mChartView.repaint();
			}
		});
		setTuBiaoType();
		setChartOption();
		if (type.equals("TiWen")) {
			mRenderer.setYAxisMin(36.0);
			mRenderer.setYAxisMax(41.6);
		} else {
			mRenderer.setYAxisMin(65.0);
			mRenderer.setYAxisMax(120.6);
		}
		if (mDataset != null) {
			mDataset.clear();
		}
		
		listX.clear();
		listY.clear();
		huatu();
	}

	private void setTuBiaoType() {
		type = preferences.getString("TuBiaoType", "");
	}

	// 设置图表选项
	private void setChartOption() {
		mRenderer.setApplyBackgroundColor(true);// 设置是否显示背景色
		mRenderer.setBackgroundColor(Color.WHITE);
		mRenderer.setMarginsColor(Color.WHITE);
		mRenderer.setAxesColor(Color.BLACK);
		mRenderer.setLabelsColor(Color.BLACK);
		mRenderer.setXLabelsColor(Color.BLACK);
		mRenderer.setYLabelsColor(0, Color.BLACK);
		mRenderer.setYLabelsAlign(Align.RIGHT);
		mRenderer.setAxisTitleTextSize(30); // 设置轴标题文字的大小
		mRenderer.setChartTitleTextSize(20);// 设置整个图表标题文字大小
		mRenderer.setLabelsTextSize(15);// 设置刻度显示文字的大小(XY轴都会被设置)
		mRenderer.setLegendTextSize(30);// 图例文字大小
		mRenderer.setZoomButtonsVisible(false);// 是否显示放大缩小按钮
		mRenderer.setPointSize(5);// 设置点的大小(图上显示的点的大小和图例中点的大小都会被设置)
		mRenderer.setMargins(new int[] { 10, 30, 10, 10 });
		mRenderer.setShowGrid(true);
	}

	private void huatu() {
		//add();
		mtabReader = new MytabCursor(
				((MyApplication) getApplication()).dbHelper
						.getReadableDatabase());
		String title = null;
		List<HealthData> list = null;
		try {
			if (type.equals("TiWen")) {
				title = "体温表";
				list = mtabReader.BodyTemperatureFind();
			} else {
				title = "心律表";
				list = mtabReader.HeartRateFind();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		mtabReader.Stop();
		if (list == null) {
			Toast.makeText(BaoBeiJianKangActivity.this, "读取数据失败",
					Toast.LENGTH_LONG).show();
		} else {
			for (int i = 0; i < list.size(); i++) {
				HealthData healthData = list.get(i);
				listX.add(healthData.getDay());
				if (type.equals("TiWen")) {
					listY.add((double) healthData.getBodyTemperature());					
				} else {
					listY.add((double) healthData.getHeartRate());
				}
			}
			addSeries(title, listX, listY);
		}
	}

	// 向图表添加折线
	public void addSeries(String seriesname, List<Date> x, List<Double> y) {
		TimeSeries series = new TimeSeries(seriesname);// 定义XYSeries
		mDataset.addSeries(series);// 在XYMultipleSeriesDataset中添加XYSeries
		mCurrentSeries = series;// 设置当前需要操作的XYSeries
		mCurrentRenderer = renderer;
		for (int i = 0; i < x.size(); i++) {
			mCurrentSeries.add(x.get(i), y.get(i));
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("dataset", mDataset);
		outState.putSerializable("renderer", mRenderer);
		outState.putSerializable("current_series", mCurrentSeries);
		outState.putSerializable("current_renderer", mCurrentRenderer);
		outState.putString("date_format", mDateFormat);
	}

	protected void onRestoreInstanceState(Bundle savedState) {
		super.onRestoreInstanceState(savedState);
		mDataset = (XYMultipleSeriesDataset) savedState
				.getSerializable("dataset");
		mRenderer = (XYMultipleSeriesRenderer) savedState
				.getSerializable("renderer");
		mCurrentSeries = (TimeSeries) savedState
				.getSerializable("current_series");
		mCurrentRenderer = (XYSeriesRenderer) savedState
				.getSerializable("current_renderer");
		mDateFormat = savedState.getString("date_format");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@SuppressWarnings("deprecation")
	protected void onResume() {
		
		super.onResume();
		if (mChartView == null) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
			mChartView = ChartFactory.getTimeChartView(this, mDataset,
					mRenderer, "MM-dd a");
			mRenderer.setClickEnabled(true);// 设置图表是否允许点击
			mRenderer.setSelectableBuffer(15);// 设置点的缓冲半径值(在某点附件点击时,多大范围内都算点击这个点)
			mChartView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SeriesSelection seriesSelection = mChartView
							.getCurrentSeriesAndPoint();
					if (seriesSelection == null) {

					} else {
						if (type.equals("TiWen")) {
							Toast.makeText(
									BaoBeiJianKangActivity.this,
									format.format(listX.get(seriesSelection
											.getPointIndex()))
											+ "体温为： "
											+ String.format("%.2f",
													seriesSelection.getValue())
											+ "度", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(
									BaoBeiJianKangActivity.this,
									format.format(listX.get(seriesSelection
											.getPointIndex()))
											+ "心律为： "
											+ String.format("%.2f",
													seriesSelection.getValue())
											+ "次每分钟", Toast.LENGTH_SHORT)
									.show();
						}
					}
				}
			});
			// mChartView.setOnLongClickListener(new View.OnLongClickListener()
			// {
			// @Override
			// public boolean onLongClick(View v) {
			// SeriesSelection seriesSelection = mChartView
			// .getCurrentSeriesAndPoint();
			// if (seriesSelection == null) {
			// Toast.makeText(BaoBeiJianKangActivity.this,
			// "No chart element was long pressed",
			// Toast.LENGTH_SHORT).show();
			// return false; // no chart element was long pressed, so
			// // let something
			// // else handle the event
			// } else {
			// Toast.makeText(
			// BaoBeiJianKangActivity.this,
			// "Chart element in series index "
			// + seriesSelection.getSeriesIndex()
			// + " data point index "
			// + seriesSelection.getPointIndex()
			// + " was long pressed",
			// Toast.LENGTH_SHORT).show();
			// return true; // the element was long pressed - the event
			// // has been
			// // handled
			// }
			// }
			// });
			// 这段代码处理放大缩小
			// -->start
			mChartView.addZoomListener(new ZoomListener() {
				public void zoomApplied(ZoomEvent e) {
					@SuppressWarnings("unused")
					String typeq = "out";
					if (e.isZoomIn()) {
						typeq = "in";
					}
				}

				public void zoomReset() {
					System.out.println("Reset");
				}
			}, true, true);
			// -->end
			// 设置拖动图表时后台打印出图表坐标的最大最小值.

			layout.addView(mChartView, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		} else {
			mChartView.repaint();
		}
	}

	protected void onRestart() {
		super.onRestart();
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
