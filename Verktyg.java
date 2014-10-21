import java.util.Random;

public class Verktyg {


    public static void slumpOrdning(Object[] array) {   //slumpar om ordningen på fält av valfri typ

        int antal = array.length;
        Object arrCopy[] = new Object[antal];
        int n = 0;

        while (n < antal) { //körs till alla index är slumpade

            Random generator = new Random();
            int plats = generator.nextInt(antal);

            if (arrCopy[plats] == null) {   //om platsen är ledig läggs in
                arrCopy[plats] = array[n];
                n += 1;
            }

        }

        for (int i = 0; i < array.length; i++) {    //kopierar arrayen
            array[i] = arrCopy[i];
        }

    }


}


