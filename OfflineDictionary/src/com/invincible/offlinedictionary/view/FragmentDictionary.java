package com.invincible.offlinedictionary.view;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.invincible.offlinedictionary.R;
import com.invincible.offlinedictionary.Utils;
import com.invincible.offlinedictionary.database.DicDataBase;
import com.invincible.offlinedictionary.database.Model;
import com.invincible.offlinedictionary.database.parse.HtmlParserHelper;
import com.invincible.offlinedictionary.view.MainActivity.OnBackPressed;
import com.invincible.offlinedictionary.view.MainActivity.OnRefreshList;

public class FragmentDictionary extends Fragment implements OnClickListener, OnBackPressed, OnItemClickListener, AnimationListener, OnRefreshList {
	public static String		EXTRA_FAV_HIST_LIST	= "EXTRA_FAV_HIST_LIST";
	public static String		EXTRA_INDEX			= "EXTRA_INDEX";
	public static String		EXTRA_ARRAY_LIST	= "EXTRA_ARRAY_LIST";
	private static final int	SPEEK_TO_TEXT		= 1000;
	private SearchView			mSearchView;
	private ArrayList<String>	mWordList;
	private ArrayList<String>	mArrayListWordSearched;
	private String				mWordAudioText		= "", mWordMeaning;
	private SearchWordAdapter	mWordAdapter;
	private boolean				mIsBackPressed;
	private boolean				mIsFavHisList;
	private Animation			mAnimationShow, mAnimationHide, mAnimationRotateOpen, mAnimationRotateClose;
	private boolean				mIsAnimationRunning;

	public FragmentDictionary() {
		int duration = 50;
		mAnimationShow = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
		mAnimationShow.setDuration(duration);
		mAnimationShow.setFillAfter(true);
		mAnimationShow.setAnimationListener(this);
		mAnimationHide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
		mAnimationHide.setDuration(duration);
		mAnimationHide.setFillAfter(true);
		mAnimationHide.setAnimationListener(this);
		mAnimationRotateOpen = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
		mAnimationRotateOpen.setDuration(3 * duration);
		mAnimationRotateOpen.setFillAfter(true);
		mAnimationRotateClose = new RotateAnimation(90, 0, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
		mAnimationRotateClose.setDuration(3 * duration);
		mAnimationRotateClose.setFillAfter(true);
		mArrayListWordSearched = new ArrayList<String>();
		mWordList = new ArrayList<String>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mWordAdapter = new SearchWordAdapter(getActivity());
		return inflater.inflate(R.layout.fragment_dictionary, container, false);
	}

	@Override
	public void onViewCreated(View parentView, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		getActivity().invalidateOptionsMenu();
		parentView.findViewById(R.id.fragment_dictionary_imageView_favourite).setOnClickListener(this);
		parentView.findViewById(R.id.fragment_dictionary_imageView_plus).setOnClickListener(this);
		parentView.findViewById(R.id.fragment_dictionary_imageView_share).setOnClickListener(this);
		parentView.findViewById(R.id.fragment_dictionary_imageView_sound).setOnClickListener(this);
		parentView.findViewById(R.id.fragment_dictionary_imageView_left_arrow).setOnClickListener(this);
		parentView.findViewById(R.id.fragment_dictionary_imageView_right_arrow).setOnClickListener(this);
		ListView listView = (ListView) parentView.findViewById(R.id.fragment_dictionary_listView_search);
		listView.setAdapter(mWordAdapter);
		listView.setOnItemClickListener(this);
		WebView webView = (WebView) parentView.findViewById(R.id.fragment_dictionary_webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webView.getSettings().setLightTouchEnabled(false);
		webView.setHorizontalScrollBarEnabled(false);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView webView, String url) {
				super.onPageFinished(webView, url);
				webView.invalidate();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView webView, String url) {
				url = url.replace("http://", "").replace("/", "").trim().toUpperCase(Locale.ENGLISH);
				url = HtmlParserHelper.getHrefLink(url).trim();
				if (DicDataBase.getInstance(getActivity()).isExist(url)) {
					mWordList.clear();
					mWordList.addAll(DicDataBase.getInstance(getActivity()).getWordList(url));
					showMeaning(url);
				} else {
					Toast.makeText(getActivity(), "Word not exist, search online", Toast.LENGTH_SHORT).show();
				}
				return true;
			}
		});
		mWordMeaning = Utils.getWordOfTheDay(getActivity());
		if (getArguments() != null) {
			Bundle arguments = getArguments();
			int index = arguments.getInt(EXTRA_INDEX, -1);
			mIsFavHisList = arguments.getBoolean(EXTRA_FAV_HIST_LIST, false);
			ArrayList<String> stringArrayList = arguments.getStringArrayList(EXTRA_ARRAY_LIST);
			if (stringArrayList != null && index >= 0) {
				mWordList.clear();
				mWordList.addAll(stringArrayList);
				showMeaning(mWordMeaning = stringArrayList.get(index));
			} else {
				mWordList.clear();
				mWordList.addAll(DicDataBase.getInstance(getActivity()).getWordList(mWordMeaning));
				showMeaning(mWordMeaning);
			}
		} else {
			mWordList.clear();
			mWordList.addAll(DicDataBase.getInstance(getActivity()).getWordList(mWordMeaning));
			showMeaning(mWordMeaning);
		}
		getView().findViewById(R.id.fragment_dictionary_imageView_favourite).setVisibility(View.INVISIBLE);
		getView().findViewById(R.id.fragment_dictionary_imageView_share).setVisibility(View.INVISIBLE);
		getView().findViewById(R.id.fragment_dictionary_imageView_sound).setVisibility(View.INVISIBLE);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_search, menu);
		final MenuItem menuItem = menu.findItem(R.id.menu_search_searchView);
		mSearchView = (SearchView) menuItem.getActionView();
		// SET EXPAND AND COLLAPS LISTENER ON SEARCH VIEW
		menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				if (getActivity() instanceof OnFragmentDictionaryListener) {
					((OnFragmentDictionaryListener) getActivity()).onFragmentDictionaryCloseSlidingMenu();
				}
				return true;
			}

			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				mSearchView.setQuery("", false);
				if (getActivity() instanceof OnFragmentDictionaryListener) {
					((OnFragmentDictionaryListener) getActivity()).onFragmentDictionaryCloseSlidingMenu();
				}
				return true;
			}
		});
		// SET QUERY TEXT LISTENER ON SEARCH VIEW
		mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				query = query.toUpperCase(Locale.ENGLISH).trim();
				mWordList.clear();
				mWordList.addAll(DicDataBase.getInstance(getActivity()).getWordList(query));
				showMeaning(query);
				mSearchView.setQuery("", false);
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				if (getView() != null) {
					newText = newText.trim();
					ListView listView = (ListView) getView().findViewById(R.id.fragment_dictionary_listView_search);
					if (newText.length() == 1) {
						int xy[] = new int[2];
						mSearchView.getLocationOnScreen(xy);
						listView.setVisibility(View.VISIBLE);
						RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) listView.getLayoutParams();
						int padding = getResources().getDimensionPixelSize(R.dimen.search_item_padding);
						layoutParams.setMargins(xy[0] + padding, 0, 0, 0);
						layoutParams.width = mSearchView.getWidth() - (padding << 1);
					}
				}
				updateWordList(newText);
				return true;
			}
		});
		// SET SEARCH TEXT COLORAND CLOSE BUTTON
		int idSearchCloseImageView = mSearchView.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
		((ImageView) mSearchView.findViewById(idSearchCloseImageView)).setImageResource(R.drawable.selector_search_cross);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_voice_detector:
			if (Utils.isNetworkAvailable(getActivity())) {
				Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getActivity().getPackageName());
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
				intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en");
				startActivityForResult(intent, SPEEK_TO_TEXT);
			}
			break;
		case android.R.id.home:
			if (getActivity() instanceof OnFragmentDictionaryListener) {
				((OnFragmentDictionaryListener) getActivity()).onFragmentDictionaryToggleSlidingMenu();
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mWordAudioText != null && mWordAudioText.trim().length() > 0) {
			if (DicDataBase.getInstance(getActivity()).isExist(mWordAudioText)) {
				mWordList.clear();
				mWordList.addAll(DicDataBase.getInstance(getActivity()).getWordList(mWordAudioText));
				showMeaning(mWordAudioText);
			} else {
				Toast.makeText(getActivity(), "Word not exist in DB.....", Toast.LENGTH_SHORT).show();
			}
			mWordAudioText = "";
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.fragment_dictionary_imageView_plus:
			if (!mIsAnimationRunning) {
				mIsAnimationRunning = true;
				View viewFav = getView().findViewById(R.id.fragment_dictionary_imageView_favourite);
				View viewSpeeker = getView().findViewById(R.id.fragment_dictionary_imageView_sound);
				if (viewFav.getVisibility() == View.VISIBLE) {
					getView().findViewById(R.id.fragment_dictionary_imageView_plus).startAnimation(mAnimationRotateClose);
					viewFav.startAnimation(mAnimationHide);
				} else {
					getView().findViewById(R.id.fragment_dictionary_imageView_plus).startAnimation(mAnimationRotateOpen);
					viewSpeeker.setVisibility(View.VISIBLE);
					viewSpeeker.startAnimation(mAnimationShow);
				}
			}

			break;
		case R.id.fragment_dictionary_imageView_favourite:
			((ImageView) view).setImageResource(Model.getInstance(getActivity()).setFavourite(mWordMeaning) ? R.drawable.icon_fav_yes : R.drawable.icon_fav_no);
			break;
		case R.id.fragment_dictionary_imageView_share:
			String data = DicDataBase.getInstance(getActivity()).getMeaningFromDataBase(mWordMeaning, false);
			break;
		case R.id.fragment_dictionary_imageView_sound:
			if (getActivity() instanceof OnFragmentListener) {
				((OnFragmentListener) getActivity()).onFragmentWordListSpeekWord(mWordMeaning);
			}
			break;
		case R.id.fragment_dictionary_imageView_left_arrow:
			try {
				showMeaning(mWordList.get(mWordList.indexOf(mWordMeaning) - 1));
			} catch (Exception e) {
				Log.e("INVINCIBLE", "FragmentDictionary.onClick(L) " + e.getMessage());
			}
			break;
		case R.id.fragment_dictionary_imageView_right_arrow:
			try {
				showMeaning(mWordList.get(mWordList.indexOf(mWordMeaning) + 1));
			} catch (Exception e) {
				Log.e("INVINCIBLE", "FragmentDictionary.onClick(R) " + e.getMessage());
			}
			break;
		}
	}

	/**
	 * Method to update the word list
	 * 
	 * @param word
	 */
	private void updateWordList(String word) {
		mWordAdapter.setData(DicDataBase.getInstance(getActivity()).getPattern(word, 100));
	}

	/**
	 * Method to load the dictionary fragment
	 */
	private void showMeaning(String word) {
		if (getView() != null) {
			if (!mIsBackPressed && !mWordMeaning.equals(word)) {
				mArrayListWordSearched.remove(mWordMeaning);
				mArrayListWordSearched.add(mWordMeaning);
			}
			int wordIndex = mWordList.indexOf(word);
			getView().findViewById(R.id.fragment_dictionary_imageView_left_arrow).setVisibility((wordIndex == 0) ? View.INVISIBLE : View.VISIBLE);
			getView().findViewById(R.id.fragment_dictionary_imageView_right_arrow).setVisibility((wordIndex == mWordList.size() - 1) ? View.INVISIBLE : View.VISIBLE);
			mIsBackPressed = false;
			((WebView) getView().findViewById(R.id.fragment_dictionary_webView)).loadDataWithBaseURL(null, DicDataBase.getInstance(getActivity()).getMeaningFromDataBase(mWordMeaning = word, true), "text/html", "utf-8", null);
			((ImageView) getView().findViewById(R.id.fragment_dictionary_imageView_favourite)).setImageResource(Model.getInstance(getActivity()).isFavouriteWord(mWordMeaning) ? R.drawable.icon_fav_yes : R.drawable.icon_fav_no);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null && resultCode == Activity.RESULT_OK && requestCode == SPEEK_TO_TEXT) {
			ArrayList<String> list = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			if (list != null && list.size() > 0) {
				mWordAudioText = list.get(0).trim();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onBackPressed() {
		if (!Utils.isBackKeyOperationExit(getActivity())) {
			int size = mArrayListWordSearched.size();
			if (size > 0) {
				mIsBackPressed = true;
				String word = mArrayListWordSearched.get(size - 1);
				mWordList.clear();
				mWordList.addAll(DicDataBase.getInstance(getActivity()).getWordList(word));
				showMeaning(word);
				mArrayListWordSearched.remove(size - 1);
				return true;
			}
		}
		if (mIsFavHisList) {
			String word = Utils.getWordOfTheDay(getActivity());
			mWordList.clear();
			mWordList.addAll(DicDataBase.getInstance(getActivity()).getWordList(word));
			showMeaning(word);
			mIsFavHisList = false;
			return true;
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		mSearchView.setQuery(mWordAdapter.getItem(position), true);
		((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		if (getView() != null) {
			View viewFav = getView().findViewById(R.id.fragment_dictionary_imageView_favourite);
			View viewShare = getView().findViewById(R.id.fragment_dictionary_imageView_share);
			View viewSpeeker = getView().findViewById(R.id.fragment_dictionary_imageView_sound);
			if (animation.equals(mAnimationHide)) {
				if (viewFav.getVisibility() == View.VISIBLE) {
					viewFav.clearAnimation();
					viewFav.setVisibility(View.INVISIBLE);
					viewShare.startAnimation(mAnimationHide);
				} else if (viewShare.getVisibility() == View.VISIBLE) {
					viewShare.clearAnimation();
					viewShare.setVisibility(View.INVISIBLE);
					viewSpeeker.startAnimation(mAnimationHide);
				} else if (viewSpeeker.getVisibility() == View.VISIBLE) {
					viewSpeeker.clearAnimation();
					viewSpeeker.setVisibility(View.INVISIBLE);
					mIsAnimationRunning = false;
				}
			} else {
				if (viewShare.getVisibility() == View.INVISIBLE) {
					viewSpeeker.clearAnimation();
					viewShare.setVisibility(View.VISIBLE);
					viewShare.startAnimation(mAnimationShow);
				} else if (viewFav.getVisibility() == View.INVISIBLE) {
					viewShare.clearAnimation();
					viewFav.setVisibility(View.VISIBLE);
					viewFav.startAnimation(mAnimationShow);
				} else {
					mIsAnimationRunning = false;
				}
			}
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}

	@Override
	public void onRefreshList() {
		showMeaning(mWordMeaning);
	}

	/**
	 * Call Back to listen the event inside in Dictionary Fragment
	 */
	public interface OnFragmentDictionaryListener {

		/**
		 * Notify that want to close Sliding menu
		 */
		public void onFragmentDictionaryCloseSlidingMenu();

		/**
		 * Notify that want to toggle Sliding menu
		 */
		public void onFragmentDictionaryToggleSlidingMenu();

	}
}
