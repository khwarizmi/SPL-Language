/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplelang;

import java.util.ArrayList;
import lexer.Lexer;
import lexer.Token;

/**
 *
 */
public class SimpleLang {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        
        String newLine = System.getProperty("line.separator");
        ArrayList<Token> tokstream = new ArrayList<Token>();
      
        String input = "n+" + newLine + newLine  + " /* fers "+ newLine + " comment */ " + newLine +"96.2" +" - ";
        //System.out.println((int)System.getProperty("line.separator").charAt(1));
        
        Lexer lx = new Lexer(input);
        try
        {
             tokstream = lx.scan();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
     
        for(int i = 0; i < tokstream.size(); i++)
          System.out.println(tokstream.get(i).toString());
    }
}
