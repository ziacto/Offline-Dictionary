package com.invincible.offlinedictionary;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Utils {

	private static final String		WORD_OF_THE_DAY				= "com.invincinle.dictionaryen.Utils.WORD_OF_THE_DAY";
	private static final String		WORD_OF_THE_CLIP_MANAGER	= "com.invincinle.dictionaryen.Utils.WORD_OF_THE_CLIP_MANAGER";
	private static final String		WORD_SNITCHER				= "com.invincinle.dictionaryen.Utils.WORD_SNITCHER";
	private static final String		SPEEKER						= "com.invincinle.dictionaryen.Utils.SPEEKER";
	public static final String[]	SPEEKER_OPERATION_ARRAY		= { "Speak only word", "Speak whole text" };
	private static final String		TEXT_SIZE					= "com.invincinle.dictionaryen.Utils.TEXT_SIZE";

	private static final String		BACK_KEY_OPERATION			= "com.invincible.dictionaryen.Constants.BACK_KEY_OPERATION";
	public static final String[]	BACK_KEY_OPERATION_ARRAY	= { "Exit", "Previous Word" };
	// HISTORY
	private static final String		HISTORY_ITEM				= "com.invincible.dictionaryen.Constants.HISTORY_ITEM";
	public static final String[]	HISTORY_ITEM_STRING_ARRAY	= { "0", "10", "100", "500", "1000" };
	// CUSTOM VIEW
	private static final String		CUSTOM_VIEW_ETYMOLOGY		= "com.invincible.dictionaryen.Constants.CUSTOM_VIEW_ETYMOLOGY";
	private static final String		CUSTOM_VIEW_ADJECTIVE		= "com.invincible.dictionaryen.Constants.CUSTOM_VIEW_ADJECTIVE";
	private static final String		CUSTOM_VIEW_PRONOUNCIATION	= "com.invincible.dictionaryen.Constants.CUSTOM_VIEW_PRONOUNCIATION";
	private static final String		CUSTOM_VIEW_NOUN			= "com.invincible.dictionaryen.Constants.CUSTOM_VIEW_NOUN";
	private static final String		CUSTOM_VIEW_PRO_NOUN		= "com.invincible.dictionaryen.Constants.CUSTOM_VIEW_PRO_NOUN";
	private static final String		CUSTOM_VIEW_VERB			= "com.invincible.dictionaryen.Constants.CUSTOM_VIEW_VERB";
	private static final String		CUSTOM_VIEW_SYNONYMS		= "com.invincible.dictionaryen.Constants.CUSTOM_VIEW_SYNONYMS";
	private static final String		CUSTOM_VIEW_ANTONYMS		= "com.invincible.dictionaryen.Constants.CUSTOM_VIEW_ANTONYMS";
	private static final String		CUSTOM_VIEW_TRANSLATION		= "com.invincible.dictionaryen.Constants.CUSTOM_VIEW_TRANSLATION";
	public static final String[]	CUSTOM_VIEW_ARRAY			= { "Etymology", "Pronounciation", "Adjective", "Noun", "Pro-Noun", "Verb", "Synonyms", "Antonyms", "Translations" };

	/**
	 * Method to get the word of the day
	 * 
	 * @param context
	 * @return
	 */
	public static String getWordOfTheDay(Context context) {
		return context.getSharedPreferences(WORD_OF_THE_DAY, Context.MODE_PRIVATE).getString(WORD_OF_THE_DAY, "INVINCIBLE");
	}

	/**
	 * Method to set the word of the day
	 * 
	 * @param context
	 * @param word
	 *            Word of the day
	 * @return
	 */
	public static void setWordOfTheDay(Context context, String word) {
		context.getSharedPreferences(WORD_OF_THE_DAY, Context.MODE_PRIVATE).edit().putString(WORD_OF_THE_DAY, word).commit();
	}

	/**
	 * Method to get the word of the day
	 * 
	 * @param context
	 * @return
	 */
	public static String getWordOfClipManager(Context context) {
		return context.getSharedPreferences(WORD_OF_THE_CLIP_MANAGER, Context.MODE_PRIVATE).getString(WORD_OF_THE_CLIP_MANAGER, "INVINCIBLE");
	}

	/**
	 * Method to set the word of the day
	 * 
	 * @param context
	 * @param word
	 *            Word of the day
	 * @return
	 */
	public static void setWordOfClipManager(Context context, String word) {
		context.getSharedPreferences(WORD_OF_THE_CLIP_MANAGER, Context.MODE_PRIVATE).edit().putString(WORD_OF_THE_CLIP_MANAGER, word).commit();
	}

	/**
	 * Method to check weather the network is available in the device
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		Toast.makeText(context, "No network available...", Toast.LENGTH_SHORT).show();
		return false;
	}

	/**
	 * Method to know weather to speek word only or the whole meaning
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isSpeekWordOnly(Context context) {
		return context.getSharedPreferences(SPEEKER, Context.MODE_PRIVATE).getBoolean(SPEEKER, true);
	}

	/**
	 * Method to set speek whole meaning
	 * 
	 * @param context
	 * @param index
	 * @return
	 */
	public static void setSpeekOperation(Context context, int index) {
		context.getSharedPreferences(SPEEKER, Context.MODE_PRIVATE).edit().putBoolean(SPEEKER, (index == 0)).commit();
	}

	/**
	 * Method to know weather to show Word Snitcher is activet or not
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWordSnitcherActive(Context context) {
		return context.getSharedPreferences(WORD_SNITCHER, Context.MODE_PRIVATE).getBoolean(WORD_SNITCHER, true);
	}

	/**
	 * Method to set the Word Snitcher
	 * 
	 * @param context
	 * @return
	 */
	public static void stopWordSnitcher(Context context) {
		context.getSharedPreferences(WORD_SNITCHER, Context.MODE_PRIVATE).edit().putBoolean(WORD_SNITCHER, false).commit();
	}

	/**
	 * Method to set the Word Snitcher
	 * 
	 * @param context
	 * @return
	 */
	public static void startWordSnitcher(Context context) {
		context.getSharedPreferences(WORD_SNITCHER, Context.MODE_PRIVATE).edit().putBoolean(WORD_SNITCHER, true).commit();
	}

	/**
	 * Method to know weather to exit or show previous when back key pressed
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isBackKeyOperationExit(Context context) {
		return context.getSharedPreferences(BACK_KEY_OPERATION, Context.MODE_PRIVATE).getBoolean(BACK_KEY_OPERATION, false);
	}

	/**
	 * Method to set weather to exit or show previous when back key pressed
	 * 
	 * @param context
	 * @param index
	 * @return
	 */
	public static void setBackKeyOperation(Context context, int index) {
		context.getSharedPreferences(BACK_KEY_OPERATION, Context.MODE_PRIVATE).edit().putBoolean(BACK_KEY_OPERATION, (index == 0)).commit();
	}

	/**
	 * Method to get the back key index
	 * 
	 * @param context
	 * @return
	 */
	public static int getBackKeyOperationIndex(Context context) {
		return isBackKeyOperationExit(context) ? 0 : 1;
	}

	/**
	 * Method to get the text size
	 * 
	 * @param context
	 * @return
	 */
	public static int getTextSize(Context context) {
		return context.getSharedPreferences(TEXT_SIZE, Context.MODE_PRIVATE).getInt(TEXT_SIZE, 100);
	}

	/**
	 * Method to set the size of Text
	 * 
	 * @param context
	 * @param index
	 *            text size
	 * @return
	 */
	public static void setTextSize(Context context, int index) {
		context.getSharedPreferences(TEXT_SIZE, Context.MODE_PRIVATE).edit().putInt(TEXT_SIZE, index).commit();
	}

	/**
	 * Method to get the history item size
	 * 
	 * @param context
	 * @return
	 */
	public static int getHistoryItemSize(Context context) {
		return context.getSharedPreferences(HISTORY_ITEM, Context.MODE_PRIVATE).getInt(HISTORY_ITEM, 100);
	}

	/**
	 * Method to get the history item size index
	 * 
	 * @param context
	 * @return
	 */
	public static int getHistoryItemSizeIndex(Context context) {
		switch (getHistoryItemSize(context)) {
		case 0:
			return 0;
		case 10:
			return 1;
		case 100:
			return 2;
		case 500:
			return 3;
		case 1000:
			return 4;
		default:
			return 2;
		}
	}

	/**
	 * Method to set the size of History item
	 * 
	 * @param context
	 * @param index
	 *            Item size for history
	 * @return
	 */
	public static void setHistoryItemSize(Context context, int index) {
		context.getSharedPreferences(HISTORY_ITEM, Context.MODE_PRIVATE).edit().putInt(HISTORY_ITEM, Integer.parseInt(HISTORY_ITEM_STRING_ARRAY[index])).commit();
	}

	// ================================================================================================================
	// CUSTOM VIEW GET
	// ================================================================================================================

	/**
	 * Method to know weather to show Etymology or not
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isShowEtymology(Context context) {
		return context.getSharedPreferences(CUSTOM_VIEW_ETYMOLOGY, Context.MODE_PRIVATE).getBoolean(CUSTOM_VIEW_ETYMOLOGY, true);
	}

	/**
	 * Method to know weather to show Adjective or not
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isShowAdjective(Context context) {
		return context.getSharedPreferences(CUSTOM_VIEW_ADJECTIVE, Context.MODE_PRIVATE).getBoolean(CUSTOM_VIEW_ADJECTIVE, true);
	}

	/**
	 * Method to know weather to show Pronounciation or not
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isShowPronounciation(Context context) {
		return context.getSharedPreferences(CUSTOM_VIEW_PRONOUNCIATION, Context.MODE_PRIVATE).getBoolean(CUSTOM_VIEW_PRONOUNCIATION, true);
	}

	/**
	 * Method to know weather to show Noun or not
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isShowNoun(Context context) {
		return context.getSharedPreferences(CUSTOM_VIEW_NOUN, Context.MODE_PRIVATE).getBoolean(CUSTOM_VIEW_NOUN, true);
	}

	/**
	 * Method to know weather to show ProNoun or not
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isShowProNoun(Context context) {
		return context.getSharedPreferences(CUSTOM_VIEW_PRO_NOUN, Context.MODE_PRIVATE).getBoolean(CUSTOM_VIEW_PRO_NOUN, true);
	}

	/**
	 * Method to know weather to show Verb or not
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isShowVerb(Context context) {
		return context.getSharedPreferences(CUSTOM_VIEW_VERB, Context.MODE_PRIVATE).getBoolean(CUSTOM_VIEW_VERB, true);
	}

	/**
	 * Method to know weather to show Synonyms or not
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isShowSynonyms(Context context) {
		return context.getSharedPreferences(CUSTOM_VIEW_SYNONYMS, Context.MODE_PRIVATE).getBoolean(CUSTOM_VIEW_SYNONYMS, true);
	}

	/**
	 * Method to know weather to show Antonyms or not
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isShowAntonyms(Context context) {
		return context.getSharedPreferences(CUSTOM_VIEW_ANTONYMS, Context.MODE_PRIVATE).getBoolean(CUSTOM_VIEW_ANTONYMS, true);
	}

	/**
	 * Method to know weather to show Translation or not
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isShowTranslation(Context context) {
		return context.getSharedPreferences(CUSTOM_VIEW_TRANSLATION, Context.MODE_PRIVATE).getBoolean(CUSTOM_VIEW_TRANSLATION, true);
	}

	/**
	 * Method to get the array list of Custom view selected Items
	 * 
	 * @param context
	 * @return
	 */
	public static boolean[] getCustomViewCheckedItemArray(Context context) {
		return new boolean[] { isShowEtymology(context), isShowPronounciation(context), isShowAdjective(context), isShowNoun(context), isShowProNoun(context), isShowVerb(context), isShowSynonyms(context), isShowAntonyms(context), isShowTranslation(context) };
	}

	/**
	 * Method to change the visibility of custom view
	 * 
	 * @param context
	 * @param index
	 *            Index of Custom view
	 * @param isChecked
	 *            True if visible else false
	 */
	public static void setCustomViewChecked(Context context, int index, boolean isChecked) {
		switch (index) {
		case 0:
			context.getSharedPreferences(CUSTOM_VIEW_ETYMOLOGY, Context.MODE_PRIVATE).edit().putBoolean(CUSTOM_VIEW_ETYMOLOGY, isChecked).commit();
			break;
		case 1:
			context.getSharedPreferences(CUSTOM_VIEW_PRONOUNCIATION, Context.MODE_PRIVATE).edit().putBoolean(CUSTOM_VIEW_PRONOUNCIATION, isChecked).commit();
			break;
		case 2:
			context.getSharedPreferences(CUSTOM_VIEW_ADJECTIVE, Context.MODE_PRIVATE).edit().putBoolean(CUSTOM_VIEW_ADJECTIVE, isChecked).commit();
			break;
		case 3:
			context.getSharedPreferences(CUSTOM_VIEW_NOUN, Context.MODE_PRIVATE).edit().putBoolean(CUSTOM_VIEW_NOUN, isChecked).commit();
			break;
		case 4:
			context.getSharedPreferences(CUSTOM_VIEW_PRO_NOUN, Context.MODE_PRIVATE).edit().putBoolean(CUSTOM_VIEW_PRO_NOUN, isChecked).commit();
			break;
		case 5:
			context.getSharedPreferences(CUSTOM_VIEW_VERB, Context.MODE_PRIVATE).edit().putBoolean(CUSTOM_VIEW_VERB, isChecked).commit();
			break;
		case 6:
			context.getSharedPreferences(CUSTOM_VIEW_SYNONYMS, Context.MODE_PRIVATE).edit().putBoolean(CUSTOM_VIEW_SYNONYMS, isChecked).commit();
			break;
		case 7:
			context.getSharedPreferences(CUSTOM_VIEW_ANTONYMS, Context.MODE_PRIVATE).edit().putBoolean(CUSTOM_VIEW_ANTONYMS, isChecked).commit();
			break;
		case 8:
			context.getSharedPreferences(CUSTOM_VIEW_TRANSLATION, Context.MODE_PRIVATE).edit().putBoolean(CUSTOM_VIEW_TRANSLATION, isChecked).commit();
			break;
		default:
			break;
		}
	}

	/**
	 * Method to initilize word of the day service
	 * 
	 * @param context
	 */
	public static void initilizeWordOfTheDayService(Context context) {
		// SET ALARM MANAGER
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent serviceIntent = new Intent(context, WordOfTheDayService.class);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, serviceIntent, 0);
		alarmManager.cancel(pendingIntent);
		Calendar calendar = Calendar.getInstance();
		boolean isSendBroadcast = calendar.get(Calendar.HOUR_OF_DAY) >= 12;
		calendar.set(Calendar.HOUR_OF_DAY, 12);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		alarmManager.setRepeating(AlarmManager.RTC, isSendBroadcast ? calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY : calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
	}

}
