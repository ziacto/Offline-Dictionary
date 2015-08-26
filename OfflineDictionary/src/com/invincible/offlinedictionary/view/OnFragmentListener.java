package com.invincible.offlinedictionary.view;

import java.util.ArrayList;

public interface OnFragmentListener {
	/**
	 * Notify that want to close Sliding menu
	 */
	public void onFragmentWordListCloseSlidingMenu();

	/**
	 * Notify that want to toggle Sliding menu
	 */
	public void onFragmentWordListToggleSlidingMenu();

	/**
	 * Notify that show dictionary
	 */
	public void onFragmentWordListShowDictionary();
	
	/**
	 * Notify that Sppek word
	 */
	public void onFragmentWordListSpeekWord(String word);

	/**
	 * Notify that show dictionary
	 */
	public void onFragmentWordListShowDictionary(ArrayList<String> wordList, int index);
}
