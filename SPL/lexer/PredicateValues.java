/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;

/**
 *
 */
public class PredicateValues extends Predicate{
    
     char [] values;
    
    public PredicateValues(char [] args) {
    
        values = args;
    }
    
    @Override
    public boolean is(char ch) {
        
        for(int i = 0; i < values.length; i++)
            if(values[i] == ch)
                return true;
        
        return false;
    }
            
    
}
