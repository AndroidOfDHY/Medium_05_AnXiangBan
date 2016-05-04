package Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetConnect {
    /**
     * �ж��Ƿ������������
     * @param context
     * @return
     */
    public static final boolean hasNetWorkConection(Context context){
        //��ȡ���ӻ������
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //��ȡ���ӵ�������Ϣ
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isAvailable());
    }
    
    /**
     * �ж��Ƿ����wifi����
     * @param context
     * @return
     */
    public static final boolean hasWifiConnection(Context context){
        //��ȡ���ӻ������
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return (networkInfo != null && networkInfo.isAvailable());
    }
    
    /**
     * �ж��Ƿ���GPRS����
     * @param context
     * @return
     */
    public static final boolean hasGPRSConnection(Context context){
        //��ȡ���ӻ������
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (networkInfo != null && networkInfo.isAvailable());
    }
    
    /**
     * �ж�������������
     * @param context
     * @return
     */
    public static final int getNetworkConnectionType(Context context){
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo wifiNetWorkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final NetworkInfo mobileNetWorkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        
        if(wifiNetWorkInfo != null && wifiNetWorkInfo.isAvailable()){
            return ConnectivityManager.TYPE_WIFI;
        } else if(mobileNetWorkInfo != null && mobileNetWorkInfo.isAvailable()){
            return ConnectivityManager.TYPE_MOBILE;
        } else {
            return -1;
        }
    }
}