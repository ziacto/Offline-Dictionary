package com.invincible.offlinedictionary;

import java.util.ArrayList;
import java.util.Random;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.invincible.offlinedictionary.database.DicDataBase;
import com.invincible.offlinedictionary.view.MainActivity;

public class WordOfTheDayService extends IntentService {
	public static final int	NOTIFICATION_WORD_OF_THE_DAY	= 1000000;

	public WordOfTheDayService() {
		super(WordOfTheDayService.class.getName());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Random random = new Random();
		ArrayList<String> wordList = DicDataBase.getInstance(this).getWordList("" + (char) ((int) 'A' + random.nextInt(26)));
		Utils.setWordOfTheDay(this, wordList.get(random.nextInt(wordList.size())));
		Notification.Builder builder = new Notification.Builder(this);
		builder.setContentTitle("Word Of The Day");
		builder.setContentText(Utils.getWordOfTheDay(this));
		builder.setSmallIcon(R.drawable.ic_launcher);
		Intent intentMainActivity = new Intent(this, MainActivity.class);
		intentMainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		builder.setContentIntent(PendingIntent.getActivity(this, 0, intentMainActivity, 0));
		builder.setAutoCancel(true);
		Notification build = builder.build();
		((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIFICATION_WORD_OF_THE_DAY, build);
	}
}
