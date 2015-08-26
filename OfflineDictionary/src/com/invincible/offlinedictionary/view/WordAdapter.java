package com.invincible.offlinedictionary.view;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.invincible.offlinedictionary.R;

public class WordAdapter extends BaseAdapter {
	private LayoutInflater		mLayoutInflater;
	private ArrayList<String>	mWordList;
	private ArrayList<String>	mSelectedWords;

	public WordAdapter(Context context) {
		mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mWordList = new ArrayList<String>();
		mSelectedWords = new ArrayList<String>();
	}

	/**
	 * Method to set the new wordlist
	 * 
	 * @param wordList
	 */
	public void setWordList(ArrayList<String> wordList) {
		mWordList.clear();
		mWordList.addAll(wordList);
		mSelectedWords.clear();
		notifyDataSetChanged();
	}

	/**
	 * Method to get the word list
	 * 
	 * @return
	 */
	public ArrayList<String> getWordList() {
		return mWordList;
	}

	/**
	 * Method to get the list of selected words
	 * 
	 * @return
	 */
	public ArrayList<String> getSelectedWords() {
		return mSelectedWords;
	}

	/**
	 * Method to selece all words
	 */
	public void selectAll() {
		mSelectedWords.clear();
		mSelectedWords.addAll(mWordList);
		notifyDataSetChanged();
	}

	/**
	 * Method to selece all words
	 */
	public void unselectAll() {
		mSelectedWords.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mWordList.size();
	}

	@Override
	public String getItem(int position) {
		return mWordList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.list_item, null);
		}
		final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.list_item_checkbox);
		if (mSelectedWords.contains(getItem(position))) {
			checkBox.setChecked(true);
		} else {
			checkBox.setChecked(false);
		}
		convertView.findViewById(R.id.list_item_layout_checkbox).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mSelectedWords.contains(getItem(position))) {
					mSelectedWords.remove(getItem(position));
					checkBox.setChecked(false);
				} else {
					mSelectedWords.add(getItem(position));
					checkBox.setChecked(true);
				}
			}
		});
		((TextView) convertView.findViewById(R.id.list_item_textView)).setText(mWordList.get(position));
		return convertView;
	}
}