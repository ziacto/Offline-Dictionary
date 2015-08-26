package com.invincible.offlinedictionary;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.Toast;

public class Promotion {

	private static final String	REVIEW_DONE		= "ReviewDone";
	private static final String	REVIEW_COUNT	= "ReviewCount";
	private static final String	ASK_FEEDBACK	= "AskFeedback";
	private static final String	ASK_SHARE		= "AskShare";

	/**
	 * Method to get weather asking feedback or not
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isAskingFeedback(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(ASK_FEEDBACK, Context.MODE_PRIVATE);
		int count = sharedPreferences.getInt(ASK_FEEDBACK, 0);
		if (count % 10 == 0) {
			sharedPreferences.edit().putInt(ASK_FEEDBACK, 1).commit();
			return true;
		} else {
			sharedPreferences.edit().putInt(ASK_FEEDBACK, count + 1).commit();
			return false;
		}

	}

	/**
	 * Method to get weather asking share or not
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isAskingShare(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(ASK_SHARE, Context.MODE_PRIVATE);
		int count = sharedPreferences.getInt(ASK_SHARE, 0);
		if (count % 10 == 0) {
			sharedPreferences.edit().putInt(ASK_SHARE, 1).commit();
			return true;
		} else {
			sharedPreferences.edit().putInt(ASK_SHARE, count + 1).commit();
			return false;
		}

	}

	/**
	 * Method to get weather asking review or not
	 * 
	 * @param ctx
	 * @return
	 */
	public static boolean isAskingReview(Context ctx) {
		if (isReviewDone(ctx) || !isConnectionAvailable(ctx)) {
			return false;
		}
		return true;
	}

	/**
	 * Method to get weather asking review or not
	 * 
	 * @param ctx
	 * @param count
	 *            first interval after dialog appear
	 * @param incrementCount
	 *            True if get review dialog again and again until review done by user, False will never ask again
	 * @return
	 */
	public static boolean isAskingReview(Context ctx, int count, boolean incrementCount) {
		if (incrementCount && getReviewCount(ctx) <= count + 1) {
			incrementReviewCount(ctx);
		}
		if (isReviewDone(ctx) || !isConnectionAvailable(ctx) || count > getReviewCount(ctx)) {
			return false;
		}
		return true;
	}

	/**
	 * Get review count
	 * 
	 * @param context
	 * @return
	 */
	private static int getReviewCount(Context context) {
		return context.getSharedPreferences(REVIEW_COUNT, Context.MODE_PRIVATE).getInt(REVIEW_COUNT, 0);
	}

	/**
	 * increment the value of review count
	 * 
	 * @param context
	 */
	private static void incrementReviewCount(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(REVIEW_COUNT, Context.MODE_PRIVATE);
		sharedPreferences.edit().putInt(REVIEW_COUNT, sharedPreferences.getInt(REVIEW_COUNT, 0) + 1).commit();
	}

	/**
	 * Method to send review
	 * 
	 * @param context
	 */
	public static void sendReview(Context context) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName()));
		if (isConnectionAvailable(context) && isIntentAvailable(context, intent)) {
			// SET THAT REVIEW IS DONE
			context.getSharedPreferences(REVIEW_DONE, Context.MODE_PRIVATE).edit().putBoolean(REVIEW_DONE, true).commit();
			// START REVIEW ACTIVITY
			context.startActivity(intent);
		} else {
			Toast.makeText(context, "No network connection", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Method to get weather the review is done or not
	 * 
	 * @param context
	 * @return TRUE is user already given review , FALSE other wise
	 */
	public static boolean isReviewDone(Context context) {
		return context.getSharedPreferences(REVIEW_DONE, Context.MODE_PRIVATE).getBoolean(REVIEW_DONE, false);
	}

	/**
	 * Method to get more apps
	 * 
	 * @param context
	 * @param publisherName
	 */
	public static void getMoreApps(Context context) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:" + context.getResources().getString(R.string.promotion_publisher_name)));
		if (isIntentAvailable(context, intent)) {
			context.startActivity(intent);
		} else {
			Toast.makeText(context, "No network connection", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Method top send feedback
	 * 
	 * @param context
	 * @param feedBackEmailId
	 * @param emailSubject
	 * @param msg
	 */
	public static void sendFeedback(Context context) {
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { context.getResources().getString(R.string.promotion_publisher_email) });
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.promotion_publisher_subject));
		emailIntent.putExtra(Intent.EXTRA_TEXT, "");
		emailIntent.setType("message/rfc822");
		emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (isIntentAvailable(context, emailIntent)) {
			context.startActivity(emailIntent);
		} else {
			Toast.makeText(context, "No Email Application Found", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Method to share application
	 * 
	 * @param context
	 * @param chooserTitle
	 * @param message
	 * @param messageSubject
	 */
	public static void shareApp(Context context) {
		String message = context.getResources().getString(R.string.promotion_share) + " http://play.google.com/store/apps/details?id=" + context.getPackageName();
		Intent sentIntent = new Intent(Intent.ACTION_SEND);
		sentIntent.setType("text/plain");
		sentIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.promotion_publisher_subject));
		sentIntent.putExtra(Intent.EXTRA_TEXT, "\n"+message);
		Intent sendIntent = Intent.createChooser(sentIntent, "Share File");
		sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(sendIntent);
	}

	/**
	 * Method to check weather the network is available in the device
	 * 
	 * @param context
	 * @return
	 */
	private static boolean isConnectionAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	/**
	 * Method to get weather the intent is available in system to perform specified operation
	 * 
	 * @param ctx
	 * @param in
	 * @return
	 */
	private static boolean isIntentAvailable(Context ctx, Intent in) {
		PackageManager packageManager = ctx.getPackageManager();
		List<ResolveInfo> activities = packageManager.queryIntentActivities(in, 0);
		return (activities == null) ? false : (activities.size() > 0);
	}

}
