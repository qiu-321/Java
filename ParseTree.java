

class ParseTree {
	
	public final Scanner S;
	
	ParseTree(String fileName) { 
		 S = new Scanner(fileName);
	}

	public PROG getParseTree() {
		// Generate Parse Tree;
		PROG tree = new PROG(); tree.parse();
		return tree;
	}
	static int tokens = 0;
	private boolean beforeProgramBegin = false;

class PROG {	//1
	private DECL_SEQ declSeq;
	private STMT_SEQ stmtSeq;

	public void parse() {
		tokens++;
		S.match("PROGRAM", 1);
		beforeProgramBegin = true;
		declSeq = new DECL_SEQ(); declSeq.parse();
		S.match("BEGIN",1);
		stmtSeq = new STMT_SEQ(); stmtSeq.parse();
		S.match("END",1);
		//For the case where there is code after END
		S.match("EOS",1);
	}

	public DECL_SEQ getDeclSeq() { return declSeq; }
	public STMT_SEQ getStmtSeq() { return stmtSeq; }
}

class DECL_SEQ {	//2

	private int altNo = 0;      
	private DECL decl;          // 0 is <decl>;
	private DECL_SEQ declSeq;   // 1 is <decl><declSeq>;

	public void parse() {
		tokens++;
		//System.out.println("Inside declseq,Token " + tokens);
		decl = new DECL(); decl.parse();
		if (S.currentToken() != Core.BEGIN) {
			altNo = 1;
			declSeq = new DECL_SEQ(); declSeq.parse();
		}
	}

	public int getNo() { return altNo; }
	public DECL getDecl() { return decl; }
	public DECL_SEQ getDeclSeq() { return declSeq; }
}

class STMT_SEQ {	//3

	private int altNo = 0;    
	private STMT stmt;          // 0 is <stmt>;
	private STMT_SEQ stmtSeq;   // 1 is <stmt><stmtSeq>;

	public void parse() {
		tokens++;
		//System.out.println("inside stmtseq, Token " + tokens);
		stmt = new STMT(); stmt.parse();
		Core token = S.currentToken();
		if (token != Core.END && token != Core.ENDIF
				&& token != Core.ENDWHILE && token != Core.ELSE && token != Core.ENDFUNC) {
			altNo = 1;
			stmtSeq = new STMT_SEQ(); stmtSeq.parse();
		}
	}

	public int getNo() { return altNo; }
	public STMT getStmt() { return stmt; }
	public STMT_SEQ getStmtSeq() { return stmtSeq; }
}

class DECL{	//4
	private int getNo = 0;  //0 is declId, 1 is DeclFunc
	private DECL_ID DeclId;
	private DECL_FUNC DeclFunc;
	
	public void parse() {
		tokens++;
		//System.out.println("Inside decl, Token " + tokens);
		Core token = S.currentToken();
		if(token == Core.INT) {
			DeclId = new DECL_ID();	DeclId.parse();
			//S.match("SEMICOLON", 4);
		}else if (token == Core.ID) {
			DeclFunc = new DECL_FUNC();	DeclFunc.parse();
			getNo=1;
		}else {
			System.out.println("ERROR: parsing DECL failed! Token:" + S.currentToken().toString());
			System.exit(1000);
		}
	}
	public int getNo() {return getNo;}
	public DECL_ID getDeclId() {return DeclId;}
	public DECL_FUNC getDeclFunc() {return DeclFunc;}
	
}
class DECL_ID {		//5

	private ID_LIST idList;

	public void parse() {
		tokens++;
		//System.out.println("Inside declId, Token " + tokens);
		S.match("INT",5);
		idList = new ID_LIST(); idList.parse();
		S.match("SEMICOLON",5);
		if(S.currentToken() == Core.INPUT) {
			System.out.println("ERROR: missing BEGIN for program!");
			System.exit(1000);
		}
	}

	public ID_LIST getIdList() { return idList; }
}

class DECL_FUNC{	//6 In progress
	private String id;
	private ID_LIST IdList;
	private STMT_SEQ StmtSeq;
	
	public void parse() {
		tokens++;
		//System.out.println("Inside DeclFunc, Token " + tokens);
		id = S.getID();
		S.nextToken();	
		S.match("LPAREN",6);
		IdList = new ID_LIST(); IdList.parse();
		S.match("RPAREN",6);
		S.match("BEGIN",6);
		StmtSeq = new STMT_SEQ(); StmtSeq.parse();
		S.match("ENDFUNC",6);
		S.match("SEMICOLON",6);
		if(S.currentToken() != Core.BEGIN && beforeProgramBegin) {
			System.out.println("ERROR: Missing BEGIN for PROGRAM");
			System.exit(1000);
		}else {
			beforeProgramBegin = false;
		}
	}
	public String getId() {return id;}
	public ID_LIST getIdList() {return IdList;}
	public STMT_SEQ getStmtSeq() {return StmtSeq;}
}

class ID_LIST {		//7 ???Correct?

	private int altNo = 0;
	private String id;      // 0 is id;
	private ID_LIST idList; // 1 is id<idList>;

	public void parse() {
		tokens++;
		//System.out.println("Inside idList, Token " + tokens);
		//System.out.println("current " + S.currentToken().toString());
		
		id = S.getID();
		S.nextToken();
		if(S.currentToken() == Core.COMMA) {
			S.match("COMMA", 7);
			altNo = 1;
			idList = new ID_LIST(); idList.parse();
		}
	}

	public int getNo() { return altNo; }
	public String getId() { return id; }
	public ID_LIST getIdList() { return idList; }
}


class STMT {		//8

	private int altNo = 0;
	private ASSIGN s1;  // 1 is <assign>;
	private IF s2;      // 2 is <if>;
	private LOOP s3;    // 3 is <loop>;
	private IN s4;      // 4 is <input>;
	private OUT s5;     // 5 is <output>;
	private FUNC s6;

	public void parse() {
		tokens++;
		//System.out.println("Inside stmt, Token " + tokens);
		//System.out.println( S.currentToken());
		
		Core token = S.currentToken();
		if (token == Core.ID) {
			altNo = 1;
			s1 = new ASSIGN(); s1.parse();
		} else if (token == Core.IF) {
			altNo = 2;
			s2 = new IF(); s2.parse();
		} else if (token == Core.WHILE) {
			altNo = 3;
			s3 = new LOOP(); s3.parse();
		} else if (token == Core.INPUT) {
			altNo = 4;
			s4 = new IN(); s4.parse();
		} else if (token == Core.OUTPUT) {
			altNo = 5;
			s5 = new OUT(); s5.parse();
		} else if (token == Core.BEGIN) {
			altNo = 6;
			s6 = new FUNC(); s6.parse();
		} else {
			System.out.println("ERROR: Expected a stmt, found " + token);
			System.exit(1000);
		}
	}

	public int getNo() { return altNo; }
	public ASSIGN getAssign() { return s1; }
	public IF getIf() { return s2; }
	public LOOP getLoop() { return s3; }
	public IN getIn() { return s4; }
	public OUT getOut() { return s5; }
	public FUNC getFunc() { return s6; }
}

class FUNC{		//9
	private String id;
	private ID_LIST IdList;
	
	public void parse() {
		tokens++;
		//System.out.println("Inside func, Token " + tokens);
		S.match("BEGIN",9);
		id = S.getID();
		S.nextToken();
		S.match("LPAREN",9);
		IdList = new ID_LIST(); IdList.parse();
		S.match("RPAREN",9);
		S.match("SEMICOLON",9);
	}
	public String getId() {return id;}
	public ID_LIST getIdList() {return IdList;}
}

class ASSIGN {		//10
	
	private String id;
	private EXPR expr;

	public void parse() {
		tokens++;
		//System.out.println("inside assign, Token " + tokens);
		
		id = S.getID();
		S.nextToken();
		S.match("ASSIGN", 10);
		expr = new EXPR(); expr.parse();
		S.match("SEMICOLON", 10);
	}

	public EXPR getExpr() { return expr; }
	public String getLvalue() { return id; }
}


class IN {		//11

	private ID_LIST idList;

	public void parse() {
		tokens++;
		//System.out.println("Inside in, Token " + tokens);
		S.match("INPUT",11);
		idList = new ID_LIST(); idList.parse();
		S.match("SEMICOLON",11);
	}

	public ID_LIST getIdList() { return idList; }
}


class OUT {		//12

	private EXPR expr;

	public void parse() {
		tokens++;
		//System.out.println("Inside out, Token " + tokens);
		S.match("OUTPUT",12);
		expr = new EXPR(); expr.parse();
		S.match("SEMICOLON",12);
	}

	public EXPR getExpr() { return expr; }
}
class IF {	//13

	private int altNo = 0;
	private COND cond;             
	private STMT_SEQ stmtSeq;       // 0 is if <cond> then <stmtSeq>;
	private STMT_SEQ elseStmtSeq;   // 1 is if <cond> then <stmtSeq> else <stmtSeq>;

	public void parse() {
		tokens++;
		//System.out.println("Inside if, Token " + tokens);
		
		S.match("IF",13);
		cond = new COND(); cond.parse();
		S.match("THEN",13);
		stmtSeq = new STMT_SEQ(); stmtSeq.parse();
		Core token = S.currentToken();
		if (token == Core.ELSE) {
			S.match("ELSE", 13);
			altNo = 1;
			elseStmtSeq = new STMT_SEQ(); elseStmtSeq.parse();
		}
		S.match("ENDIF",13);
		S.match("SEMICOLON",13);
	}

	public int getNo() { return altNo; }
	public COND getCond() { return cond; }
	public STMT_SEQ getStmtSeq() { return stmtSeq; }
	public STMT_SEQ getElseStmtSeq() { return elseStmtSeq; }
}

class LOOP {	//14

	private STMT_SEQ stmtSeq;
	private COND cond;

	public void parse() {
		tokens++;
		//System.out.println("inside loop, Token " + tokens);
		S.match("WHILE",14);
		cond = new COND(); cond.parse();
		S.match("BEGIN",14);
		stmtSeq = new STMT_SEQ(); stmtSeq.parse();
		S.match("ENDWHILE",14);
		S.match("SEMICOLON",14);
	}
	public COND getCond() { return cond; }
	public STMT_SEQ getStmtSeq() { return stmtSeq; }

}


class COND {		//15	In progress?

	private int altNo = 0;  // 0 is cmpr, 1 is !(<COND>), 2 is <CMPR> of <COND>
	private CMPR cmpr;  
	private COND Cond;
	private COND negCond;

	public void parse() {
		tokens++;
		//System.out.println("inside cond, Token " + tokens);
		Core token = S.currentToken();
		if (token == Core.NEGATION) { // !<cond>;
			altNo = 1;
			S.match("NEGATION", 15);
			S.match("LPAREN",15);
			negCond = new COND(); negCond.parse();
			S.match("RPAREN",15);
		} else{ 
			cmpr = new CMPR(); cmpr.parse();
			if(S.currentToken() == Core.OR) {
				S.match("OR", 15);
				altNo = 2;
				Cond = new COND(); Cond.parse();
			}
		}
			
	}

	public int getNo() { return altNo; }
	public COND getNegCond() {return negCond;}
	public CMPR getCmpr() {return cmpr;}
	public COND getCond() {return Cond;}

}


class CMPR {		//16 In progress
	private EXPR expr1;
	private EXPR expr2;
	private int altNo;	//= is 0, < is 1, <= is 2
	public void parse() {
		tokens++;
		//System.out.println("Inside cmpr, Token " + tokens);
		
		expr1 = new EXPR(); expr1.parse();
		Core token = S.currentToken();
		if(token == Core.EQUAL) {
			S.match("EQUAL", 16);
			altNo = 0;
		}else if(token == Core.LESS) {
			S.match("LESS", 16);
			altNo =1;
		}else if(token == Core.LESSEQUAL) {
			S.match("LESSEQUAL", 16);
			altNo =2;
		}else {
			System.out.println("Illegal comparsion operator: " + token.toString());
			System.exit(10000);
		}
		//S.nextToken();
		expr2 = new EXPR(); expr2.parse();
	}

	public int getNo() { return altNo; }
	public EXPR getExpr1() { return expr1; }
	public EXPR getExpr2() { return expr2; }
}

class EXPR {	//17

	private int altNo = 0;  // 0 is term, 1 is +, 2 is -
	private TERM term;      // 
	private EXPR expr;      //

	public void parse() {
		tokens++;
		//System.out.println("inside expr, Token " + tokens);

		term = new TERM(); term.parse();
		
		//System.out.println("Expr "+S.currentToken());
		Core token = S.currentToken();
		if (token == Core.ADD) {
			S.match("ADD", 17);
			altNo = 1;
			expr = new EXPR(); expr.parse();
		}else if(token == Core.SUB) {
			S.match("SUB", 17);
			altNo = 2;
			expr = new EXPR(); expr.parse();
		}
	}

	public int getNo() { return altNo; }
	public TERM getTerm() { return term; }
	public EXPR getExpr() { return expr; }
}

class TERM {		//18

	private int altNo = 0;
	private FACTOR factor;  // 0 is <factor>;
	private TERM term;      // 1 is <factor> * <term>;

	public void parse() {
		tokens++;
		//System.out.println("inside term, Token " + tokens);
		factor = new FACTOR(); factor.parse();
		if (S.currentToken() == Core.MULT) {
			altNo = 1;
			S.match("MULT", 18);
			term = new TERM(); term.parse();
		}
	}

	public int getNo() { return altNo; }
	public FACTOR getFactor() { return factor; }
	public TERM getTerm() { return term; }
}

class FACTOR {		//19

	private int altNo=0;     
	private int cnst;      // 0 is const
	private String id;      // 1 is id
	private EXPR expr;      // 2 is (<expr>)

	public void parse() {
		tokens++;
		//System.out.println("Inside factor, Token " + tokens);
		Core token = S.currentToken();
		if (token == Core.CONST) { // const;
			altNo = 0;
			cnst = S.getCONST();
			S.nextToken();
		} else if (token == Core.ID) { // id;	
			id = S.getID();
			S.nextToken();
			altNo = 1;
		}else if (token == Core.LPAREN) {
			altNo = 2;
			S.match("LPAREN",19);
			expr = new EXPR(); expr.parse();
			S.match("RPAREN",19);
		}
	}

	public int getNo() { return altNo; }
	public int getValue() { return cnst; }
	public String getId() { return id; }
	public EXPR getExpr() { return expr; }
}

}
