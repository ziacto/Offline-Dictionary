package com.invincible.offlinedictionary.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.invincible.offlinedictionary.ClipManager;
import com.invincible.offlinedictionary.Utils;

public class DBInitializer extends AsyncTask<String, Void, Void> {
	private ProgressDialog				dialog;
	private Context						mContext;
	private OnInitializingTaskComplete	mOnInitializingTaskComplete;

	public DBInitializer(Context context, OnInitializingTaskComplete onInitializingTaskComplete) {
		mContext = context;
		mOnInitializingTaskComplete = onInitializingTaskComplete;
		dialog = new ProgressDialog(context);
		dialog.setTitle("Wait !!");
		dialog.setMessage("Initializing App for first time.");
		dialog.setCancelable(false);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		try {
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Void doInBackground(String... params) {
		SQLiteDatabase database = mContext.openOrCreateDatabase(DicDataBase.DATA_BASE_WORD_INDEX, Context.MODE_PRIVATE, null);
		File path = mContext.getDatabasePath(DicDataBase.DATA_BASE_WORD_INDEX);
		FileOutputStream fout = null;
		InputStream fin = null;
		try {
			fin = new InflaterInputStream(mContext.getAssets().open(DicDataBase.DATA_BASE_WORD_INDEX));
			fout = new FileOutputStream(path);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = fin.read(buffer)) > 0) {
				fout.write(buffer, 0, len);
			}
			fout.flush();
			SharedPreferences sharedPreferences = mContext.getSharedPreferences(DicDataBase.SHARED_PREFFERENCE, Context.MODE_PRIVATE);
			Editor edit = sharedPreferences.edit();
			edit.putBoolean(DicDataBase.SHARED_PREFFERENCE, true);
			edit.commit();
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("MKR", "DBInitializer.doInBackground()  " + e.getMessage());
		} finally {
			try {
				fout.close();
				fin.close();
				if (database != null) {
					database.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		try {
			if (mOnInitializingTaskComplete != null) {
				Utils.initilizeWordOfTheDayService(mContext);
				if (Utils.isWordSnitcherActive(mContext)) {
					mContext.startService(new Intent(mContext, ClipManager.class));
				}
				mOnInitializingTaskComplete.onInitializingTaskComplete();
			}
			dialog.dismiss();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Callback to notify that the initialization task is being completed
	 * 
	 * @author THE-MKR
	 * 
	 */
	public interface OnInitializingTaskComplete {
		/**
		 * Callback to notify that the initialization task is being completed, it run on UI-Thread
		 */
		public void onInitializingTaskComplete();
	}
}
