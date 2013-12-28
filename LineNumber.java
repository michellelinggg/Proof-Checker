import java.util.ArrayList;
 
 
 
public class LineNumber {
     
        private String myReason;
        private Expression myExpression;
        private String myEntireLine;
        private int myLine;
        private ArrayList<LineNumber> subLines;
        private LineNumber parentLine; 
     
        public LineNumber(String entireLine, String reason, Expression expr, LineNumber parent) {
            myEntireLine = entireLine;
            myReason = reason;
            myExpression = expr;
            parentLine = parent;
            subLines = new ArrayList<LineNumber>();
        }
         
        //this constructor is only used for the root line
        public LineNumber() {
            myReason = null;
            myExpression = null;
            parentLine = null;
            myLine = 0;
            subLines = new ArrayList<LineNumber>();
        }
        public LineNumber addSubLine(LineNumber line, LineNumber parent){
        	subLines.add(line);
        	//set the most recent sub-line to have a line number and keeps track of each sub-line's number based on ArrayList size
        	subLines.get(subLines.size() - 1).myLine = subLines.size();
        	subLines.get(subLines.size() - 1).parentLine = parent;
        	return subLines.get(subLines.size() - 1);
        }
         
        public void fillSubLine(String entireLine, String reason, Expression expr) {
        	subLines.get(subLines.size() - 1).myEntireLine = entireLine;
        	subLines.get(subLines.size() - 1).myReason = reason;
        	subLines.get(subLines.size() - 1).myExpression = expr;
        	

             
        }
         
        public LineNumber getLastSubLine() {
            return subLines.get(subLines.size() - 1);
        }
         
        public LineNumber goBack() {
         
            if (myLine > 1) {
                return parentLine.subLines.get(myLine - 2);
            }
             
            return parentLine;
        }
         
        public Expression getExpression() {
            return myExpression;
        }
         
        public int getLine() {
            return myLine;
        }
         
        public String getReason() {
            return myReason;
        }
         
        public String getEntireLine() {
            return myEntireLine;
        }
         
        public boolean hasSubLines() {
            return !subLines.isEmpty();
        }
         
        public LineNumber getParentLine() {
            return parentLine;
        }
         
        public ArrayList<LineNumber> getSubLines() {
            return subLines;
        }
         
        public String toString() {
             
                ArrayList<Integer> numbers = new ArrayList<Integer>();
                LineNumber line = this;
                String number_string = "";
                while (line.getLine() != 0) {
                    numbers.add(0, line.getLine());
                    line = line.getParentLine();
                }
                 
                int[] currentLineNumber = new int[numbers.size()];
                for (int i = 0; i < numbers.size(); i++) {
                    currentLineNumber[i] = numbers.get(i);
     
                    number_string += currentLineNumber[i] + ".";
                    
                }
             return number_string.substring(0,number_string.length()-1);
             
        }
}