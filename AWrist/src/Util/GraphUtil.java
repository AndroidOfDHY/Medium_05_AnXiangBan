package Util;

import java.util.List;

import domain.GPS;


public class GraphUtil {
	public static boolean pointInPolygon(GPS dian, List<GPS> AllDian) {
		int polySides = AllDian.size();
		int i, j = polySides - 1;
		boolean oddNodes = false;
		for (i = 0; i < polySides; i++) {
			if ((AllDian.get(i).getLongitude() < dian.getLongitude()
					&& AllDian.get(j).getLongitude() >= dian.getLongitude() || AllDian.get(j)
					.getLongitude() < dian.getLongitude()
					&& AllDian.get(i).getLongitude() >= dian.getLongitude())
					&& (AllDian.get(i).getLatitude() <= dian.getLatitude() || AllDian.get(j)
							.getLatitude() <= dian.getLatitude())) {
				if (AllDian.get(i).getLatitude()
						+ (dian.getLongitude() - AllDian.get(i).getLongitude())
						/ (AllDian.get(j).getLongitude() - AllDian.get(i).getLongitude())
						* (AllDian.get(j).getLatitude() - AllDian.get(i).getLatitude()) < dian
							.getLatitude()) {
					oddNodes = !oddNodes;
				}
			}
			j = i;
		}
		return oddNodes;
	}
	/*
	 * 1在安全区内
	 * -1在危险区内
	 * 0都不在
	 */
	public static int isSafe(GPS dian, List<GPS> safe, List<GPS> danger) {
		int oddNodes = 0;
		if(pointInPolygon(dian, danger)){
			oddNodes = -1;
		}else if(pointInPolygon(dian, safe)){
			oddNodes = 1;
		}
		return oddNodes;
	}
}
