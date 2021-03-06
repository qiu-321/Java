

import java.io.FileReader;
import java.io.IOException;
import java.lang.String;
import java.io.BufferedReader;
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
class Scanner {
	//private int count;
	private BufferedReader sc;
	private Queue<String> q = new LinkedList<>();
	private Queue<String> c; 
	private StringBuilder currentWord = new StringBuilder();
	private Queue<Core> qq = new LinkedList<>();
	ArrayList<String> terminator = new ArrayList<>();
	ArrayList<String> whiteSpace = new ArrayList<>();

  // Initialize the scanner
  Scanner(String filename)throws IOException{

	  terminator.add("+");
	  terminator.add("=");
	  terminator.add("-");
	  terminator.add("<");
	  terminator.add("(");
	  terminator.add(")");
	  terminator.add(";");
	  terminator.add(":");
	  terminator.add(",");
	  terminator.add("*");
	  terminator.add("!");
	  terminator.add("|");
	  
	  
	  whiteSpace.add(" ");
	  whiteSpace.add("\t");
	  whiteSpace.add("\n");
	  
    System.out.println(filename);
    sc = new BufferedReader(new FileReader(filename));
    int i = 0;
    String iToC = "";

    while((i = sc.read())!= -1) {
    	//check that character is valid
    	iToC = Character.toString((char)i);
        //System.out.println(iToC);
    	if(terminator.contains(iToC)) {
    		if(currentWord.length()>0) {
    			q.add(currentWord.toString());
    			currentWord = new StringBuilder();
    		}
    		//terminator goes straight to queue
    		q.add(iToC);
    	}else if(currentWord.length()==0 &&(!whiteSpace.contains(iToC))) {
    		//start building word
    		currentWord.append(iToC);
    	}else if((i >=65 && i<=90) || (i>=97 && i<=122) || i>=48 && i<=57) {
    		//if currently pointing at a character
    		if((i >=65 && i<=90) || (i>=97 && i<=122)) {
    			//if current word is constant, save constant to queue, clear string
	    		if(Character.isDigit(currentWord.charAt(0))) {
	    			q.add(currentWord.toString());
	    			currentWord = new StringBuilder();
	    		}
	    		//either current word is empty or is continuing building id
    		}
    			currentWord.append(iToC);
    		
    	}else if(!whiteSpace.contains(iToC)) {
    		if(currentWord.length()>0) {
    			q.add(currentWord.toString());
    		}
    		System.out.println("Illegal character: " + iToC);
    		break;
    	}else if(whiteSpace.contains(iToC)) {
    		if(currentWord.length()>0) {
    			q.add(currentWord.toString());
    			currentWord = new StringBuilder();
    		}
    	}else {
    		System.out.println("Illegal character: " + iToC);
    		break;
    	}
    }
    

  }
  
  // Advance to the next token
  public Core nextToken() {
	  q.remove();
	  return qq.remove();
    
  }

  // Return the current token
public Core currentToken() {
	
  	Core t = Core.INT;
	String str = q.peek();
	if(str == null) {
		return Core.EOS;
	}

  	if(str.equals("while")) {
  		t = Core.WHILE;
  		qq.add(t);
  	}else if(str.equals("program")) {
  		t = Core.PROGRAM;
  		qq.add(t);
  	}else if(str.equals("if")) {
  		t = Core.IF;
  		qq.add(t);
  	}else if(str.equals("begin")) {
  		t = Core.BEGIN;
  		qq.add(t);
  	}else if(str.equals("end")) {
  		t = Core.END; 
  		qq.add(t);
  	}else if(str.equals("int")) {
  		t = Core.INT;
  		qq.add(t);
  	}else if(str.equals("endfunc")) {
  		t = Core.ENDFUNC;
  		qq.add(t);
  	}else if(str.equals("else")) {
  		t = Core.ELSE;
  		qq.add(t);
  	}else if(str.equals("then")) {
  		t = Core.THEN;	
  		qq.add(t);
  	}else if(str.equals("endwhile")) {
  		t = Core.ENDWHILE;
  		qq.add(t);
  	}else if(str.equals("endif")) {
  		t = Core.ENDIF;
  		qq.add(t);
  	}else if(str.equals(";")) {
  		t = Core.SEMICOLON;
  		qq.add(t);
  	}else if(str.equals("(")) {
  		t = Core.LPAREN;
  		qq.add(t);
  	}else if(str.equals(")")) {
  		t = Core.RPAREN;
  		qq.add(t);
  	}else if(str.equals(",")) {
  		t = Core.COMMA;
  		qq.add(t);
  	}else if(str.equals(":")) {
  		q.remove();
  		if(q.peek().equals("="))
  		{
  	
  			t = Core.ASSIGN;
  			System.out.println(t);
  			q.remove();
  		}else {
  			System.out.println("Token :"+q.peek()+ " does not exist.");
  			System.exit(1000);
  		}
  		qq.add(t);
  	}else if(str.equals("-")) {
  		t = Core.SUB;
  		qq.add(t);
  	}else if(str.equals("|")) {
  		t = Core.OR;
  		qq.add(t);
  	}else if(str.equals("<")) {
  		q.remove();
  		if(q.peek().equals("=")) {
  			t = Core.LESSEQUAL;
  			System.out.println(t);
  			q.remove();
  			qq.add(t);

  		}else {
  			t = Core.LESS;
  			System.out.println(t);
  			qq.add(t);
  		}
  	}else if(str.equals("+")) {
  		t = Core.ADD;
  		qq.add(t);
  	}else if(str.equals("*")) {
  		t = Core.MULT;
  		qq.add(t);
  	}else if(str.equals("input")) {
  		t = Core.INPUT;
  		qq.add(t);
  	}else if(str.equals("output")) {
  		t = Core.OUTPUT;
  		qq.add(t);
  	}else if(str.equals("!")) {
  		t = Core.NEGATION;
  		qq.add(t);
  	}else if(str.equals("=")) {
  		t = Core.EQUAL;
  		qq.add(t);
  	}else if(Character.isLetter(q.peek().charAt(0))) {
  		t = Core.ID;
  		qq.add(t);
  	}else {
  		t = Core.CONST;
  		qq.add(t);
  	}
	return t;
}

  public String getID() {

	  String s = q.peek();

    return s;
  }

  public int getCONST() {
	  int num =  Integer.parseInt(q.peek());

	  if(num > 1023 || num < 0) {
		  System.out.println("\nCONST given is not within the range 0 < CONST < 1023");
		  System.exit(1000);
	  }
    return num;
  }

}
