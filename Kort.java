import javax.swing.*;
import java.awt.*;


/**
 * @author Tobias Hallberg
 * @author Emil Hukic
 */
public class Kort extends JColorfulButton {


    private Status currentStatus;   //kortets status
    private Icon picture;   //kortets bild



    public Kort(Icon icon) { //Konstruktor med Icon-parameter
        this(icon, Status.SAKNAS);  //kallar på nästa konstruktor, status saknas
    }

    public Kort(Icon icon, Status s) {   //Konstruktor med Icon- och Statusparameter
        setIcon(icon);
        this.picture=icon;
        setStatus(s);
    }


    public enum Status {    //Statusar kortet kan anta
        DOLT, SYNLIGT, SAKNAS
    }

    public void setStatus (Status s) {  //Ändra kortets tillstånd och sätter färg
        currentStatus = s;


        if (s == Status.DOLT) {
            setBackground(Color.BLUE);
            setIcon(null);
        } else if (s == Status.SAKNAS) {
            setBackground(Color.WHITE);
            setIcon(null);
        } else if (s == Status.SYNLIGT) {
            setIcon(this.picture);
        }

    }

    public Status getStatus () {  //Hämta kortets tillstånd
        return this.currentStatus;
    }

    public Kort copy() { //Returnerar en kopia
        Kort copy = new Kort(this.picture, this.currentStatus);
        return copy;
    }


    public Boolean sammaBild (Kort cardOne) {   //kollar om bilden är lik
        return (cardOne.getIcon() == this.getIcon());

    }











}


