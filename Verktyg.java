import java.util.Random;

public class Verktyg {


    public static void slumpOrdning(Object[] array) {

        int antal = array.length;
        Object arrCopy[] = new Object[antal];
        int n = 0;

        while (n < antal) { //körs till alla tal är slumpade

            Random generator = new Random();
            int plats = generator.nextInt(antal);

            if (arrCopy[plats] == null) {   //om platsen är ledig läggs talet in
                arrCopy[plats] = array[n];
                n += 1;
            }

        }

        for (int i = 0; i < array.length; i++) {    //kopierar arrayen
            array[i] = arrCopy[i];
        }

    }


}


