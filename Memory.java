import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 *
 */
public class Memory extends JFrame implements ActionListener{


    private Kort k[]; //alla kort
    private Kort spelKort[]; //kort vi spelar med
    private int rows;
    private int columns;
    private JButton newBtn, quitBtn;
    private JPanel gamePanel;


    public Memory() {

        File bildmapp = new File("bildmapp");
        File[] bilder = bildmapp.listFiles();

        this.k = new Kort[bilder.length];


        for(int i =0; i<bilder.length;i++) {    //loopar in alla kort i k

            ImageIcon bild = new ImageIcon(bilder[i].getPath());

            k[i] = new Kort(bild, Kort.Status.SYNLIGT); //ska ej vara synligt

            System.out.println(bild);

        }




/*
        String antKol = JOptionPane.showInputDialog("Ange antal kolumner: ");
        this.columns = Integer.parseInt(antKol);
        String antRad = JOptionPane.showInputDialog("Ange antal rader: ");
        this.rows = Integer.parseInt(antRad);
*/
        columns = 4; //satte värde för pallade inte alla rutor
        rows = 4;







        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400,400); //ska bero på antalet kolumner/rader!
        setLocation(500,200);
        spelPlan plan = new spelPlan();
        add(plan);



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
            this.remove(gamePanel);     //tar bort gamePanel för att kunna måla om den på nytt
            nyttSpel();
        } else if (e.getSource() == quitBtn) {
            System.exit(1);
        }

    }

    public void nyttSpel () {

        Verktyg verktyg = new Verktyg();
        verktyg.slumpOrdning(this.k);    //blandar om för att kunna plocka ut hälften


        this.spelKort = new Kort[rows*columns];

        for (int i=0; i<((rows*columns)/2);i++) {
            this.spelKort[i] = this.k[i];   //lägger in de /2 första
            this.spelKort[i+((rows*columns)/2)] = this.k[i];    //lägger in andra hälften
        }

        verktyg.slumpOrdning(this.spelKort);    //slumpar slutligen om igen



        System.out.println("rows: " + rows + ", columns: " + columns);
        this.gamePanel = new JPanel(new GridLayout(rows,columns));


        for (int i=0; i<(rows*columns);i++) {   //loopar ut alla kort
            gamePanel.add(spelKort[i]);
            spelKort[i].addActionListener(this);
        }




        add(gamePanel, BorderLayout.CENTER);




        revalidate();
        repaint();










    }


    public static void main(String[]args) {

        new Memory();

    }


}
