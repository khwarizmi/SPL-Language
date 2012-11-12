/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;

/**
 *
 */
public class TransitionState {
    
    Predicate p;
    int s;
    
    public TransitionState(Predicate pre, int st) {
        s = st;
        p = pre;
    }
    
    public boolean linkTo(char a) {
        return p.is(a); 
    }
    
    public int getState()
    {
        return s;
    }
}
