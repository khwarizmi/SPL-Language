/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;

import java.util.ArrayList;

/**
 *
 */
public class PredicateRanges extends Predicate{
    
    ArrayList<PredicateRange> predicateRanges;
    
    public PredicateRanges(ArrayList<PredicateRange> ranges) {
        
        predicateRanges = new ArrayList<PredicateRange>();
        
        for(int i =0; i < ranges.size(); i++)
            predicateRanges.add(ranges.get(i));
    }
    
    @Override
    public boolean is(char ch) {
        
        for(int i = 0; i < predicateRanges.size(); i++)
            if(predicateRanges.get(i).is(ch))
                return true;
        
        return false;
    }
}
