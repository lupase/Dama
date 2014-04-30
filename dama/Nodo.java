package dama;

import java.util.ArrayList;

/**
 *
 * @authors Francesco Farina, Luca Olivieri, Luca Pasetto
 */
public class Nodo implements Comparable<Nodo> {
        private Integer pos;

        private Integer length;
        private ArrayList<Integer> mydaddys;

        /**
         * Il costruttore della classe Nodo
         * @param pos la posizione della pedina nella damiera
         * @param mydaddys la lista dei padri del nodo nell'albero
         * @param length la lunghezza dell'albero fino a questo nodo
         */
        public Nodo(Integer pos,ArrayList<Integer> mydaddys,int length){
            this.length=length;
            this.pos=pos;
            this.mydaddys = new ArrayList<Integer>();
           if(mydaddys != null)
            for(Integer i : mydaddys)
                this.mydaddys.add(i);


        }

        /**
         * 
         * @return la posizione della pedina nella damiera
         */
        public Integer getPos(){
            return pos;
        }
        
        /**
         * 
         * @return la lunghezza dell'albero fino a questo nodo
         */
       public int getLength(){
            return length;
        }
       
       /**
        * 
        * @return la lista dei padri del nodo nell'albero
        */
       public ArrayList<Integer> getMydaddys(){
            return mydaddys;
        }

       /**
        * aggiunge una pedina come padre
        * @param pos la posizione della pedina da aggiungere come padre
        */
       public void addDaddy(Integer pos){
           
           mydaddys.add(pos);
           
       }



	@Override
	public int compareTo(Nodo n) {

		return length - n.getLength();
	}

}

