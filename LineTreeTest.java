import java.util.Arrays;
import java.util.Iterator;

import junit.framework.TestCase;


public class LineTreeTest extends TestCase {

	public void testConstructor() {
		LineTree tree = new LineTree();
		assertEquals("LineNumber object not properly constructed.", tree.getRootLine().getLine(), 0);
		assertEquals("LineNumber object not properly constructed.", tree.getRootLine().getReason(), null);
		assertEquals("LineNumber object not properly constructed.", tree.getRootLine().getExpression(), null);

	}
	
	public void testAddSubLine() {
		
		//tests both addSubLine and fillSubLine as addSubLine just adds an empty line
		
		try { 
			
			LineTree tree = new LineTree();
			tree.addSubLine();
			tree.fill("1 show (((p=>q)=>q)=>((q=>p)=>p))", "show", new Expression("(((p=>q)=>q)=>((q=>p)=>p))"));
			assertEquals("Sub-line not properly added.", tree.getLastSubLine().getLine(), 1);
			assertEquals("Sub-line not properly added.", tree.getLastSubLine().getReason(), "show");
			assertEquals("Sub-line not properly added.", tree.getLastSubLine().getExpression().toString(), "(((p=>q)=>q)=>((q=>p)=>p))");
			
			tree.addSubLine();
			tree.fill("2 assume ((p=>q)=>q)", "assume", new Expression("q"));
			assertEquals("Sub-line not properly added.", tree.getLastSubLine().getLine(), 2);
			assertEquals("Sub-line not properly added.", tree.getLastSubLine().getReason(), "assume");
			assertEquals("Sub-line not properly added.", tree.getLastSubLine().getExpression().toString(), "q");
			
			tree.addSubLine();
			tree.fill("3 show ((q=>p)=>p)", "ic", new Expression("(q=>q)"));
			assertEquals("Sub-line not properly added.", tree.getLastSubLine().getLine(), 3);
			assertEquals("Sub-line not properly added.", tree.getLastSubLine().getReason(), "ic");
			assertEquals("Sub-line not properly added.", tree.getLastSubLine().getExpression().toString(), "(q=>q)");
			} catch (IllegalLineException e) {
				System.err.println("Illegal line argument for Expression constructor");
				e.printStackTrace();
				fail();
			}
		
	}
	
	
	public void testMoveDown() {
		
		try {
		
		LineTree tree = new LineTree();
		tree.addSubLine();
		tree.fill("show (((p=>q)=>q)=>((q=>p)=>p))", "show", new Expression("(((p=>q)=>q)=>((q=>p)=>p))"));
		tree.addSubLine();
		tree.fill("assume ((p=>q)=>q)", "assume", new Expression("q"));
		tree.addSubLine();
		tree.fill("show ((q=>p)=>p)", "show", new Expression("((q=>p)=>p)"));
		tree.moveDown();
		assertEquals("LineNumber doesn't move down properly", tree.getCurrentLine().getReason(), "show");
		assertEquals("LineNumber doesn't move down properly", tree.getCurrentLine().getExpression().toString(), "((q=>p)=>p)");
		
		tree.addSubLine();
		tree.fill("assume (q=>p)","assume", new Expression("(q=>p)"));
		tree.addSubLine();
		tree.fill("show p" ,"show", new Expression("p"));
		tree.moveDown();
		assertEquals("LineNumber doesn't move down properly", tree.getCurrentLine().getReason(), "show");
		assertEquals("LineNumber doesn't move down properly", tree.getCurrentLine().getExpression().toString(), "p");
		
		tree.addSubLine();
		tree.fill("assume ~p", "assume", new Expression("~p"));
		assertEquals("LineNumber doesn't move down properly", tree.getCurrentLine().getReason(), "show");
		assertEquals("LineNumber doesn't move down properly", tree.getCurrentLine().getExpression().toString(), "p");
		
		tree.addSubLine();
		tree.fill("mt 3.2.1 3.1 ~q", "mt", new Expression("~q"));
		assertEquals("LineNumber doesn't move down properly", tree.getCurrentLine().getReason(), "show");
		assertEquals("LineNumber doesn't move down properly", tree.getCurrentLine().getExpression().toString(), "p");
		
		tree.addSubLine();
		tree.fill("mt 2 3.2.2 ~(p=>q)", "mt", new Expression("~(p=>q)"));
		tree.addSubLine();
		tree.fill("show (p=>q)", "show", new Expression("(p=>q)"));
		assertEquals("LineNumber doesn't move down properly", tree.getCurrentLine().getReason(), "show");
		assertEquals("LineNumber doesn't move down properly", tree.getCurrentLine().getExpression().toString(), "p");
		
		tree.moveDown();
		tree.addSubLine();
		tree.fill("assume p", "assume", new Expression("p"));
		tree.addSubLine();
		tree.fill("co 3.2.4.1 3.2.1 (p=>q)", "co", new Expression("(p=>q)"));
		assertEquals("LineNumber doesn't move down properly", tree.getCurrentLine().getReason(), "show");
		assertEquals("LineNumber doesn't move down properly", tree.getCurrentLine().getExpression().toString(), "(p=>q)");
		}  catch (IllegalLineException e) {
			System.err.println("Illegal line argument for Expression constructor");
			fail();
		}
		
	}
	
	public void testGoBack() {
		
		try {
			
		LineTree tree = new LineTree();
		tree.addSubLine();
		tree.fill("show (((p=>q)=>q)=>((q=>p)=>p))", "show", new Expression("(((p=>q)=>q)=>((q=>p)=>p))"));
		tree.addSubLine();
		tree.fill("assume ((p=>q)=>q)", "assume", new Expression("q"));
		tree.addSubLine();
		tree.fill("show ((q=>p)=>p)", "show", new Expression("((q=>p)=>p)"));
		tree.moveDown();
		
		tree.addSubLine();
		tree.fill("assume (q=>p)","assume", new Expression("(q=>p)"));
		tree.addSubLine();
		tree.fill("show p" ,"show", new Expression("p"));
		tree.moveDown();
		
		tree.addSubLine();
		tree.fill("assume ~p", "assume", new Expression("~p"));
		tree.addSubLine();
		tree.fill("mt 3.2.1 3.1 ~q", "mt", new Expression("~q"));
		tree.addSubLine();
		tree.fill("mt 2 3.2.2 ~(p=>q)", "mt", new Expression("~(p=>q)"));
		tree.addSubLine();
		tree.fill("show (p=>q)", "show", new Expression("(p=>q)"));
		
		tree.moveDown();
		tree.addSubLine();
		tree.fill("assume p", "assume", new Expression("p"));
		tree.addSubLine();
		tree.fill("co 3.2.4.1 3.2.1 (p=>q)", "co", new Expression("(p=>q)"));
		
		tree.moveDown();  //currentLine should now be at 3.2.4.2
		
		tree.goBack();
		assertEquals("LineNumber doesn't move up properly", tree.getCurrentLine().getReason(), "assume");
		assertEquals("LineNumber doesn't move up properly", tree.getCurrentLine().getExpression().toString(), "p");
		
		tree.goBack();
		assertEquals("LineNumber doesn't move up properly", tree.getCurrentLine().getReason(), "show");
		assertEquals("LineNumber doesn't move up properly", tree.getCurrentLine().getExpression().toString(), "(p=>q)");
		
		tree.goBack();
		assertEquals("LineNumber doesn't move up properly", tree.getCurrentLine().getReason(), "mt");
		assertEquals("LineNumber doesn't move up properly", tree.getCurrentLine().getExpression().toString(), "~(p=>q)");
		
		tree.goBack();
		tree.goBack();
		tree.goBack();
		tree.goBack();
		assertEquals("LineNumber doesn't move up properly", tree.getCurrentLine().getReason(), "assume");
		assertEquals("LineNumber doesn't move up properly", tree.getCurrentLine().getExpression().toString(), "(q=>p)");
		
		tree.goBack();
		tree.goBack();
		tree.goBack();
		assertEquals("LineNumber doesn't move up properly", tree.getCurrentLine().getReason(), "show");
		assertEquals("LineNumber doesn't move up properly", tree.getCurrentLine().getExpression().toString(), "(((p=>q)=>q)=>((q=>p)=>p))");
		
		tree.goBack(); //now at root line
		boolean thrown = false;
		try {
		tree.moveUp();  //should throw exception because you're trying to go up past root line
		} catch (NullPointerException e) {
			thrown = true;
		}
		assertTrue("LineNumber moved up past the root line.", thrown);
		}  catch (IllegalLineException e) {
			System.err.println("Illegal line argument for Expression constructor");
			fail();
		}
		
	}
	
	public void testMoveUp() {
		
		try {
		
		LineTree tree = new LineTree();
		tree.addSubLine();
		tree.fill("show (((p=>q)=>q)=>((q=>p)=>p))", "show", new Expression("(q=>q)"));
		tree.addSubLine();
		tree.fill("assume ((p=>q)=>q)", "assume", new Expression("q"));
		tree.addSubLine();
		tree.fill("show ((q=>p)=>p)", "show", new Expression("((q=>p)=>p)"));
		tree.moveDown();
		
		tree.addSubLine();
		tree.fill("assume (q=>p)","assume", new Expression("(q=>p)"));
		tree.addSubLine();
		tree.fill("show p" ,"show", new Expression("p"));
		tree.moveDown();
		
		tree.addSubLine();
		tree.fill("assume ~p", "assume", new Expression("~p"));
		tree.addSubLine();
		tree.fill("mt 3.2.1 3.1 ~q", "mt", new Expression("~q"));
		tree.addSubLine();
		tree.fill("mt 2 3.2.2 ~(p=>q)", "mt", new Expression("~(p=>q)"));
		tree.addSubLine();
		tree.fill("show (p=>q)", "show", new Expression("(p=>q)"));
		
		tree.moveDown();
		tree.addSubLine();
		tree.fill("assume p", "assume", new Expression("p"));
		tree.addSubLine();
		tree.fill("co 3.2.4.1 3.2.1 (p=>q)", "co", new Expression("(p=>q)"));
		
		tree.moveDown();  //now at the last line
		
		tree.moveUp();
		
		assertEquals("Current line doesn't access its parent line", tree.getCurrentLine().getReason(), "show");
		assertEquals("Current line doesn't access its parent line", tree.getCurrentLine().getExpression().toString(), "(p=>q)");
		
		tree.goBack();
		tree.moveUp();
		assertEquals("Current line doesn't access its parent line", tree.getCurrentLine().getReason(), "show");
		assertEquals("Current line doesn't access its parent line", tree.getCurrentLine().getExpression().toString(), "p");
		
		tree.moveUp();
		assertEquals("Current line doesn't access its parent line", tree.getCurrentLine().getReason(), "show");
		assertEquals("Current line doesn't access its parent line", tree.getCurrentLine().getExpression().toString(), "((q=>p)=>p)");
		
		}  catch (IllegalLineException e) {
			System.err.println("Illegal line argument for Expression constructor");
			e.printStackTrace();
			fail();
		}
	}
	
	public void testIterator() {
		
		try {
			
			LineTree tree = new LineTree();
			tree.addSubLine();
			tree.fill("show (((p=>q)=>q)=>((q=>p)=>p))", "show", new Expression("(q=>q)"));
			tree.addSubLine();
			tree.fill("assume ((p=>q)=>q)", "assume", new Expression("q"));
			tree.addSubLine();
			tree.fill("show ((q=>p)=>p)", "ic", new Expression("(q=>q)"));
			tree.moveDown();
			
			tree.addSubLine();
			tree.fill("assume (q=>p)","assume", new Expression("(q=>p)"));
			tree.addSubLine();
			tree.fill("show p" ,"show", new Expression("p"));
			tree.moveDown();
			
			tree.addSubLine();
			tree.fill("assume ~p", "assume", new Expression("~p"));
			tree.addSubLine();
			tree.fill("mt 3.2.1 3.1 ~q", "mt", new Expression("~q"));
			tree.addSubLine();
			tree.fill("mt 2 3.2.2 ~(p=>q)", "mt", new Expression("~(p=>q)"));
			tree.addSubLine();
			tree.fill("show (p=>q)", "show", new Expression("(p=>q)"));
			
			tree.moveDown();
			tree.addSubLine();
			tree.fill("assume p", "assume", new Expression("p"));
			tree.addSubLine();
			tree.fill("co 3.2.4.1 3.2.1 (p=>q)", "co", new Expression("(p=>q)"));
			
			Iterator<LineNumber> iter = tree.iterate();
			assertTrue("First ArrayList of sub-lines not added",iter.hasNext());
			LineNumber line = iter.next();
			assertEquals("Next method does not return the next element", line.getExpression().toString(), "(q=>q)");
			line = iter.next();
			assertEquals("Next method does not return the next element", line.getExpression().toString(), "q");
			line = iter.next();
			line = iter.next();
			assertEquals("Next method does not return the next element", line.getExpression().toString(), "(q=>p)");
			
		}  catch (IllegalLineException e) {
			System.err.println("Illegal line argument for Expression constructor");
			fail();
		}
		
	}
	
	public void testGetExpression() {
		
		try {
		LineTree tree = new LineTree();
		tree.addSubLine();
		tree.fill("show (((p=>q)=>q)=>((q=>p)=>p))", "show", new Expression("(q=>q)"));
		tree.addSubLine();
		tree.fill("assume ((p=>q)=>q)", "assume", new Expression("q"));
		tree.addSubLine();
		tree.fill("show ((q=>p)=>p)", "ic", new Expression("(q=>q)"));
		tree.moveDown();
		
		tree.addSubLine();
		tree.fill("random ", "assume", new Expression("(p=>q)"));
		tree.moveDown();
		
		tree.addSubLine();
		tree.fill("show (((p=>q)=>q)=>((q=>p)=>p))", "show", new Expression("(q=>q)"));
		tree.addSubLine();
		tree.fill("assume ((p=>q)=>q)", "assume", new Expression("~q"));
		tree.addSubLine();
		tree.fill("show ((q=>p)=>p)", "ic", new Expression("(q=>q)"));
		
		int[] line = new int[2];
		line[0] = 3;
		line[1] = 1;
		assertEquals("Got the wrong expression.",tree.getExpression(line).toString(), "(p=>q)");
		
		int[] line2 = new int[1];
		line2[0] = 2;
		assertEquals("Got the wrong expression.",tree.getExpression(line2).toString(), "q");
		
		int[] line3 = new int[3];
		line3[0] = 3;
		line3[1] = 1;
		line3[2] = 2;
		assertEquals("Got the wrong expression.",tree.getExpression(line3).toString(), "~q");
		
		} catch (IllegalLineException e) {
			System.err.println("Illegal line argument for Expression constructor");
			fail();
		}
		
	}
	
	public void testPrint() {
		
		try {
			LineTree tree = new LineTree();
			tree.addSubLine();
			tree.fill("show (((p=>q)=>q)=>((q=>p)=>p))", "show", new Expression("(q=>q)"));
			tree.addSubLine();
			tree.fill("assume ((p=>q)=>q)", "assume", new Expression("q"));
			tree.moveDown();
			tree.addSubLine();
			tree.fill("show (p=>q)", "show", new Expression("(p=>q)"));
		
			String allLines = tree.print();
			assertEquals("Print method does not work correctly.", allLines, "1\tshow (((p=>q)=>q)=>((q=>p)=>p))\n2\tassume ((p=>q)=>q)\n2.1\tshow (p=>q)\n");
		} catch (IllegalLineException e) {
			System.err.println("Illegal line argument for Expression constructor");
			fail();
		}
	}
	
	public void testGetCurrentLineNumber() {
		
		try {
			LineTree tree = new LineTree();
			tree.addSubLine();
			tree.fill("show (((p=>q)=>q)=>((q=>p)=>p))", "show", new Expression("(q=>q)"));
			tree.addSubLine();
			tree.fill("assume ((p=>q)=>q)", "assume", new Expression("q"));
			
			int[] currentLineNumber = tree.getCurrentLineNumber();
			String currentLine = Arrays.toString(currentLineNumber);
			
			assertEquals("getCurrentLine doesn't work properly.", currentLine, "[2]");
			
			tree.addSubLine();
			tree.fill("show ((q=>p)=>p)", "ic", new Expression("(q=>q)"));
			tree.moveDown();
			
			tree.addSubLine();
			tree.fill("random ", "assume", new Expression("(p=>q)"));
			
			currentLineNumber = tree.getCurrentLineNumber();
			currentLine = Arrays.toString(currentLineNumber);
			assertEquals("getCurrentLine doesn't work properly.", currentLine, "[3, 1]");
			
			tree.moveDown();
			tree.addSubLine();
			tree.fill("show (((p=>q)=>q)=>((q=>p)=>p))", "show", new Expression("(q=>q)"));
			tree.addSubLine();
			tree.fill("show (((p=>q)=>q)=>((q=>p)=>p))", "show", new Expression("(q=>q)"));
			tree.addSubLine();
			tree.fill("show (((p=>q)=>q)=>((q=>p)=>p))", "show", new Expression("(q=>q)"));
			tree.addSubLine();
			tree.fill("show (((p=>q)=>q)=>((q=>p)=>p))", "show", new Expression("(q=>q)"));
			currentLineNumber = tree.getCurrentLineNumber();
			currentLine = Arrays.toString(currentLineNumber);
			assertEquals("getCurrentLine doesn't work properly.", currentLine, "[3, 1, 4]");
			
			tree.moveUp();
			tree.moveUp();
			tree.addSubLine();
			tree.fill("show (((p=>q)=>q)=>((q=>p)=>p))", "show", new Expression("(q=>q)"));
			tree.addSubLine();
			tree.fill("show (((p=>q)=>q)=>((q=>p)=>p))", "show", new Expression("(q=>q)"));
		
			currentLineNumber = tree.getCurrentLineNumber();
			currentLine = Arrays.toString(currentLineNumber);
			assertEquals("getCurrentLine doesn't work properly.", currentLine, "[5]");
			
			
			} catch (IllegalLineException e) {
				System.err.println("Illegal line argument for Expression constructor");
				fail();
			}	
	}
	
	public void testExtremeCaseMoveUp() {
		
		LineTree tree = new LineTree();
		boolean thrown = false;
		try {
			//see if moving up past root line results in an exception thrown
			
			tree.addSubLine();
			tree.fill("show (((p=>q)=>q)=>((q=>p)=>p))", "show", new Expression("(q=>q)"));
			tree.addSubLine();
			tree.fill("assume ((p=>q)=>q)", "assume", new Expression("q"));
			tree.moveDown();
			tree.addSubLine();
			tree.fill("show (((p=>q)=>q)=>((q=>p)=>p))", "show", new Expression("(q=>q)"));
			tree.addSubLine();
			tree.fill("show (((p=>q)=>q)=>((q=>p)=>p))", "show", new Expression("(q=>q)"));
			tree.moveUp();
	
		} catch (IllegalLineException e) {
			System.err.println("Illegal line argument for Expression constructor");
			fail();
		}
		try {
			tree.moveUp();
		} catch (NullPointerException e) {
			thrown = true;
		}
		assertTrue(thrown);
		
	}
	
	public void testGetIllegalExpression() {
		//tests to see if getting an expression that's not in the LineTree results in an exception thrown
		
		LineTree tree = new LineTree();
		boolean thrown = false;
		try {
			
			tree.addSubLine();
			tree.fill("show (((p=>q)=>q)=>((q=>p)=>p))", "show", new Expression("(q=>q)"));
			tree.addSubLine();
			tree.fill("assume ((p=>q)=>q)", "assume", new Expression("q"));
			tree.moveDown();
			tree.addSubLine();
			tree.fill("show (((p=>q)=>q)=>((q=>p)=>p))", "show", new Expression("(p=>q)"));
			tree.addSubLine();
			tree.fill("show (((p=>q)=>q)=>((q=>p)=>p))", "show", new Expression("(q=>q)"));
	
		} catch (IllegalLineException e) {
			System.err.println("Illegal line argument for Expression constructor");
			fail();
		}
		
		
		try {
			int[] numbers = new int[2];
			numbers[0] = 2;
			numbers[1] = 3;
			tree.getExpression(numbers);
		} catch (IndexOutOfBoundsException e) { 
            thrown = true;
        } 
		assertTrue(thrown);
	}
	
	public void testIllegalGoBack() {
		
		//tests to see if going back past the root line results in an exception thrown
		LineTree tree = new LineTree();
		boolean thrown = false;
		try {
			
			tree.addSubLine();
			tree.fill("show (((p=>q)=>q)=>((q=>p)=>p))", "show", new Expression("(q=>q)"));
			tree.addSubLine();
			tree.fill("assume ((p=>q)=>q)", "assume", new Expression("q"));
			tree.moveDown();
			tree.addSubLine();
			tree.fill("show (((p=>q)=>q)=>((q=>p)=>p))", "show", new Expression("(q=>q)"));
			tree.addSubLine();
			tree.fill("show (((p=>q)=>q)=>((q=>p)=>p))", "show", new Expression("(q=>q)"));
			tree.moveDown();
			tree.goBack();
			tree.goBack();
			tree.goBack();
			tree.goBack();
	   
		}  catch (IllegalLineException e) {
			System.err.println("Illegal line argument for Expression constructor");
			fail();
		}
		
		try {
				tree.goBack();
			} catch(NullPointerException e) {
				thrown = true;
			}
		assertTrue(thrown);
	}
	
}
