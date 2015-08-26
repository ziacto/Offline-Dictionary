package com.invincible.offlinedictionary.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.invincible.offlinedictionary.Utils;

public class Model implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private static final String	FILE_NAME			= "DICTIONARY";
	private static Model		mModel;
	private static Context		mContext;
	private ArrayList<String>	mFavoriteWords;
	private ArrayList<String>	mHistoryWords;

	// CONSTRUCTOR
	private Model() {
		mFavoriteWords = new ArrayList<String>();
		mHistoryWords = new ArrayList<String>();
	}

	/**
	 * Get Model Instance
	 * 
	 * @param ctx
	 * @return
	 */
	public static Model getInstance(Context ctx) {
		mContext = ctx;
		if (mModel == null) {
			mModel = getsavedInstance();
			if (mModel == null) {
				mModel = new Model();
				mModel.saveInstance();
			}
			return mModel;
		}
		return mModel;
	}

	/**
	 * Method to save Model Instance
	 * 
	 * @return
	 */
	private static Model getsavedInstance() {
		try {
			FileInputStream fin = mContext.openFileInput(FILE_NAME);
			ObjectInputStream in = new ObjectInputStream(fin);
			return (Model) in.readObject();
		} catch (FileNotFoundException e) {
			Log.e("INVINCIBLE", "Model.getsavedInstance() " + e.getMessage());
			e.printStackTrace();
			return null;
		} catch (StreamCorruptedException e) {
			Log.e("INVINCIBLE", "Model.getsavedInstance() " + e.getMessage());
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			Log.e("INVINCIBLE", "Model.getsavedInstance() " + e.getMessage());
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			Log.e("INVINCIBLE", "Model.getsavedInstance() " + e.getMessage());
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			Log.e("INVINCIBLE", "Model.getsavedInstance() " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Save Instance into file
	 * 
	 * @return
	 */
	public boolean saveInstance() {
		try {
			FileOutputStream fos = mContext.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(this);
			out.flush();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.e("INVINCIBLE", "Model.saveInstance() " + e.getMessage());
			return false;
		} catch (IOException e) {
			Log.e("INVINCIBLE", "Model.saveInstance() " + e.getMessage());
			return false;
		}
	}

	/**
	 * Method to word set as favourite
	 * 
	 * @param word
	 * @return TRUE if word add in Fav list, FALSE if removed from Fav list
	 */
	public boolean setFavourite(String word) {
		if (mFavoriteWords.contains(word)) {
			mFavoriteWords.remove(word);
			saveInstance();
			return false;
		}
		mFavoriteWords.add(word);
		saveInstance();
		return true;
	}

	/**
	 * Reomve list from fav word list
	 * 
	 * @param list
	 */
	public void removeFav(ArrayList<String> list) {
		mFavoriteWords.removeAll(list);
		saveInstance();
	}

	/**
	 * Method to check weather the word is exist in Fav word list or not
	 * 
	 * @param word
	 * @return TRUE if word add in Fav list, FALSE if removed from Fav list
	 */
	public boolean isFavouriteWord(String word) {
		return mFavoriteWords.contains(word);
	}

	/**
	 * Get the list of history
	 * 
	 * @return
	 */
	public ArrayList<String> getFavoriteWords() {
		return mFavoriteWords;
	}

	/**
	 * Method to word set History
	 * 
	 * @param word
	 */
	public void setHistory(Context context, String word) {
		mHistoryWords.remove(word);
		mHistoryWords.add(0, word);
		if (mHistoryWords.size() > Utils.getHistoryItemSize(context)) {
			try {
				mHistoryWords.remove(mHistoryWords.size() - 1);
			} catch (Exception e) {
				Log.e("INVINCIBLE", "Model.setHistory() " + e.getMessage());
			}
		}
		saveInstance();
	}

	/**
	 * Get the list of history Words
	 * 
	 * @return
	 */
	public ArrayList<String> getHistoryWords() {
		return mHistoryWords;
	}

	/**
	 * Reomve list from history word list
	 * 
	 * @param list
	 */
	public void removeHistory(ArrayList<String> list) {
		mHistoryWords.removeAll(list);
		saveInstance();
	}

	/**
	 * Method to set the size of the history items
	 * 
	 * @param historyItemSize
	 */
	public void setHistoryItemSize(int historyItemSize) {
		while (mHistoryWords.size() > historyItemSize) {
			try {
				mHistoryWords.remove(mHistoryWords.size() - 1);
			} catch (Exception e) {
				Log.e("INVINCIBLE", "Model.setHistory() " + e.getMessage());
			}
		}
	}
}
