import com.sun.codemodel.internal.JOp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
/**
 *
 */
public class Memory extends JFrame implements ActionListener{
    public static final int DELAY = 1500;
    private Kort k[]; //alla kort
    private Kort spelKort[]; //kort vi spelar med
    private int rows;
    private int columns;
    private Kort första;
    private Kort andra;
    private Player playerEtt;
    private Player playerTvå;
    private Player activePlayer;
    private JButton newBtn, quitBtn;
    private JPanel gamePanel;
    private Timer timer;

    public Memory() {
        try {
            File bildmapp = new File("bildmapp");
            File[] bilder = bildmapp.listFiles();
            if (bilder.length<2) { //bildmappen innehåller för få bilder
                JOptionPane.showMessageDialog(this, "Mappen bildmapp innehåller endast " + bilder.length + " bilder. " +
                        "Lägg in fler bilder. Programmet avslutas");
                System.exit(0);
            }
            this.k = new Kort[bilder.length];
//NullPointerException = ingen mapp
            for (int i = 0; i < bilder.length; i++) { //loopar in alla kort i k
                ImageIcon bild = new ImageIcon(bilder[i].getPath());
                k[i] = new Kort(bild); //ska ej vara synligt
                System.out.println(bild);
            }
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(this, "Mappen \"bildmapp\" hittas ej. Programmet avslutas");
            System.out.println("bildmapp not found: " + e);
            System.exit(0);
        }
        boolean notEnoughCards = true;
        while (notEnoughCards) {
            String antKol = JOptionPane.showInputDialog("Ange antal kolumner: ");
            String antRad = JOptionPane.showInputDialog("Ange antal rader: ");
//numberformatexcetion
            try {
                this.columns = Integer.parseInt(antKol);
                this.rows = Integer.parseInt(antRad);
                if ((this.columns*this.rows)/2 > this.k.length) {
                    JOptionPane.showMessageDialog(this, "Du har för få bilder i din bildmapp. Ange färe kolumner/rader.");
                } else {
                    notEnoughCards = false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Ange antaler rader/kolumner som en siffra, tex \"4\".");
            }
        }
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400,400); //ska bero på antalet kolumner/rader!
        setLocation(500,200);
        spelPlan plan = new spelPlan();
        add(plan);
        Player playerEtt = new Player("Huey", true);
        Player playerTvå = new Player("Boss");
        JPanel btnPanel = new JPanel(new FlowLayout());
        this.newBtn = new JButton("Nytt");
        this.quitBtn = new JButton("Avsluta");
        newBtn.addActionListener(this);
        quitBtn.addActionListener(this);
        btnPanel.add(this.newBtn);
        btnPanel.add(this.quitBtn);
        add(btnPanel, BorderLayout.SOUTH);
        JLabel lblSpelare1 = new JLabel("Spelare 1");
        JLabel lblSpelare2 = new JLabel("Spelare 2");
        JPanel playerPanel = new JPanel(new GridLayout(2,1));
        playerPanel.setPreferredSize(new Dimension(80, 260));
        JPanel player1 = new JPanel();
        player1.setBackground(Color.YELLOW);
        player1.add(lblSpelare1);
        JPanel player2 = new JPanel();
        player2.setBackground(Color.YELLOW);
        player2.add(lblSpelare2);
        playerPanel.add(player1);
        playerPanel.add(player2);
        add(playerPanel, BorderLayout.WEST);
        nyttSpel();
        setVisible(true);
        pack();
    }
    private class spelPlan extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
        }
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newBtn) {
            this.remove(gamePanel); //tar bort gamePanel för att kunna måla om den på nytt
            nyttSpel();
        } else if (e.getSource() == quitBtn) {
            System.exit(1);
        } else if (e.getSource() instanceof Kort) {
            Kort valtKort = (Kort) e.getSource();
            if (första == null) {
                första = valtKort;
                första.setStatus(Kort.Status.SYNLIGT);
            }  else if (första != valtKort && andra == null) {
                andra = valtKort;
                andra.setStatus(Kort.Status.SYNLIGT);
                this.timer = new Timer(DELAY, new TimerListener());
                timer.setRepeats(false);
                timer.start();
                }
        }
    }
    public void nyttSpel () {
        Verktyg verktyg = new Verktyg();
        verktyg.slumpOrdning(this.k); //blandar om för att kunna plocka ut hälften
        this.spelKort = new Kort[rows*columns];
        for (int i=0; i<((rows*columns)/2);i++) {
            this.spelKort[i] = this.k[i].copy(); //lägger in de /2 första
            this.spelKort[i+((rows*columns)/2)] = this.k[i]; //lägger in andra hälften
        }
        verktyg.slumpOrdning(this.spelKort); //slumpar slutligen om igen
        System.out.println("rows: " + rows + ", columns: " + columns);
        this.gamePanel = new JPanel(new GridLayout(rows,columns));
        gamePanel.setPreferredSize(new Dimension(150*columns,150*rows));
        for (int i=0; i<(rows*columns);i++) { //loopar ut alla kort
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
            } else {
                första.setStatus(Kort.Status.DOLT);
                andra.setStatus(Kort.Status.DOLT);
                if (activePlayer == playerEtt) {
                    activePlayer = playerTvå;
                } else {
                    activePlayer = playerEtt;
                }
            }
            första = null;
            andra = null;
        }
    }

    public static void main(String[]args) {
        new Memory();
    }
}
