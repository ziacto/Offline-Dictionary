package com.invincible.offlinedictionary;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.invincible.offlinedictionary.database.DicDataBase;
import com.invincible.offlinedictionary.database.Model;
import com.invincible.offlinedictionary.database.parse.HtmlParserHelper;
import com.invincible.offlinedictionary.view.MainActivity;

@SuppressLint("NewApi")
public class ClipManager extends Service implements OnPrimaryClipChangedListener {
	public static final int				NOTIFICATION_CLIP_MANAGER	= 1000001;
	private WindowManager				mWindowManager;
	private WindowManager.LayoutParams	mParamsCallReminder;
	private View						mView;

	@Override
	public void onCreate() {
		super.onCreate();
		((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).addPrimaryClipChangedListener(this);
		mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		mParamsCallReminder = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, (int) ((float) getResources().getDisplayMetrics().heightPixels * 0.55F), WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
		mParamsCallReminder.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
		Notification.Builder builder = new Notification.Builder(this);
		builder.setContentTitle("Offline Dictionary");
		builder.setContentText("Click to stop word snitcher");
		builder.setSmallIcon(R.drawable.ic_launcher);
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(MainActivity.EXTRA_SNITCHER, true);
		builder.setContentIntent(PendingIntent.getActivity(this, 0, intent, 0));
		builder.setAutoCancel(true);
		Notification build = builder.build();
		stopForeground(true);
		startForeground(NOTIFICATION_CLIP_MANAGER, build);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).removePrimaryClipChangedListener(this);
		stopForeground(true);
		super.onDestroy();
	}

	@Override
	public void onPrimaryClipChanged() {
		try {
			ClipData primaryClip = ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).getPrimaryClip();
			if (primaryClip.getDescription().hasMimeType("text/plain") && primaryClip.getItemAt(0) != null && primaryClip.getItemAt(0).getText() != null) {
				String word = (String) primaryClip.getItemAt(0).getText().toString().toUpperCase(Locale.ENGLISH).trim();
				if (DicDataBase.getInstance(this).isExist(word)) {
					removeView();
					Utils.setWordOfClipManager(this, word);
					mView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.clip_view, null);
					((TextView) mView.findViewById(R.id.clip_view_textView)).setText(word);
					((ImageView) mView.findViewById(R.id.clip_view_imageView_fav)).setImageResource(Model.getInstance(ClipManager.this).isFavouriteWord(word) ? R.drawable.icon_fav_yes : R.drawable.icon_fav_no);
					mView.findViewById(R.id.clip_view_imageView_exit).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							removeView();
						}
					});
					mView.findViewById(R.id.clip_view_imageView_fav).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							((ImageView) mView.findViewById(R.id.clip_view_imageView_fav)).setImageResource(Model.getInstance(ClipManager.this).setFavourite(Utils.getWordOfClipManager(ClipManager.this)) ? R.drawable.icon_fav_yes : R.drawable.icon_fav_no);
						}
					});
					mView.findViewById(R.id.clip_view_imageView_appIcon).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(ClipManager.this, MainActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.putExtra(MainActivity.EXTRA, Utils.getWordOfClipManager(ClipManager.this));
							ClipManager.this.startActivity(intent);
							removeView();
						}
					});
					final WebView webView = (WebView) mView.findViewById(R.id.clip_view_webView);
					webView.getSettings().setJavaScriptEnabled(true);
					webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
					webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
					webView.getSettings().setLightTouchEnabled(false);
					webView.setHorizontalScrollBarEnabled(false);
					webView.setWebViewClient(new WebViewClient() {
						@Override
						public boolean shouldOverrideUrlLoading(WebView webView, String url) {
							url = url.replace("http://", "").replace("/", "").trim().toUpperCase(Locale.ENGLISH);
							url = HtmlParserHelper.getHrefLink(url).toUpperCase(Locale.ENGLISH).trim();
							if (DicDataBase.getInstance(ClipManager.this).isExist(url)) {
								Utils.setWordOfClipManager(ClipManager.this, url);
								webView.loadDataWithBaseURL(null, DicDataBase.getInstance(ClipManager.this).getMeaningFromDataBase(Utils.getWordOfClipManager(ClipManager.this), true), "text/html", "utf-8", null);
								((TextView) mView.findViewById(R.id.clip_view_textView)).setText(Utils.getWordOfClipManager(ClipManager.this));
								((ImageView) mView.findViewById(R.id.clip_view_imageView_fav)).setImageResource(Model.getInstance(ClipManager.this).isFavouriteWord(Utils.getWordOfClipManager(ClipManager.this)) ? R.drawable.icon_fav_yes : R.drawable.icon_fav_no);
							} else {
								Toast.makeText(ClipManager.this, "Word not exist, search online", Toast.LENGTH_SHORT).show();
							}
							return true;
						}
					});
					webView.loadDataWithBaseURL(null, DicDataBase.getInstance(this).getMeaningFromDataBase(word, true), "text/html", "utf-8", null);
					mWindowManager.addView(mView, mParamsCallReminder);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to remove the window view
	 */
	private void removeView() {
		if (mView != null && mView.getParent() != null) {
			mWindowManager.removeView(mView);
		}
	}

}
