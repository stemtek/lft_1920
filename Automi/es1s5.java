public class es1s5 {

// Esercizio 1.5
// Accettazione di matricola T2 e T3 con cognome che precede il numero di matricola
    public static boolean scan(String s){
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state) {

            case 0:
                if (ch >='A' && ch <='K')
                    state = 1;
                else if (ch >='L' && ch <='Z')
                    state = 2;
                    else state = -1;
                break;

			// A-K
            case 1:
                if (ch >='a' && ch <='z')
                    state = 1;
                else if (ch =='0' || ch =='2' || ch =='4' || ch =='6' || ch =='8')
                    state = 3;
                    else if (ch =='1' || ch =='3' || ch =='5' || ch =='7' || ch =='9')
                            state = 5;
                break;

			// L-Z
            case 2:
                if (ch >='a' && ch <='z')
                    state = 2;
                else if (ch =='0' || ch =='2' || ch =='4' || ch =='6' || ch =='8')
                    state = 6;
                    else if (ch =='1' || ch =='3' || ch =='5' || ch =='7' || ch =='9')
                            state = 4;
                break;

			// Matricola T2 Pari
            case 3:
                if (ch =='0' || ch =='2' || ch =='4' || ch =='6' || ch =='8')
                    state = 3;
                else if (ch =='1' || ch =='3' || ch =='5' || ch =='7' || ch =='9')
                        state = 5;
                    else state = -1;
                break;

			// Matricola T3 Dispari
            case 4:
                if (ch =='1' || ch =='3' || ch =='5' || ch =='7' || ch =='9')
                    state = 4;
                else if (ch =='0' || ch =='2' || ch =='4' || ch =='6' || ch =='8')
                        state = 6;
                    else state = -1;
                break;

            case 5:
                if (ch =='0' || ch =='2' || ch =='4' || ch =='6' || ch =='8')
                    state = 3;
                else if (ch =='1' || ch =='3' || ch =='5' || ch =='7' || ch =='9')
                        state = 5;
                    else state = -1;
                break;

            case 6:
                if (ch =='1' || ch =='3' || ch =='5' || ch =='7' || ch =='9')
                    state = 4;
                else if (ch =='0' || ch =='2' || ch =='4' || ch =='6' || ch =='8')
                        state = 6;
                    else state = -1;
                break;
            }
        }
    return (state == 3 || state == 4);
    }

	public static void main(String[] args){
		System.out.println(scan(args[0]) ? "OK" : "NOPE");
}
}
