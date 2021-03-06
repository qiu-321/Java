
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import principleProject2.ParseTree.PROG;

public class myinterpreter {

	//private PROG program;

    public static void main(String args[]) {
    	//expect 2 args for input: input program and input data file
    	//To run: Run Config-> arguments->path for file1 path for file2
    	if(args.length < 2) {
    		System.out.println("Number of arguments cannot be less than 2!");
    		System.exit(1000);
    	}
    	//Scanner S = new Scanner(args[0]);
    	ParseTree pt = new ParseTree(args[0]);
    	PROG parseTree = pt.getParseTree();
    	printer.prettyPrint(parseTree);
    	Executor.execute(parseTree, args[1]);
    }
}
