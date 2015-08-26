package com.invincible.offlinedictionary.database;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Locale;
import java.util.zip.GZIPInputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.invincible.offlinedictionary.database.parse.HtmlParserHelper;
import com.invincible.offlinedictionary.database.parse.Length;
import com.invincible.offlinedictionary.database.parse.Page;

public class DicDataBase extends SQLiteOpenHelper {
	public static final String								SHARED_PREFFERENCE		= "ShearedPrefference";
	public static final String								DATA_BASE_WORD_INDEX	= "WordIndex";
	private static final String								TABLE_WORD_ALPHA		= "table_";
	private static final String								COLLUMN_WORD			= "word";
	private static final String								COLLUMN_BYTE_SKIP		= "skipByte";
	private static final String								COLLUMN_MEANING_INDEX	= "meaning";
	private static Hashtable<Character, ArrayList<String>>	ALL_WORD_LIST			= new Hashtable<Character, ArrayList<String>>();
	private static DicDataBase								mDicDataBaseHelper;
	private static Context									mContext;
	private Hashtable<String, String>						CONST_PRONOUNCIATION;

	/**
	 * Get the instance of DataHelper
	 * 
	 * @param context
	 * @return
	 */
	public static DicDataBase getInstance(Context context) {
		mContext = context;
		if (mDicDataBaseHelper == null) {
			mDicDataBaseHelper = new DicDataBase(context);
		}
		return mDicDataBaseHelper;
	}

	private DicDataBase(Context context) {
		super(context, DATA_BASE_WORD_INDEX, null, 1);
		CONST_PRONOUNCIATION = new Hashtable<String, String>();
		CONST_PRONOUNCIATION.put("AU", "Australia");
		CONST_PRONOUNCIATION.put("RP", "Received Pronunciation");
		CONST_PRONOUNCIATION.put("GA", "General American");
		CONST_PRONOUNCIATION.put("AuE", "Australian English");
		CONST_PRONOUNCIATION.put("CaE", "Canadian English");
		CONST_PRONOUNCIATION.put("CanE", "Canadian English");
		CONST_PRONOUNCIATION.put("Cdn", "Canadian");
		CONST_PRONOUNCIATION.put("CDN", "Canadian");
		CONST_PRONOUNCIATION.put("BE", "British English");
		CONST_PRONOUNCIATION.put("BrE", "British English");
		CONST_PRONOUNCIATION.put("BrEn", "British English");
		CONST_PRONOUNCIATION.put("BR-en", "British English");
		CONST_PRONOUNCIATION.put("EN-br", "British English");
		CONST_PRONOUNCIATION.put("Brit", "British English");
		CONST_PRONOUNCIATION.put("en-gb", "British English");
		CONST_PRONOUNCIATION.put("GenAm", "General American");
		CONST_PRONOUNCIATION.put("GnAm", "General American");
		CONST_PRONOUNCIATION.put("InE", "Indian English");
		CONST_PRONOUNCIATION.put("IrE", "Irish English");
		CONST_PRONOUNCIATION.put("NZE", "New Zealand English");
		CONST_PRONOUNCIATION.put("ScE", "Scottish English");
		CONST_PRONOUNCIATION.put("SAE", "South African English");
		CONST_PRONOUNCIATION.put("SSE", "Standard Singapore English");
		CONST_PRONOUNCIATION.put("WaE", "Welsh English");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (char i = 'A'; i <= 'Z'; i++) {
			db.execSQL("Create table " + TABLE_WORD_ALPHA + i + "(" + COLLUMN_WORD + " VARCHAR PRIMARY KEY, " + COLLUMN_BYTE_SKIP + " INTEGER, " + COLLUMN_MEANING_INDEX + " INTEGER)");
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	/**
	 * Method to get the meaning of a word
	 * 
	 * @param context
	 * @param word
	 *            Word whose meaning want to search
	 * @param isHtmlForm
	 *            If TRUE return Html data else Text data
	 * @return Return the meaning of word, if not exist 0 length String is
	 *         returned, But the meaning need to be parsed
	 */
	public String getMeaningFromDataBase(String word, boolean isHtmlForm) {
		String str = "";
		try {
			if (word != null && word.trim().length() > 0) {
				word = getActualWord(word).toUpperCase(Locale.ENGLISH);
				Model.getInstance(mContext).setHistory(mContext, word);
				SQLiteDatabase database = getWritableDatabase();
				Cursor sql = null;
				char suffix = word.charAt(0);
				if (suffix >= 'A' && suffix <= 'Z') {
					sql = database.query((TABLE_WORD_ALPHA + suffix), null, COLLUMN_WORD + " = ?", new String[] { word }, null, null, null, null);
				}
				if (sql.moveToNext()) {
					int byteSkiped = sql.getInt(sql.getColumnIndex(COLLUMN_BYTE_SKIP));
					int meaningIndex = sql.getInt(sql.getColumnIndex(COLLUMN_MEANING_INDEX));
					try {
						InputStream inputStreem = mContext.getAssets().open("db.jet");
						inputStreem.skip(byteSkiped);
						byte[] byteStreem = new byte[Length.getNumberOfByte(byteSkiped)];
						inputStreem.read(byteStreem);
						final int BUFFER_SIZE = 512;
						ByteArrayInputStream is = new ByteArrayInputStream(byteStreem);
						GZIPInputStream gis = new GZIPInputStream(is, BUFFER_SIZE);
						StringBuilder string = new StringBuilder();
						byte[] data = new byte[BUFFER_SIZE];
						int bytesRead;
						while ((bytesRead = gis.read(data)) != -1) {
							string.append(new String(data, 0, bytesRead));
						}
						gis.close();
						is.close();
						str = string.toString().split(HtmlParserHelper.REGEX_DEFFINETION_SPLITTER)[meaningIndex];
						str = parseText(str, word, isHtmlForm);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				sql.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * Method to know weather the word is exist or not
	 * 
	 * @param word
	 *            Word whose existence is checked
	 * @return TRUE if exist else FALSE
	 */
	public boolean isExist(String word) {
		boolean isExist = false;
		try {
			if (word != null && word.trim().length() > 0) {
				word = getActualWord(word).toUpperCase(Locale.ENGLISH);
				SQLiteDatabase database = getWritableDatabase();
				Cursor sql = null;
				char suffix = word.charAt(0);
				if (suffix >= 'A' && suffix <= 'Z') {
					sql = database.query((TABLE_WORD_ALPHA + suffix), null, COLLUMN_WORD + " = ?", new String[] { word }, null, null, null, null);
				}
				if (sql.moveToNext()) {
					isExist = true;
				}
				sql.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isExist;
	}

	/**
	 * Method to get multiple words as per word pattern
	 * 
	 * @param patternWord
	 * @param limit
	 * @return WordPattern List
	 */
	public ArrayList<String> getPattern(String patternWord, int limit) {
		ArrayList<String> words = new ArrayList<String>();
		try {
			if (patternWord != null && patternWord.trim().length() > 0) {
				patternWord = patternWord.toUpperCase(Locale.ENGLISH);
				SQLiteDatabase database = getWritableDatabase();
				Cursor sql = null;
				char suffix = patternWord.charAt(0);
				if (suffix >= 'A' && suffix <= 'Z') {
					sql = database.query((TABLE_WORD_ALPHA + suffix), new String[] { COLLUMN_WORD }, COLLUMN_WORD + " LIKE  ?", new String[] { patternWord + "%" }, null, null, null, "" + limit);
				}
				int columnIndex = sql.getColumnIndex(COLLUMN_WORD);
				while (sql.moveToNext()) {
					words.add(sql.getString(columnIndex).trim());
				}
				// SORT DATA
				sql.close();
				if (words.size() > 0) {
					Collections.sort(words, new Comparator<String>() {
						@Override
						public int compare(String lhs, String rhs) {
							return lhs.trim().compareToIgnoreCase(rhs.trim());
						}
					});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return words;
	}

	/**
	 * Method to get multiple words as per word pattern
	 * 
	 * @param patternWord
	 * @return Near about words
	 */
	public ArrayList<String> getWordList(String patternWord) {
		try {
			if (patternWord != null && patternWord.trim().length() > 0) {
				patternWord = getActualWord(patternWord).toUpperCase(Locale.ENGLISH);
				char c = patternWord.charAt(0);
				if (!ALL_WORD_LIST.contains(c)) {
					SQLiteDatabase database = getWritableDatabase();
					Cursor sql = null;
					char suffix = patternWord.charAt(0);
					if (suffix >= 'A' && suffix <= 'Z') {
						sql = database.query((TABLE_WORD_ALPHA + suffix), new String[] { COLLUMN_WORD }, null, null, null, null, COLLUMN_WORD + " ASC");
					}
					int columnIndex = sql.getColumnIndex(COLLUMN_WORD);
					ArrayList<String> words = new ArrayList<String>();
					while (sql.moveToNext()) {
						words.add(sql.getString(columnIndex).trim());
					}
					ALL_WORD_LIST.put(c, words);
				}
				return ALL_WORD_LIST.get(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}

	/**
	 * Method to parse Raw Data in Html Format
	 * 
	 * @param data
	 *            Raw Data that contain <> as line breaker
	 * @return
	 */
	private String parseText(String data, String word, boolean isHtmlData) {
		String[] splitedLines = data.split(HtmlParserHelper.REGEX_LINE_SPLITTER);
		Page page = new Page(mContext, word);
		for (String line : splitedLines) {
			page.addDataLine(line);
		}
		return isHtmlData ? page.getDataInHtmlForm() : page.getDataInTextForm();
	}

	/**
	 * Method to get the actual word
	 * 
	 * @param word
	 * @return
	 */
	private String getActualWord(String word) {
		if (CONST_PRONOUNCIATION.containsKey(word)) {
			String newWord = CONST_PRONOUNCIATION.get(word);
			return isExist(newWord) ? newWord : word;
		}
		return word;
	}
}
