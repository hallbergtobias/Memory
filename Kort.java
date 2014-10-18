import javax.swing.*;
import java.awt.*;


/**
 * @author Tobias Hallberg & Emil Hukic
 */
public class Kort extends JColorfulButton {


    private Status currentStatus;   //kortets status



    public Kort(Icon myIcon) { //Konstruktor med Icon-parameter
        this.setIcon(myIcon);
        setStatus(Status.SAKNAS);


    }

    public Kort(Icon myIcon, Status s) {   //Konstruktor med Icon- och Statusparameter
        this.setIcon(myIcon);
        System.out.println("Konstrutkor- status sätts till: " + s);
        this.setStatus(s);
    }


    public enum Status {
        DOLT, SYNLIGT, SAKNAS
    }

    public void setStatus (Status s) {  //Ändra kortets tillstånd
        //System.out.println("setStatus: " + s);
        currentStatus = s;
        //System.out.println("Status satt till: " + currentStatus);


        if (s == Status.DOLT) {
            setBackground(Color.BLUE);
           // setIcon(null);
            System.out.println("DOLT: Icon till null");
        } else if (s == Status.SAKNAS) {
            setBackground(Color.WHITE);
        } else if (s == Status.SYNLIGT) {
            //Sätta bild
        }

    }

    public Status getStatus () {  //Hämta kortets tillstånd
        System.out.println("getStatus är: " + currentStatus);
        return this.currentStatus;
    }

    public Kort copy() { //osäker
        Kort kopia = this;  //samma icon? samma tillstånd?
        return kopia;
    }


    public Boolean sammaBild (Kort cardOne) {
        return cardOne.getIcon().equals(this.getIcon()); //equals kollar data ej adress
    }




    /*
    private class Icon { //bild för varje kort. Osäker på den ska vara en inner class eller inte
        //Om DOLT => blå, SAKNAR => vit, SYNLIGT=> bild




    } */








}


