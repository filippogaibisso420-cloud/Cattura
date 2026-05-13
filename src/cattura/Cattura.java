package cattura;

import java.awt.CardLayout;
import java.awt.Container;
import javax.swing.JFrame;

/**
 * Classe principale del programma che visualizza il menù
 * ed esegue il gioco.
 */
public class Cattura extends JFrame {
    public static final int LARGHEZZA = 1900;
    public static final int LUNGHEZZA = 880;
    
    private CardLayout cardLayout;
    private Container pnlPrincipale;
    private MenuPanel menu;
    private GiocoPanel gioco;
    
    /**
     * Costruisce la finestra principale dell'applicazione
     * e inizializza i pannelli.
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
     * Avvia una nuova partita con le opzioni scelte dal giocatore.
     * Reimposta il pannello di gioco, lo avvia con le opzioni fornite
     * e lo porta in primo piano.
     * @param opzioni le opzioni di configurazione scelte dal giocatore
     */
    public void avviaGioco(Opzioni opzioni) {
        gioco.reset();
        gioco.avvia(opzioni);
        cardLayout.show(pnlPrincipale, "GIOCO");
    }
    
    /**
     * Ferma la partita in corso e riporta l'utente al menù principale.
     */
    public void tornaAlMenu() {
        gioco.ferma();
        menu.reset();
        cardLayout.show(pnlPrincipale, "MENU");
    }
    
    /**
     * Il main dell'applicazione.
     * @param args argomenti da riga di comando (non utilizzati)
     */
    public static void main(String[] args) {
        new Cattura();
    }
}