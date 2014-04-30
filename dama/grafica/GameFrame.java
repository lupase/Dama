package dama.grafica;

import dama.AI;
import dama.Damiera;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.Arrays;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @authors Francesco Farina, Luca Olivieri, Luca Pasetto
 */
public class GameFrame extends javax.swing.JFrame {

    private static final int HEIGHT = 810;
    private static final int WIDTH = 810;
    static Damiera d;
    static int idFrom;
    static boolean firstClick;
    static boolean semaphore;
    static boolean mangiaAncora;
    static AI intelligence;
    static boolean mycolor;
    static JPanel bottonPanel;

    /**
     * Il costruttore della classe GameFrame
     */
    public GameFrame() {

        initComponents();

        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {

                //quando chiudo la finestra di gioco, il pulsante "Nuova Partita" viene riabilitato
                dama.Main.f.buttonNuovapartita.setEnabled(true);
            }
        });


    }

    /**
     *  Inizializza le componenti della damiera
     */
    private void initComponents() {

        d = new Damiera();
        d.riempiDamiera();

        setTitle("Dama Project");

        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(GameFrame.HIDE_ON_CLOSE);

        setLayout(new BorderLayout(10, 10));

        //JPanel damiera = centerComponent();
        bottonPanel = centerComponent();
        add(bottonPanel, BorderLayout.CENTER);

        add(southComponent(), BorderLayout.SOUTH);
        add(eastComponent(), BorderLayout.EAST);


        firstClick = true;// il primo click di un bottone per selezionare la pedina da muovere
        semaphore = true;//mutua esclusione dei bottoni 
        mangiaAncora = false;//per le mangiate multiple
        
        Object[] options = {"Bianco",
            "Nero"};
        
        mycolor = JOptionPane.showOptionDialog(null, "Scegli il colore con cui vuoi giocare", "Colore",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]) == 1 ? true : false;         //bianco 0   //nero 1
        intelligence = new AI(!mycolor, d); //creo l'intelligenza artificiale avversaria
    }

    /**
     * Crea JPeast
     * @return JPeast il JPanel che contiene la griglia dei numeri 1-8 nella parte destra dello schermo
     */
    private JPanel eastComponent() {
        JPanel JPeast = new JPanel();
        JPeast.setLayout(new GridLayout(8, 0));

        for (int i = 1; i <= 8; i++) {
            JPeast.add(new JLabel("" + i));

        }

        return JPeast;
    }
    
    /**
     * Crea JPcenter
     * @return JPcenter il JPanel che contiene le 64 caselle della damiera
     */
    private JPanel centerComponent() {

        JPanel JPcenter = new JPanel();
        JPcenter.setLayout(new GridLayout(8, 8));


        boolean change = false;// serve per stampare a righe alternate i colori delle caselle
        
        for (int i = 0; i < d.getDamiera().length; i++) {
            if (i % 4 == 0) 
                change = !change;
            

            if (change) {      
                if (d.getDamiera()[i] != null) {
                    if (d.getDamiera()[i].getColore()) {
                        JPcenter.add(new JCasella("" + i, 3));//casella nera con pedina nera
                    } else {
                        JPcenter.add(new JCasella("" + i, 2));//casella nera con pedina bianca
                    }

                } else {
                    JPcenter.add(new JCasella("" + i, 1));  //casella nera vuota
                }
                JPcenter.add(new JCasella("", 0));  //casella bianca

            } else {
                JPcenter.add(new JCasella("", 0));  //casella bianca
                if (d.getDamiera()[i] != null) {
                    if (d.getDamiera()[i].getColore()) {
                        JPcenter.add(new JCasella("" + i, 3));//casella nera con pedina nera
                    } else {
                        JPcenter.add(new JCasella("" + i, 2));//casella nera con pedina bianca
                    }

                } else {
                    JPcenter.add(new JCasella("" + i, 1));  //casella nera vuota
                }
            }

        }
        return JPcenter;



    }

    /**
     * Crea JPsouth
     * @return JPsouth il JPanel che contiene la griglia delle lettere A-H nella parte bassa dello schermo
     */
    private JPanel southComponent() {
        JPanel JPsouth = new JPanel();
        JPsouth.setLayout(new GridLayout(0, 8));

        for (int i = 0; i < 8; i++) {
            JPsouth.add(new JLabel("" + (char) ('A' + i)));

        }

        return JPsouth;
    }

    /**
     * Ridipinge le JCasella (caselle nere)
     */
    public static void ridipingi() {
        JCasella jc = null;
        for (Component cmp : bottonPanel.getComponents()) {
            if (!cmp.getName().equals("")) {// è JCasella
                jc = (JCasella) cmp;
                jc.disabilitaBord();
                if (d.getDamiera()[Integer.parseInt(cmp.getName())] == null) {
                    jc.setImmagine(1);  //casella nera vuota
                } else {//c'è una pedina
                    if (d.getDamiera()[Integer.parseInt(cmp.getName())].getColore()) {
                        if (d.getDamiera()[Integer.parseInt(cmp.getName())].isDama()) {
                            jc.setImmagine(5);//dama nero
                        } else {
                            jc.setImmagine(3);//pedina nera
                        }
                    } else {
                        if (d.getDamiera()[Integer.parseInt(cmp.getName())].isDama()) {
                            jc.setImmagine(4);//dama bianca
                        } else {
                            jc.setImmagine(2);//pedina bianca
                        }
                    }
                }
            }
       
        }
    }
    
    /**
     * Gestisce i click dell'utente 
     * @param posCampo la posizione presa dal bottone
     */
    public static void gestisciEvento(Integer posCampo) {
        if (firstClick && (d.getDamiera()[posCampo] != null) && (d.getDamiera()[posCampo].getColore() == mycolor)) {
            if (mangiaAncora == false) {
                idFrom = posCampo; //il bottone è un punto di partenza per la mossa
            }
            firstClick = false;
            // scelgo una mossa disponibile e la stampo a video

            if (d.mangiabili(idFrom, idFrom) == null && d.mosseDisponibili(idFrom) != null) {
                //non ho da mangiare
                System.out.println("Scegliere una delle seguenti mosse: ");
                for (Integer pos : d.mosseDisponibili(idFrom)) {
                    System.out.println(pos);
                    colDest(pos);    //se hai selezionato un punto di partenza valido disegna i bordi gialli
                }
            }
            if (d.mangiabili(idFrom, idFrom) != null) { //mangiata
                // scelgo una mangiata disponibile e la stampo a video
                System.out.println("Scegliere una delle seguenti mangiate: ");
                for (Integer pos : d.mangiabili(idFrom, idFrom)) {
                    System.out.println(pos);
                    colDest(pos);    //se hai selezionato un punto di partenza valido disegna i bordi gialli
                }
            }


        } else {
            game(idFrom, posCampo); //è la destinazione della mossa
                                    //oppure è una mangiata multipla

            ridipingi();
            if (!partitaFinita()) { 

                firstClick = true; //fai altre mosse
            }

        }

    }

    /**
     * Gestisce le mosse e fa partire l'AI
     * @param from la posizione di partenza della mossa
     * @param to   la posizione di destinazione della mossa
     */
    private static void game(Integer from, Integer to) {

        if (d.getDamiera()[from] != null && d.getDamiera()[from].getColore() == mycolor) {

            if (d.eaters(mycolor) != null) {
                //devo effettuare la mangiata


                if (d.eaters(mycolor).contains(from)) {
                    if (Arrays.asList(d.mangiabili(from, from)).contains(to)) {
                        d.mangia(from, to);
 
                        if (!d.getDamiera()[to].isDama()
                                && ((to >= 28 && to <= 31 && d.getDamiera()[to].getColore()) || (to >= 0
                                && to <= 3 && !d.getDamiera()[to].getColore()))) {
                            d.getDamiera()[to].setDama();   //la pedina diventa dama
                            System.out.println("La pedina " + to + " è diventata Dama");
                            mangiaAncora = false;
                            ridipingi();
                            
                            //se la pedina diventa dama cedo il turno
                            
                            intelligence.getRes();  //esegue la mossa dell'AI
                            ridipingi();

                        } else {
                            if (d.mangiabili(to, to) != null) {

                                System.out.println(to + " può mangiare ancora");
                                mangiaAncora = true;
                                idFrom = to;    //il to diventa from per eseguire la mangiata multipla
                            } else {
                                mangiaAncora = false;
                                ridipingi();

                                intelligence.getRes(); //esegue la mossa dell'AI
                                ridipingi();

                            }
                        }
                    }
                }
            } else {
                //cotrollo se mi posso muovere

                if (d.mangiabili(from, from) == null) {
                    // non devo mangiare

                    if (d.mosseDisponibili(from) != null && Arrays.asList(d.mosseDisponibili(from)).contains(to)) {

                        d.execMossa(from, to);
                        
                        if (!d.getDamiera()[to].isDama()
                                && ((to >= 28 && to <= 31 && d.getDamiera()[to].getColore()) || (to >= 0
                                && to <= 3 && !d.getDamiera()[to].getColore()))) {
                            d.getDamiera()[to].setDama();
                            System.out.println("La pedina " + to + " è diventata Dama");


                        }
                        ridipingi();

                        intelligence.getRes();  //esegue la mossa dell'AI
                        ridipingi();

                    }

                }

            }


        }

    }

    /**
     * Verifica se la partita è terminata 
     * @return boolean true oppure false
     */
    private static boolean partitaFinita() {
        int bianco = 0;
        int nero = 0;
        for (int i = 0; i < d.getDamiera().length; i++) {
            if (d.getDamiera()[i] != null
                    && (d.mosseDisponibili(i) != null || d.mangiabili(i, i) != null)) {
                if (d.getDamiera()[i].getColore()) {
                    nero++;
                } else {
                    bianco++;
                }
            }
        }

        if (bianco == 0) {
            JOptionPane.showMessageDialog(null, "Il giocatore nero ha vinto la partita");       
            return true;
        }
        if (nero == 0) {
            JOptionPane.showMessageDialog(null, "Il giocatore bianco ha vinto la partita");
            return true;
        }
        return false;
    }

    
    /**
     * Colora le posizioni possibili di destinazione 
     * @param pos la posizione da controllare
     */
    private static void colDest(Integer pos) {
        if (pos != null) {
            for (Component cmp : bottonPanel.getComponents()) {
                if (!cmp.getName().equals("")) {// è JCasella
                    JCasella jc = (JCasella) cmp;
                    if (pos.toString().compareTo(jc.getName()) == 0) {
                        jc.abilitaPosFinale();
                    }
                }
            }
        }
    }
}