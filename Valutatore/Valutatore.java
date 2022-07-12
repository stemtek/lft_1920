import java.io.*; 
import java.util.*;
import src.*;

// Es Valutatore 4.1
public class Valutatore {
    private Lexer lex; // utilizzo il Lexer 2.3
    private BufferedReader pbr;
    private Token look;

    public Valutatore(Lexer l, BufferedReader br) { 
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
	int expr_val;
		if(look.tag == 256 || look.tag == 40){
			expr_val = expr();
			match(Tag.EOF);
			System.out.println(expr_val);
		}

		else error ("Error in start\n");
    }

    private int expr() { 
	int term_val, exprp_val = 0;
		if(look.tag == 256 || look.tag == 40){
			term_val = term();
			exprp_val = exprp(term_val);
	}

		else error("Error in expr\n");
			return exprp_val;
    }


    private int exprp(int exprp_i) {
	int term_val, exprp_val = 0;
		switch (look.tag) {
		case '+':
             match('+');
             term_val = term();
             exprp_val = exprp(exprp_i + term_val);
             break;
			
		case '-':
			 match('-');
			 term_val = term();
			 exprp_val = exprp(exprp_i - term_val);
			 break;
		
		case ')':
        case Tag.EOF:
             exprp_val = exprp_i;
             break;

        default:
             error("Error in exprp\n");
	
		}
			return exprp_val;
    }

    private int term() { 
		int fact_val, termp_val = 0;
		if(look.tag == 256 || look.tag == 40){
			fact_val = fact();
			termp_val = termp(fact_val);
		
	} else error("Error in term\n");
			return termp_val;
		
    }
    
    private int termp(int termp_i) { 
		int fact_val, termp_val = 0;
		switch(look.tag) {
			case '*':
				match('*');
                fact_val = fact();
                termp_val = termp(termp_i * fact_val);
                break;
			
			case '/':
				match('/');
                fact_val = fact();
                termp_val = termp(termp_i / fact_val);
                break;
			
			case '+':
            case '-':
            case ')':
            case Tag.EOF:
                termp_val = termp_i;
                break;

            default:
                error("Error in termp\n");
		} return termp_val;
    }
    
    private int fact() { 
		int fact_val = 0, expr_val;
		if(look.tag == '(') {
			match('(');
			fact_val = expr();
			match(')');
		}	
		
		else if (look.tag == 256) { 
				fact_val = Integer.parseInt(((src.NumberTok)look).number);
				match(256);
    	}
		else error("Error in fact\n");
		return fact_val;
	}
			
		
    

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "Valutatore.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore valutatore = new Valutatore(lex, br);
            valutatore.start();
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
