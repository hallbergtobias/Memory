import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
/**
 * @author Tobias Hallberg
 * @author Emil Hukic
 */
public class Memory extends JFrame implements ActionListener{

    public static final int DELAY = 1500;
    private Kort k[]; //alla kort
    private Kort spelKort[]; //kort vi spelar med

    private int rows;   //antal rader
    private int columns;    //antal kolumner
    private Kort första;
    private Kort andra;

    private Player playerEtt;
    private Player playerTvå;
    private Player activePlayer;
    private JButton newBtn, quitBtn;    //knapp Nytt och Avbryt
    private JPanel gamePanel;   //Spelpanelen
    private Timer timer;

    private JPanel player1;
    private JPanel player2;

    private JLabel points1;     //poäng spelare 1
    private JLabel points2;     //poäng spelare 2
    
    private String namn;
    public int maxpoint;

    public Memory() {
        try {   //Kollar att mappen finns och att mappen innehåller > 1 bild

            File bildmapp = new File("bildmapp");
            File[] bilder = bildmapp.listFiles();

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

        } catch (NullPointerException e) {  //mappen finns ej - avslutar
            JOptionPane.showMessageDialog(this, "Mappen \"bildmapp\" hittas ej. Programmet avslutas");
            System.out.println("bildmapp not found: " + e);
            System.exit(0);
        }


        boolean notEnoughCards = true;

        while (notEnoughCards) {    //Kollar om vi har tillräckligt med kort för inmatade kolumner/rader
            String antKol = JOptionPane.showInputDialog("Ange antal kolumner med ett heltal över 1 ");
                        if(antKol == null) {
                System.exit(0);
            }
            
            String antRad = JOptionPane.showInputDialog("Ange antal rader med et heltal över 1: ");
                        if(antRad == null) {
                System.exit(0);
            }

            try {   // Rad/kolumn ej angiven som siffra
                this.columns = Integer.parseInt(antKol);
                this.rows = Integer.parseInt(antRad);
                if ((this.columns*this.rows)/2 > this.k.length) {
                    JOptionPane.showMessageDialog(this, "Du har för få bilder i din bildmapp. Ange färe kolumner/rader.");
                } else if ((this.columns*this.rows)%2==1) {     //I memory krävs jämnt antal bilder
                    JOptionPane.showMessageDialog(this, "Du kan inte ha ett ojämnt antal memorykort");
                } else if (columns < 2 || rows < 2){
                    JOptionPane.showMessageDialog(this, "Både antal kolumner och rader måste vara över 1");
                } else {
                    notEnoughCards = false;
                }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Ange antaler rader/kolumner som en siffra, tex \"4\".");
            }
        }


        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400,400); //ska bero på antalet kolumner/rader!
        setLocation(300,100);





        spelPlan plan = new spelPlan();
        add(plan);

        while (namn == null || namn.length() == 0) {
            namn = JOptionPane.showInputDialog("Vad heter spelare 1?");
            if(namn == null) {
                System.exit(0);
            }
        }
        playerEtt = new Player(namn, true);
        namn = null;
        while (namn == null || namn.length() == 0) {
            namn = JOptionPane.showInputDialog("Vad heter spelare 2?");
            if(namn == null) {
                System.exit(0);
            }
        }
        playerTvå = new Player(namn);

        JPanel btnPanel = new JPanel(new FlowLayout());

        this.newBtn = new JButton("Nytt");
        this.quitBtn = new JButton("Avsluta");

        newBtn.addActionListener(this);
        quitBtn.addActionListener(this);
        btnPanel.add(this.newBtn);
        btnPanel.add(this.quitBtn);
        add(btnPanel, BorderLayout.SOUTH);

        Font playerFont = new Font(Font.SANS_SERIF, Font.BOLD, 20);
        JLabel lblSpelare1 = new JLabel(playerEtt.getName());
        lblSpelare1.setFont(playerFont);
        JLabel lblSpelare2 = new JLabel(playerTvå.getName());
        lblSpelare2.setFont(playerFont);
        JPanel playerPanel = new JPanel(new GridLayout(2,1));
        playerPanel.setPreferredSize(new Dimension(80, 260));

        player1 = new JPanel();
        player1.setBackground(Color.YELLOW);
        player1.setBorder(BorderFactory.createLoweredBevelBorder());
        activePlayer = playerEtt;
        player1.add(lblSpelare1);
        points1 = new JLabel("0");
        player1.add(points1, BorderLayout.CENTER);

        player2 = new JPanel();
        player2.setBackground(Color.LIGHT_GRAY);
        player2.setBorder(BorderFactory.createRaisedBevelBorder());
        player2.add(lblSpelare2);
        points2 = new JLabel("0");
        player2.add(points2, BorderLayout.CENTER);
        playerPanel.add(player1);
        playerPanel.add(player2);
        add(playerPanel, BorderLayout.WEST);


        nyttSpel();
        setVisible(true);
        pack();

    }


    private class spelPlan extends JPanel { //skapar vår spelplan
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
        }
    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newBtn) {  //om Nytt-knappen blivit klickad
            this.remove(gamePanel); //tar bort gamePanel för att kunna måla om den på nytt
            playerEtt.reset();
            playerTvå.reset();
            points2.setText("0");
            points1.setText("0");
            nyttSpel();
        } else if (e.getSource() == quitBtn) {  //Avbryt - stänger ned programmet
            System.exit(1);

        } else if (e.getSource() instanceof Kort) {     //kort är klickat
            Kort valtKort = (Kort) e.getSource();
            if (första == null) {   //visar kort
                första = valtKort;
                första.setStatus(Kort.Status.SYNLIGT);
            }  else if (första != valtKort && andra == null) {  //visar andra kortet
                andra = valtKort;
                andra.setStatus(Kort.Status.SYNLIGT);
                this.timer = new Timer(DELAY, new TimerListener());
                timer.setRepeats(false);
                timer.start();
                }
        }
    }


    public void nyttSpel () {   //nytt spel skapas
        Verktyg verktyg = new Verktyg();
        verktyg.slumpOrdning(this.k); //blandar om för att kunna plocka ut hälften
        this.spelKort = new Kort[rows*columns];

        for (int i=0; i<((rows*columns)/2);i++) {   //loopar för att lägga in kort vi ska använda i spelKort
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
            if (första.sammaBild(andra)) {
                första.setStatus(Kort.Status.SAKNAS);
                andra.setStatus(Kort.Status.SAKNAS);
                //få poäng
                if (playerEtt == activePlayer) {
                    playerEtt.addPoint();
                    points1.setText(String.valueOf(playerEtt.getPoints()));
                    System.out.println("playerEtt fick poäng, har nu: " + playerEtt.getPoints());
                } else {
                    playerTvå.addPoint();
                    points2.setText(String.valueOf(playerTvå.getPoints()));
                    System.out.println("playerTvå fick poäng, har nu: " + playerTvå.getPoints());
                }
                vinnare();
            } else {
                första.setStatus(Kort.Status.DOLT);
                andra.setStatus(Kort.Status.DOLT);
                if (activePlayer == playerEtt) {
                    activePlayer = playerTvå;
                    player2.setBackground(Color.YELLOW);
                    player1.setBackground(Color.LIGHT_GRAY);
                    player2.setBorder(BorderFactory.createLoweredBevelBorder());
                    player1.setBorder(BorderFactory.createRaisedBevelBorder());
                } else {
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
    
        public void vinnare() {
        if (första.sammaBild(andra)) {
            maxpoint = maxpoint - 1;
            System.out.print("Maxpoint =" + maxpoint);
        }
        if (maxpoint == 0) {
            if (playerEtt.getPoints() > playerTvå.getPoints()) {
                JOptionPane.showMessageDialog(null, "Grattis " + playerEtt.getName() + " du vann!");
            } else if (playerEtt.getPoints() > playerTvå.getPoints()){
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
