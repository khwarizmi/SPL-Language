/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;

/**
 *
 */
public class PredicateRange extends Predicate {
 
    char a,b;
    public PredicateRange(char r1, char r2)
    {
        a = r1;
        b = r2;
    }
    
    @Override
    public boolean is(char ch) {
        return (a <= ch && ch <= b);
    }
}
