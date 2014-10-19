import javax.swing.*;
import java.awt.*;


/**
 * @author Tobias Hallberg & Emil Hukic
 */
public class Kort extends JColorfulButton {


    private Status currentStatus;   //kortets status
    private Icon picture;



    public Kort(Icon icon) { //Konstruktor med Icon-parameter
        this(icon, Status.SAKNAS);
    }

    public Kort(Icon icon, Status s) {   //Konstruktor med Icon- och Statusparameter
        setIcon(icon);
        this.picture=icon;
        setStatus(s);
    }


    public enum Status {
        DOLT, SYNLIGT, SAKNAS
    }

    public void setStatus (Status s) {  //Ändra kortets tillstånd
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

    public Kort copy() { //osäker
        Kort copy = new Kort(this.picture, this.currentStatus);
        return copy;
    }

/*
    public Boolean sammaBild (Kort cardOne) {
        return cardOne.getIcon().equals(this.getIcon()); //equals kollar data ej adress
    }
*/
    public Boolean sammaBild (Kort cardOne) {
        return (cardOne.getIcon() == this.getIcon());

    }




    /*
    private class Icon { //bild för varje kort. Osäker på den ska vara en inner class eller inte
        //Om DOLT => blå, SAKNAR => vit, SYNLIGT=> bild




    } */








}


