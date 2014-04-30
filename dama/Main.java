package dama;

import dama.grafica.WelcomeMenu;

/**
 *
 * @authors Francesco Farina, Luca Olivieri, Luca Pasetto
 */
public class Main {

    /**
     * il Jframe del menu iniziale
     */
    public static WelcomeMenu f;
                                 // static perchè lo chiamo in un altro packege

    /**
     * 
     * @param args 
     */
    public static void main(String[] args) {

        f = new WelcomeMenu();
        f.createAndShowGUI(); //eseguo il jframe

    }

}
