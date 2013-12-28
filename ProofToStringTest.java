import static org.junit.Assert.*;

import org.junit.Test;


public class ProofTest {

	@Test
	public void testToString() {
		try {
			TheoremSet myTheorems = new TheoremSet();
			Proof p = new Proof(myTheorems);
			p.extendProof("show (p=>(~p=>q))");
			p.isComplete();
			p.extendProof("assume p");
			p.isComplete();
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

}