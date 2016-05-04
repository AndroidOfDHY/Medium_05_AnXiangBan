package com.anxiangban.activitytimeline;

import java.util.List;
import com.anxiangban.R;
import com.anxiangban.activityguijiditu.GuiJiDiTuActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TimeLineViewAdapter extends BaseAdapter {

	public static interface IMsgViewType {
		int IMVT_head_MSG = -1;
		int IMVT_content_MSG = 0;
		int IMVT_foot_MSG = 1;
	}
	@SuppressWarnings("unused")
	private static final String TAG = TimeLineViewAdapter.class.getSimpleName();
	private List<TimeLineEntity> coll;
	private Context ctx;
	private LayoutInflater mInflater;
	public TimeLineViewAdapter(Context context, List<TimeLineEntity> coll) {
		ctx = context;
		this.coll = coll;
		mInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return coll.size();
	}

	public Object getItem(int position) {
		return coll.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		TimeLineEntity entity = coll.get(position);
		int flag = entity.getType();
		if (flag == -1) {
			return IMsgViewType.IMVT_head_MSG;
		} else if(flag == 0){
			return IMsgViewType.IMVT_content_MSG;
		}else{
			return IMsgViewType.IMVT_foot_MSG;
		}
	}

	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		final TimeLineEntity entity = coll.get(position);
		int flag = entity.getType();
		ViewHolder viewHolder = null;
		if (flag == -1) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.timeline_head, null);
				viewHolder = new ViewHolder();
				viewHolder.time = (TextView) convertView.findViewById(R.id.tv_sendtime);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.time.setText(entity.getTime());
		} else if(flag == 0){
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.timeline_itme, null);
				viewHolder = new ViewHolder();
				viewHolder.time = (TextView) convertView.findViewById(R.id.time);
				viewHolder.text = (TextView) convertView.findViewById(R.id.text);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.time.setText(entity.getTime());
			viewHolder.text.setText(entity.getText());			
			viewHolder.text.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			viewHolder.text.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {					
					Intent intent = new Intent(ctx,	GuiJiDiTuActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("lat", (int) (entity.getLat()*1E6));
					bundle.putInt("lon", (int) (entity.getLon()*1E6));
					intent.putExtras(bundle);
					ctx.startActivity(intent);
				}
			});
		}else{
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.timeline_foot, null);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
		}
		
		return convertView;
	}

	static class ViewHolder {
		public TextView time;
		public TextView text;
	}


}
