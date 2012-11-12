/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class Lexer {
    //Using special newLine to help in printing output
    public static final int NewLine = System.getProperty("line.separator").charAt(0), specialNewLine = (int)'#'; 
    int p = -1;
    int eof = -1;
    int line, col;
    String buffer;
    StateMachine sm;
    HashMap<String, Integer> reserved_Keywords;
    HashMap<String, Integer> reserved_SpecialChars;
   
    
    public Lexer(String sb) {
        
        line = col = 0;
        buffer = sb;
        
        reserved_Keywords = new HashMap<String, Integer>();
        reserved_SpecialChars = new HashMap<String, Integer>(); 
        sm = new StateMachine();
        
        try {
            init_SpecialChars(); //Create special chars tags
            init_keywords(); //Create reserved words hashtable.
            init(); //Creates the state machine.
            
        } catch (Exception ex) {
            Logger.getLogger(Lexer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int peek() {
        if(p + 1 >= buffer.length())
            return -1;
        
        return buffer.charAt(p + 1);
    }
    public String peek(int steps) {
        int temp;
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < steps; i++)
        {
            temp = peek();
            if(temp == -1)
              return "";
            sb.append((char)temp);
        }
        return sb.toString();
    }    
    public int move() {
        
        int t = peek();
        //only updates Line and column when moving the cursor
        p++; 
        if(t == NewLine)
        {
            line++;
            col = 0;
            return specialNewLine; //represents special newLineChar 
        }
        else
        {
            col++;
        }

        return t;
    }
    public String move(int steps) {
        int temp;
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < steps; i++)
        {
            temp = move();
            if(temp == eof)
                return "";
            sb.append((char)temp);
        }
        return sb.toString();
    } 
    
    public Token formatToken(String lexeme) {
        //col points to the start of the new lexeme
        //incremented while reading
        return new Token(lexeme, line, col - lexeme.length() , tokenTag(lexeme));
    }
    
    public ArrayList<Token> scan() throws Exception {
        
        int ch;
        String lex = "";
        int sc = 0, sb = 0; //holds current state and the state before
        ArrayList<Token> tokenStream = new ArrayList<Token>();
        
        while(peek() != eof) {
            ch = peek();
            
            //Check State according to current Finite State Machine
            int state = sm.moveState((char)ch);
            if(state == StateMachine.UnExpectedError)
            {
                //if the flow of control moved from non-accepting state to undefined state (-1) 
                //the scanner signals error at the given line un expected input.
                throw new Exception ("Error at Line: " + line + " Col: "+ col + " " + sm.getErrMessage(sb) + sm.getBeforeState() +" -> " + sm.getCurrentState());
            }
            else if(state == StateMachine.AcceptInput)
            {
               Token t = formatToken(lex);
               tokenStream.add(t);          
               lex = "";     
            }
            else
            {
               lex = lex + (char)move();
            }
         }
        //last check we send empty to force state to check current input
        switch(sm.moveState(' '))
        {
            case StateMachine.AcceptInput:
                            Token t = formatToken(lex);
                            tokenStream.add(t);
                            break;
                
            case StateMachine.UnExpectedError: 
                              throw new Exception ("Error at Line: " + line + " Col: "+ col + " " + sm.getErrMessage(sc));
        }
            
        return tokenStream;
    }
    
    private void init() throws Exception {
        
        /* first phase defining Machine States Predicates */
        //Accepting one-pass chars
        char[] onePass_chars = new char[] {'(', ')', ',', '*', '-', '+'};
        PredicateValues onePass_Predicates = new PredicateValues(onePass_chars);
        //special two-pass chars
        Predicate equal = new Predicate('=');
        Predicate dot = new Predicate('.');
        Predicate newLine = new Predicate((char)NewLine);
        Predicate slash = new Predicate('/');
        Predicate astriks = new Predicate('*');
        Predicate space = new Predicate(' ');
        PredicateExcept exceptAstriks = new PredicateExcept(astriks);
        
        //Relational Operators
        char [] relOps = new char[] { '>', '<', '=' };
        PredicateValues relationalOps = new PredicateValues(relOps);
        //Lower Letters, Upper Letters, Digits Predicate range
        PredicateRange lowerLetter = new PredicateRange('a', 'z');
        PredicateRange upperLetter = new PredicateRange('A', 'Z');
        PredicateRange digits = new PredicateRange('0', '9');
        //Letters Predicate
        ArrayList<PredicateRange> Letters = new ArrayList<PredicateRange>();
        Letters.add(lowerLetter);
        Letters.add(upperLetter);
        PredicateRanges letters = new PredicateRanges(Letters);
        //Letters or Digits Predicate
        ArrayList<PredicateRange> Letters_Digits = new ArrayList<PredicateRange>();
        Letters_Digits.add(lowerLetter);
        Letters_Digits.add(upperLetter);
        Letters_Digits.add(digits);
        PredicateRanges letterOrDigits = new PredicateRanges(Letters_Digits);
          
        //init_Machine States
        int s = 0; //represents start state
        int currentState = s + 1;

        //state machine for Letter(Letter + Digit)*
        sm.addAcceptingState(currentState, StateMachine.FailureState); //Accepting State
        sm.Add(s, new TransitionState(letters, currentState));
        sm.Add(currentState, new TransitionState(letterOrDigits, currentState));
       
        currentState++;
        //state machine for Digit+ (. Digit+)?  - integer or float acceptance
        sm.addAcceptingState(currentState, StateMachine.FailureState); //Accepting State
        sm.addAcceptingState(currentState + 2, StateMachine.FailureState); //Accepting State
        sm.Add(s, new TransitionState(digits, currentState));
        sm.Add(currentState, new TransitionState(digits, currentState));
        sm.Add(currentState, new TransitionState(dot, currentState + 1)); // goes to 'dot' state
        sm.Add(currentState + 1, new TransitionState(digits, currentState + 2));
        sm.Add(currentState + 2, new TransitionState(digits, currentState + 2));
        sm.setErrMessage(currentState + 1, "Expected a digit after '.' - regex = Digit+ (. Digit+)?");
        
        currentState+= 3;
        //state machine for accepting special ops  (, ), ',', +, -, * 
        sm.addAcceptingState(currentState, StateMachine.FailureState); //Accepting State
        sm.Add(s, new TransitionState(onePass_Predicates, currentState));
        
        currentState++;
        //state machine for accepting relational ops >, <, =, ==, >=, <=
        sm.addAcceptingState(currentState, StateMachine.FailureState); //Accepting State
        sm.addAcceptingState(currentState + 1, StateMachine.FailureState); //Accepting State
        sm.Add(s, new TransitionState(relationalOps, currentState));
        sm.Add(currentState, new TransitionState(equal, currentState + 1));
        
        currentState+= 2;
        //State machine for '/' and /* */
        sm.addAcceptingState(currentState, StateMachine.FailureState); // Accepts Div operator
        sm.addAcceptingState(currentState + 3, StateMachine.FailureState); // Accepts Multi-Line Comment
        sm.Add(s, new TransitionState(slash, currentState));
        sm.Add(currentState, new TransitionState(astriks, currentState + 1));
        sm.Add(currentState + 1, new TransitionState(exceptAstriks, currentState + 1));
        sm.Add(currentState + 1, new TransitionState(astriks, currentState + 2));
        sm.Add(currentState + 2, new TransitionState(slash, currentState + 3));
        sm.setErrMessage(currentState + 1, "Expected end of multi-line comment - regex = /* anything */");
        sm.setErrMessage(currentState + 2, "Expected slash end for multi-line comment - regex = /* anything in here */");
         
        currentState += 4;
        //read spaces and return to start state
        sm.addAcceptingState(currentState, StateMachine.FailureState); 
        sm.Add(s, new TransitionState(space, currentState));
        sm.Add(currentState, new TransitionState(space, currentState));
        
        
        currentState ++;
        sm.addAcceptingState(currentState, StateMachine.FailureState);
        sm.Add(s, new TransitionState(newLine, currentState));
        
        currentState ++;  

    }
    private void init_keywords() {
        /* If = 257, Else = 258, Read = 259, Write = 260, Var = 261, func = 262, 
           For = 263, Do = 264, While = 265, End = 266, True = 267, False = 268 */
        
        reserved_Keywords.put("if", Tag.If);
        reserved_Keywords.put("else", Tag.Else);
        reserved_Keywords.put("read", Tag.Read);
        reserved_Keywords.put("write", Tag.Write);
        reserved_Keywords.put("var", Tag.Var);
        reserved_Keywords.put("func", Tag.func);
        reserved_Keywords.put("for", Tag.For);
        reserved_Keywords.put("do", Tag.Do);
        reserved_Keywords.put("while", Tag.While);
        reserved_Keywords.put("end", Tag.End);
        reserved_Keywords.put("true", Tag.True);
        reserved_Keywords.put("false", Tag.False);
    }
    private void init_SpecialChars() {
         /* GT = 270, LT = 271, EQuality = 272, GE = 273, LE = 274, NE = 275, OR = 276
            LParan = 277, RParan = 278, EQ = 279, Colon = 280, Dot = 281,
            Plus = 282, Minus = 283, Div = 284, Mul = 285; */
        
        reserved_SpecialChars.put(">", Tag.GT);
        reserved_SpecialChars.put("<", Tag.LT);
        reserved_SpecialChars.put("==", Tag.EQuality);
        reserved_SpecialChars.put(">=", Tag.GE);
        reserved_SpecialChars.put("<=", Tag.LE);
        reserved_SpecialChars.put("!=", Tag.NE);
        reserved_SpecialChars.put("||", Tag.OR);
        reserved_SpecialChars.put("(", Tag.LParan);
        reserved_SpecialChars.put(")", Tag.RParan);
        reserved_SpecialChars.put("=", Tag.EQ);
        reserved_SpecialChars.put(",", Tag.Colon);
        reserved_SpecialChars.put(".", Tag.Dot);
        reserved_SpecialChars.put("+", Tag.Plus);
        reserved_SpecialChars.put("-", Tag.Minus);
        reserved_SpecialChars.put("/", Tag.Div);
        reserved_SpecialChars.put("*", Tag.Mul);
        
    }
    private int tokenTag(String lexeme) {
       
        //Search reserved keywords
        Object tag = reserved_Keywords.get(lexeme);
        if(tag != null)
            return (Integer)tag;
        
        //Search reserved Special chars
        tag = reserved_SpecialChars.get(lexeme);
        if(tag != null)
            return (Integer)tag;
        
        //Else it must be an identifier
        return Tag.ID;
    }
    
}
