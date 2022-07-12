import java.io.*; 
import java.util.*;
import src.*;

// Utilizzo il Lexer 2.3
public class Lexer {

    public static int line = 1;
    private char peek = ' ';
    
    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    public Token lexical_scan(BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') {
            if (peek == '\n') line++;
            readch(br);
        }
        
        switch (peek) {
            case '!':
                peek = ' ';
                return Token.not;

			// ... gestire i casi di (, ), {, }, +, -, *, /, ; ... //				
			case '(':
                peek = ' ';
                return Token.lpt;

            case ')':
                peek = ' ';
                return Token.rpt;
				
			case '{':
				peek = ' ';
				return Token.lpg;
				
			case '}':
				peek = ' ';
				return Token.rpg;
				
			case '+':
                peek = ' ';
                return Token.plus;

            case '-':
                peek = ' ';
                return Token.minus;
			
			case '*':
                peek = ' ';
                return Token.mult;
				
			// verifico la presenza di commenti 
			case '/':
				readch(br);
				switch (peek) {
                    case '/':
                        readch(br);
                        while (!(peek == '\n' || peek == Tag.EOF)) {
                            readch(br);
                        }

                        return lexical_scan(br);
                    case '*':
                        boolean comment = true;  // nel caso in cui ho /*
                        while (comment){
                            if (peek == '*') {    
                                readch(br);
                                if (peek == '/') {   
                                    comment = false;
                                }
                            } else{
                              readch(br);
                            }
                            if (peek == (char) -1) {        // se arrivo a EOF concludo
                                return new Token(Tag.EOF);
                            }

                        }

                        peek = ' ';
                        return lexical_scan(br);

                    default:                    // nel caso in cui / sia un diviso e quindi non un commento

                        return Token.div;


                }
				
			case ';':
                peek = ' ';
                return Token.semicolon;

            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character"
                            + " after & : "  + peek );
                    return null;
                }

			// ... gestire i casi di ||, <, >, <=, >=, ==, <>, = ... //		
			case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character" 
							+ " after | : " + peek );
                    return null;
                }

            case '<':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.le;
                } else if (peek == '>') {
                    peek = ' ';
                    return Word.ne;
                } else {
                    return Word.lt;
                }

            case '>':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.ge;
                } else {
                    return Word.gt;
                }

            case '=':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.eq;
                } else {
                    return Token.assign;
                }
          
            case (char)-1:
                return new Token(Tag.EOF);

			default:
                if (Character.isLetter(peek) || peek == '_') { // verifico l'inserimento di una lettera o di underscore
					boolean control = false;
					String s = "";
                    do {
                        if (Character.isLetter(peek) || Character.isDigit(peek)) { // verifico che non ho underscore
                            control = true;
                        }
                        s += peek;
                        readch(br);
                    } while (Character.isLetter(peek) || peek == '_');


                    switch (s) {
						case "cond":
							return Word.cond;
						
						case "when":
							return Word.when;					

                        case "then":
                            return Word.then;

                        case "else":
                            return Word.elsetok;
						
						case "while":
							return Word.whiletok;
							
                        case "do":
                            return Word.dotok;
						
						case "seq":
							return Word.seq;
							
                        case "print":
                            return Word.print;

                        case "read":
                            return Word.read;

                        default:
                            if (!control) {
								// la sequenza che ho inserito è formata da soli caratteri underscore
                                System.err.println("Error, you typed only _");
                                return null;
                            } else {
                                return new Word(Tag.ID, s);
                            }
                    }
	
                } else if (Character.isDigit(peek)) { // nel caso in cui inizio con un numero
					String n = "";
                    do {
                        n = n + peek;
                        readch(br);
                    } while (Character.isDigit(peek));
					
					if (Character.isLetter(peek)|| peek =='_') {
				    	System.err.println("you typed a number followed by other characters");
		    		 return null;
				    }
                    return new src.NumberTok(Tag.NUM, n);

                } else {
                        System.err.println("Erroneous character: " 
                                + peek );
                        return null;
                }
         }
    }
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "lexertest3.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {e.printStackTrace();}    
    }

}
