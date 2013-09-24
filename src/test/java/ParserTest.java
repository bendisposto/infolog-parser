import static org.junit.Assert.*;
import infolog.PrologLexer;
import infolog.node.Token;

import java.util.List;

import org.junit.Test;

public class ParserTest {

	@Test
	public void test1() {
		String t = "b(a). \n b(X) :- X > 0,!, b(X). /** \n@author Mike\n    @date Today\n    @descr  \n    This module cotains several basic examples\n*/\n\n:-module(test, [foo/1]).\n\n%% element(Element,List) if E in List then true\n\nelement(E,[E|_T]).\nelement(E, [_H|T]):-\n    element( E, T ).\n%! is_not_visited(Element,List) if E not in List then true\n\nis_not_visited( _ , [] ).\nis_not_visited( H, [H|_T] ):-\n    false.\nis_not_visited( A, [H|T] ):-\n    A = H,\n    is_not_visited(A, T).\n    \n/** @author me\n    @descr returns true\n*/\n\nistrue(true).\n\n\ncheck_boolean_expression(BExpr) :- %%covering all boolean expression with relational operators instead of '==' and '!='%\n  relational_binary_op(BExpr,Arg1,Arg2,EX,EY,Call),!,\n  evaluate_int_argument(Arg1,EX),\n  evaluate_int_argument(Arg2,EY),\n  Call.\n\n\nfoo(a).";
		List<Token> tokens = PrologLexer.lex(t);
		assertEquals(2, tokens.size());
		// printTokens(tokens);
	}

	@Test
	public void test2() {
		String t = "b(a). \n b(X) :- X > 0,!, b(X). /** \n@author Mike\n  *  @date Today\n    @descr  \n    This module cotains several basic examples\n*/\n\n:-module(test, [foo/1]).\n\n%% element(Element,List) if E in List then true\n\nelement(E,[E|_T]).\nelement(E, [_H|T]):-\n    element( E, T ).\n%! is_not_visited(Element,List) if E not in List then true\n\nis_not_visited( _ , [] ).\nis_not_visited( H, [H|_T] ):-\n    false.\nis_not_visited( A, [H|T] ):-\n    A = H,\n    is_not_visited(A, T).\n    \n/** @author me\n    @descr returns true\n*/\n\nistrue(true).\n\n\ncheck_boolean_expression(BExpr) :- %%covering all boolean expression with relational operators instead of '==' and '!='%\n  relational_binary_op(BExpr,Arg1,Arg2,EX,EY,Call),!,\n  evaluate_int_argument(Arg1,EX),\n  evaluate_int_argument(Arg2,EY),\n  Call.\n\n\nfoo(a).";
		List<Token> tokens = PrologLexer.lex(t);
		assertEquals(2, tokens.size());
		// printTokens(tokens);
	}

	@Test
	public void test3() {
		String t = "/** \n@author spaghetti yolonese\n\t@date /Jan Wielemaker\n\t@TimLippold  yolo\n\tswagelicious\n\t@descr /* GPL \n\n*/\n\n\nelement(E,[E|_T]).\nelement(E, [_H|T]):-\n\telement( E, T ).\n\n/** @author Prolog documentation processor\n\t\n\t@date 2.1.3\n\t@descr\n\tThis module processes structured comments and generates both formal\n\tmode declarations from them as well as documentation in the form of\n\tHTML or LaTeX.\n*/\nis_not_visited( _ , [] ).\nis_not_visited( H, [H|_T] ):-\n\tfalse.\nis_not_visited( A, [H|T] ):-\n\tA \\= H,\n\tis_not_visited(A, T).\n\t\n/** @author Prolog documentation processor\n\t@descr descr\n*/\n\nistrue(true).\n\n/** \n\t@date 2.1.3\n\t@descr descr\n\t@author Prolog documentation processor\n*/";
		List<Token> tokens = PrologLexer.lex(t);
		assertEquals(4, tokens.size());
		printTokens(tokens);

	}

	private void printTokens(List<Token> tokens) {
		for (Token token : tokens) {
			System.out.print(token.getText());
			System.out.print(" ");
			System.out.print(token.getStartPos());
			System.out.print(" ");
			System.out.print(token.getEndPos());
			System.out.println();
		}
	}

}
