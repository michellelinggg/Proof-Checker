import java.util.*;



public class Proof {
	Boolean firstshow;
	TheoremSet mytheorems;
	LineTree tree;
	Boolean have_to_move_down = false;
	Boolean isPrint = false;

	public Proof (TheoremSet theorems) {
		firstshow = true;
		mytheorems = theorems;
		tree = new LineTree();
		tree.addSubLine();
	}

	public LineNumber nextLineNumber ( ) {

		return tree.getLastSubLine();
	}

	public void extendProof (String x) throws IllegalLineException, IllegalInferenceException {
		String reason;
		String[] input = x.split(" ");
		reason = input[0];
		if (firstshow && !reason.equals("show")){
			throw new IllegalLineException("The first line has to begin with show");
		}
		if (reason.equals("show")){
			if (input.length != 2){
				throw new IllegalLineException("Wrong number of input");
			}
			if(firstshow){
				tree.fill(x, reason, new Expression(input[1]));
				firstshow = false;
				// We dont have to move down the tree here since its the first show.
			}else{
				tree.fill(x, reason, new Expression(input[1])); // add the show expression;
				have_to_move_down = true;
				

			}

		}else if (reason.equals("assume")){
			if(input.length != 2){
				throw new IllegalLineException("Wrong number of input");
			}
			// check whether the previous line is a show;
			if (!tree.getPrevious().getReason().equals("show")){
				throw new IllegalLineException("The line before assume has to begin with 'show'");
			// check if the expression is not the one on the right of show expression
			}else if (tree.getPrevious().getExpression().root().symbol().equals("=>") && !tree.getPrevious().getExpression().root().subExprOne().equals(new Expression(input[1]).root())){ // goback to the line with show
				throw new IllegalLineException("You can only assume the left side of the expression you want to show");
			}else if (!tree.getPrevious().getExpression().root().symbol().equals("=>")){
				if (new Expression(input[1]).root().negatedExpr() == null || !new Expression(input[1]).root().negatedExpr().equals(tree.getPrevious().getExpression().root())){
					throw new IllegalLineException("Only ~E can be assumed after show E");
				}
			}
			tree.fill(x, reason, new Expression(input[1])); // add the assume expression
			
		}else if (reason.equals("mp") || reason.equals("mt") || reason.equals("co")){
			if (input.length != 4){
				throw new IllegalLineException("Wrong number of input");
			}
			int[] linenumber1;
			int[] linenumber2;
			try{
			if (input[1].length() != 1){
			String[] number1 = input[1].split("\\.");
			linenumber1 = new int[number1.length];
			for(int j =0; j< number1.length; j++){
				linenumber1[j] = Integer.parseInt(number1[j]);
			}
			}else{
				linenumber1 = new int[1];
				linenumber1[0] = Integer.parseInt(input[1]);
			}
			if (input[2].length() != 1){
			String[] number2 = input[2].split("\\.");
			linenumber2 = new int[number2.length];
			for(int j = 0; j<number2.length; j++){
				
				linenumber2[j] = Integer.parseInt(number2[j]);
			}
			}else {
				linenumber2 = new int[1];
				linenumber2[0] = Integer.parseInt(input[2]);
			}
			}catch(NumberFormatException e){
				throw new IllegalLineException("line number has to be a number");
			}
			// linenumber1 and linenumber2 are int array.
			// at this point, they can be passed to the method that handle mp mt or co.

			int[] curr_number = tree.getCurrentLineNumber();
			if(curr_number.length < linenumber1.length || curr_number.length < linenumber2.length){
				throw new IllegalLineException("Illegal access to a line");
			}
			for (int i = 0; i < linenumber1.length; i++){
				if (linenumber1[i] > curr_number[i]){
					throw new IllegalLineException("The line doesn't exist");
				}else if (i == (linenumber1.length-1) && linenumber1[i] >= curr_number[i]){
					throw new IllegalLineException("The line doesn't exist");
				}
				}

			for (int i = 0; i < linenumber2.length; i++){
				if (linenumber2[i] > curr_number[i]){
					throw new IllegalLineException("The line doesn't exist");
				}else if (i == (linenumber2.length-1) && linenumber2[i] >= curr_number[i]){
					throw new IllegalLineException("The line doesn't exist");
				}	
				}
			
			try{
				TheoremChecker.checkPreTheorem(tree, reason, linenumber1, linenumber2, new Expression(input[3]));
			}catch(IllegalInferenceException e){
				throw e;
			}catch(IllegalLineException err){
				throw err;
			}
			tree.fill(x, reason, new Expression(input[3]));
	
			
		}else if (reason.equals("ic")){
			if (input.length != 3){
				throw new IllegalLineException("Wrong number of input");
			}
			int[] linenumber;
			String[] number = input[1].split("\\.");
			linenumber = new int[number.length];
			for(int j =0; j< number.length; j++){
				linenumber[j] = Integer.parseInt(number[j]);
			}
			int[] curr_number = tree.getCurrentLineNumber();
			if(curr_number.length < linenumber.length){
				throw new IllegalLineException("Illegal access to a line");
			}
			for (int i = 0; i < linenumber.length; i++){
				if (linenumber[i] > curr_number[i]){
					throw new IllegalLineException("The line doesn't exist");
				}else if (i == (linenumber.length-1) && linenumber[i] >= curr_number[i]){
					throw new IllegalLineException("The line doesn't exist");
				}
				}
			try{
				TheoremChecker.checkPreTheorem(tree, reason, linenumber, null, new Expression(input[2]));
			}catch(IllegalInferenceException e){
				throw e;
			}catch(IllegalLineException err){
				throw err;
			}
			tree.fill(x, reason, new Expression(input[2]));
			
				
			
			
		}else if (reason.equals("repeat")){
				if (input.length != 3){
					throw new IllegalLineException("Wrong number of input");
				}
			
				if (input[1].equals("1")){
					throw new IllegalLineException("Line 1 can't be repeated.");
				}
	              
	            String[] number = input[1].split("\\."); 
	            int[] lineNumber = new int[number.length]; 
	            for(int j = 0; j< number.length; j++){ 
	                lineNumber[j] = Integer.parseInt(number[j]); 
	            } 
	            int[] curr_number = tree.getCurrentLineNumber();
				if(curr_number.length < lineNumber.length){
					throw new IllegalLineException("Illegal access to a line");
				}
				for (int i = 0; i < lineNumber.length; i++){
					if (lineNumber[i] > curr_number[i]){
						throw new IllegalLineException("The line doesn't exist");
					}else if (i == (lineNumber.length-1) && lineNumber[i] >= curr_number[i]){
						throw new IllegalLineException("The line doesn't exist");
					}
					}
	            Expression repeat_expr = tree.getExpression(lineNumber);
	            if(!(new Expression(input[2]).root().equals(repeat_expr.root()))){
	            	throw new IllegalInferenceException("The repeated expression is not the same as the input expression.");
	            }
	            tree.fill(x, reason, repeat_expr); 
	          
	        	
		}else if (mytheorems.contains(reason) ){
			if(input.length != 2){
				throw new IllegalLineException("Wrong number of input");
			}
			try{
				this.theorem_handling(reason, new Expression(input[1]));
				tree.fill(x, reason, new Expression(input[1]));
			}catch(IllegalLineException error){
				throw error;
			}catch(IllegalInferenceException err){
				throw err;
			}
			tree.fill(x, reason, new Expression(input[1]));
		}else if (reason.equals("print")){
			if(input.length != 1){
				throw new IllegalLineException("There shouldnt be anything besides print");
			}else{
				System.out.println(this.toString());
				isPrint = true;
			}
		}else{
			throw new IllegalLineException("The input is not in the right format.");
		}
		// Handling line is done. next we check whether we should move up the tree after the line.
		
		if (this.subProofIsComplete()){
			
			
			tree.moveUp(); // so after every step, we check if our subproof is complete, if it is
					// we move up the tree;
		}
	
	}

	private void theorem_handling(String theorem_name, Expression e) throws IllegalInferenceException{
		Expression theorem_exp = mytheorems.get(theorem_name);
		HashMap<String, ExprsNode> match_variable = new HashMap<String, ExprsNode>();
		try{
			Proof.matchhelper(match_variable, theorem_exp.root(), e.root());

		}catch(IllegalInferenceException err){
			throw err;
		}
	}
	

	private static void matchhelper(HashMap<String, ExprsNode> match_variable, ExprsNode thr, ExprsNode e) throws IllegalInferenceException{
		if(thr.symbol() != e.symbol() && !Character.isLetter(thr.symbol().charAt(0))){
			throw new IllegalInferenceException("At least one of operator doesn't match with the theorem");
		}else if (Character.isLetter(thr.symbol().charAt(0))){
			if (match_variable.containsKey(thr.symbol())){
				if (!e.equals(match_variable.get(thr.symbol()))){ // how to to .equal for ExprsNode
					throw new IllegalInferenceException("The varaible in the theorem and the expression doesn't match.");
				}
			}else{
				match_variable.put(thr.symbol(), e);
			}
		}else if (thr.symbol().equals("~")){
			try{
			Proof.matchhelper(match_variable, thr.negatedExpr(), e.negatedExpr());
			}catch(IllegalInferenceException err){
				throw err;
			}
		}else{
			try{
			Proof.matchhelper(match_variable, thr.subExprOne(), e.subExprOne()); // what if the user input e with a null subexpr but it's not a letter.
			Proof.matchhelper(match_variable, thr.subExprTwo(), e.subExprTwo()); // Does the match_variable hash map get updated before it's passed in as an arguement?
			}catch(IllegalInferenceException err){
				throw err;
			}
		}

	}
	
	private boolean subProofIsComplete () throws IllegalLineException { 
        //This method checks to see if a subproof is complete. 
        //If it is, it should signify the code in extendProof to move up!
        LineNumber parentLine = tree.getCurrentLine();
        if (parentLine.getLine() == 0){
        	//If the first line is the parent line, there is no subproof!
        	return false;
        }
        ArrayList<String> thrm = new ArrayList<String>();
		thrm.add("mp");
		thrm.add("mt");
		thrm.add("co");
		thrm.add("ic");
		thrm.add("repeat");
        LineNumber currentLine = tree.getLastSubLine();
		if (!thrm.contains(currentLine.getReason()) && !mytheorems.contains(currentLine.getReason())){
        	return false;
        }
		
        Expression parent = parentLine.getExpression();
        Expression current = currentLine.getExpression();
        if(parent.root().equals(current.root())){
        	return true;
        }
        return false;
    }

	public String toString ( ) {
		return tree.print();
	}

	public boolean isComplete ( ) {
		if(isPrint){
			isPrint = false;
			return false;
		}
		ArrayList<String> thrm = new ArrayList<String>();
		thrm.add("mp");
		thrm.add("mt");
		thrm.add("co");
		thrm.add("ic");
		thrm.add("repeat");
		LineNumber parentLine = tree.getCurrentLine();
        LineNumber currentLine = tree.getLastSubLine();
        if (!thrm.contains(currentLine.getReason()) && !mytheorems.contains(currentLine.getReason())){
        	if(have_to_move_down){
            	tree.moveDown();
            	have_to_move_down = false;
            	}
                tree.addSubLine();
        	return false;
        }
        LineNumber firstLine = tree.getFirstLine();
        Expression parent = parentLine.getExpression();
        Expression current = currentLine.getExpression();
        Expression first = firstLine.getExpression();
        if (parentLine.getLine() != 0 || (currentLine.getLine() == 1 && parentLine.getLine() == 0)){ 
        //If the first line is the parent line, there is no subproof!
        	if(have_to_move_down){
        	tree.moveDown();
        	have_to_move_down = false;
        	}
            tree.addSubLine();
        	return false;
        }else if (!first.root().equals(current.root())){
        	if(have_to_move_down){
            	tree.moveDown();
            	have_to_move_down = false;
            	}
        	tree.addSubLine();
        	return false;
        }else{
        	if(have_to_move_down){
            	tree.moveDown();
            	have_to_move_down = false;
            	}
        	return true;
        }

    }
}
