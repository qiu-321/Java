Project2


All the functionality of the project is broken up into different files, myinterpretor.java runs the whole project
Files needed to compile the project, make sure all the files are in the same folder!

	1. Core.java	
	2. Scanner.java		*modification is made to the original file, please do not use the older version of this file
	3. ParseTree.java	*used to parse the program into a parse tree using Scanner.java
	4. Executor.java	*used to execute the provided program using parsed tree
	5. printer.java		*used to print the provided program in a pretty, neat way using parse tree
	6. myinterpreter.java	*runs the project, parse, print and execute the provided program

Make sure EXECUTOR, PRINTER has imported all the classes in parsetree!!
Make sure MYINTERPRETER imported ParseTree.PROG from ParseTree.java so that it can execute the program.

If unexpected token found during parsing, in the error message, NT id # refers to the nonterminal id number assigned to each nonterminal(given in comment). It tells where during parsing has the unexpected token found. Example: if a given program does not have BEGIN keyword, right after parsing <decl_id>, if Core.ID is found, the parser is going to assume that ID refers to the <id> of function even if it ID for assignment

To compile the project:
	run myinterpretor.java
	provide two arguments for the program, args[0] should be the code file and args[1] should be the data file

Known bugs:
	If the value of a declared identifier is updated during function call, the executor does not accurately update the identifier associated value
	Because of the previous problem, recursion is also not applicable for the program
