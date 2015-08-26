package com.invincible.offlinedictionary.database.parse;

import java.util.LinkedList;

import android.content.Context;

public class Section {
	protected LinkedList<Line>	mLines;
	protected Line				mCurrentLine;
	protected String			mSectionHeading;
	protected Context			mContext;

	public Section(Context context, String heading) {
		mContext = context;
		mLines = new LinkedList<Line>();
		mSectionHeading = heading;
	}

	/**
	 * Method to insert the data line by line, this method call every time when a new line of data is inserted
	 * 
	 * @param line
	 *            Line having data of word meaning
	 */
	public void addLine(String line) {
		mLines.add(mCurrentLine = new Line(line));
	}

	/**
	 * Method to get data in Html Data Form
	 * 
	 * @return
	 */
	public String getDataInHtmlTagForm() {
		String html = "<div class=\"" + "headder" + "\"><u><b>" + mSectionHeading + "</u></b></div><UL>";
		for (Line line : mLines) {
			html += "<LI>" + line.getDataInHtmlTagForm() + "</LI>";
		}
		html += "</UL>";
		return html;
	}

	/**
	 * Method to get data in html form
	 * 
	 * @return
	 */
	public String getDataInTextForm() {
		String html = "\n" + mSectionHeading;
		for (Line line : mLines) {
			html += "\n" + line.getDataInTextForm().trim();
		}
		return html;
	}
}
