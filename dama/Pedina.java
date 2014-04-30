package dama;

/**
 *
 * @authors Francesco Farina, Luca Olivieri, Luca Pasetto
 */
public class Pedina {
	private boolean colore; // True = Nero; False = Bianco;
	private boolean dama;

/**
         * Il costruttore della classe Pedina
         * @param colore true nero, false bianco
         */
	public Pedina(boolean colore) {
		this.colore = colore;
		this.dama = false;
          

	}

        /**
         * crea una pedina
         * @param colore il colore della pedina
         * @param dama true se la pedina è dama
         */
	public Pedina(boolean colore, boolean dama) {
		this.colore = colore;
		this.dama = dama;

	}

        
        /**
         * 
         * @return il colore della pedina (true = nero)
         */
	public boolean getColore() {
		return colore;
	}

        /**
         * 
         * @return true se la pedina è dama
         */
	public boolean isDama() {
		return dama;
	}

        /**
         * setta la pedina come dama
         */
	public void setDama() {
		dama = true;
                
		System.out.println("La pedina è diventata una dama");
	}
        

}