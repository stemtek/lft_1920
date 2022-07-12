public class es1s2 {

// Esercizio 1.2
/* DFA che riconosce il linguaggio degli identificatori che non comincia con un numero
   e che non può contenere solo underscore */
    public static boolean scan (String s){
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state){

                case 0:
                    if (ch == '_')
                        state = 1;
                    else if (Character.isLetter(ch))
                        state = 2;
                    else state = -1;
                break;

                // loop degli underscore se rimango nello state 1
                case 1:
                    if (ch == '_')
                        state = 1;
                    else if (Character.isLetter(ch) || Character.isDigit(ch))
                        state = 2;
                    else state = -1;
                break;

                case 2:
                    if(Character.isLetter(ch) || Character.isDigit(ch) || ch == '_')
                      state = 2;
                    else state = -1;
            }
        }
        return (state == 2);
    }

    public static void main(String[] args){
        System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
