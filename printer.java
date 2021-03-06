

import principleProject2.ParseTree.*;

public class printer {


	private printer() { }

	public static void prettyPrint(PROG parseTree) { printPROG(parseTree); }

	private static void spaces(int num) { 
		for(int i = 0; i < num; i++) System.out.print("  "); 
	}
	
	private static void printPROG(PROG prog) {	//1
		System.out.println("program");
		printDECLSEQ(prog.getDeclSeq());
		System.out.println("\nbegin");
		printSTMTSEQ(prog.getStmtSeq(), 1);
		System.out.println("end");
	}

	private static void printDECLSEQ(DECL_SEQ declSeq) {	//2
		spaces(1);
		printDECL(declSeq.getDecl(),1);
		if (declSeq.getNo() == 1) {
			printDECLSEQ(declSeq.getDeclSeq());
		}
	}

	private static void printSTMTSEQ(STMT_SEQ stmtSeq, int indent) {	//3
		spaces(indent);
		printSTMT(stmtSeq.getStmt(), indent);
		System.out.print("\n");
		if (stmtSeq.getNo() == 1) {
			printSTMTSEQ(stmtSeq.getStmtSeq(), indent);
		}
	}

	private static void printDECL(DECL decl, int indent) {	//4
		if(decl.getNo() == 0) {
			printDECLID(decl.getDeclId());
		}else if(decl.getNo() == 1) {
			printDECLFUNC(decl.getDeclFunc(), indent);
		}
	}
	
	private static void printDECLID(DECL_ID DeclId) {	//5
		//ToDo
		System.out.print("int");
		printIDLIST(DeclId.getIdList());
		System.out.print(";");
	}

	private static void printDECLFUNC(DECL_FUNC DeclFunc, int indent) {	//6 
		System.out.println();
		spaces(indent);
		System.out.print(DeclFunc.getId() + "(");
		printIDLIST(DeclFunc.getIdList());
		System.out.print(") begin\n");
		printSTMTSEQ(DeclFunc.getStmtSeq(), indent+1);
		spaces(indent);
		System.out.print("endfunc;");
		
	}

	private static void printIDLIST(ID_LIST idList) {	//7
		System.out.print(idList.getId());
		if (idList.getNo() == 1) {
			System.out.print(",");
			printIDLIST(idList.getIdList());
		}
	}


	private static void printSTMT(STMT stmt, int indent) {	//8
		switch (stmt.getNo()) {
			case 1: // ASSIGN
				printASSIGN(stmt.getAssign());
				break;
			case 2: // IF
				printIF(stmt.getIf(), indent);
				break;
			case 3: // LOOP
				printLOOP(stmt.getLoop(), indent);
				break;
			case 4: // IN
				printIN(stmt.getIn());
				break;
			case 5: // OUT
				printOUT(stmt.getOut());
				break;
			case 6: //FUNC
				printFUNC(stmt.getFunc());
			default:
				break;
		}
	}
	
	private static void printFUNC(FUNC func) {	//9
		//To DO
		System.out.print("begin " + func.getId());
		System.out.print("(");
		printIDLIST(func.getIdList());
		System.out.print(");");
		
	}

	private static void printASSIGN(ASSIGN assignStmt) {	//10
		System.out.print(assignStmt.getLvalue() + ":=");
		printEXPR(assignStmt.getExpr());
		System.out.print(";");
	}


	private static void printIN(IN inputStmt) {	//11
		System.out.print("input ");
		printIDLIST(inputStmt.getIdList());
		System.out.print(";");
	}


	private static void printOUT(OUT outputStmt) {	//12
		System.out.print("output ");
		printEXPR(outputStmt.getExpr());
		System.out.print(";");
	}

	private static void printIF(IF ifStmt, int indent) {	//13
		System.out.print("if ");
		printCOND(ifStmt.getCond());
		System.out.println(" then");
		printSTMTSEQ(ifStmt.getStmtSeq(), indent + 1);
		// Decision: print ELSE clause;
		if (ifStmt.getNo() == 1) {
			spaces(indent);
			System.out.println("else");
			printSTMTSEQ(ifStmt.getElseStmtSeq(), indent + 1);
		}
		spaces(indent);
		System.out.print("endif;");
	}

	private static void printLOOP(LOOP loopStmt, int indent) {	//14
		System.out.print("while ");
		//if(loopStmt.getCond().getNo() != 1) {System.out.print("(");}
		printCOND(loopStmt.getCond());
		//if(loopStmt.getCond().getNo() != 1) {System.out.print(")");}
		System.out.print(" begin\n");
		printSTMTSEQ(loopStmt.getStmtSeq(), indent + 1);
		spaces(indent);
		System.out.print("endwhile;");
	}

	private static void printCOND(COND cond) {	//15
		
		switch (cond.getNo()) {
			case 0: // cmpr
				printCMPR(cond.getCmpr());
				break;
			case 1: // negation
				System.out.print("!(");
				printCOND(cond.getNegCond());
				System.out.print(")");
				break;
			case 2: // or
				printCMPR(cond.getCmpr());
				System.out.print(" or ");
				printCOND(cond.getCond());
				break;
			default:
				break;
		}
	}

	private static void printCMPR(CMPR cmpr) {	//16

		printEXPR(cmpr.getExpr1());
		if(cmpr.getNo() == 0) {
			System.out.print("=");
		}else if (cmpr.getNo() == 1) {
			System.out.print("<");
		}else if(cmpr.getNo() == 2) {
			System.out.print("<=");
		}
		printEXPR(cmpr.getExpr2());
		//System.out.print(" ");
	}

	private static void printEXPR(EXPR expr) {	//17
		printTERM(expr.getTerm());
		if (expr.getNo() == 1) {
			System.out.print("+");
			printEXPR(expr.getExpr());
		}else if(expr.getNo() == 2) {
			System.out.print("-");
			printEXPR(expr.getExpr());
		}
	}

	private static void printTERM(TERM term) {	//18
		printFACTOR(term.getFactor());
		if (term.getNo() == 1) {
			System.out.print("*");
			printTERM(term.getTerm());
		}
	}

	private static void printFACTOR(FACTOR factor) {	//19
		switch (factor.getNo()) {
			case 0: 
				System.out.print(factor.getValue());
				break;
			case 1: 
				System.out.print(factor.getId());
				break;
			case 2:
				System.out.print("(");
				printEXPR(factor.getExpr());
				System.out.print(")");
				break;
			default:
				break;
		}
	}

}
