package com.invincible.offlinedictionary.view;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.invincible.offlinedictionary.ClipManager;
import com.invincible.offlinedictionary.Promotion;
import com.invincible.offlinedictionary.R;
import com.invincible.offlinedictionary.Utils;
import com.invincible.offlinedictionary.database.DBInitializer;
import com.invincible.offlinedictionary.database.DBInitializer.OnInitializingTaskComplete;
import com.invincible.offlinedictionary.database.DicDataBase;
import com.invincible.offlinedictionary.database.Model;
import com.invincible.offlinedictionary.view.FragmentDictionary.OnFragmentDictionaryListener;

public class MainActivity extends Activity implements OnClickListener, OnInitializingTaskComplete, OnFragmentDictionaryListener, OnFragmentListener, OnInitListener {

	private Fragment			mFragmentCurrent;
	public static final String	EXTRA			= "MainActivity.EXTRA";
	public static final String	EXTRA_SNITCHER	= "MainActivity.EXTRA_SNITCHER";
	private TextToSpeech		mTextToSpeek;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity);
		mTextToSpeek = new TextToSpeech(this, this);
		mTextToSpeek.setLanguage(Locale.ENGLISH);
		findViewById(R.id.sliding_menu_layout_word_snitcher).setOnClickListener(this);
		findViewById(R.id.sliding_menu_textView_back_key).setOnClickListener(this);
		findViewById(R.id.sliding_menu_textView_speaker).setOnClickListener(this);
		findViewById(R.id.sliding_menu_textView_custom_view).setOnClickListener(this);
		findViewById(R.id.sliding_menu_textView_favourite).setOnClickListener(this);
		findViewById(R.id.sliding_menu_textView_feedback).setOnClickListener(this);
		findViewById(R.id.sliding_menu_textView_font_size).setOnClickListener(this);
		findViewById(R.id.sliding_menu_textView_history).setOnClickListener(this);
		findViewById(R.id.sliding_menu_textView_history_line).setOnClickListener(this);
		findViewById(R.id.sliding_menu_textView_more_apps).setOnClickListener(this);
		findViewById(R.id.sliding_menu_textView_rate_us).setOnClickListener(this);
		findViewById(R.id.sliding_menu_textView_share_app).setOnClickListener(this);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		((CheckBox) findViewById(R.id.sliding_menu_checkBox_word_snitcher)).setChecked(Utils.isWordSnitcherActive(this));
		if (!getSharedPreferences(DicDataBase.SHARED_PREFFERENCE, Context.MODE_PRIVATE).getBoolean(DicDataBase.SHARED_PREFFERENCE, false)) {
			try {
				new DBInitializer(this, this).execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			onInitializingTaskComplete();
		}
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
		if (drawerLayout.isDrawerVisible(findViewById(R.id.activity_main_hide_layout))) {
			drawerLayout.closeDrawer(findViewById(R.id.activity_main_hide_layout));
			return;
		}
		if (mFragmentCurrent instanceof OnBackPressed && ((OnBackPressed) mFragmentCurrent).onBackPressed()) {
			return;
		}
		if (Promotion.isAskingReview(this, 4, true)) {
			showReviewDialog();
		} else if (Promotion.isReviewDone(this) && Promotion.isAskingShare(this)) {
			showShareDialog();
		} else {
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		if (mTextToSpeek != null) {
			mTextToSpeek.stop();
			mTextToSpeek.shutdown();
		}
		super.onDestroy();
	}

	@Override
	public void onInitializingTaskComplete() {
		Toast.makeText(this, "If there is any problem then clear the cache", Toast.LENGTH_SHORT).show();
		Intent intent = getIntent();
		if (intent != null && intent.getStringExtra(EXTRA) != null) {
			String word = intent.getStringExtra(EXTRA).toUpperCase(Locale.ENGLISH);
			ArrayList<String> wordList = DicDataBase.getInstance(this).getWordList(word);
			FragmentDictionary fragmentDictionary = new FragmentDictionary();
			Bundle bundle = new Bundle();
			bundle.putInt(FragmentDictionary.EXTRA_INDEX, wordList.indexOf(word));
			bundle.putStringArrayList(FragmentDictionary.EXTRA_ARRAY_LIST, wordList);
			fragmentDictionary.setArguments(bundle);
			getFragmentManager().beginTransaction().replace(R.id.main_view_container, mFragmentCurrent = fragmentDictionary).commit();
		} else {
			getFragmentManager().beginTransaction().replace(R.id.main_view_container, mFragmentCurrent = new FragmentDictionary()).commit();
		}
		// ON OFF SNITCHER
		((CheckBox) findViewById(R.id.sliding_menu_checkBox_word_snitcher)).setChecked(Utils.isWordSnitcherActive(this));
		if (intent != null && intent.getBooleanExtra(EXTRA_SNITCHER, false) && Utils.isWordSnitcherActive(this)) {
			showWordSnitcherDialog();
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.sliding_menu_layout_word_snitcher:
			if (Utils.isWordSnitcherActive(this)) {
				showWordSnitcherDialog();
			} else {
				startSnitcher();
			}
			break;
		case R.id.sliding_menu_textView_speaker:
			showSpeakerOperationDialog();
			break;
		case R.id.sliding_menu_textView_back_key:
			showBackKeyOperationDialog();
			break;
		case R.id.sliding_menu_textView_custom_view:
			showCustomViewDialog();
			break;
		case R.id.sliding_menu_textView_favourite:
			getFragmentManager().beginTransaction().replace(R.id.main_view_container, mFragmentCurrent = new FragmentFavouriteWordList()).commit();
			onFragmentDictionaryCloseSlidingMenu();
			break;
		case R.id.sliding_menu_textView_history:
			getFragmentManager().beginTransaction().replace(R.id.main_view_container, mFragmentCurrent = new FragmentHistoryWordList()).commit();
			onFragmentDictionaryCloseSlidingMenu();
			break;
		case R.id.sliding_menu_textView_history_line:
			showHistoryItemSizeDialog();
			break;
		case R.id.sliding_menu_textView_font_size:
			showFontSize();
			break;
		case R.id.sliding_menu_textView_feedback:
			Promotion.sendFeedback(this);
			break;
		case R.id.sliding_menu_textView_more_apps:
			Promotion.getMoreApps(this);
			break;
		case R.id.sliding_menu_textView_rate_us:
			Promotion.sendReview(this);
			break;
		case R.id.sliding_menu_textView_share_app:
			Promotion.shareApp(this);
			break;
		}
	}

	@Override
	public void onFragmentDictionaryCloseSlidingMenu() {
		DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
		if (drawerLayout.isDrawerVisible(findViewById(R.id.activity_main_hide_layout))) {
			drawerLayout.closeDrawer(findViewById(R.id.activity_main_hide_layout));
		}
	}

	@Override
	public void onFragmentDictionaryToggleSlidingMenu() {
		DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
		if (drawerLayout.isDrawerVisible(findViewById(R.id.activity_main_hide_layout))) {
			drawerLayout.closeDrawer(findViewById(R.id.activity_main_hide_layout));
		} else {
			drawerLayout.openDrawer(findViewById(R.id.activity_main_hide_layout));
		}
	}

	@Override
	public void onFragmentWordListCloseSlidingMenu() {
		DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
		if (drawerLayout.isDrawerVisible(findViewById(R.id.activity_main_hide_layout))) {
			drawerLayout.closeDrawer(findViewById(R.id.activity_main_hide_layout));
		}
	}

	@Override
	public void onFragmentWordListToggleSlidingMenu() {
		DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
		if (drawerLayout.isDrawerVisible(findViewById(R.id.activity_main_hide_layout))) {
			drawerLayout.closeDrawer(findViewById(R.id.activity_main_hide_layout));
		} else {
			drawerLayout.openDrawer(findViewById(R.id.activity_main_hide_layout));
		}
	}

	@Override
	public void onFragmentWordListShowDictionary() {
		getFragmentManager().beginTransaction().replace(R.id.main_view_container, mFragmentCurrent = new FragmentDictionary()).commit();
	}

	@Override
	public void onFragmentWordListShowDictionary(ArrayList<String> wordList, int index) {
		FragmentDictionary fragmentDictionary = new FragmentDictionary();
		Bundle bundle = new Bundle();
		bundle.putBoolean(FragmentDictionary.EXTRA_FAV_HIST_LIST, true);
		bundle.putInt(FragmentDictionary.EXTRA_INDEX, index);
		bundle.putStringArrayList(FragmentDictionary.EXTRA_ARRAY_LIST, wordList);
		fragmentDictionary.setArguments(bundle);
		getFragmentManager().beginTransaction().replace(R.id.main_view_container, mFragmentCurrent = fragmentDictionary).commit();
	}

	@Override
	public void onFragmentWordListSpeekWord(String word) {
		mTextToSpeek.speak(Utils.isSpeekWordOnly(this) ? word : DicDataBase.getInstance(this).getMeaningFromDataBase(word, false), TextToSpeech.QUEUE_FLUSH, null);
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
		} else {
			Intent installIntent = new Intent();
			installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
			startActivity(installIntent);
		}
	}

	/**
	 * Method to start word snitcher service
	 */
	private void startSnitcher() {
		Utils.startWordSnitcher(MainActivity.this);
		((CheckBox) findViewById(R.id.sliding_menu_checkBox_word_snitcher)).setChecked(true);
		startService(new Intent(MainActivity.this, ClipManager.class));
	}

	/**
	 * Method to start word snitcher service
	 */
	private void stopSnitcher() {
		Utils.stopWordSnitcher(MainActivity.this);
		((CheckBox) findViewById(R.id.sliding_menu_checkBox_word_snitcher)).setChecked(false);
		stopService(new Intent(MainActivity.this, ClipManager.class));
	}

	// ===============================================================================================================================
	// DIALOGS
	// ===============================================================================================================================

	/**
	 * Method top show feedback dialog
	 */
	private void showShareDialog() {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setTitle(R.string.dialog_title_share);
			builder.setMessage(R.string.promotion_share_dialog);
			builder.setPositiveButton(R.string.dialog_button_share, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Promotion.shareApp(MainActivity.this);
					dialog.dismiss();
				}
			});
			builder.setNegativeButton(R.string.dialog_button_quit, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					finish();
				}
			});
			builder.create().show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method top show review dialog
	 */
	private void showReviewDialog() {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setTitle(R.string.dialog_title_review);
			builder.setMessage(R.string.promotion_reivew);
			builder.setPositiveButton(R.string.dialog_button_rate, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Promotion.sendReview(MainActivity.this);
					dialog.dismiss();
				}
			});
			builder.setNegativeButton(R.string.dialog_button_quit, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					finish();
				}
			});
			builder.create().show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method top show word snitcher dialog
	 */
	private void showWordSnitcherDialog() {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setTitle(R.string.dialog_title_word_snitcher);
			builder.setMessage(R.string.dialog_messang_word_snitcher);
			builder.setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					stopSnitcher();
					dialog.dismiss();
				}
			});
			builder.setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method top show font size dialog
	 */
	private void showFontSize() {
		try {
			final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setTitle(R.string.dialog_title_font_size);
			View parentView = getLayoutInflater().inflate(R.layout.dialog_seek, null);
			builder.setView(parentView);
			final SeekBar seek = (SeekBar) parentView.findViewById(R.id.dialog_seek_seekBar);
			final TextView text = (TextView) parentView.findViewById(R.id.dialog_seek_textView);
			seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {

				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {

				}

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
					text.setText("" + (progress + 50) + " %");
				}
			});
			seek.setProgress(50);
			builder.setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Utils.setTextSize(builder.getContext(), seek.getProgress() + 50);

					dialog.dismiss();
				}
			});
			builder.setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					dialog.dismiss();
				}
			});
			builder.create().show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method top show back key operation dialog
	 */
	private void showBackKeyOperationDialog() {
		try {
			final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setTitle(R.string.dialog_title_back_key);
			builder.setSingleChoiceItems(Utils.BACK_KEY_OPERATION_ARRAY, Utils.getBackKeyOperationIndex(builder.getContext()), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Utils.setBackKeyOperation(builder.getContext(), which);
					dialog.dismiss();
				}
			});
			builder.create().show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method top show speaker operation dialog
	 */
	private void showSpeakerOperationDialog() {
		try {
			final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setTitle(R.string.dialog_title_Speak);
			builder.setSingleChoiceItems(Utils.SPEEKER_OPERATION_ARRAY, Utils.isSpeekWordOnly(MainActivity.this) ? 0 : 1, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Utils.setSpeekOperation(builder.getContext(), which);
					dialog.dismiss();
				}
			});
			builder.create().show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method top show History item dialog
	 */
	private void showHistoryItemSizeDialog() {
		try {
			final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setTitle(R.string.dialog_title_history_size);
			builder.setSingleChoiceItems(Utils.HISTORY_ITEM_STRING_ARRAY, Utils.getHistoryItemSizeIndex(builder.getContext()), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Utils.setHistoryItemSize(builder.getContext(), which);
					Model.getInstance(builder.getContext()).setHistoryItemSize(Utils.getHistoryItemSize(builder.getContext()));
					dialog.dismiss();
				}
			});
			builder.create().show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method top show custom view dialog
	 */
	private void showCustomViewDialog() {
		try {
			final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setTitle(R.string.dialog_title_history_size);
			builder.setMultiChoiceItems(Utils.CUSTOM_VIEW_ARRAY, Utils.getCustomViewCheckedItemArray(builder.getContext()), new DialogInterface.OnMultiChoiceClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which, boolean isChecked) {
					Utils.setCustomViewChecked(builder.getContext(), which, isChecked);
					if (mFragmentCurrent != null & mFragmentCurrent instanceof OnRefreshList) {
						((OnRefreshList) mFragmentCurrent).onRefreshList();
					}
				}
			});
			builder.create().show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Call Back to notify Back Pressed event
	 */
	public interface OnBackPressed {
		/**
		 * Method called when back pressed event Occur
		 * 
		 * @return True if want to block back event otherwise False
		 */
		public boolean onBackPressed();
	}

	/**
	 * Call Back to notify to refresh list
	 */
	public interface OnRefreshList {
		/**
		 * Method to refresh list
		 */
		public void onRefreshList();
	}

}
