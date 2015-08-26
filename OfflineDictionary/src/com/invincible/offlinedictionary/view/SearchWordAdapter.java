package com.invincible.offlinedictionary.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.invincible.offlinedictionary.R;

public class SearchWordAdapter extends BaseAdapter {
	private ArrayList<String>	mWordList;
	private Context				mContext;

	public SearchWordAdapter(Context context) {
		mWordList = new ArrayList<String>();
		mContext = context;
	}

	/**
	 * Method to update the word list
	 * 
	 * @param wordList
	 */
	public void setData(ArrayList<String> wordList) {
		mWordList.clear();
		mWordList.addAll(wordList);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			TextView textView = new TextView(mContext);
			textView.setTextColor(Color.WHITE);
			textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimensionPixelSize(R.dimen.text_size));
			int padding = mContext.getResources().getDimensionPixelSize(R.dimen.search_item_padding);
			textView.setPadding(padding, padding, padding, padding);
			convertView = textView;
		}
		((TextView) convertView).setText(getItem(position));
		return convertView;
	}
}