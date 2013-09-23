package infolog;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import infolog.lexer.Lexer;
import infolog.lexer.LexerException;
import infolog.node.EOF;
import infolog.node.TBody;
import infolog.node.TComment;
import infolog.node.TCommentEnd;
import infolog.node.Token;

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
			commentBuffer = new StringBuilder(token.getText());
			token = null;
		} else {
			commentBuffer.append(token.getText());
			if (token instanceof TCommentEnd) {
				comment.setEndPos(token.getEndPos());
				String text = commentBuffer.toString();
				comment.setText(text);
				token = comment;
				comment = null;
				commentBuffer = null;
				state = State.NORMAL;
			} else {
				token = null;
			}
		}

	}

	public static List<Token> lex(String input) {
		ArrayList<Token> tokens = new ArrayList<Token>();

		Lexer l = new PrologLexer(new PushbackReader(new StringReader(input)));
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
