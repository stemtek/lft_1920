public class es1s4 {

// Esercizio 1.4
// Accettazione di matricola T2 e T3 con eventuali spazi bianchi
    public static boolean scan (String s){
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()){
            final char ch = s.charAt(i++);
            switch (state){
				
                case 0:
                    if (ch ==' ')
                         state = 0;
                    else if (ch =='0' || ch =='2' || ch =='4' || ch =='6' || ch =='8')
                         state = 1;
                    else if (Character.isDigit(ch))
                         state = 2;
                    else state = -1;
                    break;

                case 1:
                    if (Character.isDigit(ch))
                         state = 2;
                    else if (ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
                         state = 1;
                    else if (ch ==' ')
                         state = 5;
                    else if (ch>='A' && ch<='K')
						 state = 3;
                    else state = -1;
                    break;

                case 2:
                    if (ch =='1' || ch =='3' || ch =='5' || ch =='7' || ch =='9')
                         state = 2;
                    else if (Character.isDigit(ch))
                         state = 1;
                    else if (ch ==' ')
                         state = 6;
                    else if (ch >='L' && ch <='Z')
                         state = 4;
                    else state = -1;
					break;
				
				// A-K
                case 3:
                    if (ch ==' ')
                         state = 7;
                    else if (ch >='a' && ch <='k')
                         state = 3;
					else if (ch >='l' && ch <='z')
			             state = 4;
                    else state = -1;
                    break;

				// L-Z
                case 4:
                    if (ch ==' ')
                         state = 8;
                    else if (ch >='l' && ch <='z')
                         state = 4;
		            else if (ch >='a' && ch <='k')
                         state = 3;
                    else state = -1;
                    break;
					
				// spazio prima della matricola T2
        		case 5:
        			if(ch ==' ')
        				 state = 5;
        			else if (ch >='A'&& ch <='K')
                         state = 3;
       				else state = -1;
					break;

				// spazio prima della matricola T3
				case 6:
					if (ch ==' ')
		                 state = 6; //
					else if (ch >='L'&& ch <='Z')
		                 state = 4;
	                else state = -1;
				break;

				// cognome composto in matricola T2
       			case 7:
      				if(ch ==' ')
        				 state = 7;
        			else if (ch >='A'&& ch <='Z')
        				 state = 3;
        			else state = -1;
				break;

				// cognome composto in matricola T3
				case 8:
					if (ch ==' ')
						 state = 5; //
					else if (ch >='A'&& ch <='Z') //
						 state = 4;
					else state = -1;
				break;
          }
        }
        return (state == 3 || state == 4 || state == 7 || state == 8);
    }
    public static void main(String[] args){
        System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
