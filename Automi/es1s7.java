public class es1s7 {

// Esercizio 1.7
// a occorre almeno una volta nelle prime tre posizioni
    public static boolean scan(String s){
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
			switch (state){

			// Prima posizione per a
            case 0:
				if (ch =='a')
					state = 3;
				else if(ch =='b')
					state = 1;
				else 
					state = -1;
				break;

			// Seconda posizione per a
			case 1: 
				if (ch =='a')
					state = 3;
				else if(ch =='b')
					state = 2;
				else 
					state = -1;
				break;

			// Terza e ultima posizione per a
			case 2:
				if(ch =='a')
					state = 3;
				else
					state = -1;
				break;
			
			case 3:
				break;

            }
        }
    return (state == 3);
    }

	public static void main(String[] args){
		System.out.println(scan(args[0]) ? "OK" : "NOPE");
}
}
