import java.io.*; 
import java.util.*;
import src.*;

// Parser 3.1
public class Parser {
    private Lexer lex; // utilizzo il Lexer 2.3
    private BufferedReader pbr;
    private Token look;

    public Parser(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) {
	throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) {
	if (look.tag == t) {
	    if (look.tag != Tag.EOF) move();
	} else error("syntax error");
    }

    public void start() {
		if(look.tag == 256 || look.tag == 40){
			expr();
			match(Tag.EOF);
		}
			else error("Error in start\n");
    }

    private void expr() {
		if(look.tag == 256 || look.tag == 40){
			term();
			exprp();
		}
		else error("Error in expr\n");
    }

    private void exprp() {
			switch (look.tag) {
			case '+':
				match(look.tag);
				term();
				exprp();
				break;
				
			case '-':
				match(look.tag);
				term();
				exprp();
				break;
				
			default: 
				break;
			}
		}
   

    private void term() {
		if(look.tag == 256 || look.tag == 40){
			fact();
			termp();
		}
		else error("Error in term\n");
	}

    private void termp() {
			switch (look.tag) {
				case '*':
					match(look.tag);
					fact();
					termp();
					break;
				
				case '/':
					match(look.tag);
					fact();
					termp();
					break;
			
				case ')':  break;
	
			default: 
					break;
				
		}
    }

    private void fact() {
		if(look.tag == 256){
			match(look.tag);
		}
		else if (look.tag==40) {
			match(look.tag);
			expr();
			match(41);
		}
		else error("Error in fact\n");
    }
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "parser.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
