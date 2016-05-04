package com.anxiangban.activityguijiditu;

import android.graphics.drawable.Drawable;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class OverlayTest extends ItemizedOverlay<OverlayItem> {
	public OverlayTest(Drawable mark, MapView mapView) {
		super(mark, mapView);
	}

	protected boolean onTap(int index) {
		// �ڴ˴���item����¼�
		System.out.println("item onTap: " + index);
		return true;
	}

	public boolean onTap(GeoPoint pt, MapView mapView) {
		// �ڴ˴���MapView�ĵ���¼��������� trueʱ
		super.onTap(pt, mapView);
		return false;
	}
}
