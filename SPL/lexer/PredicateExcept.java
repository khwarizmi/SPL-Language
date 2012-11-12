/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;

import java.util.ArrayList;

/**
 *
 */
public class PredicateExcept extends Predicate{
    
    ArrayList<Predicate> p;
    public PredicateExcept(Predicate pre) {
        p = new ArrayList<Predicate>();
        p.add(pre);
    }
    public PredicateExcept(ArrayList<Predicate> pre) {
        
        p = pre;
    }
    
    @Override
    public boolean is(char ch) {
        
        Predicate temp;
        for(int i = 0; i < p.size(); i++) {
            temp = p.get(i);
            if(temp.is(ch))
                return false;
        }
        
        return true;
    }
}
