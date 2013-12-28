import static org.junit.Assert.*;

import org.junit.Test;


public class ProofTest {
	
	
	@Test
	public void testextendProof(){
		TheoremSet myTheorems = new TheoremSet ( );
		Proof a = new Proof(myTheorems);
		ProofTest.extendProoftestbadinput(a, "");
		ProofTest.extendProoftestbadinput(a, "s");
		ProofTest.extendProoftestbadinput(a, "sdjfjdfask");
		ProofTest.extendProoftestbadinput(a, "show");
		ProofTest.extendProoftestbadinput(a, "show (p=>q) (p=>k)");
		ProofTest.extendProoftestgoodinput(a, "show (p=>q)");
		assertFalse(a.isComplete());
		ProofTest.extendProoftestbadinput(a, "assume q");
		ProofTest.extendProoftestbadinput(a, "assume ~p");
		ProofTest.extendProoftestbadinput(a, "assume p p");
		ProofTest.extendProoftestgoodinput(a, "assume p");
		assertFalse(a.isComplete());
		ProofTest.extendProoftestgoodinput(a, "show q");
		assertFalse(a.isComplete());
		ProofTest.extendProoftestbadinput(a, "assume ~~q");
		ProofTest.extendProoftestgoodinput(a, "assume ~q");
		assertFalse(a.isComplete());
		
		Proof b = new Proof(myTheorems);
		ProofTest.extendProoftestgoodinput(b, "show ((a&(p=>q))=>c)");
		assertFalse(b.isComplete());
		ProofTest.extendProoftestbadinput(b, "assume c");
		ProofTest.extendProoftestbadinput(b, "assume (a&p)");
		ProofTest.extendProoftestbadinput(b, "assume (p=>q)");
		ProofTest.extendProoftestgoodinput(b, "assume (a&(p=>q))");
		assertFalse(b.isComplete());
		ProofTest.extendProoftestbadinput(b, "assume (a&p)");
		ProofTest.extendProoftestgoodinput(b, "show ~~q");
		assertFalse(b.isComplete());
		ProofTest.extendProoftestbadinput(b, "assume ~~q");
		ProofTest.extendProoftestbadinput(b, "assume q");
		ProofTest.extendProoftestbadinput(b, "assume ~q");
		ProofTest.extendProoftestbadinput(b, "assume p");
		
		Proof c = new Proof(myTheorems);
		ProofTest.extendProoftestgoodinput(c, "show ((p&b)|t)");
		assertFalse(c.isComplete());
		ProofTest.extendProoftestbadinput(c, "assume ~~((p&b)|t)");
		ProofTest.extendProoftestbadinput(c, "assume ~~~((p&b)|t)");
		ProofTest.extendProoftestbadinput(c, "assume ~(p&b)");
		ProofTest.extendProoftestbadinput(c, "assume ~((p&t)|t)");
		ProofTest.extendProoftestgoodinput(c, "assume ~((p&b)|t)");
		
		Proof d = new Proof(myTheorems);
		ProofTest.extendProoftestgoodinput(d, "show (p=>p)");
		assertFalse(d.isComplete());
		ProofTest.extendProoftestgoodinput(d, "show (p=>p)");
		assertFalse(d.isComplete());
		ProofTest.extendProoftestgoodinput(d, "assume p");
		assertFalse(d.isComplete());
		ProofTest.extendProoftestgoodinput(d, "ic 2.1 (p=>p)");
		assertFalse(d.isComplete());
		ProofTest.extendProoftestbadinput(d, "repeat 2.1.2 (p=>p)");
		ProofTest.extendProoftestbadinput(d, "repeat 2.1 (p=>p)");
		ProofTest.extendProoftestbadinput(d, "repeat 1 (p=>p)");
		ProofTest.extendProoftestbadinput(d, "repeat 2 (p=>q)");
		ProofTest.extendProoftestgoodinput(d, "repeat 2 (p=>p)");
		assertTrue(d.isComplete());
		
		
	}
	public static void extendProoftestgoodinput(Proof a, String input){
		try{
			a.extendProof(input);
		}catch(IllegalInferenceException e1){
			fail();
		}catch(IllegalLineException e2){
			fail();
		}
	}
	public static void extendProoftestbadinput(Proof a, String input){
		try{
			
			a.extendProof(input);
			fail();
			}catch(IllegalInferenceException e1){
				System.out.println(e1.getMessage());
			}catch(IllegalLineException e2){
				System.out.println(e2.getMessage());
			}
	}
	
	public void testtheorem_handling(){
		
	}
	public void testSubproofisComplete () {

	    TheoremSet set1 = new TheoremSet();  
	    TheoremSet set2 = new TheoremSet(); 
	    Proof p1 = new Proof(set1);  
	    Proof p2 = new Proof(set2); 
	        
	
        try { 
            Expression e1 = new Expression("(~p=>(q|~p)");  
            Expression e2 = new Expression("(~~a=>a)"); 
            set1.put("testTheorem", e1);  
            set2.put("dn", e2); 
        } catch (IllegalLineException err){ 
            System.err.println("Error with expression made."); 
        } 
        try {  
            p1.extendProof("show (p=>(~p=>q))");  
            p1.isComplete(); 
            assertEquals("subProofisComplete failed.", "2", p1.nextLineNumber().toString()); 
            p1.extendProof("assume p");  
            p1.isComplete(); 
            assertEquals("subProofisComplete failed.", "3", p1.nextLineNumber().toString()); 
            p1.extendProof("show (~p=>q)");  
            p1.isComplete(); 
            assertEquals("subProofisComplete failed.", "3.1", p1.nextLineNumber().toString()); 
            p1.extendProof("assume ~p");  
            p1.isComplete(); 
            assertEquals("subProofisComplete failed.", "3.2", p1.nextLineNumber().toString()); 
            p1.extendProof("co 2 3.1 (~p=>q)");  
            p1.isComplete(); 
            assertEquals("subProofisComplete failed to move up the tree.", "4", p1.nextLineNumber().toString()); 
            p1.extendProof("ic 3 (p=>(~p=>q))");  
            p1.isComplete(); 
              
              
            p2.extendProof("show (a=>~~a)"); 
            p2.isComplete(); 
            assertEquals("subProofisComplete failed.", "2", p2.nextLineNumber().toString()); 
            p2.extendProof("assume a"); 
            p2.isComplete(); 
            assertEquals("subProofisComplete failed.", "3", p2.nextLineNumber().toString()); 
            p2.extendProof("show ~~a"); 
            p2.isComplete(); 
            assertEquals("subProofisComplete failed.", "3.1", p2.nextLineNumber().toString()); 
            p2.extendProof("assume ~~~a"); 
            p2.isComplete(); 
            assertEquals("subProofisComplete failed.", "3.2", p2.nextLineNumber().toString()); 
            p2.extendProof("dn (~~~a=>~a)"); 
            p2.isComplete(); 
            assertEquals("subProofisComplete failed.", "3.3", p2.nextLineNumber().toString()); 
            p2.extendProof("mp 3.2 3.1 ~a"); 
            p2.isComplete(); 
            assertEquals("subProofisComplete failed.", "3.4", p2.nextLineNumber().toString()); 
            p2.extendProof("co 2 3.3 ~~a"); 
            p2.isComplete(); 
            assertEquals("subProofisComplete failed to move up the tree.", "4", p2.nextLineNumber().toString()); 
            p2.extendProof("ic 3 (a=>~~a)"); 
            p2.isComplete(); 
              
            } catch (IllegalLineException e){  
                System.err.println("Illegal line argument.");  
            } catch (IllegalInferenceException f){  
                System.err.println("Illegal inference created.");  
            }  
	}
	 public void testIsComplete () {
	
		 	TheoremSet set1 = new TheoremSet();  
		    TheoremSet set2 = new TheoremSet(); 
		    Proof p1 = new Proof(set1);  
		    Proof p2 = new Proof(set2); 
		        
	        try { 
	            Expression e1 = new Expression("(~p=>(q|~p)");  
	            Expression e2 = new Expression("(~~a=>a)"); 
	            set1.put("testTheorem", e1);  
	            set2.put("dn", e2); 
	        } catch (IllegalLineException err){ 
	            System.err.println("Error with expression made."); 
	        } 
	        try {  
	            p1.extendProof("show (p=>(~p=>q))");  
	            assertFalse("isComplete() failed.", p1.isComplete());  
	            p1.extendProof("assume p");  
	            assertFalse("isComplete() failed.", p1.isComplete());  
	            p1.extendProof("show (~p=>q)");  
	            assertFalse("isComplete() failed.", p1.isComplete());  
	            p1.extendProof("assume ~p");  
	            assertFalse("isComplete() failed.", p1.isComplete());  
	            p1.extendProof("co 2 3.1 (~p=>q)");  
	            assertFalse("isComplete() failed.", p1.isComplete());  
	            p1.extendProof("ic 3 (p=>(~p=>q))");  
	            assertTrue("isComplete() failed, but should have passed.", p1.isComplete());  
	            assertTrue("isComplete() failed, but should have passed.", p1.isComplete());  
	            //The second assert makes sure it can continue to pass!  
	              
	              
	            p2.extendProof("show (a=>~~a)"); 
	            assertFalse("isComplete() failed.", p2.isComplete());  
	            p2.extendProof("assume a"); 
	            assertFalse("isComplete() failed.", p2.isComplete()); 
	            p2.extendProof("show ~~a"); 
	            assertFalse("isComplete() failed.", p2.isComplete()); 
	            p2.extendProof("assume ~~~a"); 
	            assertFalse("isComplete() failed.", p2.isComplete()); 
	            p2.extendProof("dn (~~~a=>~a)"); 
	            assertFalse("isComplete() failed.", p2.isComplete()); 
	            p2.extendProof("mp 3.2 3.1 ~a"); 
	            assertFalse("isComplete() failed.", p2.isComplete()); 
	            p2.extendProof("co 2 3.3 ~~a"); 
	            assertFalse("isComplete() failed.", p2.isComplete()); 
	            p2.extendProof("ic 3 (a=>~~a)"); 
	            assertTrue("isComplete() failed, but should have passed.", p2.isComplete());  
	              
	            } catch (IllegalLineException e){  
	                System.err.println("Illegal line argument.");  
	            } catch (IllegalInferenceException f){  
	                System.err.println("Illegal inference created.");  
	            }  
	 }
	 public void testToString() { 
	        try { 
	            TheoremSet myTheorems = new TheoremSet(); 
	            Proof p = new Proof(myTheorems); 
	            p.extendProof("show (p=>(~p=>q))"); 
	            p.isComplete();
	            p.extendProof("print");
	            p.isComplete();
	            p.extendProof("assume p"); 
	            p.isComplete();
	            assertEquals("toString method does not work properly.", p.toString(),"1\tshow (p=>(~p=>q))\n2\tassume p\n");
	            p.extendProof("show (~p=>q)"); 
	            p.isComplete(); 
	            p.extendProof("assume ~p"); 
	            p.isComplete(); 
	            p.extendProof("co 2 3.1 (~p=>q)"); 
	            p.isComplete(); 
	            p.extendProof("ic 3 (p=>(~p=>q))"); 
	            p.isComplete(); 
	              
	              
	           assertEquals("toString method does not work properly.", p.toString(),"1\tshow (p=>(~p=>q))\n2\tassume p\n3\tshow (~p=>q)\n3.1\tassume ~p\n3.2\tco 2 3.1 (~p=>q)\n4\tic 3 (p=>(~p=>q))\n"); 
	             
	             
	        } catch (IllegalLineException e) { 
	            System.err.println("Illegal line argument for Expression constructor"); 
	            fail(); 
	        } 
	        catch (IllegalInferenceException e) { 
	            System.err.println("Illegal inference"); 
	            fail(); 
	        } 
	    }
	 public void testExtendProofTheorems() { 
	        TheoremSet myTheorems = new TheoremSet(); 
	        Proof test = new Proof(myTheorems); 
	        String line = "show (((p=>q)=>q)=>((q=>p)=>p)"; 
	        assertErrorThrown(line, test); 
	        assertErrorThrown("show (a=>b)(b)", test); 
	        assertOk("show (((p=>q)=>q)=>((q=>p)=>p))", test); 
	        assertOk("assume ((p=>q)=>q)", test); 
	        assertErrorThrown("", test); 
	        assertOk("show ((q=>p)=>p)", test); 
	        assertErrorThrown("assume(q=>p)", test); 
	        assertOk("assume (q=>p)", test); 
	        assertErrorThrown("assume q", test); 
	        assertErrorThrown("ic p", test); //first time checking for incorrect use of theorems 
	        assertOk("show p", test); 
	        assertErrorThrown("assume ~~p", test); 
	        assertOk("assume ~p", test); 
	        assertErrorThrown("mp 3.2.1 3.1 ~q", test); 
	        assertErrorThrown("mt 3.2 3.1 ~q", test); 
	        assertErrorThrown("mt 3.2.1 3.2.2 ~q", test); //illegal access 
	        assertErrorThrown("mt 3.2.1 3.5 ~q", test); //line doesn't exist 
	        assertErrorThrown("ic 3.2.1 3.1 ~q", test); 
	        assertErrorThrown("mt 3.1 ~q", test); 
	        assertErrorThrown("mt 3.2.1 3.1", test); 
	        assertErrorThrown("mt     ", test); 
	        assertErrorThrown("mt", test); 
	        assertOk("mt 3.2.1 3.1 ~q", test); 
	        assertOk("mt 2 3.2.2 ~(p=>q)", test); 
	        assertOk("show (p=>q)", test); 
	        assertOk("assume p", test); 
	        assertOk("co 3.2.4.1 3.2.1 (p=>q)", test); 
	        assertErrorThrown("co 3.2.4.1 3.2.3 p", test); //illegal access 
	        assertErrorThrown("co 3&3 3.2.3 p", test);       
	    } 
	  
	    public void testTheoremHandling(){ 
	        TheoremSet myTheorems = new TheoremSet(); 
	        try{ 
	        myTheorems.put("buildAnd", new Expression("(a=>(b=>(a&b)))")); 
	        myTheorems.put("demorgan2", new Expression("((~a&~b)=>~(a|b))")); 
	        }catch (IllegalLineException e){ 
	            fail("unexpected Illegal Line Exception"); 
	        } 
	        Proof test = new Proof(myTheorems); 
	        assertOk("show ((p|q)=>(~p=>q))", test); 
	        assertErrorThrown("buildAnd 1 (a=>(b=>(a&b)))", test); 
	        assertOk("assume (p|q)", test); 
	        assertOk("show (~p=>q)", test); 
	        assertOk("assume ~p", test); 
	        assertOk("show q", test); 
	        assertOk("assume ~q", test); 
	        assertErrorThrown("buildAnd (~q=>(~q=>(~p&~q)))",test); 
	        assertErrorThrown("buildAnd (~~p=>(~q=>(~p&~q)))",test); 
	        assertErrorThrown("buildAnd (p=>(~q=>(~p&~q)))",test); 
	        assertErrorThrown("buildAnd (~p=>(~q=>(~p|~q)))",test); 
	        assertErrorThrown("buildAnd 3.2.1",test); 
	        assertErrorThrown("buildAnd   ",test); 
	        assertErrorThrown("buildAnd afw&&aajc",test); 
	        assertErrorThrown("buildAnd (~~p=>(  ~q=>(~p&~q)))",test); 
	        assertErrorThrown("buildAnd ((q=>(~p&q))=>~p)",test); 
	        assertErrorThrown("buildAnd ((~a&~b)=>~(a|b))",test); 
	        assertOk("buildAnd (~p=>(~q=>(~p&~q)))", test); 
	        assertOk("mp 3.1 3.2.2. (~q=>(~p&~q))", test); 
	        assertOk("mp 3.2.3 3.2.1 (~p&~q)", test); 
	        assertErrorThrown("demorgan2 ((~p&~q )=>~(p|q))", test); 
	        assertErrorThrown("demorgan2 ((~p&~q)=>~(p|q)", test); 
	        assertErrorThrown("demorgan2 ((~p|~q)=>~(p&q))", test); 
	        assertOk("demorgan2 ((~p&~q)=>~(p|q))", test); 
	    } 
	      
	    public void testNextLineNumber(){ 
	        TheoremSet myTheorems = new TheoremSet(); 
	        Proof test = new Proof(myTheorems); 
	        assertTrue(test.nextLineNumber().toString().equals("1")); 
	        assertOk("show (((p=>q)=>q)=>((q=>p)=>p))", test); 
	        assertTrue(test.nextLineNumber().toString().equals("2")); 
	        assertErrorThrown("assume (p|q)", test); 
	        assertErrorThrown("assume (p=>q)", test); 
	        assertTrue(test.nextLineNumber().toString().equals("2")); 
	        assertOk("assume ((p=>q)=>q)", test); 
	        assertTrue(test.nextLineNumber().toString().equals("3")); 
	        assertOk("show ((q=>p)=>p)", test); 
	        assertTrue(test.nextLineNumber().toString().equals("3.1")); 
	        assertErrorThrown("assume (p=>q)", test); 
	        assertTrue(test.nextLineNumber().toString().equals("3.1")); 
	        assertOk("assume (q=>p)", test); 
	        assertTrue(test.nextLineNumber().toString().equals("3.2")); 
	        assertOk("show p", test); 
	        assertTrue(test.nextLineNumber().toString().equals("3.2.1")); 
	        assertOk("assume ~p", test); 
	        assertErrorThrown("", test);
	        assertTrue(test.nextLineNumber().toString().equals("3.2.2")); 
	        assertOk("mt 3.2.1 3.1 ~q", test); 
	        assertOk("mt 2 3.2.2 ~(p=>q)", test); 
	        assertOk("show (p=>q)", test); 
	        assertTrue(test.nextLineNumber().toString().equals("3.2.4.1")); 
	        assertOk("assume p", test); 
	        assertTrue(test.nextLineNumber().toString().equals("3.2.4.2")); 
	        assertOk("print", test); //should not change line number 
	        assertTrue(test.nextLineNumber().toString().equals("3.2.4.2")); 
	        assertOk("co 3.2.4.1 3.2.1 (p=>q)", test); 
	        assertTrue(test.nextLineNumber().toString().equals("3.2.5")); 
	        assertOk("co 3.2.4 3.2.3 p", test); 
	        assertTrue(test.nextLineNumber().toString().equals("3.3")); 
	        assertOk("ic 3.2 ((q=>p)=>p)", test); 
	        assertTrue(test.nextLineNumber().toString().equals("4")); 
	        assertOk("ic 3 (((p=>q)=>q)=>((q=>p)=>p))", test); 
	    } 
	  
	    public void assertErrorThrown(String s, Proof test){ 
	    try { 
	        test.extendProof (s); 
	        fail("should not have been a legal line"); 
	    } catch (IllegalLineException e) { 
	        System.out.println(e.getMessage()); 
	        assertTrue(true); 
	    } catch (IllegalInferenceException e) { 
	        System.out.println(e.getMessage()); 
	        assertTrue(true); 
	    } 
	    } 
	      
	    public void assertOk(String s, Proof test){ 
	        try { 
	            test.extendProof (s); 
	            test.isComplete(); 
	            assertTrue(true); 
	        } catch (IllegalLineException e) { 
	            fail("should not have failed."); 
	        } catch (IllegalInferenceException e) { 
	            fail("should not have failed"); 
	        } 
	        } 
}
