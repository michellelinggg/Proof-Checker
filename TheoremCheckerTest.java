import junit.framework.TestCase;


public class TheoremCheckerTest extends TestCase {

	public void testPreTheoremPass(){
		try{
			LineTree tree = new LineTree();
			tree.addSubLine("show (((p=>q)=>q)=>((q=>p)=>p))", "show", new Expression("(((p=>q)=>q)=>((q=>p)=>p))"));
			tree.addSubLine("assume ((p=>q)=>q)", "assume", new Expression("((p=>q)=>q)"));
			tree.addSubLine("show ((q=>p)=>p)", "show", new Expression("((q=>p)=>p)"));
			tree.moveDown();
			tree.addSubLine("assume (q=>p)", "assume", new Expression("(q=>p)"));
			tree.addSubLine("show p", "show", new Expression("p"));
			tree.moveDown();
			tree.addSubLine("assume ~p", "assume", new Expression("~p"));
			int[] one = new int[3];
			int[] two = new int[2];
			one[0] =3;
			one[1] =2;
			one[2] =1;
			two[0] =3;
			two[1] =1;
			TheoremChecker.checkPreTheorem(tree, "mt", one, two, new Expression("~q"));
			tree.addSubLine("mt 3.2.1 3.1 ~q", "mt", new Expression("~q"));
			int[] three = new int[1];
			int[] four = new int[3];
			three[0] =2;
			four[0] =3;
			four[1] =2;
			four[2] =2;
			TheoremChecker.checkPreTheorem(tree, "mt", three, four, new Expression("~(p=>q)"));
			tree.addSubLine("mt 2 3.2.2 ~(p=>q)", "mt", new Expression("~(p=>q)"));
			tree.addSubLine("show (p=>q)", "show", new Expression("(p=>q)"));
			tree.moveDown();
			tree.addSubLine("assume p", "assume", new Expression("p"));
			int[] five = new int[4];
			int[] six = new int[3];
			five[0] =3;
			five[1] =2;
			five[2] =4;
			five[3] =1;
			six[0] =3;
			six[1] =2;
			six[2] =1;
			TheoremChecker.checkPreTheorem(tree, "co", five, six, new Expression("(p=>q)"));
			tree.addSubLine("co 3.2.4.1 3.2.1 (p=>q)", "co", new Expression("(p=>q)"));
			int[] seven = new int[3];
			int[] eight = new int[3];
			seven[0] =3;
			seven[1] =2;
			seven[2] =4;
			eight[0] =3;
			eight[1] =2;
			eight[2] =3;
			TheoremChecker.checkPreTheorem(tree, "co", seven, eight, new Expression("p"));
			int[] nine = new int[2];
			nine[0] =3;
			nine[1] =2;
			tree.moveUp();
			TheoremChecker.checkPreTheorem(tree, "ic", nine, null, new Expression("((q=>p)=>p)"));
			tree.addSubLine("ic 3.2 ((q=>p)=>p)", "ic", new Expression("((q=>p)=>p)"));
			tree.moveUp();
			int[] ten = new int[1];
			ten[0] =3;
			TheoremChecker.checkPreTheorem(tree, "ic", ten, null, new Expression("(((p=>q)=>q)=>((q=>p)=>p))"));
			LineTree tree2 = new LineTree();
			tree2.addSubLine("show (p=>((p=>q)=>q))", "show", new Expression("(p=>((p=>q)=>q))"));
			tree2.addSubLine("assume p", "assume", new Expression("p"));
			tree2.addSubLine("show ((p=>q)=>q)", "show", new Expression("((p=>q)=>q)"));
			tree2.moveDown();
			tree2.addSubLine("assume (p=>q)", "assume", new Expression("(p=>q)"));
			tree2.addSubLine("show q", "show", new Expression("q"));
			int[] ele = new int[1];
			int[] twe = new int[2];
			ele[0] =2;
			twe[0] =3;
			twe[1] =1;
			TheoremChecker.checkPreTheorem(tree2, "mp", ele, twe, new Expression("q"));
			tree2.moveDown();
			tree2.addSubLine("mp 2 3.1 q", "mp", new Expression("q"));
			tree2.moveUp();
			int[] thir = new int[2];
			thir[0] =3;
			thir[1] =2;
			TheoremChecker.checkPreTheorem(tree2, "ic", thir, null, new Expression("((p=>q)=>q)"));
			tree2.addSubLine("ic 3.2 ((p=>q)=>q)", "ic", new Expression("((p=>q)=>q)"));
			int[] fourt = new int[1];
			fourt[0] =3;
			TheoremChecker.checkPreTheorem(tree2, "ic", fourt, null, new Expression("(q=>((p=>q)=>q))"));
		}catch(IllegalLineException e){
			e.printStackTrace();
			assertFalse(true);
		}catch(IllegalInferenceException err){
			err.printStackTrace();
			assertFalse(true);
		}
	}

	public void testPreTheoremsFail(){
		try{
			LineTree tree = new LineTree();
			tree.addSubLine("show (((p=>q)=>q)=>((q=>p)=>p))", "show", new Expression("(((p=>q)=>q)=>((q=>p)=>p))"));
			tree.addSubLine("assume ((p=>q)=>q)", "assume", new Expression("((p=>q)=>q)"));
			tree.addSubLine("show ((q=>p)=>p)", "show", new Expression("((q=>p)=>p)"));
			tree.moveDown();
			tree.addSubLine("assume (q=>p)", "assume", new Expression("(q=>p)"));
			tree.addSubLine("show p", "show", new Expression("p"));
			tree.moveDown();
			tree.addSubLine("assume ~p", "assume", new Expression("~p"));
			int[] one = new int[3];
			int[] two = new int[2];
			one[0] =3;
			one[1] =2;
			one[2] =1;
			two[0] =3;
			two[1] =1;
			assertThrowsException(tree, "mp", one, two, new Expression("~q"));
			assertThrowsException(tree, "mt", one, two, new Expression("q"));
			tree.addSubLine("mt 3.2.1 3.1 ~q", "mt", new Expression("~q"));
			int[] three = new int[1];
			int[] four = new int[3];
			three[0] =2;
			four[0] =3;
			four[1] =2;
			four[2] =2;
			assertThrowsException(tree, "mt", null, four, new Expression("~(p=>q)"));
			assertThrowsException(tree, "mt", three, null, new Expression("~(p=>q)"));
			assertThrowsException(tree, "mt", three, four, new Expression("~~(p=>q)"));
			assertThrowsException(tree, "mt", three, four, new Expression("(p=>q)"));
			tree.addSubLine("mt 2 3.2.2 ~(p=>q)", "mt", new Expression("~(p=>q)"));
			tree.addSubLine("show (p=>q)", "show", new Expression("(p=>q)"));
			tree.moveDown();
			tree.addSubLine("assume p", "assume", new Expression("p"));
			int[] five = new int[4];
			int[] six = new int[2];
			five[0] =3;
			five[1] =2;
			five[2] =4;
			five[3] =1;
			six[0] =3;
			six[1] =2;
			assertThrowsException(tree, "co", five, six, new Expression("(p=>q)"));
			tree.addSubLine("co 3.2.4.1 3.2.1 (p=>q)", "co", new Expression("(p=>q)"));
			int[] seven = new int[3];
			int[] eight = new int[3];
			seven[0] =3;
			seven[1] =2;
			seven[2] =4;
			eight[0] =3;
			eight[1] =2;
			eight[2] =3;
			assertThrowsException(tree, "ic", seven, eight, new Expression("p"));
			int[] nine = new int[2];
			nine[0] =3;
			nine[1] =2;
			tree.moveUp();
			assertThrowsException(tree, "ic", nine, null, new Expression("(p=>(q=>p))"));
			tree.addSubLine("ic 3.2 ((q=>p)=>p)", "ic", new Expression("((q=>p)=>p)"));
			tree.moveUp();
			int[] ten = new int[1];
			ten[0] =3;
			assertThrowsException(tree, "ic", null, null, new Expression("(((p=>q)=>q)=>((q=>p)=>p))"));
			LineTree tree2 = new LineTree();
			tree2.addSubLine("show (p=>((p=>q)=>q))", "show", new Expression("(p=>((p=>q)=>q))"));
			tree2.addSubLine("assume p", "assume", new Expression("p"));
			tree2.addSubLine("show ((p=>q)=>q)", "show", new Expression("((p=>q)=>q)"));
			tree2.moveDown();
			tree2.addSubLine("assume (p=>q)", "assume", new Expression("(p=>q)"));
			tree2.addSubLine("show q", "show", new Expression("q"));
			int[] ele = new int[1];
			int[] twe = new int[2];
			ele[0] =2;
			twe[0] =3;
			twe[1] =1;
			assertThrowsException(tree2, "oo", ele, twe, new Expression("q"));
			assertThrowsException(tree2, null, ele, twe, new Expression("q"));
			assertThrowsException(tree2, "", ele, twe, new Expression("q"));
			assertThrowsException(null, "mp", ele, twe, new Expression("q"));
			assertThrowsException(tree2, "mp", ele, twe, null);
			assertThrowsException(tree2, "mp", ele, twe, new Expression("~q"));
			tree2.moveDown();
			tree2.addSubLine("mp 2 3.1 q", "mp", new Expression("q"));
			tree2.moveUp();
			int[] thir = new int[2];
			thir[0] =3;
			thir[1] =2;
			assertThrowsException(tree2, "ic", thir, twe, new Expression("((p=>q)=>q)"));
			tree2.addSubLine("ic 3.2 ((p=>q)=>q)", "ic", new Expression("((p=>q)=>q)"));
			int[] fourt = new int[1];
			fourt[0] =3;
			assertThrowsException(tree2, "ic", fourt, null, new Expression("(q=>((p=>q)=>p))"));
		}catch (IllegalLineException err){
			fail("unexpected Illegal Line Exception");
		}
		
	}
	
	private void assertThrowsException(LineTree tree, String theorem, int[] lineone, int[] linetwo, Expression express){
		try{
			TheoremChecker.checkPreTheorem(tree, theorem, lineone, linetwo, express);
			fail("Should have thrown an exception");
		} catch (IllegalInferenceException e){
			assertTrue(true);
		}
	}
	
}
