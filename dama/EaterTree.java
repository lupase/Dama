package dama;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;

/**
 *
 * @authors Francesco Farina, Luca Olivieri, Luca Pasetto
 */
public class EaterTree {

    TreeSet<Nodo> tree;
    Damiera d;

    /**
     * Il costruttore della classe EaterTree
     * @param d la damiera
     * @param pos la posizione della pedina da cui costruire l'albero delle mangiate
     */
    public EaterTree(Damiera d, Integer pos) {
        tree = new TreeSet<Nodo>();
        this.d = d;
        creaAlbero(new Nodo(pos, new ArrayList<Integer>(), 0));

    }

    /**
     * Crea un albero a partire da un nodo
     * @param n il nodo contenente le informazioni
     */
    public void creaAlbero(Nodo n) {
        if (n.getPos() != null) {
            tree.add(n);
            if (d.mangiabili(n.getPos(), tree.first().getPos()) != null && d.mangiabili(n.getPos(), tree.first().getPos())[0] != null && (n.getMydaddys() == null || (n.getMydaddys() != null && !n.getMydaddys().contains(d.mangiabili(n.getPos(), tree.first().getPos())[0])))) {
                Nodo tmp = new Nodo(d.mangiabili(n.getPos(), tree.first().getPos())[0], n.getMydaddys(), n.getLength() + 1);
                tmp.addDaddy(n.getPos());
                //mangio a sinistra e creo un ramo
                creaAlbero(tmp);
            }
            if (d.mangiabili(n.getPos(), tree.first().getPos()) != null && d.mangiabili(n.getPos(), tree.first().getPos())[1] != null && (n.getMydaddys() == null || (n.getMydaddys() != null && !n.getMydaddys().contains(d.mangiabili(n.getPos(), tree.first().getPos())[1])))) {
                Nodo tmp = new Nodo(d.mangiabili(n.getPos(), tree.first().getPos())[1], n.getMydaddys(), n.getLength() + 1);
                tmp.addDaddy((Integer) n.getPos());
                //mangio a destra e creo un ramo
                creaAlbero(tmp);
            }
            //opzioni per le dame
            if (d.getDamiera()[n.getPos()] != null && d.getDamiera()[n.getPos()].isDama()) {
                if (d.mangiabili(n.getPos(), tree.first().getPos()) != null && d.mangiabili(n.getPos(), tree.first().getPos())[2] != null && (n.getMydaddys() == null || (n.getMydaddys() != null && !n.getMydaddys().contains(d.mangiabili(n.getPos(), tree.first().getPos())[2])))) {
                    Nodo tmp = new Nodo(d.mangiabili(n.getPos(), tree.first().getPos())[2], n.getMydaddys(), n.getLength() + 1);
                    tmp.addDaddy(n.getPos());
                    creaAlbero(tmp);
                }
                if (d.mangiabili(n.getPos(), tree.first().getPos()) != null && d.mangiabili(n.getPos(), tree.first().getPos())[3] != null && (n.getMydaddys() == null || (n.getMydaddys() != null && !n.getMydaddys().contains(d.mangiabili(n.getPos(), tree.first().getPos())[3])))) {
                    Nodo tmp = new Nodo(d.mangiabili(n.getPos(), tree.first().getPos())[3], n.getMydaddys(), n.getLength() + 1);
                    tmp.addDaddy(n.getPos());
                    creaAlbero(tmp);
                }
            }
        }
    }

    /**
     * Restituisce una lista dei movimenti per una delle mangiate migliori
     * @return mangiataMigliore la lista dei movimenti per fare la mangiata
     */
    public ArrayList<Integer> mangiataMigliore() {

        ArrayList<Nodo> miglioriMangiate = new ArrayList<Nodo>();
        ArrayList<Integer> mangiataMigliore = new ArrayList<Integer>();
        for (Nodo n : tree) {
            if (n.getLength() == lunghezzaAlbero()) {
                miglioriMangiate.add(n);
            }
        }
        Random r = new Random();
        int rand = r.nextInt(miglioriMangiate.size());
        for (Integer i : miglioriMangiate.get(rand).getMydaddys()) {
            mangiataMigliore.add(i);
        }
        mangiataMigliore.add(miglioriMangiate.get(rand).getPos());
        return mangiataMigliore;
    }

    
    /**
     * 
     * @return la lunghezza dell'albero
     */
    public int lunghezzaAlbero() {

        return tree.last().getLength();
    }
}
