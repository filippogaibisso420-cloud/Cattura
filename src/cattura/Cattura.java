package cattura;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JFrame;

/**
 * Classe principale del programma che visualizza il menù
 * ed esegue il gioco
 */
public class Cattura extends JFrame {
    public static final int LARGHEZZA = 1900;
    public static final int LUNGHEZZA = 880;
    
    private CardLayout cardLayout;
    private java.awt.Container pnlPrincipale;
    private MenuPanel menu;
    private GiocoPanel gioco;
    
    /**
     * 
     */
    public Cattura() {
        super("Cattura");
        setSize(LARGHEZZA, LUNGHEZZA);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        pnlPrincipale = getContentPane();
        pnlPrincipale.setLayout(cardLayout);

        menu = new MenuPanel(this);
        gioco = new GiocoPanel(this);

        pnlPrincipale.add(menu, "MENU");
        pnlPrincipale.add(gioco, "GIOCO");

        setVisible(true);
    }

    /**
     * Avvia una nuova partita con le opzioni scelte
     * @param opzioni le opzioni configurate dal giocatore
     */
    public void avviaGioco(Opzioni opzioni) {
        gioco.reset();
        gioco.avvia(opzioni);
        cardLayout.show(pnlPrincipale, "GIOCO");
    }

    /**
     * Riporta l'utente al menù principale
     */
    public void tornaAlMenu() {
        gioco.ferma();
        menu.reset();
        cardLayout.show(pnlPrincipale, "MENU");
    }

    public static void main(String[] args) {
        new Cattura();
    }
}