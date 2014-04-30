package dama.grafica;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @authors Francesco Farina, Luca Olivieri, Luca Pasetto
 */
public class WelcomeMenu {
    
    private JFrame frame;

    /**
     *Il pulsante per avviare una nuova partita
     */
    public JButton buttonNuovapartita;
    private JButton buttonCrediti;
    private JButton esci;
    
    /**
     * Il metodo fa apparire la schermata iniziale
     */
    public void createAndShowGUI() {
        
        frame = new JFrame("Dama");
        
        buttonNuovapartita = new JButton("Nuova partita");
        
        buttonCrediti = new JButton("Crediti");
        
        esci = new JButton("Esci");
        
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buttonNuovapartita.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
   
               buttonNuovapartita.setEnabled(false);    //per disabilitare il pulsante "Nuova partita"
                                                        //quando ne è già in corso una
               GameFrame g = new GameFrame();           //faccio partire la partita
               g.setVisible(true);
               
            }
        });
        
        buttonCrediti.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                //i nostri crediti
                JOptionPane.showMessageDialog(null, "Creato da:\n          Francesco Farina\n          Luca Olivieri\n"
                        + "          Luca Pasetto", "Crediti",
                        JOptionPane.PLAIN_MESSAGE);
                
                
            }
        });
        
        esci.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                System.exit(0);
                
            }
        });
        
        
        frame.setLayout(new BorderLayout());
        
        //l'immagine di sfondo
        JLabel background = new JLabel(new ImageIcon(getClass().getResource("/images/sfondo.jpg")));
        
        frame.add(background);
        
        //GridBagLayout per ottenere i pulsanti centrati
        background.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        
        //uso i JPanel per ottenere uno sfondo grigio attorno ai pulsanti
        JPanel h = new JPanel(new FlowLayout());
        h.setBackground(Color.gray);
        JPanel j = new JPanel(new FlowLayout());
        j.setBackground(Color.gray);
        JPanel k = new JPanel(new FlowLayout());
        k.setBackground(Color.gray);
        
        buttonNuovapartita.setBackground(Color.white);
        
        //setto la dimensione di tutti i pulsanti pari a quella del più largo
        buttonCrediti.setPreferredSize(buttonNuovapartita.getPreferredSize());
        buttonCrediti.setBackground(Color.white);
        
        esci.setPreferredSize(buttonNuovapartita.getPreferredSize());
        esci.setBackground(Color.white);
        
        h.add(buttonNuovapartita);
        j.add(buttonCrediti);
        k.add(esci);
        
        background.add(h, gbc);
        background.add(j, gbc);
        background.add(k, gbc);
        
        //pack() per settare la dimensione ottimale del frame in base a quello che contiene
        frame.pack();        
        
        frame.setVisible(true);
        
        
    }
}
