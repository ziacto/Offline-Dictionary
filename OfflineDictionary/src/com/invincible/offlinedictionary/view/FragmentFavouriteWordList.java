package com.invincible.offlinedictionary.view;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.invincible.offlinedictionary.R;
import com.invincible.offlinedictionary.database.Model;
import com.invincible.offlinedictionary.view.MainActivity.OnBackPressed;

public class FragmentFavouriteWordList extends Fragment implements OnItemClickListener, OnBackPressed {
	private ArrayList<String>	mArrayListOrignal;
	private WordAdapter			mWordAdapter;
	private SearchView			mSearchView;

	public FragmentFavouriteWordList() {
		mArrayListOrignal = new ArrayList<String>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		getActivity().invalidateOptionsMenu();
		mWordAdapter = new WordAdapter(getActivity());
		return inflater.inflate(R.layout.fragment_wordlist, container, false);
	}
	
	@Override
	public void onViewCreated(View parentView, Bundle savedInstanceState) {
		mArrayListOrignal.clear();
		mArrayListOrignal.addAll(Model.getInstance(getActivity()).getFavoriteWords());
		ListView listView = (ListView) parentView.findViewById(R.id.fragment_wordlist_listView);
		listView.setAdapter(mWordAdapter);
		listView.setOnItemClickListener(this);
		mWordAdapter.setWordList(mArrayListOrignal);
		((TextView)parentView.findViewById(R.id.fragment_wordlist_textView)).setText(R.string.headding_favourite);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_list, menu);
		final MenuItem menuItem = menu.findItem(R.id.menu_list_search_searchView);
		mSearchView = (SearchView) menuItem.getActionView();
		// SET EXPAND AND COLLAPS LISTENER ON SEARCH VIEW
		menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				if (getActivity() instanceof OnFragmentListener) {
					((OnFragmentListener) getActivity()).onFragmentWordListCloseSlidingMenu();
				}
				return true;
			}

			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				if (getActivity() instanceof OnFragmentListener) {
					((OnFragmentListener) getActivity()).onFragmentWordListCloseSlidingMenu();
				}
				return true;
			}
		});
		// SET QUERY TEXT LISTENER ON SEARCH VIEW
		mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
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
		case android.R.id.home:
			if (getActivity() instanceof OnFragmentListener) {
				((OnFragmentListener) getActivity()).onFragmentWordListToggleSlidingMenu();
			}
			break;
		case R.id.menu_list_check_all:
			mWordAdapter.selectAll();
			break;
		case R.id.menu_list_delete:
			Model model = Model.getInstance(getActivity());
			model.removeFav(mWordAdapter.getSelectedWords());
			mArrayListOrignal.clear();
			mArrayListOrignal.addAll(model.getFavoriteWords());
			mSearchView.setQuery("", false);
			updateWordList("");
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (mWordAdapter.getSelectedWords().size() > 0) {
			Toast.makeText(getActivity(), "Unselect all first...", Toast.LENGTH_SHORT).show();
		} else {
			if (getActivity() instanceof OnFragmentListener) {
				((OnFragmentListener) getActivity()).onFragmentWordListShowDictionary(mArrayListOrignal, mArrayListOrignal.indexOf(mWordAdapter.getItem(position)));
			}
		}
	}

	/**
	 * Method to gripe word list
	 * 
	 * @param newText
	 */
	private void updateWordList(String newText) {
		newText = newText.trim().toUpperCase(Locale.ENGLISH);
		if (newText.length() > 0) {
			ArrayList<String> arrayList = new ArrayList<String>();
			for (String string : mArrayListOrignal) {
				if (string.startsWith(newText)) {
					arrayList.add(string);
				}
			}
			mWordAdapter.setWordList(arrayList);
		} else {
			mWordAdapter.setWordList(mArrayListOrignal);
		}
	}

	@Override
	public boolean onBackPressed() {
		if (mWordAdapter.getSelectedWords().size() > 0) {
			mWordAdapter.unselectAll();
			return true;
		}
		if (mWordAdapter.getWordList().size() != mArrayListOrignal.size()) {
			updateWordList("");
			return true;
		}
		if (getActivity() instanceof OnFragmentListener) {
			((OnFragmentListener) getActivity()).onFragmentWordListShowDictionary();
		}
		return true;
	}
}
