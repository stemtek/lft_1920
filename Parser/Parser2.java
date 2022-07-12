import java.io.*; 
import java.util.*;
import src.*;

// Parser 3.2
public class Parser2 {
    private Lexer lex; // utilizzo il Lexer 2.3
    private BufferedReader pbr;
    private Token look;

    public Parser2(Lexer l, BufferedReader br) {
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
	
    public void prog() { 
        if(look.tag == '('){
            stat();
            match(Tag.EOF);
        }else {
            error("Error in prog\n");
        }
    }

    public void statlist() { 
        if(look.tag == '('){
            stat();
            statlistp();
        }else {
            error("Error in statlist\n");
        }
    }

    public void statlistp() { 
        switch(look.tag){
            case '(':
                stat();
                statlistp();
            break;

            case ')':
            break;

            default:
                error("Error in statlistp\n");
        }
    }

    public void stat() { 
        if(look.tag == '('){
            match('(');
            statp();
            match(')');
        }else{
            error("Error in stat\n");
        }
    }

    public void statp() { 
        switch(look.tag){
            case '=':
                match('=');
                match(Tag.ID);
                expr();
            break;

            case Tag.COND:
                match(Tag.COND);
                bexpr();
                stat();
                elseopt();
            break;

            case Tag.WHILE:
                match(Tag.WHILE);
                bexpr();
                stat();
            break;

            case Tag.DO:
                match(Tag.DO);
                statlist();
            break;

            case Tag.PRINT:
                match(Tag.PRINT);
                exprlist();
            break;

            case Tag.READ:
                match(Tag.READ);
                match(Tag.ID);
            break;

            default:
                error("Error in statp\n");
        }
    }

    public void elseopt() { 
        switch(look.tag){
            case '(':
                match('(');
                match(Tag.ELSE);
                stat();
                match(')');
            break;

            case ')':
            break;

            default:
                error("Error in elseopt\n");
        }
    }

    public void bexpr() { 
        if(look.tag == '('){
            match('(');
            bexprp();
            match(')');
        }else{
            error("Error in bexpr\n");
        }
    }

    public void bexprp() { 
        switch(look.tag){
            case Tag.RELOP:
                match(Tag.RELOP);
                expr();
                expr();
            break;

            default:
                error("Error in bexprp\n");
        }
    }


    public void expr() { 
        switch(look.tag){
            case Tag.NUM:
                match(Tag.NUM);
            break;

            case Tag.ID:
                match(Tag.ID);
            break;

            case '(':
                match('(');
                exprp();
                match(')');
            break;

            default:
                error("Error in expr\n");
        }
    }


    public void exprp() { 
        switch(look.tag){
            case '+':
                match('+');
                exprlist();
            break;
			
            case '*':
                match('*');
                exprlist();
            break;

            case '-':
                match('-');
                expr();
                expr();
            break;

            case '/':
                match('/');
                expr();
                expr();
            break;

            default:
                error("Error in exprp\n");
        }
    }


    public void exprlistp() { 
        switch(look.tag){
            case Tag.NUM:
                expr();
                exprlistp();
            break;

            case Tag.ID:
                expr();
                exprlistp();
            break;

            case '(':
                expr();
                exprlistp();
            break;

            case ')':
            break;

            default:
                error("Error in exprlistp\n");
        }
    }


    public void exprlist() { 
        switch(look.tag){
            case Tag.NUM:
                expr();
                exprlistp();
            break;

            case Tag.ID:
                expr();
                exprlistp();
            break;

            case '(':
                expr();
                exprlistp();
            break;

            default:
                error("Error in exprlist\n");
        }
    }


		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "test.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser2 parser = new Parser2(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
