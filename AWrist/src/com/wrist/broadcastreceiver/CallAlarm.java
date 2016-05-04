package com.wrist.broadcastreceiver;

import Util.MediaUtil;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CallAlarm extends BroadcastReceiver {

	public void onReceive(Context context, Intent intent) {
		MediaUtil mediaUtil = new MediaUtil();
		mediaUtil.playMedia();
		System.out.println("ÄÖÖÓ------->>Æô¶¯");
	}

}
