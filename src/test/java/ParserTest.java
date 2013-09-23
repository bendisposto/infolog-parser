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
	}

}
