import src.*;
import src_translate.*;
import java.io.*;
import java.util.*;

// Es Traduttore 5.1
public class Translator {
    private Lexer lex; // utilizzo il Lexer 2.3
    private BufferedReader pbr;
    private Token look;
    
    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count=0;

    public Translator(Lexer l, BufferedReader br) {
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
                int lnext_prog = code.newLabel(); 
                stat(lnext_prog);                
                code.emitLabel(lnext_prog);      
                match(Tag.EOF);
            try {
            	code.toJasmin(); 
            }
            catch(java.io.IOException e) {
            	System.out.println("IO error\n");
            };
        }else{
            error("Error in grammar prog\n");
        }
    }

    public void statlist(int lnext){
       if(look.tag == '('){
            stat(lnext); 
            statlistp(lnext);
        }else{
            error("Error in grammar statlist\n");
        }
    }


    public void statlistp(int lnext) { 
        switch(look.tag){
            case '(':
                stat(lnext);
                statlistp(lnext);
            break;

            case ')':
            break;

            default:
                error("Error in grammar statlistp\n");
        }
    }


    public void stat(int lnext) { 
       if(look.tag == '('){
            match('(');
            statp(lnext);
            match(')');
        }else{
            error("Error in grammar stat\n");
        }
    }

    public void statp(int lnext) {
        switch(look.tag) {
            case '=': 
				match('=');
                if (look.tag == Tag.ID){
                    int read_id_addr = st.lookupAddress(((Word)look).lexeme); 
                    if (read_id_addr == -1) {
                        read_id_addr = count;
                        st.insert(((Word)look).lexeme,count++);
                    }
                    match(Tag.ID); 
                    expr(); 
                    code.emit(OpCode.istore,read_id_addr); 
                } else {
                    error("Error in grammar (statp) after read with " + look);
                }
            break;

            case Tag.COND:
                match(Tag.COND);
                {
                    int btrue = code.newLabel(); 
                    int bfalse= code.newLabel();
                    int snext = code.newLabel();
                    bexpr(btrue,bfalse); 
                    stat(lnext);
                    code.emit(OpCode.GOto,snext);
                    code.emitLabel(bfalse);
                    elseopt(lnext,snext);    
                }
            break;

            case Tag.WHILE:

                match(Tag.WHILE);
                {
                    int lblif   = code.newLabel(); 
                    int btrue = code.newLabel(); 
                    int bfalse = code.newLabel(); 
                    code.emitLabel(lblif);
                    bexpr(btrue,bfalse); 
                    stat(lnext);
                    code.emit(OpCode.GOto,lblif);
                    code.emitLabel(bfalse); 
               
                } 
            break;

            case Tag.DO:
                match(Tag.DO);
                statlist(lnext);
            break;

    
            case Tag.PRINT:
                match(Tag.PRINT);
                exprlist(0); 
            break;

            case Tag.READ:
                match(Tag.READ);
                if(look.tag == Tag.ID){
                    int read_id_addr = st.lookupAddress(((Word)look).lexeme); 
                    if (read_id_addr==-1) {
                        read_id_addr = count;
                        st.insert(((Word)look).lexeme,count++);
                    }                    
                    match(Tag.ID);
                    code.emit(OpCode.invokestatic,0); 
                    code.emit(OpCode.istore,read_id_addr);
					
                } else {
                    error("Error in grammar (statp) after read with " + look);
                }
            break;
        }
    }


    public void elseopt(int lnext,int snext){
        switch(look.tag){
            case '(':
                match('(');
                match(Tag.ELSE);
                stat(lnext);
                code.emitLabel(snext);
                match(')');
            break;

            case ')':
                code.emitLabel(snext);

            break;

            default:
                error("Error in grammar elseopt\n");
        }
    }



    public void bexpr(int btrue,int bfalse) {
       if(look.tag == '('){
            match('(');
            bexprp(btrue,bfalse);
            match(')');
        } else {  error("Error in grammar bexpr\n");
           
        }
    }


    public void bexprp(int btrue,int bfalse) {

        if(look.tag == Tag.RELOP){
            OpCode myOpcode = OpCode.if_icmpeq;
			
            if(look instanceof Word){ 
                if(((Word)look).lexeme == "==") {
                    myOpcode = OpCode.if_icmpeq;
                } else if (((Word)look).lexeme == "<=") {
                    myOpcode = OpCode.if_icmple;
                } else if (((Word)look).lexeme == "<>") {
                    myOpcode = OpCode.if_icmpne;
                } else if (((Word)look).lexeme == ">=") {
                    myOpcode = OpCode.if_icmpge;
                } else if (((Word)look).lexeme == "<") {
                    myOpcode = OpCode.if_icmplt;
                } else if (((Word)look).lexeme == ">" ){
                    myOpcode = OpCode.if_icmpgt;
                }

            } else { error("Error in grammar bexprp\n");
                
            }

            match(Tag.RELOP);
            expr(); 
            expr();
            code.emit(myOpcode,btrue); 
            code.emit(OpCode.GOto,bfalse); 
            code.emitLabel(btrue); 
        }else{
            error("Error in grammar bexprp\n");
        }
    } 


    public void expr(){
        switch(look.tag){
            case Tag.NUM:
				code.emit(OpCode.ldc, Integer.parseInt(((src.NumberTok)look).number));
				match(Tag.NUM);
            break;

            case Tag.ID:
                 int read_id_addr = st.lookupAddress(((Word)look).lexeme); 
                        if (read_id_addr == -1) {
                            read_id_addr = count;
                            st.insert(((Word)look).lexeme,count++);
                }
                match(Tag.ID);
                code.emit(OpCode.iload,read_id_addr); 
            break;

            case '(':
                match('(');
                exprp();
                match(')');
            break;

            default:
                error("Error in grammar expr\n");
        }
    }


    private void exprp() {
        switch(look.tag) {
            case '+':
                match('+');
                exprlist(1);
            break;
			
            case '*':
                match('*');
                exprlist(2);
            break;
			
            case '-':
                match('-');
                expr();
                expr();
                code.emit(OpCode.isub);
                break;
				
            case '/':
                match('/');
                expr();
                expr();
                code.emit(OpCode.idiv);
            break;
			
	        default:
                System.out.println("Error in grammar exprp\n");
        }
    }

    public void exprlist(int i) {
        switch(look.tag){
            case Tag.NUM:
                expr();
                if(i == 0) {
                    code.emit(OpCode.invokestatic,1); 
                }
                exprlistp(i);
            break;

            case Tag.ID:
                expr();
                if(i == 0) {
                    code.emit(OpCode.invokestatic,1); 
                }
                exprlistp(i);
            break;

            case '(':
                expr();
                if(i == 0) {
                    code.emit(OpCode.invokestatic,1); 
                }
                exprlistp(i);
            break;

            default:
                error("Error in grammar exprlist\n");
        }
    }

    public void exprlistp(int i) {
        switch(look.tag){
            case Tag.NUM:
                expr();
                if(i == 0) {
                    code.emit(OpCode.invokestatic,1); 
                } else if(i == 1) {
                    code.emit(OpCode.iadd);
                } else {
                    code.emit(OpCode.imul);
                }
                exprlistp(i);
            break;

            case Tag.ID:
                expr();
                if(i == 0) {
                    code.emit(OpCode.invokestatic,1); 
                } else if(i == 1) {
                    code.emit(OpCode.iadd);
                } else {
                    code.emit(OpCode.imul);
                }
                exprlistp(i);
            break;

            case '(':
                expr();
                if(i == 0) {
                    code.emit(OpCode.invokestatic,1); 
                } else if(i == 1) {
                    code.emit(OpCode.iadd);
                } else {
                    code.emit(OpCode.imul);
                }
                exprlistp(i);
            break;
			
            case ')':    
            break;

            default:
                error("Error in grammar exprlistp\n");
        }

    }

	
	public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "esempio_semplice.lft"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator translator = new Translator(lex, br);
            translator.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}

