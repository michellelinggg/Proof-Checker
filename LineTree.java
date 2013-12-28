import java.util.ArrayList; 
import java.util.Iterator; 
import java.util.LinkedList; 
  
  
public class LineTree { 
      
    private LineNumber rootLine;  //root line is Line 0; root line is never displayed and has no expression 
    private LineNumber currentLine; 
      
    public LineTree() { 
        rootLine = new LineNumber(); 
        currentLine = rootLine; 
    } 
      
    public void moveUp() { 
        if (currentLine == rootLine) { 
            throw new NullPointerException("Can not move up past the root line."); 
        } else { 
        currentLine = currentLine.getParentLine(); 
        } 
    } 
      
    public void goBack() { 
        if (currentLine == rootLine) { 
            throw new NullPointerException("Can not move up past the root line."); 
        } else { 
        currentLine = currentLine.goBack(); 
        } 
    } 
      
    public void moveDown() { 
        if (currentLine.hasSubLines()) { 
            currentLine = currentLine.getLastSubLine(); 
        } else { 
            try { 
            currentLine = currentLine.getParentLine().getSubLines().get(currentLine.getLine());  //ex: if 2.1 has no sub-lines then move down to 2.2 
            }  
            catch (NullPointerException e) { 
                throw new NullPointerException("No line number availible to move down to."); 
            } 
        } 
    } 
      
    public void fill(String entireLine, String reason, Expression expr) { 
        currentLine.fillSubLine(entireLine, reason, expr); 
    }
    public void addSubLine(){
    	currentLine.addSubLine(new LineNumber(), currentLine);
    }
      
    public LineNumber getPrevious() { 
        return currentLine.getLastSubLine().goBack(); 
    } 
      
    public Expression getExpression(int[] number) { 
          
        try { 
      
        LineNumber line = rootLine; 
        for (int i = 0; i < number.length; i++) { 
            line = line.getSubLines().get(number[i]-1); 
        } 
        return line.getExpression(); 
          
        } catch (IndexOutOfBoundsException e) { 
            System.err.println(number + " is not in the LineTree."); 
        } 
        return null; 
    } 
/*      
    public LineNumber getParentLine() { 
        return currentLine.getParentLine(); 
    } 
  */    
      
    public Iterator<LineNumber> iterate() { 
        return new LineIterator(); 
    } 
      
    public LineNumber getCurrentLine() { 
        return currentLine; 
    } 
      
    public int[] getCurrentLineNumber() { 
      /*    
        ArrayList<Integer> numbers = new ArrayList<Integer>(); 
        LineNumber line = currentLine; 
        numbers.add(0, line.getLine()); 
        while (line != rootLine) { 
            line = currentLine.getParentLine(); 
            numbers.add(0, line.getLine()); 
        } 
          
        int[] currentLineNumber = new int[numbers.size()]; 
        for (int i = 0; i < numbers.size(); i++) { 
            currentLineNumber[i] = numbers.get(i); 
        } 
        return currentLineNumber; */
    	
    	String currentnumstr = currentLine.getLastSubLine().toString();
    	String[] numstr = currentnumstr.split("\\.");
    	int[] currnum = new int[numstr.length];
    	for(int j = 0; j<numstr.length; j++){
			
			currnum[j] = Integer.parseInt(numstr[j]);
		}
    	return currnum;
    } 
      
    public LineNumber getFirstLine() { 
        return rootLine.getSubLines().get(0);
    } 
      
    public LineNumber getLastSubLine() { 
        return currentLine.getLastSubLine(); 
    } 
      
    public class LineIterator implements Iterator<LineNumber> { 
          
        private ArrayList<LineNumber> lines; 
          
        public LineIterator() { 
              
            lines = new ArrayList <LineNumber>(); 
            lines.addAll(0, rootLine.getSubLines()); 
        } 
      
        public boolean hasNext() { 
              
            if (lines.isEmpty()) { 
                return false; 
            } 
              
            return true; 
        } 
          
        public LineNumber next() { 
              
            LineNumber line = lines.remove(0); 
              
            //if the line has no sub-lines, then don't add anything else and just return that line 
            if (!line.hasSubLines()) { 
                return line; 
            } 
              
            else  { 
                    lines.addAll(0, line.getSubLines()); 
                    return line; 
            } 
              
        } 
          
        public void remove() { 
            //not used 
        } 
    } 
      
    public String print() {  
        
        LineIterator iter = new LineIterator();  
        LinkedList<Integer> stored_line_numbers;   //stores all line numbers in step from last to first  
        String allLines = ""; 
            
        while (iter.hasNext()) {  
            stored_line_numbers = new LinkedList<Integer>();   
            LineNumber line = iter.next();  
              
            if (line.getExpression() != null) { 
                LineNumber startLine = line;    //used for printing line steps  
                
                //handles adding line numbers and their sub-line numbers  
                while (startLine.getParentLine().getLine() != 0) {   
                    startLine = startLine.getParentLine(); //moves the start line back to retrace each parent line number  
                    stored_line_numbers.addFirst(startLine.getLine());  
                }  
                
                for (int i = 0; i < stored_line_numbers.size(); i++) {  
                    allLines += stored_line_numbers.get(i) + ".";  
                }  
                allLines += line.getLine() + "\t" + line.getEntireLine() + "\n"; //adds the last number in the line, does a tab space, and prints what the user typed for that line 
            } 
        } 
          
        return allLines; 
    }
      
    public static void main (String [ ] args) { 
          
        LineTree tree = new LineTree(); 
          
        //Testing for when Line only took in String 
        /* 
        tree.addSubLine("testing"); 
        tree.addSubLine("show"); 
        tree.moveDown(); 
        tree.addSubLine("add"); 
        tree.addSubLine("testing23"); 
        tree.moveDown(); 
        tree.addSubLine("herp"); 
        tree.addSubLine("derp"); 
        tree.addSubLine("merp"); 
        tree.print(); 
        tree.moveDown(); 
        System.out.println(tree.currentLine.parentLine); 
        System.out.print("Going back: "); 
        tree.moveUp(); 
        System.out.println(tree.currentLine); 
        tree.moveUp(); 
        System.out.println(tree.currentLine); 
        tree.moveUp(); 
        System.out.println(tree.currentLine); 
        tree.moveUp(); 
        System.out.println(tree.currentLine); 
        tree.moveUp(); 
        System.out.println(tree.currentLine); 
        tree.moveUp(); 
        System.out.println(tree.currentLine); 
        tree.moveUp(); 
        tree.addSubLine("boing"); 
        tree.addSubLine("lolz"); 
        tree.addSubLine("cat"); 
        tree.print(); 
        System.out.print("Accessing line 2.2.2: "); 
        Integer[] line = {2,2,2}; 
        System.out.println(tree.getExpression(line)); 
        */
    } 
}