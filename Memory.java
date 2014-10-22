import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
/**
 * @author Tobias Hallberg
 * @author Emil Hukic
 */
public class Memory extends JFrame implements ActionListener{
    //alla variabler, kanske lite väl många...
    public static final int DELAY = 1500;
    private Kort k[]; //alla kort
    private Kort spelKort[]; //kort vi spelar med
    private int rows; //antal rader
    private int columns; //antal kolumner
    private Kort första;
    private Kort andra;
    private Player playerEtt;
    private Player playerTvå;
    private Player activePlayer;
    private JButton newBtn, quitBtn; //knapp Nytt och Avbryt
    private JPanel gamePanel; //Spelpanelen
    private Timer timer;
    private JPanel player1;
    private JPanel player2;
    private JLabel points1; //poäng spelare 1
    private JLabel points2; //poäng spelare 2
    public int maxpoint;


    public Memory() {
        try { //Kollar att mappen finns och att mappen innehåller > 1 bild
            File bildmapp = new File("bildmapp");
            File[] bilder = bildmapp.listFiles();   // gör en array av alla bilder
            if (bilder.length<2) { //för få bilder - avslutar
                JOptionPane.showMessageDialog(this, "Mappen bildmapp innehåller endast " + bilder.length + " bilder. " +
                        "Lägg in fler bilder. Programmet avslutas");
                System.exit(0);
            }
            this.k = new Kort[bilder.length];
            for (int i = 0; i < bilder.length; i++) { //loopar in alla kort i k
                ImageIcon bild = new ImageIcon(bilder[i].getPath());
                k[i] = new Kort(bild); //ska ej vara synligt
                System.out.println(bild);
            }
        } catch (NullPointerException e) { //mappen finns ej - avslutar
            JOptionPane.showMessageDialog(this, "Mappen \"bildmapp\" hittas ej. Programmet avslutas");
            System.out.println("bildmapp not found: " + e);
            System.exit(0);
        }
        boolean notEnoughCards = true;
        while (notEnoughCards) { //Kollar om vi har tillräckligt med kort för inmatade kolumner/rader
            String antKol = JOptionPane.showInputDialog("Ange antal kolumner med ett heltal över 0 ");
            if(antKol == null) {    //om man trycker avbryt på InputDialogen så stängs programmet
                System.exit(0);
            }
            String antRad = JOptionPane.showInputDialog("Ange antal rader med ett heltal över 0: ");
            if(antRad == null) {   //återigen för att stänga programmet
                System.exit(0);
            }
            try { // Rad/kolumn ej angiven som siffra
                this.columns = Integer.parseInt(antKol);
                this.rows = Integer.parseInt(antRad);       //om man inte har lämpligt antal rader och kolumner
                if ((this.columns*this.rows)/2 > this.k.length) {   //så kommer programmet fortsätta be om input
                    JOptionPane.showMessageDialog(this, "Du har för få bilder i din bildmapp. Ange färe kolumner/rader.");
                } else if ((this.columns*this.rows)%2==1) { //I memory krävs jämnt antal bilder
                    JOptionPane.showMessageDialog(this, "Du kan inte ha ett ojämnt antal memorykort");
                } else if (columns < 1 || rows < 1){
                    JOptionPane.showMessageDialog(this, "Du kan inte ha 0 rader eller kolumner");
                } else {
                    notEnoughCards = false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Ange antaler rader/kolumner som en siffra, tex \"4\".");
            }
        }


        //får båda spelares namn

        //skapar spelare och ger namn, spelare 1 får vara den som börjar
        playerEtt = new Player(true);
        playerTvå = new Player(false);

        //allt det grafiska skapas här, bland annat informationen om spelarna till höger, startar även spelet
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400,400);
        setLocation(100,100);
        spelPlan plan = new spelPlan();
        add(plan);
        JPanel btnPanel = new JPanel(new FlowLayout()); //knapp-panel
        this.newBtn = new JButton("Nytt");
        this.quitBtn = new JButton("Avsluta");
        newBtn.addActionListener(this);
        quitBtn.addActionListener(this);
        btnPanel.add(this.newBtn);
        btnPanel.add(this.quitBtn);
        add(btnPanel, BorderLayout.SOUTH);
        Font playerFont = new Font(Font.SANS_SERIF, Font.BOLD, 20); //typsnitt för spelarpanel
        Font score = new Font(Font.SANS_SERIF, Font.BOLD, 50);
        JLabel lblSpelare1 = new JLabel(playerEtt.getName()); //Spelare 1 namn
        lblSpelare1.setFont(playerFont);
        lblSpelare1.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel lblSpelare2 = new JLabel(playerTvå.getName()); //Spelare 2 namn
        lblSpelare2.setFont(playerFont);
        lblSpelare2.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel playerPanel = new JPanel(new GridLayout(2,1)); //spelarpanel, 2x1 rutor
        playerPanel.setPreferredSize(new Dimension(80, 260));
        player1 = new JPanel(new BorderLayout()); //spelare 1 ruta
        player1.setBackground(Color.YELLOW);
        player1.setBorder(BorderFactory.createLoweredBevelBorder());
        player1.add(lblSpelare1, BorderLayout.NORTH);
        activePlayer = playerEtt;
        points1 = new JLabel("0"); //spelare 1 poäng
        points1.setFont(score);
        points1.setHorizontalAlignment(SwingConstants.CENTER);
        player1.add(points1, BorderLayout.CENTER);
        player2 = new JPanel(new BorderLayout()); //spelare 2 ruta
        player2.setBackground(Color.LIGHT_GRAY);
        player2.setBorder(BorderFactory.createRaisedBevelBorder());
        player2.add(lblSpelare2, BorderLayout.NORTH);
        points2 = new JLabel("0"); //spelare 2 poäng
        points2.setFont(score);
        points2.setHorizontalAlignment(SwingConstants.CENTER);
        player2.add(points2, BorderLayout.CENTER);
        playerPanel.add(player1);
        playerPanel.add(player2);
        add(playerPanel, BorderLayout.WEST); //Spelarpanel läggs in
        nyttSpel(); //Kallar på nytt spel
        setVisible(true);
        pack();
    }
    private class spelPlan extends JPanel { //skapar vår spelplan
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
        }
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newBtn) { //om Nytt-knappen blivit klickad
            this.remove(gamePanel); //tar bort gamePanel för att kunna måla om den på nytt
            playerEtt.reset();      //om man inte gör spelarnas poäng 0 igen blir det problem
            playerTvå.reset();
            points2.setText("0");
            points1.setText("0");
            nyttSpel();
        } else if (e.getSource() == quitBtn) { //Avbryt - stänger ned programmet
            System.exit(1);
        } else if (e.getSource() instanceof Kort) { //kort är klickat
            Kort valtKort = (Kort) e.getSource();
            if (valtKort.getStatus() == Kort.Status.SAKNAS) //utan detta så kan man "välja" kort som är ur spelet igen
                return;
            if (första == null) { //visar kort
                första = valtKort;
                första.setStatus(Kort.Status.SYNLIGT);
            } else if (första != valtKort && andra == null) { //visar andra kortet
                andra = valtKort;
                andra.setStatus(Kort.Status.SYNLIGT);
                this.timer = new Timer(DELAY, new TimerListener()); //skapar timer
                timer.setRepeats(false);    //skickar bara en action event, om true blir timern lite konstig
                timer.start();              //startar timern
            }
        }
    }
    public void nyttSpel () { //nytt spel skapas
        maxpoint = (columns*rows)/2;        //variabel för största möjliga poäng
        Verktyg verktyg = new Verktyg();
        verktyg.slumpOrdning(this.k); //blandar om för att kunna plocka ut hälften
        this.spelKort = new Kort[rows*columns];
        for (int i=0; i<((rows*columns)/2);i++) { //loopar för att lägga in kort vi ska använda i spelKort
            this.spelKort[i] = this.k[i].copy(); //lägger in de /2 första
            this.spelKort[i+((rows*columns)/2)] = this.k[i]; //lägger in andra hälften
        }
        verktyg.slumpOrdning(this.spelKort); //slumpar slutligen om igen
        System.out.println("rows: " + rows + ", columns: " + columns);
        this.gamePanel = new JPanel(new GridLayout(rows,columns));
        gamePanel.setPreferredSize(new Dimension(150*columns,150*rows));
        for (int i=0; i<(rows*columns);i++) { //loopar ut alla kort på spelplanen
            gamePanel.add(spelKort[i]);
            spelKort[i].setStatus(Kort.Status.DOLT);
            spelKort[i].addActionListener(this);
        }
        add(gamePanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    public class TimerListener implements ActionListener {
        public void actionPerformed (ActionEvent e){
            if (första.sammaBild(andra)) { //Båda valda bilder lika
                första.setStatus(Kort.Status.SAKNAS);
                andra.setStatus(Kort.Status.SAKNAS);
                if (playerEtt == activePlayer) { //poäng till spelare 1
                    playerEtt.addPoint();
                    points1.setText(String.valueOf(playerEtt.getPoints()));
                } else { //poäng till spelare 2
                    playerTvå.addPoint();
                    points2.setText(String.valueOf(playerTvå.getPoints()));
                }
                vinnare(); //kollar om någon har vunnit
            } else { //De två valda bilderna var ej lika
                första.setStatus(Kort.Status.DOLT);
                andra.setStatus(Kort.Status.DOLT);
                if (activePlayer == playerEtt) { //Byter spelares tur
                    activePlayer = playerTvå;
                    player2.setBackground(Color.YELLOW);
                    player1.setBackground(Color.LIGHT_GRAY);
                    player2.setBorder(BorderFactory.createLoweredBevelBorder());
                    player1.setBorder(BorderFactory.createRaisedBevelBorder());
                } else { //Byter spelares tur
                    activePlayer = playerEtt;
                    player1.setBackground(Color.YELLOW);
                    player2.setBackground(Color.LIGHT_GRAY);
                    player1.setBorder(BorderFactory.createLoweredBevelBorder());
                    player2.setBorder(BorderFactory.createRaisedBevelBorder());
                }
            }
            första = null;
            andra = null;
        }
    }
    public void vinnare() { //Person vann
        if (första.sammaBild(andra)) {
            maxpoint = maxpoint - 1;    //sänker antal poäng som kan fås
        }
        if (maxpoint == 0) {        //när antal poäng som kan fås är 0 (alla kort ute ur spel) utses en vinnare
            if (playerEtt.getPoints() > playerTvå.getPoints()) {
                JOptionPane.showMessageDialog(null, "Grattis " + playerEtt.getName() + " du vann!");
            } else if (playerEtt.getPoints() < playerTvå.getPoints()){
                JOptionPane.showMessageDialog(null, "Grattis " + playerTvå.getName() + " du vann!");
            } else {
                JOptionPane.showMessageDialog(null, "Det blev lika!");
            }
        }
    }
    public static void main(String[]args) {
        new Memory();
    }
}
