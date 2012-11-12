/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;

/**
 *
 */
public class Predicate {
    
    char p;
    
    public Predicate(){}
    public Predicate(char ch) {
        p = ch;
    }
    
    public boolean is(char ch){
        return p == ch;
    }
}

