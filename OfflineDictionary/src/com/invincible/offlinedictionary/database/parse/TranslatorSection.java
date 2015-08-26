package com.invincible.offlinedictionary.database.parse;

import java.util.LinkedList;

import android.content.Context;

public class TranslatorSection extends Section {
	private LinkedList<TranslatorSubSection>	mSubSections;
	private TranslatorSubSection				mCurrentSubSection;

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param page
	 * @param heading
	 */
	public TranslatorSection(Context context, String heading) {
		super(context, heading);
		mSubSections = new LinkedList<TranslatorSection.TranslatorSubSection>();
	}

	/**
	 * Method to insert the data line by line, this method call every time when
	 * a new line of data is inserted
	 * 
	 * @param line
	 *            Line having data of word meaning
	 */
	@Override
	public void addLine(String line) {
		if (line.startsWith(HtmlParserHelper.REGEX_SUB_HEADDING)) {
			return;
		} else if (line.endsWith(HtmlParserHelper.REGEX_HEADDING_TRANSLATOR_END)) {
			mSubSections.add(mCurrentSubSection = new TranslatorSubSection(line.replace(HtmlParserHelper.REGEX_HEADDING_TRANSLATOR_END, "").replace(HtmlParserHelper.REGEX_HEADDING, "").trim()));
		} else if (mCurrentSubSection != null) {
			mCurrentSubSection.addLine(line);
		}
	}

	@Override
	public String getDataInHtmlTagForm() {
		String html = "<div class=\"" + "headder" + "\"><u><b>Translations</u></b></div><UL>";
		for (TranslatorSubSection ts : mSubSections) {
			html += "<LI>" + ts.getDataInHtmlTagForm() + "</LI>";
		}
		html += "</UL>";
		return html;
	}

	/**
	 * Method to get the data in text form Data
	 * 
	 * @return
	 */
	@Override
	public String getDataInTextForm() {
		String html = "Translator";
		for (TranslatorSubSection ts : mSubSections) {
			html += "\n" + ts.getDataInTextForm();
		}
		return html;
	}

	/**
	 * Class to hold the data of subSection
	 */
	private class TranslatorSubSection {
		private LinkedList<String>	mLines;
		private String				mHeading;

		/**
		 * Constructor
		 * 
		 * @param heading
		 */
		public TranslatorSubSection(String heading) {
			mHeading = heading;
			mLines = new LinkedList<String>();
		}

		/**
		 * Method to data line
		 * 
		 * @param line
		 */
		public void addLine(String line) {
			mLines.add(line.trim());
		}

		/**
		 * Method to get the data in text form form
		 * 
		 * @return
		 */
		public String getDataInHtmlTagForm() {
			String html = "<div class=\"" + "headder" + "\"><b>" + HtmlParserHelper.getTextInInterLinkFormat(mHeading) + "</b></div><UL>";
			for (String line : mLines) {
				html += "<LI>" + HtmlParserHelper.getFullLangName(line) + "</LI>";
			}
			html += "</UL>";
			return html;
		}

		/**
		 * Method to get the section data in text form
		 * 
		 * @return
		 */
		public String getDataInTextForm() {
			String html = "\t" + mHeading;
			for (String line : mLines) {
				if (line.length() > 3) {
					html += "\t\t- " + line;
				}
			}
			return html;
		}
	}
}
