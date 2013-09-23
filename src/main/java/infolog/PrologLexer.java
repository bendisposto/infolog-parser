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
import infolog.node.Token;

public class PrologLexer extends Lexer {

	PrologLexer(PushbackReader in) {
		super(in);
	}

	public static List<Token> lex(String input) {
		ArrayList<Token> tokens = new ArrayList<Token>();

		Lexer l = new Lexer(new PushbackReader(new StringReader(input)));
		try {
			do {
				Token t;
				t = l.next();
				if (t instanceof TBody)
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
