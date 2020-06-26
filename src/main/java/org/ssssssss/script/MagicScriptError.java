package org.ssssssss.script;

import org.ssssssss.script.exception.ScriptException;
import org.ssssssss.script.parsing.Span;
import org.ssssssss.script.parsing.TokenStream;

/**
 * All errors reported by the library go through the static functions of this class.
 */
public class MagicScriptError {

	/**
	 * <p>
	 * Create an error message based on the provided message and stream, highlighting the line on which the error happened. If the
	 * stream has more tokens, the next token will be highlighted. Otherwise the end of the source of the stream will be
	 * highlighted.
	 * </p>
	 *
	 * <p>
	 * Throws a {@link RuntimeException}
	 * </p>
	 */
	public static void error(String message, TokenStream stream) {
		if (stream.hasMore()) {
			error(message, stream.consume().getSpan());
		} else {
			error(message, stream.getPrev().getSpan());
		}
	}

	/**
	 * Create an error message based on the provided message and location, highlighting the location in the line on which the
	 * error happened. Throws a {@link ScriptException}
	 **/
	public static void error(String message, Span location, Throwable cause) {

		Span.Line line = location.getLine();
		String errorMessage = "Script Error : " + message + "\n\n";
		errorMessage += line.getText();
		errorMessage += "\n";
		int errorStart = location.getStart() - line.getStart();
		int errorEnd = errorStart + location.getText().length() - 1;
		for (int i = 0, n = line.getText().length(); i < n; i++) {
			boolean useTab = line.getText().charAt(i) == '\t';
			errorMessage += i >= errorStart && i <= errorEnd ? "^" : useTab ? "\t" : " ";
		}

		if (cause == null) {
			throw new ScriptException(errorMessage, message, line);
		} else {
			throw new ScriptException(errorMessage, message, cause, line);
		}
	}

	/**
	 * Create an error message based on the provided message and location, highlighting the location in the line on which the
	 * error happened. Throws a {@link ScriptException}
	 **/
	public static void error(String message, Span location) {
		error(message, location, null);
	}

}
