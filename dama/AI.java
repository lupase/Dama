package dama;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @authors Francesco Farina, Luca Olivieri, Luca Pasetto
 */
public class AI {

    private boolean colore;
    private Damiera d;

    /**
     * Il costruttore della classe AI
     * @param colore il colore del giocatore: false = bianco , true = nero
     * @param d  la damiera 
     */
    public AI(boolean colore, Damiera d) {
        this.colore = colore;
        this.d = d;
    }

    /**
     * Modifica la damiera eseguendo le mosse dell'intelligenza artificiale
     */
    public void getRes() {


        if (d.eaters(colore) == null) {
            // non c'è nulla da mangiare, forse posso fare una mossa


            if (d.movables(colore) != null) {
                // posso fare una mossa
                Integer[] res = new Integer[2]; // 0 from
                // 1 to

                ArrayList<Integer> convenienti = new ArrayList<Integer>(); // Lista che contiene le mosse convenienti 
                //(dove la pedina il turno dopo non verrà mangiata)
                for (Integer i : d.movables(colore)) {
                    if (Arrays.asList(convenienza(i)).contains(true)) { //controllo se è conveniente muovere la pedina 
                        convenienti.add(i);
                    }
                }

                Random r = new Random();
                if (convenienti.isEmpty()) {
                    //non ci sono mosse convenienti
                    res[0] = d.movables(colore).get(r.nextInt(d.movables(colore).size()));// scelgo casualmente quale pedina muovere
                    int rand;
                    do {
                        rand = d.getDamiera()[res[0]].isDama() ? 4 : 2;
                        res[1] = d.mosseDisponibili(res[0])[r.nextInt(rand)]; // scelgo casualmente in che posizione muovere la pedina
                    } while (res[1] == null); // finchè la mossa non è valida
                } else {
                    //ci sono mosse convenienti 
                    res[0] = convenienti.get(r.nextInt(convenienti.size()));// scelgo casualmente quale pedina muovere
                    int rand, rtmp;
                    do {
                        rand = d.getDamiera()[res[0]].isDama() ? 4 : 2;
                        rtmp = r.nextInt(rand);
                        res[1] = d.mosseDisponibili(res[0])[rtmp]; // scelgo casualmente in che posizione muovere la pedina

                    } while (convenienza(res[0])[rtmp] == null || convenienza(res[0])[rtmp] == false);// finchè la mossa non è valida
                }
                d.execMossa(res[0], res[1]); // eseguo sulla damiera la mossa

                if (!d.getDamiera()[res[1]].isDama()
                        && ((res[1] >= 28 && res[1] <= 31 && d.getDamiera()[res[1]].getColore()) || (res[1] >= 0
                        && res[1] <= 3 && !d.getDamiera()[res[1]].getColore()))) {
                    d.getDamiera()[res[1]].setDama(); //la pedina muovendosi è diventata Dama
                }
            }

        } else {
            // devo mangiare
            // mangiamo dove ci sono più mangiate multiple
            int lengthMigliore = 0; // null ha lunghezza 0 nell'albero
            ArrayList<Integer> mangiateMigliori = new ArrayList<Integer>(); //Lista contenente le mangiate più convenienti

            //cerco il numero maggiore di mangiate multiple
            for (int i = 0; i < d.eaters(colore).size(); i++) {
                if (lengthMigliore < new EaterTree(d, d.eaters(colore).get(i)).lunghezzaAlbero()) {
                    lengthMigliore = new EaterTree(d, d.eaters(colore).get(i)).lunghezzaAlbero();
                }

            }
            //cerca le pedine con il numero di mangiate maggiori e le aggiungo alla lista
            for (Integer i : d.eaters(colore)) {
                if (lengthMigliore == new EaterTree(d, i).lunghezzaAlbero()) {

                    mangiateMigliori.add(i);
                }
            }

            Random r = new Random();
            Integer from = mangiateMigliori.get(r.nextInt(mangiateMigliori.size())); // scelgo casualmente una pediana
            // con le mangiate maggiori

            ArrayList<Integer> mangiata = new EaterTree(d, from).mangiataMigliore(); // prendo il percorso delle mangiate della pedina
            if (mangiata != null) {
                for (int i = 0; i < mangiata.size() - 1; i++) {

                    d.mangia(mangiata.get(i), mangiata.get(i + 1)); //eseguo le mangiate sulla damiera

                    if (!d.getDamiera()[ mangiata.get(i + 1)].isDama()
                            && ((mangiata.get(i + 1) >= 28 && mangiata.get(i + 1) <= 31 && d.getDamiera()[ mangiata.get(i + 1)].getColore()) || (mangiata.get(i + 1) >= 0
                            && mangiata.get(i + 1) <= 3 && !d.getDamiera()[ mangiata.get(i + 1)].getColore()))) {
                        d.getDamiera()[ mangiata.get(i + 1)].setDama();
                        break; // se divento dama durante le mangiate cedo il turno
                    }
                }
                System.out.println(d);
            }



        }
        try {
            Thread.sleep(200); //  do un ritardo di qualche msec per rendere più visibile le mosse all'utente
        } catch (InterruptedException ex) {
            System.out.println("Errore: tempo ritardo non eseguito");
        }

    }

    /**
     * Crea un vettore per sapere se sono convenienti le mosse di una pedina
     * @param pos la posizione della pedina nella damiera
     * @return choice un vettore booleano: false se la pedina viene mangiata, true se la pedina non viene mangiata
     */
    private Boolean[] convenienza(Integer pos) {
        if (pos != null) {
            Boolean[] choice = d.getDamiera()[pos].isDama() ? new Boolean[4]
                    : new Boolean[2];
            if (!d.getDamiera()[pos].getColore()) { // sono bianco

                if (d.mosseDisponibili(pos)[0] != null) // voglio muovermi sul
                // sinistro
                {
                    choice[0] = altoSinistra(pos);
                }

                if (d.mosseDisponibili(pos)[1] != null)// voglio muovermi sul lato
                // destro
                {
                    choice[1] = altoDestra(pos);
                }

                if (d.getDamiera()[pos].isDama()) {
                    if (d.mosseDisponibili(pos)[2] != null)// voglio muovermi sul lato
                    // sinistro
                    {
                        choice[2] = bassoSinistra(pos);
                    }

                    if (d.mosseDisponibili(pos)[3] != null)// voglio muovermi sul lato
                    // destro
                    {
                        choice[3] = bassoDestra(pos);
                    }
                }
            } else { // sono nero
                if (d.mosseDisponibili(pos)[0] != null) // voglio muovermi sul
                // sinistro
                {
                    choice[0] = bassoSinistra(pos);
                }

                if (d.mosseDisponibili(pos)[1] != null)// voglio muovermi sul lato
                // destro
                {
                    choice[1] = bassoDestra(pos);
                }

                if (d.getDamiera()[pos].isDama()) {
                    if (d.mosseDisponibili(pos)[2] != null)// voglio muovermi sul lato
                    // sinistro
                    {
                        choice[2] = altoSinistra(pos);
                    }

                    if (d.mosseDisponibili(pos)[3] != null)// voglio muovermi sul lato
                    // destro
                    {
                        choice[3] = altoDestra(pos);
                    }
                }

            }

            return choice;
        }
        return null;
    }

    /**
     * Valuta se sono convenienti le mosse di una pedina in alto a sinistra
     * @param pos
     * @return false la pedina verrà mangiata true la pedina è salva, non c'è una pedina
     *          avversaria sopra in alto a sx
     */
    private Boolean altoSinistra(Integer pos) {
        if (pos != null) {
            int p = d.getDamiera()[pos].isDama() && d.getDamiera()[pos].getColore() ? 2 : 0;

            if (d.mosseDisponibili(pos)[p] != null) {
                //posso muovermi
                if ((pos / 4) % 2 != 0
                        && d.mosseDisponibili(pos)[p] - 5 >= 0
                        && d.getDamiera()[d.mosseDisponibili(pos)[p] - 5] != null && d.getDamiera()[d.mosseDisponibili(pos)[p] - 5].getColore() != d.getDamiera()[pos].getColore()
                        && !d.leftSide(d.mosseDisponibili(pos)[p], 4)) {
                    //sono in una riga dispari e in alto a sx se muovo ho una pedina avversaria
                    return false;
                } else {
                    if ((pos / 4) % 2 == 0
                            && !d.leftSide(d.mosseDisponibili(pos)[p], 4)
                            && d.mosseDisponibili(pos)[p] - 4 >= 0
                            && d.getDamiera()[d.mosseDisponibili(pos)[p] - 4] != null && d.getDamiera()[d.mosseDisponibili(pos)[p] - 4].getColore() != d.getDamiera()[pos].getColore()) {
                        //sono in una riga pari e in alto a sx se muovo ho una pedina avversaria
                        return false;
                    }

                }

                if (pos - 1 >= 0
                        && pos - 8 >= 0
                        && ((d.getDamiera()[pos - 1] != null
                        && d.getDamiera()[pos - 1].isDama()
                        && d.getDamiera()[pos - 1].getColore() != d.getDamiera()[pos].getColore() && d.getDamiera()[pos - 8] == null) || (d.getDamiera()[pos - 8] != null
                        && d.getDamiera()[pos - 8].getColore() != d.getDamiera()[pos].getColore() && d.getDamiera()[pos - 1] == null))
                        && !d.leftSide(d.mosseDisponibili(pos)[p], 4)) {
                    // in alto a destra se muovo ho una pedina avversaria e in basso a sinistra non ci sono pedine
                    return false;

                }

                return true;
            }
        }
        return null;

    }

    /**
     * Valuta se sono convenienti le mosse di una pedina in alto a destra
     * @param pos
     * @return false la pedina verrà mangiata, true la pedina è salva, non c'è una pedina
     *          avversaria sopra in alto a dx
     */
    private Boolean altoDestra(Integer pos) {
        if (pos != null) {
            int p = d.getDamiera()[pos].isDama() && d.getDamiera()[pos].getColore() ? 3 : 1;
            if (d.mosseDisponibili(pos)[p] != null) {

                if ((pos / 4) % 2 != 0
                        && d.mosseDisponibili(pos)[p] - 4 >= 0
                        && d.getDamiera()[d.mosseDisponibili(pos)[p] - 4] != null && d.getDamiera()[d.mosseDisponibili(pos)[p] - 4].getColore() != d.getDamiera()[pos].getColore()
                        && !d.rightSide(d.mosseDisponibili(pos)[p], 4)) {
                    //sono in una riga dispari e in alto a dx se muovo ho una pedina avversaria
                    return false;
                } else {
                    if ((pos / 4) % 2 == 0
                            && !d.rightSide(d.mosseDisponibili(pos)[p], 4)
                            && d.mosseDisponibili(pos)[p] - 3 >= 0
                            && d.getDamiera()[d.mosseDisponibili(pos)[p] - 3] != null && d.getDamiera()[d.mosseDisponibili(pos)[p] - 3].getColore() != d.getDamiera()[pos].getColore()) {
                        //sono in una riga pari e in alto a dx se muovo ho una pedina avversaria
                        return false;
                    }

                }

                if (pos - 8 >= 0
                        && pos + 1 < d.getDamiera().length && ((d.getDamiera()[pos + 1] != null
                        && d.getDamiera()[pos + 1].isDama()
                        && d.getDamiera()[pos + 1].getColore() != d.getDamiera()[pos].getColore() && d.getDamiera()[pos - 8] == null) || (d.getDamiera()[pos - 8] != null
                        && d.getDamiera()[pos - 8].getColore() != d.getDamiera()[pos].getColore() && d.getDamiera()[pos + 1] == null))
                        && !d.rightSide(d.mosseDisponibili(pos)[p], 4)) {
                    //in alto a sinistra se muovo ho una pedina avversaria e in basso a destra non ci sono pedine
                    return false;

                }

                return true;
            }
        }
        return null;
    }

    /**
     * Valuta se sono convenienti le mosse di una pedina in basso a sinistra
     * @param pos
     * @return false la pedina verrà mangiata, true la pedina è salva, non c'è una pedina
     *          avversaria sopra in basso a sx
     */
    private Boolean bassoSinistra(Integer pos) {

        if (pos != null) {
            int p = d.getDamiera()[pos].isDama() && !d.getDamiera()[pos].getColore() ? 2 : 0;
            if (d.mosseDisponibili(pos)[p] != null) {
                if ((pos / 4) % 2 != 0 //riga dispari
                        && d.mosseDisponibili(pos)[p] + 3 < d.getDamiera().length
                        && d.getDamiera()[d.mosseDisponibili(pos)[p] + 3] != null && d.getDamiera()[d.mosseDisponibili(pos)[p] + 3].getColore() != d.getDamiera()[pos].getColore()
                        && !d.leftSide(d.mosseDisponibili(pos)[p], 4)) {
                    return false;
                } else {
                    if ((pos / 4) % 2 == 0 //riga pari
                            && !d.leftSide(d.mosseDisponibili(pos)[p], 4)
                            && d.mosseDisponibili(pos)[p] + 4 < d.getDamiera().length
                            && d.getDamiera()[d.mosseDisponibili(pos)[p] + 4] != null && d.getDamiera()[d.mosseDisponibili(pos)[p] + 4].getColore() != d.getDamiera()[pos].getColore()) {
                        return false;
                    }

                }

                if (pos - 1 >= 0
                        && pos + 8 < d.getDamiera().length
                        && ((d.getDamiera()[pos - 1] != null
                        && d.getDamiera()[pos - 1].isDama()
                        && d.getDamiera()[pos - 1].getColore() != d.getDamiera()[pos].getColore() && d.getDamiera()[pos + 8] == null) || (d.getDamiera()[pos + 8] != null
                        && d.getDamiera()[pos + 8].getColore() != d.getDamiera()[pos].getColore() && d.getDamiera()[pos - 1] == null))
                        && !d.leftSide(d.mosseDisponibili(pos)[p], 4)) {
                    return false;

                }

                return true;
            }
        }
        return null;
    }

    /**
     * Valuta se sono convenienti le mosse di una pedina in basso a destra
     * @param pos
     * @return false la pedina verrà mangiata, true la pedina è salva, non c'è una pedina
     *          avversaria sopra in basso a dx
     */
    private Boolean bassoDestra(Integer pos) {
        if (pos != null) {
            int p = d.getDamiera()[pos].isDama() && !d.getDamiera()[pos].getColore() ? 3 : 1;
            if (d.mosseDisponibili(pos)[p] != null) {

                if ((pos / 4) % 2 != 0 //riga dispari
                        && d.mosseDisponibili(pos)[p] + 4 < d.getDamiera().length
                        && d.getDamiera()[d.mosseDisponibili(pos)[p] + 4] != null && d.getDamiera()[d.mosseDisponibili(pos)[p] + 4].getColore() != d.getDamiera()[pos].getColore()
                        && !d.rightSide(d.mosseDisponibili(pos)[p], 4)) {
                    return false;
                } else {		//riga pari

                    if ((pos / 4) % 2 == 0 && d.mosseDisponibili(pos)[p] + 5 < d.getDamiera().length
                            && d.getDamiera()[d.mosseDisponibili(pos)[p] + 5] != null && d.getDamiera()[d.mosseDisponibili(pos)[p] + 5].getColore() != d.getDamiera()[pos].getColore() && !d.rightSide(d.mosseDisponibili(pos)[p], 4)) {
                        return false;
                    }

                }

                if (pos + 8 < d.getDamiera().length && pos + 1 < d.getDamiera().length
                        && ((d.getDamiera()[pos + 1] != null
                        && d.getDamiera()[pos + 1].isDama()
                        && d.getDamiera()[pos + 1].getColore() != d.getDamiera()[pos].getColore() && d.getDamiera()[pos + 8] == null) || (d.getDamiera()[pos + 8] != null
                        && d.getDamiera()[pos + 8].getColore() != d.getDamiera()[pos].getColore() && d.getDamiera()[pos + 1] == null))
                        && !d.rightSide(d.mosseDisponibili(pos)[p], 4)) {
                    return false;

                }

                return true;
            }

        }
        return null;
    }
}
