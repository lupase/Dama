package dama.grafica;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

/**
 *
 * @authors Francesco Farina, Luca Olivieri, Luca Pasetto
 */
public class JCasella extends JButton {

    // inizializzo le immagini che verranno inserite nei JCasella (Bottoni dell'interfaccia grafica)
    private final ImageIcon white = new ImageIcon(getClass().getResource("/images/casellaBianca.jpg"));
    private final ImageIcon black = new ImageIcon(getClass().getResource("/images/casellaNera.jpg"));
    private final ImageIcon pwhite = new ImageIcon(getClass().getResource("/images/pedinaBianca.jpg"));
    private final ImageIcon pblack = new ImageIcon(getClass().getResource("/images/pedinaNera.jpg"));
    private final ImageIcon dwhite = new ImageIcon(getClass().getResource("/images/damaBianca.jpg"));
    private final ImageIcon dblack = new ImageIcon(getClass().getResource("/images/damaNera.jpg"));
    LineBorder border = new LineBorder(Color.YELLOW, 4); // il bordo dell'immagine selezionata

    /**
     *  Il costruttore della classe JCasella
     * @param name il nome del bottone, assume valori tra 0 e 31 se il bottone è nero, altrimenti se è bianco ""
     * @param percorsoIcona il percorso dell'immagine da settare nel bottone
     */
    public JCasella(String name, int percorsoIcona) {

        setBorderPainted(false); //il bordo del bottone non viene colorato
        setName(name);  //il nome del bottone se la casella 
        setImmagine(percorsoIcona); //setto l'immagine nel bottone

        // gestisto il click del bottone
        addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (GameFrame.semaphore) { // mantiene la mutua esclusione della selezione casella
                    GameFrame.semaphore = false;
                    if (!getName().equals("")) {
                        // il bottone non è una casella bianca
                        System.out.println("" + Integer.parseInt(getName()));

                        GameFrame.gestisciEvento(Integer.parseInt(getName())); // gestisco la selezione

                    }
                    GameFrame.semaphore = true;
                }
            }
        });
    }

    /**
     * Disabilita il colore del bordo del bottone
     */
    public void disabilitaBord() {
        setBorderPainted(false);
    }

    /**
     * Colora il bordo del bottone
     */
    public void abilitaPosFinale() {
        setBorder(border);
        setBorderPainted(true);
    }

    /**
     * Setta dato un percorso un'immagine al bottone 
     * @param percorsoIcona  selezione del percorso dell'immagine del bottone
     */
    public void setImmagine(int percorsoIcona) {
        switch (percorsoIcona) {
            case 0:
                setIcon(white); // setto la casella vuota bianca
                break;
            case 1:
                setIcon(black);// setto la casella vuota nera
                break;
            case 2:
                setIcon(pwhite);// setto la casella occupata dalla pedina bianca
                break;
            case 3:
                setIcon(pblack);// setto la casella occupata dalla pedina nera
                break;
            case 4:
                setIcon(dwhite);// setto la casella occupata dalla dama bianca
                break;
            case 5:
                setIcon(dblack);// setto la casella occupata dalla dama nera
                break;

        }

    }
}
