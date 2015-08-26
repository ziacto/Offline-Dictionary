package com.invincible.offlinedictionary.database.parse;

import android.content.Context;
import android.util.Log;

import com.invincible.offlinedictionary.Utils;

public class HtmlParserHelper {
	// ==English not exist)
	public static String	REGEX_START_SUB_LINE			= "*";
	public static String	REGEX_HEADDING					= "=";
	public static String	REGEX_SUB_HEADDING				= "$";
	public static String	REGEX_HEADDING_TRANSLATOR_END	= "@";
	public static String	REGEX_ITTALIC_ON				= "" + ((char) 27);
	public static String	REGEX_ITTALIC_OFF				= "" + ((char) 28);
	public static String	REGEX_LINE_SPLITTER				= "" + ((char) 1);
	public static String	REGEX_SUB_LINE_SEPRATOR			= "" + ((char) 2);
	public static String	REGEX_DEFFINETION_SPLITTER		= "" + ((char) 3);

	// -------------------------------------------------------------------
	// SHORT NOTATION
	// -------------------------------------------------------------------
	private static String	NOTATION_LONG_ENGLISH			= "English";
	private static String	NOTATION_LONG_ADJECTIVE			= "Adjective";
	private static String	NOTATION_LONG_ETYMOLOGY			= "Etymology";
	private static String	NOTATION_LONG_PRONOUNCIATION	= "Pronunciation";
	private static String	NOTATION_LONG_PRONOUN			= "Pro-Noun";
	private static String	NOTATION_LONG_SYNONYMS			= "Synonyms";
	private static String	NOTATION_LONG_ANTONYMS			= "Antonyms";
	private static String	NOTATION_LONG_TRANSLATION		= "Translations";
	private static String	NOTATION_LONG_VERB				= "Verb";
	private static String	NOTATION_LONG_NOUN				= "Noun";
	private static String	NOTATION_LONG_TRANSATIVE		= "(transitive)";
	private static String	NOTATION_LONG_INTRANSATIVE		= "(intransitive)";
	private static String	NOTATION_LONG_COUNTABLE			= "(countable)";
	private static String	NOTATION_LONG_UNCOUNTABLE		= "(uncountable)";
	private static String	NOTATION_LONG_SLANG				= "(slang)";
	private static String	NOTATION_LONG_OBSOLETE			= "(obsolete)";
	private static String	NOTATION_LONG_HINDI				= "Hindi: ";
	private static String	NOTATION_LONG_SPANISH			= "Spanish: ";
	private static String	NOTATION_LONG_RUSSION			= "Russian: ";
	private static String	NOTATION_LONG_FRENCH			= "French: ";
	private static String	NOTATION_LONG_GERMAN			= "German: ";
	private static String	NOTATION_LONG_ITALIAN			= "Italian: ";
	private static String	NOTATION_LONG_PORTUGUESE		= "Portuguese: ";

	public static String	NOTATION_SHORT_ENGLISH			= "En";
	public static String	NOTATION_SHORT_ADJECTIVE		= "Ad";
	public static String	NOTATION_SHORT_ETYMOLOGY		= "Ey";
	public static String	NOTATION_SHORT_PRONOUNCIATION	= "P";
	public static String	NOTATION_SHORT_PRONOUN			= "Pn";
	public static String	NOTATION_SHORT_VERB				= "V";
	public static String	NOTATION_SHORT_SYNONYMS			= "S";
	public static String	NOTATION_SHORT_ANTONYMS			= "An";
	public static String	NOTATION_SHORT_TRANSLATION		= "T";
	public static String	NOTATION_SHORT_NOUN				= "N";
	private static String	NOTATION_SHORT_TRANSATIVE		= "<T>";
	private static String	NOTATION_SHORT_INTRANSATIVE		= "<I>";
	private static String	NOTATION_SHORT_COUNTABLE		= "<C>";
	private static String	NOTATION_SHORT_UNCOUNTABLE		= "<U>";
	private static String	NOTATION_SHORT_SLANG			= "<S>";
	private static String	NOTATION_SHORT_OBSOLETE			= "<O>";
	private static String	NOTATION_SHORT_HINDI			= "H:";
	private static String	NOTATION_SHORT_SPANISH			= "S:";
	private static String	NOTATION_SHORT_RUSSION			= "R:";
	private static String	NOTATION_SHORT_FRENCH			= "F:";
	private static String	NOTATION_SHORT_GERMAN			= "G:";
	private static String	NOTATION_SHORT_ITALIAN			= "I:";
	private static String	NOTATION_SHORT_PORTUGUESE		= "P:";

	/**
	 * Method to get the full form of notation
	 * 
	 * @param str
	 * @return
	 */
	public static String getFullHeaddingString(String str) {
		if (str.startsWith(NOTATION_SHORT_ENGLISH)) {
			return str.replace(NOTATION_SHORT_ENGLISH, NOTATION_LONG_ENGLISH);
		} else if (str.startsWith(NOTATION_SHORT_ADJECTIVE)) {
			return str.replace(NOTATION_SHORT_ADJECTIVE, NOTATION_LONG_ADJECTIVE);
		} else if (str.startsWith(NOTATION_SHORT_ETYMOLOGY)) {
			return str.replace(NOTATION_SHORT_ETYMOLOGY, NOTATION_LONG_ETYMOLOGY);
		} else if (str.startsWith(NOTATION_SHORT_PRONOUN)) {
			return str.replace(NOTATION_SHORT_PRONOUN, NOTATION_LONG_PRONOUN);
		} else if (str.startsWith(NOTATION_SHORT_PRONOUNCIATION)) {
			return str.replace(NOTATION_SHORT_PRONOUNCIATION, NOTATION_LONG_PRONOUNCIATION);
		} else if (str.startsWith(NOTATION_SHORT_VERB)) {
			return str.replace(NOTATION_SHORT_VERB, NOTATION_LONG_VERB);
		} else if (str.startsWith(NOTATION_SHORT_SYNONYMS)) {
			return str.replace(NOTATION_SHORT_SYNONYMS, NOTATION_LONG_SYNONYMS);
		} else if (str.startsWith(NOTATION_SHORT_ANTONYMS)) {
			return str.replace(NOTATION_SHORT_ANTONYMS, NOTATION_LONG_ANTONYMS);
		} else if (str.startsWith(NOTATION_SHORT_TRANSLATION)) {
			return str.replace(NOTATION_SHORT_TRANSLATION, NOTATION_LONG_TRANSLATION);
		} else if (str.startsWith(NOTATION_SHORT_NOUN)) {
			return str.replace(NOTATION_SHORT_NOUN, NOTATION_LONG_NOUN);
		}
		return str;
	}

	/**
	 * Method to get the full form of language notation
	 * 
	 * @param str
	 * @return
	 */
	public static String getFullLangName(String str) {
		if (str.startsWith(NOTATION_SHORT_HINDI)) {
			return str.replace(NOTATION_SHORT_HINDI, NOTATION_LONG_HINDI);
		} else if (str.startsWith(NOTATION_SHORT_SPANISH)) {
			return str.replace(NOTATION_SHORT_SPANISH, NOTATION_LONG_SPANISH);
		} else if (str.startsWith(NOTATION_SHORT_RUSSION)) {
			return str.replace(NOTATION_SHORT_RUSSION, NOTATION_LONG_RUSSION);
		} else if (str.startsWith(NOTATION_SHORT_FRENCH)) {
			return str.replace(NOTATION_SHORT_FRENCH, NOTATION_LONG_FRENCH);
		} else if (str.startsWith(NOTATION_SHORT_GERMAN)) {
			return str.replace(NOTATION_SHORT_GERMAN, NOTATION_LONG_GERMAN);
		} else if (str.startsWith(NOTATION_SHORT_ITALIAN)) {
			return str.replace(NOTATION_SHORT_ITALIAN, NOTATION_LONG_ITALIAN);
		} else if (str.startsWith(NOTATION_SHORT_PORTUGUESE)) {
			return str.replace(NOTATION_SHORT_PORTUGUESE, NOTATION_LONG_PORTUGUESE);
		}
		return str;
	}

	/**
	 * Method to get the full form of custom tags
	 * 
	 * @param str
	 * @return
	 */
	private static String getFullForm(String str) {
		return str.replace(NOTATION_SHORT_TRANSATIVE, NOTATION_LONG_TRANSATIVE).replace(NOTATION_SHORT_INTRANSATIVE, NOTATION_LONG_INTRANSATIVE).replace(NOTATION_SHORT_COUNTABLE, NOTATION_LONG_COUNTABLE).replace(NOTATION_SHORT_UNCOUNTABLE, NOTATION_LONG_UNCOUNTABLE).replace(NOTATION_SHORT_SLANG, NOTATION_LONG_SLANG).replace(NOTATION_SHORT_OBSOLETE, NOTATION_LONG_OBSOLETE);
	}

	/**
	 * Method to convert a text line in interlink format
	 * 
	 * @param word
	 * @return
	 */
	public static String getTextInInterLinkFormat(String text) {
		text = getFullForm(text).replace(":", " : ").replace("/", " / ");
		String[] words = text.split(" ");
		String html = "";
		for (String word : words) {
			word = word.trim();
			if (word.length() > 0) {
				html += "<a href=\"http://" + word + "/\">" + word + "</a> ";
			}
		}
		return html.trim();
	}

	/**
	 * Method to convert a word in interlink format
	 * 
	 * @param word
	 * @return
	 */
	public static String getHrefLink(String word) {
		try {
			word = word.trim();
			if (word.length() > 0) {
				int length = word.length();
				int fi = -1, li = -1;
				for (int i = 0; i < length; i++) {
					char ch = word.charAt(i);
					if (('A' <= ch && ch <= 'Z') || ('a' <= ch && ch <= 'z')) {
						fi = i;
						break;
					}
				}
				for (int i = length - 1; i >= 0; i--) {
					char ch = word.charAt(i);
					if (('A' <= ch && ch <= 'Z') || ('a' <= ch && ch <= 'z')) {
						li = i;
						break;
					}
				}
				if (fi == 0 && li == word.length() - 1) {
					return word;
				} else if (fi == 0) {
					return word.substring(0, li + 1);
				} else if (li == word.length() - 1) {
					return word.substring(fi);
				} else if (fi != -1 && li != -1) {
					return word.substring(fi, li + 1);
				}
				return word;
			}
		} catch (Exception e) {
			Log.e("INVINCIBLE", "HtmlParserHelper.getHrefLink()  " + e.getMessage());
		}
		return word;
	}

	/**
	 * Method to get the incomplete html code and make it complete by adding
	 * headder
	 * 
	 * @param context
	 * @param html
	 *            code include only body tag
	 * @return
	 */
	public static String attatchHeadder(Context context, String html) {
		return "<html>	<head>" + getStyle(context) + "</head>" + html + "</html>";
	}

	/**
	 * Method to get the style tag
	 * 
	 * @param context
	 * @return
	 */
	private static String getStyle(Context context) {
		float textSize = (Utils.getTextSize(context) * 1F) / 100F;
		float HeadingSize = textSize * 1.25F;
		float listPadding = textSize * 1.5F;
		String style = "<style>";
		style += "a{color:#470024; text-decoration:none;}";
		style += "div.headder {color:#B20000; display:block;}";
		style += "font{font-size:" + HeadingSize + "em;}";
		style += "body {color:#470024;	background-color:#DBFFEE; margin:" + "0.5em" + ";padding:0;font-size:" + textSize + "em;}";
		style += "ul{margin:0;padding-left: " + listPadding + "em;}";
		style += "ol{margin:0;padding-left: " + listPadding + "em;}";
		style += "li{margin-top:0.5em; margin-bottom:0.5em; }";
		style += "li.headding{margin-top:1.25em; margin-bottom:1.25em;}";
		style += "</style>";
		return style;
	}
}
