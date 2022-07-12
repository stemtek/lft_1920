import java.util.*;

public class es1s9 {

// Esercizio 1.9
// DFA che riconosce il mio nome (Stefano) e la stringa ottenuta dalla sostituzione di un solo carattere
    public static boolean scan(String s){
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
			switch (state){
				case 0:
					if (ch =='S')
						state = 1;
					else 
						state = 13;
						break;
				
				case 1:
					if (ch =='t')
						state = 2;
					else 
						state = 12;
						break;
				
				case 2:
					if (ch =='e')
						state = 3;
					else 
						state = 11;
						break;

				case 3:
					if (ch =='f')
						state = 4;
					else 
						state = 10;
						break;
					
				case 4:
					if (ch =='a')
						state = 5;
					else 
						state = 9;
						break;
					
				case 5:
					if (ch =='n')
						state = 6;
					else 
						state = 8;
						break;
						
				case 6:
               	   	    state = 7;
               	        break;
						
                case 7: 
                	    state = -1;
						break;
					
				case 8:
					if (ch =='o')
						state = 7;
					else 
						state = -1;
						break;
						
				case 9:
					if (ch =='n')
						state = 8;
					else	
						state = -1;
						break;
				
				case 10:
					if (ch =='a')
						state = 9;
					else
						state = -1;
						break;
						
				case 11:
					if (ch =='f')
						state = 10;
					else 
						state = -1;
						break;
						
				case 12:
					if (ch =='e')
						state = 11;
					else 
						state = -1;
						break;
						
				case 13:
					if (ch =='t')
						state = 12;
					else 	
						state = -1;
						break;
						
				
            }
        }
    return (state == 7);
    }

	public static void main(String[] args){
		System.out.println(scan(args[0]) ? "OK" : "NOPE");
}
}
