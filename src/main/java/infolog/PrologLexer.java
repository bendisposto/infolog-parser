package infolog;

import infolog.lexer.Lexer;
import infolog.lexer.LexerException;
import infolog.node.EOF;
import infolog.node.TComment;
import infolog.node.TCommentEnd;
import infolog.node.Token;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class PrologLexer extends Lexer {

	PrologLexer(PushbackReader in) {
		super(in);
	}

	private TComment comment = null;
	private StringBuilder commentBuffer = null;

	@Override
	protected void filter() throws LexerException, IOException {
		super.filter();
		if (state.equals(State.COMMENT)) {
			collectComment();
		}
	}

	private void collectComment() throws LexerException {
		if (token instanceof EOF) {
			// make sure we don't loose this token, needed for error message
			// tokenList.add(token);
			final int line = token.getLine() - 1;
			final int pos = token.getPos() - 1;
			final String text = token.getText();
			throw new LexerException("Comment not closed. Token: "
					+ text.toString() + "(" + line + "," + pos + ")");
		}

		if (comment == null) {
			comment = (TComment) token;
			commentBuffer = new StringBuilder();
			token = null;
		} else {
			if (token instanceof TCommentEnd) {
				comment.setEndPos(token.getEndPos());
				String text = commentBuffer.toString();
				comment.setText(text);
				token = comment;
				comment = null;
				commentBuffer = null;
				state = State.NORMAL;
			} else {
				commentBuffer.append(token.getText());
				token = null;
			}
		}

	}

	private static final String readFile(final File machine)
			throws FileNotFoundException {
		final InputStreamReader inputStreamReader = new InputStreamReader(
				new FileInputStream(machine));

		final StringBuilder builder = new StringBuilder();
		final char[] buffer = new char[1024];
		int read;
		try {
			while ((read = inputStreamReader.read(buffer)) >= 0) {
				builder.append(String.valueOf(buffer, 0, read));
			}
		} catch (IOException e) {

		} finally {
			try {
				inputStreamReader.close();
			} catch (IOException e) {
			}
		}
		String content = builder.toString();
		return content.replaceAll("\r\n", "\n");
	}

	public static List<Token> lexFile(String filename)
			throws FileNotFoundException, IOException {
		String text = readFile(new File(filename));
		return lex(text);
	}

	public static List<Token> lex(String input) {
		ArrayList<Token> tokens = new ArrayList<Token>();

		Lexer l = new PrologLexer(new PushbackReader(new StringReader(input),
				input.length()));
		try {
			do {
				Token t;
				t = l.next();
				if (t instanceof TComment)
					tokens.add(t);

			} while (!(l.peek() instanceof EOF));
		} catch (LexerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return tokens;
	}

}
