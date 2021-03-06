

import java.io.IOException;

// Compile with the command "javac *.java"
// Run with the command "java Main FILENAME"
class Main {
  public static void main(String[] args) throws IOException{
    // Initialize the scanner with the input file
    Scanner S = new Scanner(args[0]);

    while (S.currentToken() != Core.EOS) {
      // Printing the current token, with any extra data needed
      System.out.print(S.currentToken());
      if (S.currentToken() == Core.ID) {
        String value = S.getID();
        System.out.print("[" + value + "]");
      } else if (S.currentToken() == Core.CONST) {
        int value = S.getCONST();
        System.out.print("[" + value + "]");
      }
      System.out.print("\n");

      // Advance to the next token
      S.nextToken();
    }
  }
}
