/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;

/**
 *
 */
public class Token {
    int line, col;
    int tag;
    String lexeme;
    
    public Token (String lex, int ln, int cl, int tg) { 
        lexeme = lex;
        line = ln; col = cl;
        tag = tg;
    }
    @Override
    public String toString() { return "lexeme: " + lexeme + " at Line: " + line + " at Col: " + col + " tag: " + tag; }
}
