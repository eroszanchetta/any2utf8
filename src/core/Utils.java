/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Eros Zanchetta <eros@sslmit.unibo.it>
 */
public class Utils {

	/**
	 * Wrap a string at column 80 using the default platform newline
	 * @param string
	 * @return
	 */
	public static String wordWrap(String string) {
		return wordWrap(string, System.getProperty("line.separator"), 80);
	}

	/**
	 * Wrap a string at the specified column using the specified newline sequence
	 * @param string
	 * @param lineSeparator
	 * @param lineLength
	 * @return
	 */
	public static String wordWrap(String string, String lineSeparator, int lineLength) {

        lineLength--;

		String pattern = ".{0," + --lineLength + "}(?:\\S(?:-| |$)|$)";

		Pattern wrapRE = Pattern.compile(pattern);

		List<String> list = new LinkedList<String>();

		Matcher m = wrapRE.matcher(string);

		while (m.find())
			list.add(m.group());

		StringBuilder out = new StringBuilder();

		for (String s : list) {
			out.append(s.trim()).append(lineSeparator);
		}

		return out.toString().trim();
	}
}
