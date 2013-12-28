public class Expression {
	
	private ExprsNode myRoot;
	public ExprsNode root () {return myRoot;}
	
	public String toString(){
		return myRoot.toString();
	}

	public Expression(String s) throws IllegalLineException{
		if (s!=null)
		myRoot =  new ExprsNode(s);
		else
		throw new IllegalLineException("can not input null");
	}
	
}


class ExprsNode{

	private String mySymbol;
	private ExprsNode myNegatedExpr;
	private ExprsNode[] mySubExprs;
	
	public String symbol () {return mySymbol;}
	public ExprsNode negatedExpr() {return myNegatedExpr;}
	public ExprsNode subExprOne() {return mySubExprs[0];}
	public ExprsNode subExprTwo() {return mySubExprs[1];}
	
	public ExprsNode(String s) throws IllegalLineException {
		if (s.length() < 1 || s == null)
			throw new IllegalLineException("empty expression");
		if (Character.isLetter(s.charAt(0))){
			if (s.length() != 1){
				throw new IllegalLineException("expected single character variable, got: "+ s);
			}
			mySymbol = String.valueOf(s.charAt(0));
		} else if (s.charAt(0) == '~'){
			myNegatedExpr = new ExprsNode(s.substring(1));
			mySymbol = "~";
		} else if (s.charAt(0) == '('){ // binary expression
			if (!s.endsWith(")"))
					throw new IllegalLineException("expected ) at end of " + s);
			int nesting = 0;
			int opPos = 0;
			for (int k=1; k<s.length()-1; k++) {
				if (s.charAt(k) == '(')
					nesting++;
				else if (s.charAt(k) == ')')
					nesting--;
				else if (nesting == 0){
					if (s.length() < k+2)
						throw new IllegalLineException("no operator found in " + s);
					if ("&".equals(s.substring(k, k+1))){
						opPos = k;
						mySymbol = "&";
						break;
					}
					else if ("|".equals(s.substring(k, k+1))){
						opPos = k;
						mySymbol = "|";
						break;
					}
					if (s.length() < k+3)
						throw new IllegalLineException("no operator found in " + s);
					if ("=>".equals(s.substring(k, k+2))){
						opPos = k;
						mySymbol = "=>";
						break;
					}
				}
			}
			if (mySymbol==null)
				throw new IllegalLineException("no operator found in " + s);
			mySubExprs = new ExprsNode [] {
				new ExprsNode(s.substring(1, opPos)),
				new ExprsNode(s.substring(opPos+mySymbol.length(), s.length()-1))
			};
		} else {
			throw new IllegalLineException("invalid first character in " + s);
		}
	}
	
	public String toString () {
		if (mySymbol.equals("|") || mySymbol.equals("&") || mySymbol.equals("=>"))
			return "(" + mySubExprs[0] + mySymbol + mySubExprs[1] + ")";
		if (mySymbol.equals("~"))
			return mySymbol + myNegatedExpr;
		return mySymbol;
	}
	
	public boolean equals (Object o){
		ExprsNode expr = (ExprsNode) o;
		return expr.toString().equals(this.toString());
	}
}