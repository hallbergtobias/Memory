import javax.swing.*;
import java.io.File;

/**
 *
 */
public class Memory {

    Memory() {

        File bildmapp = new File("bildmapp");
        File[] bilder = bildmapp.listFiles();

        Kort[] k = new Kort[bilder.length];

        for(int i =0; i<bilder.length;i++) {

            ImageIcon bild = new ImageIcon(bilder[i].getPath());

            Kort kort = new Kort(bild);
            k[i] = kort;

            System.out.println(bild);

        }

    }


}
