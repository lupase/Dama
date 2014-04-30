package dama;

import java.util.Arrays;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @authors Francesco Farina, Luca Olivieri, Luca Pasetto
 */
public class Gioco {

    private boolean turno;
    private Damiera d;

    /**
     * Il costruttore della classe Gioco
     */
    public Gioco() {


        Object[] options = {"Bianco",
            "Nero"};

        int scelta = JOptionPane.showOptionDialog(null, "Scegli il colore con cui vuoi giocare", "Colore",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        //bianco 0   //nero 1
        

        turno = false; // iniziano i bianchi (i bianchi iniziano sempre per
        // primi)
        d = new Damiera();
        d.riempiDamiera();

        do {
            mossa(turno);
            turno = !turno; // cedo il turno all'avversario

        } while (!partitaFinita());
        System.out.println("La partita è terminata");
    }

    private void mossa(boolean turno) {
        boolean mangiaAncora = false;
        Integer from;
        Integer to = 999; // il programma il while la prima volta lo fa
        // sicuramente
        // tutta via il compilatore non lo vede quindi to
        // necessita di un inizializzazione
        System.out.println(d);
        Scanner s = new Scanner(System.in);
        do {
            System.out.println("inserire la posizione della pedina "
                    + (turno ? "nera" : "bianca"));
            from = s.nextInt();
            // acquisisco la posizione della pedina
        } while (d.getDamiera()[from] == null
                || d.getDamiera()[from].getColore() != turno
                || (d.mangiabili(from,from) == null && (d.mosseDisponibili(from) == null)));
        //||non e contenuta nellarray mangiatori

        while (d.eaters(turno) != null && !d.eaters(turno).contains(from)) {
            System.out.println("scegli una delle pedine con cui puoi mangiare "
                    + d.eaters(turno));
            from = s.nextInt();
        }

        System.out.println("Ho selezionato la pedina in posizione " + from);


        /*  while(d.mangiabili(from) == null &&  (d.mosseDisponibili(from) == null) ){
        System.out.println("la pedina scelta non si può muovere\nscegli un'altra pedina");
        from = s.nextInt();
        }*/

        if (d.mangiabili(from,from) == null) {
            // non devo mangiare
            Integer[] mosseDisp = d.mosseDisponibili(from);
            // scelgo una mossa disponibile e la stampo a video

            if (mosseDisp != null) {
                System.out.println("Scegliere una delle seguenti mosse: ");
                for (Integer pos : mosseDisp) {
                    System.out.println(pos);
                }

                do {
                    to = s.nextInt();
                    // acquisisco la mossa da fare
                } while (!Arrays.asList(d.mosseDisponibili(from)).contains(to));

                d.execMossa(from, to);
                if (!d.getDamiera()[to].isDama()
                        && ((to >= 28 && to <= 31 && d.getDamiera()[to].getColore()) || (to >= 0
                        && to <= 3 && !d.getDamiera()[to].getColore()))) {
                    d.getDamiera()[to].setDama();
                    System.out.println("La pedina " + to + " è diventata Dama");
                    turno = !turno;

                }

            }
        } else {
            do {// devo mangiare

                // scelgo una mangiata disponibile e la stampo a video
                System.out.println("Scegliere una delle seguenti mangiate: ");
                for (Integer pos : d.mangiabili(from,from)) {
                    System.out.println(pos);
                }

                while (!Arrays.asList(d.mangiabili(from,from)).contains(to)) {
                    System.out.println("inserisci una mangiata corretta");
                    to = s.nextInt();
                    // acquisisco la mangiata da fare
                }

                d.mangia(from, to);
                
                if (!d.getDamiera()[to].isDama()
                        && ((to >= 28 && to <= 31 && d.getDamiera()[to].getColore()) || (to >= 0
                        && to <= 3 && !d.getDamiera()[to].getColore()))) {
                    d.getDamiera()[to].setDama();
                    System.out.println("La pedina " + to + " è diventata Dama");
                    turno = !turno;

                }

                System.out.println(d);

                if (d.mangiabili(to,to) != null) {

                    System.out.println(to + " può mangiare ancora");
                    mangiaAncora = true;
                    from = to;
                } else {
                    mangiaAncora = false;
                }
            } while (mangiaAncora);
        }
    }

    /**
     * 
     * @return true se la partita è finita
     */
    public boolean partitaFinita() {
        int bianco = 0;
        int nero = 0;
        for (int i = 0; i < d.damiera.length; i++) {
            if (d.getDamiera()[i] != null
                    && (d.mosseDisponibili(i) != null || d.mangiabili(i,i) != null)) {
                if (d.getDamiera()[i].getColore()) {
                    nero++;
                } else {
                    bianco++;
                }

            }
        }

        if (bianco == 0) {
            System.out.println("Il giocatore nero ha vinto la partita");
            return true;
        }
        if (nero == 0) {
            System.out.println("Il giocatore bianco ha vinto la partita");
            return true;
        }
        return false;
    }
}