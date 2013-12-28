import junit.framework.TestCase;


public class ExpressionTest extends TestCase {
	public void testExpression() throws IllegalLineException{
		assertOK("(a=>b)");
		assertOK("(a=>a)");
		assertOK("(a=>~~b)");
		assertOK("((~(p=>q)=>q)=>((q=>p)=>~p))");
		assertOK("(~(p=>p)=>(q|~p))");
		assertOK("a");
		assertOK("~q");
		assertOK("((~p&(r|q))=>(p|(r&q)))");
		assertOK("(~p=>~q)");
		assertTrue(new Expression("(p=>q)").root().equals(new Expression("(p=>q)").root()));
		assertThrowsException("(|a=>b)");
		assertThrowsException("(|%=>b)");
		assertThrowsException("(~)");
		assertThrowsException("()");
		assertThrowsException(null);
		assertThrowsException("");
		assertThrowsException("((a|b) => q)");
		assertThrowsException("(a=>");
		assertThrowsException("(a|b=>c&d)");
		assertThrowsException("(a~)");
		assertTrue(new Expression("p").root().equals(new Expression("p").root()));
		assertTrue(new Expression("(p=>c)").root().equals(new Expression("(p=>c)").root()));
		assertFalse(new Expression("c").root().equals(new Expression("d").root()));
	}

	private void p(Object s){
		System.out.println(s);
	}
	
	private void assertOK(String s) throws IllegalLineException{
		p(new Expression(s));
	}
	
	private void assertThrowsException(String s){
		try{
			p(new Expression(s));
			fail("Should have thrown an exception");
		} catch (IllegalLineException err){
			assertTrue(true);
		}
	}
}
