package com.invincible.offlinedictionary.database.parse;

import java.util.LinkedList;

public class Line {
	private String				mLine;
	private LinkedList<String>	mSubLine;

	public Line(String line) {
		mSubLine = new LinkedList<String>();
		String[] split = line.split(HtmlParserHelper.REGEX_SUB_LINE_SEPRATOR);
		mLine = split[0].trim();
		for (int i = 1; i < split.length; i++) {
			mSubLine.add(split[i].trim());
		}
	}

	/**
	 * Method to get data in Html Data Form
	 * 
	 * @return
	 */
	public String getDataInHtmlTagForm() {
		String htmlText = HtmlParserHelper.getTextInInterLinkFormat(mLine);
		if (mSubLine.size() > 0) {
			htmlText += "<UL>";
			for (String subLine : mSubLine) {
				htmlText += "<LI>" + HtmlParserHelper.getTextInInterLinkFormat(subLine) + "</LI>";
			}
			htmlText += "</UL>";
		}
		return htmlText;
	}

	/**
	 * Method to get the data in text form form
	 * 
	 * @return
	 */
	public String getDataInTextForm() {
		String htmlText = "\t" + mLine;
		if (mSubLine.size() > 0) {
			for (String subLine : mSubLine) {
				htmlText += "\n\t\t- " + subLine;
			}
		}
		return htmlText;
	}
}
