package com.invincible.offlinedictionary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DateChangeCastReciver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Utils.initilizeWordOfTheDayService(context);
	}
}
