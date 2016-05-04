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
	private XYSeriesRenderer renderer = new XYSeriesRenderer();// ����XYSeriesRenderer
	@SuppressLint("SimpleDateFormat")
	SimpleDateFormat format = new SimpleDateFormat("MM-dd a");
	private Button updateButton;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tubiao);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);	
		renderer.setColor(Color.BLUE);
		renderer.setPointStyle(PointStyle.CIRCLE);// ���������Բ��
		renderer.setFillPoints(true);// ���õ��Ƿ�ʵ��
		mRenderer.addSeriesRenderer(renderer);// ������XYSeriesRenderer���ӵ�XYMultipleSeriesRenderer
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

	// ����ͼ��ѡ��
	private void setChartOption() {
		mRenderer.setApplyBackgroundColor(true);// �����Ƿ���ʾ����ɫ
		mRenderer.setBackgroundColor(Color.WHITE);
		mRenderer.setMarginsColor(Color.WHITE);
		mRenderer.setAxesColor(Color.BLACK);
		mRenderer.setLabelsColor(Color.BLACK);
		mRenderer.setXLabelsColor(Color.BLACK);
		mRenderer.setYLabelsColor(0, Color.BLACK);
		mRenderer.setYLabelsAlign(Align.RIGHT);
		mRenderer.setAxisTitleTextSize(30); // ������������ֵĴ�С
		mRenderer.setChartTitleTextSize(20);// ��������ͼ��������ִ�С
		mRenderer.setLabelsTextSize(15);// ���ÿ̶���ʾ���ֵĴ�С(XY�ᶼ�ᱻ����)
		mRenderer.setLegendTextSize(30);// ͼ�����ִ�С
		mRenderer.setZoomButtonsVisible(false);// �Ƿ���ʾ�Ŵ���С��ť
		mRenderer.setPointSize(5);// ���õ�Ĵ�С(ͼ����ʾ�ĵ�Ĵ�С��ͼ���е�Ĵ�С���ᱻ����)
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
				title = "���±�";
				list = mtabReader.BodyTemperatureFind();
			} else {
				title = "���ɱ�";
				list = mtabReader.HeartRateFind();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		mtabReader.Stop();
		if (list == null) {
			Toast.makeText(BaoBeiJianKangActivity.this, "��ȡ����ʧ��",
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

	// ��ͼ���������
	public void addSeries(String seriesname, List<Date> x, List<Double> y) {
		TimeSeries series = new TimeSeries(seriesname);// ����XYSeries
		mDataset.addSeries(series);// ��XYMultipleSeriesDataset�����XYSeries
		mCurrentSeries = series;// ���õ�ǰ��Ҫ������XYSeries
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
			mRenderer.setClickEnabled(true);// ����ͼ���Ƿ�������
			mRenderer.setSelectableBuffer(15);// ���õ�Ļ���뾶ֵ(��ĳ�㸽�����ʱ,���Χ�ڶ����������)
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
											+ "����Ϊ�� "
											+ String.format("%.2f",
													seriesSelection.getValue())
											+ "��", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(
									BaoBeiJianKangActivity.this,
									format.format(listX.get(seriesSelection
											.getPointIndex()))
											+ "����Ϊ�� "
											+ String.format("%.2f",
													seriesSelection.getValue())
											+ "��ÿ����", Toast.LENGTH_SHORT)
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
			// ��δ��봦��Ŵ���С
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
			// �����϶�ͼ��ʱ��̨��ӡ��ͼ������������Сֵ.

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
