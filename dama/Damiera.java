package dama;

import java.util.ArrayList;

/**
 *
 * @authors Francesco Farina, Luca Olivieri, Luca Pasetto
 */
public class Damiera {

    Pedina[] damiera;

    /**
     * Il costruttore della classe damiera
     * @param numCaselleNere il numero di caselle nere nella damiera
     */
    public Damiera(int numCaselleNere) {

        //di una damiera considero solo le caselle nere perchè le bianche sono inutilizzate
        damiera = new Pedina[numCaselleNere];// consideriamo un array con solo le
        // caselle nere numerandole da in
        // alto a sx
    }

    /**
     **Il costruttore senza parametri della classe damiera
     */
    public Damiera() {
        //di una damiera considero solo le caselle nere perchè le bianche sono inutilizzate
        this((8 * 8) / 2);
    }

    
    /**
     * Popola la damiera con le pedine per iniziare la partita
     */
    public void riempiDamiera() {

        for (int i = 0; i < 12; i++) {
            damiera[i] = new Pedina(true);
        }

        for (int i = 20; i < 32; i++) {
            damiera[i] = new Pedina(false);
        }

    }

    public String toString() {
        String s = "";
        boolean riga = true;// riga pari true , riga dispari false
        for (int i = 0; i < damiera.length; i++) {

            if (damiera[i] != null) {
                if (riga) // numero di riga pari
                {
                    s += damiera[i].getColore() ? "|x|| |" : "|o|| |"; // |x|
                } // NERO
                else {
                    s += damiera[i].getColore() ? "| ||x|" : "| ||o|";
                }
            } else {
                s += "| || |";
            }
            if ((i + 1) % 4 == 0) { // riga finita
                s += '\n';
                riga = !riga;
            }

        }
        return s;
    }

    /**
     * Data la posizione di una pedina restituisce un array con gli spostamenti che può eseguire
     * @param from la posizione della pedina
     * @return mossa le mosse chè può eseguire, se mossa[i] = null mossa non permessa 
     */
    public Integer[] mosseDisponibili(int from) {
        Integer[] mossa; // classe wrapper, mi permette di usare il null

        boolean leftSide = leftSide(from, 8);

        boolean rightSide = rightSide(from, 8);

        if (!damiera[from].isDama()) {
            mossa = new Integer[2];
            if (damiera[from].getColore()) {
                // se è libero e non è sul lato e non esce dalla damiera,
                // altrimenti non puÃ² muoversi (null)
                if ((from >= 4 && from <= 7) || (from >= 12 && from <= 15)
                        || (from >= 20 && from <= 23) || (from >= 28 && from <= 31)) {
                    // le dispari richiedono una casella da sommare in piÃ¹
                    mossa[0] = from + 4 < damiera.length
                            && (damiera[from + 4] == null) ? from + 4 : null;
                    mossa[1] = from + 5 < damiera.length
                            && (damiera[from + 5] == null) && !rightSide ? from + 5
                            : null;
                } else {

                    mossa[0] = from + 3 < damiera.length
                            && (damiera[from + 3] == null) && !leftSide ? from + 3
                            : null;
                    mossa[1] = from + 4 < damiera.length
                            && (damiera[from + 4] == null) ? from + 4 : null;
                }
            } else {

                if ((from >= 24 && from <= 27) || (from >= 16 && from <= 19)
                        || (from >= 8 && from <= 11) || (from >= 0 && from <= 3)) {
                    mossa[0] = from - 5 >= 0 && (damiera[from - 5] == null)
                            && !leftSide ? from - 5 : null;
                    mossa[1] = from - 4 >= 0 && (damiera[from - 4] == null) ? from - 4
                            : null;
                } else {
                    mossa[0] = from - 4 >= 0 && (damiera[from - 4] == null) ? from - 4
                            : null;
                    mossa[1] = from - 3 >= 0 && (damiera[from - 3] == null)
                            && !rightSide ? from - 3 : null;
                }
            }

        } else {
            // è un Dama

            mossa = new Integer[4];

            if ((from >= 24 && from <= 27) || (from >= 16 && from <= 19)
                    || (from >= 8 && from <= 11) || (from >= 0 && from <= 3)) {
                mossa[0] = from - 5 >= 0 && (damiera[from - 5] == null)
                        && !leftSide ? from - 5 : null;
                mossa[1] = from - 4 >= 0 && (damiera[from - 4] == null) ? from - 4
                        : null;
            } else {
                mossa[0] = from - 4 >= 0 && (damiera[from - 4] == null) ? from - 4
                        : null;
                mossa[1] = from - 3 >= 0 && (damiera[from - 3] == null)
                        && !rightSide ? from - 3 : null;
            }

            if ((from >= 4 && from <= 7) || (from >= 12 && from <= 15)
                    || (from >= 20 && from <= 23) || (from >= 28 && from <= 31)) {
                // le dispari richiedono una casella da sommare in piÃ¹
                mossa[2] = from + 4 < damiera.length
                        && (damiera[from + 4] == null) ? from + 4 : null;
                mossa[3] = from + 5 < damiera.length
                        && (damiera[from + 5] == null) && !rightSide ? from + 5
                        : null;
            } else {

                mossa[2] = from + 3 < damiera.length
                        && (damiera[from + 3] == null) && !leftSide ? from + 3
                        : null;
                mossa[3] = from + 4 < damiera.length
                        && (damiera[from + 4] == null) ? from + 4 : null;
            }
        }

        if (!damiera[from].isDama()) {
            if (mossa[0] == null && mossa[1] == null) {
                return null;
            }
        } else {
            if (mossa[0] == null && mossa[1] == null && mossa[2] == null && mossa[3] == null) {
                return null;
            }

        }
        return mossa;
    }

    /**
     * Esegue la mossa nella damiera
     * @param from la posizione iniziale della pedina nella damiera
     * @param to la posizione di arrico della pedina nella damiera
     */
    public void execMossa(int from, int to) {
        // se la pedina Ã¨ bianca puÃ² andare in celle con indice maggiore,
        // se Ã¨ nera con indice minore
        // il Dama in entrambi i sensi

        // da controllare che non si possa mangiare
        damiera[to] = damiera[from];
        damiera[from] = null;
        System.out.println(toString());



    }
/**
     * Valuta se una pedina è sul lato sinistro
     * @param from la posizione della pedina nella damiera
     * @param s spiazzamento
     * @return true è sul lato sinistro false non è sul lato sinistro
     */
    public static boolean leftSide(int from, int s) {
        //se chiamo da AI,s=4
        // se chiamo da mossedisponibili, s=4
        // se chiamo da mangiabili(ricorsione), s=8
        return (from % s == 0); // la pedina si trova nel lato sinistro della
        // damiera
    }
/**
     * Valuta se una pedina è sul lato destro
     * @param from la posizione della pedina nella damiera
     * @param s spiazzamento
     * @return true è sul lato sinistro false non è sul lato destro
     */
    public static boolean rightSide(int from, int s) {
        return ((from + 1) % s == 0);// la pedina si trova sul lato destro della
        // damiera
    }

/**
     * Effettua la mangiata nella damiera
     * @param from la posizione della pedina
     * @param to la posizione della pedina dopo la mangiata
     */
    public void mangia(int from, int to) {

        

        execMossa(from, to);// eseguo la mossa

        //elimino la pedina/dama mangiata
        if (from > to) {

            // mangiata up
            if ((from >= 24 && from <= 27) || (from >= 16 && from <= 19)
                    || (from >= 8 && from <= 11) ) {
                if ((to == from - 7) && !rightSide(from, 4)) {
                    damiera[from - 4] = null;
                } else // to == from -9
                if (!leftSide(from, 4)) {
                    damiera[from - 5] = null;
                }
            }
            if ((from >= 28 && from <= 31) || (from >= 20 && from <= 23)
                    || (from >= 12 && from <= 15)) {
                if (to == from - 7 && !rightSide(from, 4)) {
                    damiera[from - 3] = null;
                } else // to == from -9
                if (!leftSide(from, 4)) {
                    damiera[from - 4] = null;
                }
            }

        } else {
            // from<to perché non è possibile from=to(from non può
            // appartenere a mangiabili

            // magia down

            if ((from >= 4 && from <= 7) || (from >= 12 && from <= 15)
                    || (from >= 20 && from <= 23)) {
                if ((to == from + 7) && !leftSide(from, 4)) {
                    damiera[from + 4] = null;
                } else // to == from +9
                if (!rightSide(from, 4)) {
                    damiera[from + 5] = null;
                }
            }
            if ((from >= 0 && from <= 3) || (from >= 8 && from <= 11)
                    || (from >= 16 && from <= 19)) {
                if (to == from + 7 && !leftSide(from, 4)) {
                    damiera[from + 3] = null;
                } else // to == from+9
                if (!rightSide(from, 4)) {
                    damiera[from + 4] = null;
                }
            }

        }

      
    }
/**
     * Restituisce un array con possibili mangiate della pedina
     * @param from la posizione attuale della pedina(anche virtuale per l'AI)
     * @param first la posizione iniziale contenente la pedina 
     * @return 
     */
    public Integer[] mangiabili(int from, int first) {

        Integer[] m;
        if (damiera[first] != null) {
            if (!damiera[first].isDama()) {
                m = new Integer[2];

                if (damiera[first].getColore()) {
                    mangiabileDown(m, from, first); // NERO
                } else {
                    mangiabileUp(m, from, first); // BIANCO
                }
            } else {
                m = new Integer[4];
                for (int i = 0; i < m.length; i++) {
                    m[i] = null; // inizializzo l'array a null
                }
                mangiabileDown(m, from, first);
                mangiabileUp(m, from, first);
            }
            int count = 0;
            for (Integer n : m) {
                if (n == null) {
                    count++;
                }
            }
            return m.length == count ? null : m;
        }
        return null;

    }


    /**
     * 
     * @param m
     *            array che conterrà  le mangiate disponibili; [0] mangio in
     *            alto a sinistra ; [1] mangio in alto a destra; nel caso di
     *            Dama [2] mangio in alto a sinistra ; [3] mangio in alto a
     *            destra;
     * @param from la posizione attuale della pedina(anche virtuale per l'AI)
     * @param first la posizione iniziale contenente la pedina 
     * 
     */
    private void mangiabileDown(Integer[] m, Integer from, int first) { // int

        if ((from >= 4 && from <= 7) || (from >= 12 && from <= 15)
                || (from >= 20 && from <= 23)) {
            if (damiera[from + 4] != null
                    && !leftSide(from, 4)
                    && damiera[first].getColore() != damiera[from + 4].getColore() && damiera[from + 7] == null) {
                m[0] = from + 7;
            }

            if (damiera[from + 5] != null
                    && !rightSide(from, 4)
                    && damiera[first].getColore() != damiera[from + 5].getColore() && damiera[from + 9] == null) {
                m[1] = from + 9;
            }
        } else {
            if ((from >= 0 && from <= 3) || (from >= 8 && from <= 11)
                    || (from >= 16 && from <= 19)) {

                if (damiera[from + 3] != null
                        && !leftSide(from, 4)
                        && damiera[first].getColore() != damiera[from + 3].getColore() && damiera[from + 7] == null) {
                    m[0] = from + 7;
                }

                if (damiera[from + 4] != null
                        && !rightSide(from, 4)
                        && damiera[first].getColore() != damiera[from + 4].getColore() && damiera[from + 9] == null) {
                    m[1] = from + 9;
                }
            }
        }

    }

    /**
     * 
     * @param m
     *            array che conterrà  le mangiate disponibili; [0] mangio in
     *            alto a sinistra ; [1] mangio in alto a destra; nel caso di
     *            Dama [2] mangio in alto a sinistra ; [3] mangio in alto a
     *            destra;
     * @param from la posizione attuale della pedina(anche virtuale per l'AI)
     * @param first la posizione iniziale contenente la pedina 
     */
    private void mangiabileUp(Integer[] m, Integer from, int first) { // int

        int d = damiera[first].isDama() ? 2 : 0; // pedina vale 0, Dama vale
        // 2
        // 1

        if ((from >= 24 && from <= 27) || (from >= 16 && from <= 19)
                || (from >= 8 && from <= 11)) {

            if (damiera[from - 4] != null
                    && !rightSide(from, 4)
                    && damiera[first].getColore() != damiera[from - 4].getColore() && damiera[from - 7] == null) {
                m[1 + d] = from - 7;
            }

            if (damiera[from - 5] != null
                    && !leftSide(from, 4)
                    && damiera[first].getColore() != damiera[from - 5].getColore() && damiera[from - 9] == null) {
                m[0 + d] = from - 9;
            }
        } else {

            if ((from >= 28 && from <= 31) || (from >= 20 && from <= 23)
                    || (from >= 12 && from <= 15)) {

                if (damiera[from - 3] != null
                        && !rightSide(from, 4)
                        && damiera[first].getColore() != damiera[from - 3].getColore() && damiera[from - 7] == null) {
                    m[0 + d] = from - 7;
                }

                if (damiera[from - 4] != null
                        && !leftSide(from, 4)
                        && damiera[first].getColore() != damiera[from - 4].getColore() && damiera[from - 9] == null) {
                    m[1 + d] = from - 9;
                }
            }

        }

    }
/**
     * Valuta se la partita è terminata
     * @param colore il colore del giocatore
     * @return true la partita è finita, false la partita non è finita
     */
    public boolean partitaFinita(boolean colore) {
        // false=bianco
        // true=nero
        int count = 0;
        for (int i = 0; i < damiera.length; i++) {
            if (damiera[i] != null && damiera[i].getColore() == colore
                    && (mosseDisponibili(i) != null || mangiabili(i, i) != null)) {
                count++;
            }
        }
        return count == 0;
    }

    /**
     * 
     * @return la damiera
     */
    public Pedina[] getDamiera() {
        return this.damiera;
    }
/**
     * Ritorna una lista contenente le pedine che si possono mangiare
     * @param colore il colore del giocatore
     * @return m la lista delle pedine che possono mangiare
     */
    public ArrayList<Integer> eaters(boolean colore) {

        ArrayList<Integer> e = new ArrayList<Integer>();

        for (int i = 0; i < damiera.length; i++) {

            if (damiera[i] != null && damiera[i].getColore() == colore
                    && mangiabili(i, i) != null) {
                e.add(i);
            }

        }

        if (e.isEmpty()) {
            return null;
        }

        return e;
    }
/**
     * Ritorna una lista contenente le pedine che si possono muovere
     * @param colore il colore del giocatore
     * @return m la lista delle pedine muovibili
     */
    public ArrayList<Integer> movables(boolean colore) {

        ArrayList<Integer> m = new ArrayList<Integer>();

        for (int i = 0; i < damiera.length; i++) {

            if (damiera[i] != null && damiera[i].getColore() == colore
                    && mosseDisponibili(i) != null) {
                m.add(i);
            }

        }

        if (m.isEmpty()) {
            return null;
        }

        return m;
    }
}