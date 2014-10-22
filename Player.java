import javax.swing.*;

public class Player {

    private String name;
    private int points;
    private boolean active;
    private static int nbrPlayers = 1;  //klassvariabel för at hålla koll på mängd spelare


    //en konstruktor för spelare, ökar klassvariabeln med en för varje Player som har skapats
    public Player (boolean setActive) {
        name = "";  //sätter namns längd till 0 så man inte kan ge ett tomt värde i input
        while (name.length() == 0) { //Hämtar namn på spelare 1
            name = JOptionPane.showInputDialog("Vad heter spelare " + getNbrPlayers() + "?");
            if(name == null) {  //om man trycker avbryt så stängs programmet
                System.exit(0);
            }
        }
        nbrPlayers = nbrPlayers + 1;
        this.points = 0;
        this.active = setActive;
    }

    //getters och setters, och en metod för att starta om en spelares poäng
    public void addPoint() {
        points++;
    }
    public int getPoints() {
        return points;
    }
    public String getName() {
        return name;
    }
    public static int getNbrPlayers() {
        return nbrPlayers;
    }
    public void reset() {
        points = 0;
    }
}
