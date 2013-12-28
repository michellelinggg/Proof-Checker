
public class TheoremChecker {

	public static void checkPreTheorem(LineTree tree, String theorem, int[] lineone, int[] linetwo, Expression express) throws IllegalInferenceException{
		if (lineone == null)
			throw new IllegalInferenceException("line number can not be null");
		else if (theorem == null)
			throw new IllegalInferenceException ("theorem can't be null");
		else if (tree == null)
			throw new IllegalInferenceException("tree can't be null");
		else if (express == null)
			throw new IllegalInferenceException("expression can't be null");
		else if (theorem.equals("mt")){
			if (linetwo == null)
				throw new IllegalInferenceException("line number can not be null");
			ExprsNode expr = express.root();
			ExprsNode exprone = tree.getExpression(lineone).root();
			ExprsNode exprtwo = tree.getExpression(linetwo).root();
			if (exprone.symbol() == null)
				throw new IllegalInferenceException("expected operator got null");
			if (exprtwo.symbol() == null)
				throw new IllegalInferenceException("expected operator got null");
			if (exprone.symbol() != "~"){
				if (exprtwo.symbol() != "~")
					throw new IllegalInferenceException("expected one expression to be negated. got: " + exprone + " and " + exprtwo);
				if (exprone.symbol() != "=>")
					throw new IllegalInferenceException("expected one expression to be an implicaton. got: " + exprone + " and " + exprtwo);
				if (!exprone.subExprTwo().equals(exprtwo.negatedExpr()))
					throw new IllegalInferenceException("expected negated expressions and got:" + exprone.subExprOne() + " " + exprtwo.negatedExpr());
				if (expr.negatedExpr() == null)
					throw new IllegalInferenceException("expected expression to be negated and got null");
				if (!exprone.subExprOne().equals(expr.negatedExpr()))
					throw new IllegalInferenceException("expected expressions to match and got: " + exprone.subExprTwo() + " " + expr);
			}
			if (exprone.symbol().equals("~")){
				if (exprtwo.symbol() != "=>")
					throw new IllegalInferenceException("expected one expression to be an implication. got: " + exprone + " and " + exprtwo);
				if (!exprtwo.subExprTwo().equals(exprone.negatedExpr()))
					throw new IllegalInferenceException("expected negated expressions and got:" + exprtwo.subExprTwo() + " " + exprone.negatedExpr());
				if (expr.negatedExpr() == null)
					throw new IllegalInferenceException("expected negated expression to be proved");
				if (!exprtwo.subExprOne().equals(expr.negatedExpr()))
					throw new IllegalInferenceException("expected expressions to match and got: " + exprtwo.subExprOne() + " " + expr);
			}
		}

		else if (theorem.equals("mp")){
			ExprsNode expr = express.root();
			ExprsNode exprone = tree.getExpression(lineone).root();
			if (linetwo == null)
				throw new IllegalInferenceException("line number can not be null");
			ExprsNode exprtwo = tree.getExpression(linetwo).root();
			if (exprone.symbol() == null)
				throw new IllegalInferenceException("expected operator got null");
			if (exprtwo.symbol() == null)
				throw new IllegalInferenceException("expected operator got null");
			if (exprone.symbol() != "=>"){
				if (exprtwo.symbol() != "=>")
					throw new IllegalInferenceException("expected one expression to be an implicaton. got: " + exprone + " and " + exprtwo);
				if (!exprtwo.subExprOne().equals(exprone))
					throw new IllegalInferenceException("expected expressions to match and got: " + exprtwo.subExprOne() + " " + exprone);
				if (!exprtwo.subExprTwo().equals(expr))
					throw new IllegalInferenceException("expected expressions to match and got: " + exprtwo.subExprTwo() + " " + expr);
			}
			if (exprone.symbol().equals("=>")){
				if (!exprone.subExprOne().equals(exprtwo))
					throw new IllegalInferenceException("expected negated expressions and got: " + exprone.subExprOne() + " " + exprtwo);
				if (!exprone.subExprTwo().equals(expr))
					throw new IllegalInferenceException("expected expressions to match and got: " + exprone.subExprTwo() + " " + expr);
			}
		}

		else if (theorem.equals("ic")){
			ExprsNode expr = express.root();
			ExprsNode exprone = tree.getExpression(lineone).root();
			if (linetwo != null)
				throw new IllegalInferenceException("ic only takes one line number");
			if (expr.symbol() == null)
				throw new IllegalInferenceException("expected operator got null");
			if (expr.symbol() != "=>")
				throw new IllegalInferenceException("expected an implication to be made. got: " + expr);
			if (!exprone.equals(expr.subExprTwo()))
				throw new IllegalInferenceException("expected second expression to be the same. got: " + exprone + " " + expr.subExprTwo());
		}

		else if (theorem.equals("co")){
			ExprsNode expr = express.root();
			ExprsNode exprone = tree.getExpression(lineone).root();
			if (linetwo == null)
				throw new IllegalInferenceException("line number can not be null");
			ExprsNode exprtwo = tree.getExpression(linetwo).root();
			if (exprone.negatedExpr()== null){
				if(exprtwo.negatedExpr() ==  null)
					throw new IllegalInferenceException("expected two expressions to negate each other. got: " + exprone + " " + exprtwo);
				if (!exprtwo.negatedExpr().equals(exprone))
					throw new IllegalInferenceException("expected two expressions to negate each other. got: " + exprone + " " + exprtwo);
			}
			if (exprtwo.negatedExpr()== null){
				if(exprone.negatedExpr() ==  null)
					throw new IllegalInferenceException("expected two expressions to negate each other. got: " + exprone + " " + exprtwo);
				if (!exprone.negatedExpr().equals(exprtwo))
					throw new IllegalInferenceException("expected two expressions to negate each other. got: " + exprone + " " + exprtwo);
			}
		}

		else 
			throw new IllegalInferenceException("not a pre-given theorem");
	}

}


