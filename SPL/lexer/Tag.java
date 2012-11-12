/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;

/**
 * tags for Keywords, Identifiers, Relational Operations and Paran, ... 
 */
public class Tag {
    public static final int 
            If = 257, Else = 258, Read = 259, Write = 260, Var = 261, func = 262, For = 263, Do = 264, While = 265, End = 266, True = 267, False = 268,
            ID = 269,
            GT = 270, LT = 271, EQuality = 272, GE = 273, LE = 274, NE = 275, OR = 276,
            LParan = 277, RParan = 278, EQ = 279, Colon = 280, Dot = 281,
            Plus = 282, Minus = 283, Div = 284, Mul = 285;
                    
    public static String type(int value) {
        if(value >= 257 && value <= 268)
            return "Keyword";
        else if(value == 269)
            return "Identifier";
        else if(value >= 270 && value <= 276)
            return "Relational Op";
        else if(value == 277)
            return "Left Paran";
        else if(value == 278)
            return "Right Paran";
        else if(value == 279)
            return "colon";
        else if(value == 280)
            return "colon";
        else if(value >= 281 && value <= 284)
            return "basic Math operators";
        
        return "Undifiend element Type";
    }
    /*
    public static String name(int value) {
        
        switch (value) {
            //Keywords phase
            case 257 : return "if";
            case 258 : return "else";
            case 259 : return "read";
            case 260 : return "write";
            case 261 : return "var";
            case 262 : return "func";
            case 263 : return "for";
            case 264 : return "do";
            case 265 : return "while";
            case 266 : return "end";
            case 267 : return "true";
            case 268 : return "false";
                
            default : return "";
        }
    }
    */
}
