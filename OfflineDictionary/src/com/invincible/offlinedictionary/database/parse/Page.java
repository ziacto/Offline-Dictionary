package com.invincible.offlinedictionary.database.parse;

import java.util.Hashtable;

import android.content.Context;

import com.invincible.offlinedictionary.Utils;

public class Page {
	private Hashtable<String, Section>	mHashTableSection;
	private Section						mCurrentSection;
	private Context						mContext;
	private String						mWord;

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public Page(Context context, String word) {
		mWord = word;
		mContext = context;
		mHashTableSection = new Hashtable<String, Section>();
	}

	/**
	 * Method to insert the data line by line, this method call every time when
	 * a new line of data is inserted
	 * 
	 * @param line
	 *            Line having data of word meaning
	 */
	public void addDataLine(String line) {
		line = line.trim();
		if (line.length() > 0) {
			if (line.startsWith(HtmlParserHelper.REGEX_HEADDING)) {
				if (line.endsWith(HtmlParserHelper.REGEX_HEADDING_TRANSLATOR_END)) {
					if (!mHashTableSection.containsKey(HtmlParserHelper.NOTATION_SHORT_TRANSLATION)) {
						mHashTableSection.put(HtmlParserHelper.NOTATION_SHORT_TRANSLATION, new TranslatorSection(mContext, HtmlParserHelper.getFullHeaddingString(HtmlParserHelper.NOTATION_SHORT_TRANSLATION)));
					}
					mCurrentSection = mHashTableSection.get(HtmlParserHelper.NOTATION_SHORT_TRANSLATION);
				} else {
					String headding = line.replace(HtmlParserHelper.REGEX_HEADDING, "").trim();
					if (!mHashTableSection.containsKey(headding)) {
						mHashTableSection.put(headding, new Section(mContext, HtmlParserHelper.getFullHeaddingString(headding)));
					}
					mCurrentSection = mHashTableSection.get(headding);
					return;
				}
			}
			// ADD LINE DATA
			if (mCurrentSection != null) {
				mCurrentSection.addLine(line);
			}
		}
	}

	/**
	 * Method to get the data in html data
	 * 
	 * @return
	 */
	public String getDataInHtmlForm() {
		String html = "<center><font color=\"#B20000\"><u><b>" + mWord + "</b></u></font></center><BR>";
		String data = "";
		if (mHashTableSection.containsKey(HtmlParserHelper.NOTATION_SHORT_ETYMOLOGY) && Utils.isShowEtymology(mContext)) {
			String subSection = mHashTableSection.get(HtmlParserHelper.NOTATION_SHORT_ETYMOLOGY).getDataInHtmlTagForm();
			data += (subSection.trim().length() > 0) ? ("<LI class=\"" + "headding" + "\">" + subSection + "</LI>") : "";
		}
		if (mHashTableSection.containsKey(HtmlParserHelper.NOTATION_SHORT_PRONOUNCIATION) && Utils.isShowPronounciation(mContext)) {
			String subSection = mHashTableSection.get(HtmlParserHelper.NOTATION_SHORT_PRONOUNCIATION).getDataInHtmlTagForm();
			data += (subSection.trim().length() > 0) ? ("<LI class=\"" + "headding" + "\">" + subSection + "</LI>") : "";
		}
		if (mHashTableSection.containsKey(HtmlParserHelper.NOTATION_SHORT_ADJECTIVE) && Utils.isShowAdjective(mContext)) {
			String subSection = mHashTableSection.get(HtmlParserHelper.NOTATION_SHORT_ADJECTIVE).getDataInHtmlTagForm();
			data += (subSection.trim().length() > 0) ? ("<LI class=\"" + "headding" + "\">" + subSection + "</LI>") : "";
		}
		if (mHashTableSection.containsKey(HtmlParserHelper.NOTATION_SHORT_NOUN) && Utils.isShowNoun(mContext)) {
			String subSection = mHashTableSection.get(HtmlParserHelper.NOTATION_SHORT_NOUN).getDataInHtmlTagForm();
			data += (subSection.trim().length() > 0) ? ("<LI class=\"" + "headding" + "\">" + subSection + "</LI>") : "";
		}
		if (mHashTableSection.containsKey(HtmlParserHelper.NOTATION_SHORT_PRONOUN) && Utils.isShowProNoun(mContext)) {
			String subSection = mHashTableSection.get(HtmlParserHelper.NOTATION_SHORT_PRONOUN).getDataInHtmlTagForm();
			data += (subSection.trim().length() > 0) ? ("<LI class=\"" + "headding" + "\">" + subSection + "</LI>") : "";
		}
		if (mHashTableSection.containsKey(HtmlParserHelper.NOTATION_SHORT_VERB) && Utils.isShowVerb(mContext)) {
			String subSection = mHashTableSection.get(HtmlParserHelper.NOTATION_SHORT_VERB).getDataInHtmlTagForm();
			data += (subSection.trim().length() > 0) ? ("<LI class=\"" + "headding" + "\">" + subSection + "</LI>") : "";
		}
		if (mHashTableSection.containsKey(HtmlParserHelper.NOTATION_SHORT_SYNONYMS) && Utils.isShowSynonyms(mContext)) {
			String subSection = mHashTableSection.get(HtmlParserHelper.NOTATION_SHORT_SYNONYMS).getDataInHtmlTagForm();
			data += (subSection.trim().length() > 0) ? ("<LI class=\"" + "headding" + "\">" + subSection + "</LI>") : "";
		}
		if (mHashTableSection.containsKey(HtmlParserHelper.NOTATION_SHORT_ANTONYMS) && Utils.isShowAntonyms(mContext)) {
			String subSection = mHashTableSection.get(HtmlParserHelper.NOTATION_SHORT_ANTONYMS).getDataInHtmlTagForm();
			data += (subSection.trim().length() > 0) ? ("<LI class=\"" + "headding" + "\">" + subSection + "</LI>") : "";
		}
		if (mHashTableSection.containsKey(HtmlParserHelper.NOTATION_SHORT_TRANSLATION) && Utils.isShowTranslation(mContext)) {
			String subSection = mHashTableSection.get(HtmlParserHelper.NOTATION_SHORT_TRANSLATION).getDataInHtmlTagForm();
			data += (subSection.trim().length() > 0) ? ("<LI class=\"" + "headding" + "\">" + subSection + "</LI>") : "";
		}
		if (data.length() > 0) {
			html += "<OL>" + data + "</OL>";
		}
		html = "<body><table><tr><td width=\"" + mContext.getResources().getDisplayMetrics().widthPixels + "px\" style=\"word-wrap:break-word;\">" + html + "</td></tr></table></body>";
		return HtmlParserHelper.attatchHeadder(mContext, html);
	}

	/**
	 * Method to get the data in Text data
	 * 
	 * @return
	 */
	public String getDataInTextForm() {
		String html = "\"" + mWord + "\"";
		if (mHashTableSection.containsKey(HtmlParserHelper.NOTATION_SHORT_ETYMOLOGY)) {
			String subSection = mHashTableSection.get(HtmlParserHelper.NOTATION_SHORT_ETYMOLOGY).getDataInTextForm();
			html += "\n" + subSection;
		}
		if (mHashTableSection.containsKey(HtmlParserHelper.NOTATION_SHORT_PRONOUNCIATION)) {
			String subSection = mHashTableSection.get(HtmlParserHelper.NOTATION_SHORT_PRONOUNCIATION).getDataInTextForm();
			html += (subSection.trim().length() > 0) ? "\n\n" + subSection : "";
		}
		if (mHashTableSection.containsKey(HtmlParserHelper.NOTATION_SHORT_ADJECTIVE)) {
			String subSection = mHashTableSection.get(HtmlParserHelper.NOTATION_SHORT_ADJECTIVE).getDataInTextForm();
			html += "\n" + subSection;
		}
		if (mHashTableSection.containsKey(HtmlParserHelper.NOTATION_SHORT_NOUN)) {
			String subSection = mHashTableSection.get(HtmlParserHelper.NOTATION_SHORT_NOUN).getDataInTextForm();
			html += "\n" + subSection;
		}
		if (mHashTableSection.containsKey(HtmlParserHelper.NOTATION_SHORT_PRONOUN)) {
			String subSection = mHashTableSection.get(HtmlParserHelper.NOTATION_SHORT_PRONOUN).getDataInTextForm();
			html += "\n" + subSection;
		}
		if (mHashTableSection.containsKey(HtmlParserHelper.NOTATION_SHORT_VERB)) {
			String subSection = mHashTableSection.get(HtmlParserHelper.NOTATION_SHORT_VERB).getDataInTextForm();
			html += "\n" + subSection;
		}
		if (mHashTableSection.containsKey(HtmlParserHelper.NOTATION_SHORT_SYNONYMS)) {
			String subSection = mHashTableSection.get(HtmlParserHelper.NOTATION_SHORT_SYNONYMS).getDataInTextForm();
			html += "\n" + subSection;
		}
		if (mHashTableSection.containsKey(HtmlParserHelper.NOTATION_SHORT_ANTONYMS)) {
			String subSection = mHashTableSection.get(HtmlParserHelper.NOTATION_SHORT_ANTONYMS).getDataInTextForm();
			html += "\n" + subSection;
		}
		if (mHashTableSection.containsKey(HtmlParserHelper.NOTATION_SHORT_TRANSLATION)) {
			String subSection = mHashTableSection.get(HtmlParserHelper.NOTATION_SHORT_TRANSLATION).getDataInTextForm();
			html += "\n" + subSection;
		}
		return html;
	}

	/**
	 * Method to get the title
	 * 
	 * @return
	 */
	private String getTitle() {
		int width = mContext.getResources().getDisplayMetrics().widthPixels;
		return "";
	}
}
