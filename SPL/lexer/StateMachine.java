/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 */
public class StateMachine {
    
    public static final int StartState = 0, FailureState = -1, 
                            AcceptInput = 0, UnExpectedError = 1, Continue = 2;
    
    private int scurrent = 0, sbefore = 0; 
    private ArrayList<ArrayList<TransitionState>> statesTable;    
    private HashMap<Integer, HashSet<Integer>> acceptance_States;
    private HashMap<Integer, String> reserved_ErrMessages;
    
    public StateMachine()
    {
        statesTable = new ArrayList<ArrayList<TransitionState>>();
        acceptance_States = new HashMap<Integer, HashSet<Integer>>();
        reserved_ErrMessages = new HashMap<Integer, String>();
    }
    
   
    public void setErrMessage(int state, String msg) {
       reserved_ErrMessages.put(state, msg);
    }
    public String getErrMessage(int state) {
       return reserved_ErrMessages.get(state);
    }
    public void Add(int s, TransitionState e)  {
        if(s >= statesTable.size())
        {
            for(int i = statesTable.size(); i <= s; i++)
                statesTable.add(new ArrayList<TransitionState>());
        }
              
        statesTable.get(s).add(e);
    }
    public boolean addAcceptingState(int from, int to) {

        if(!acceptance_States.containsKey(from))
            acceptance_States.put(from, new HashSet<Integer>());
        
        return acceptance_States.get(from).add(to);
    }
    int acceptState(int scurrent, int sbefore) {
        
        HashSet<Integer> acceptStates = acceptance_States.get(sbefore);
        if(acceptStates != null)
           if(acceptStates.contains(scurrent))
              return StateMachine.AcceptInput;
            
        //Signals Error
        //Means traversing from non-accepting state to an undefined state in the FSM
        if(scurrent == StateMachine.FailureState)
            return StateMachine.UnExpectedError;
        else
            return StateMachine.Continue;
    }
   
    public int move(int state, char ch) {

        //if the input state dosen't hold any Transition returns Faliure Transition
        if(state >= statesTable.size())
            return StateMachine.FailureState;
        
        TransitionState temp;
        ArrayList<TransitionState> transitions = statesTable.get(state);
        for(int i = 0; i < transitions.size(); i++)
        {
            temp = transitions.get(i);
            if(temp.linkTo(ch))
                return temp.getState();
        }
        
        return StateMachine.FailureState;
    }
    int moveState(char ch) {
         sbefore = scurrent;
         scurrent = move(scurrent, (char)ch);
         //System.out.println(sbefore +" -> " + scurrent);
         //Check State according to current Finite State Machine
         int state = acceptState(scurrent, sbefore);
         if(state == StateMachine.AcceptInput)
         {
            scurrent = sbefore = 0;
         }
         
         return state;
    }

    public int getCurrentState() {
        return scurrent;
    }
    public int getBeforeState() {
        return sbefore;
    }
    
            

}
