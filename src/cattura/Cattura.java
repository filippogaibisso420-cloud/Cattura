package cattura;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.HeadlessException;
import javax.swing.JFrame;

/**
 * Classe principale del programma
 */
public class Cattura extends JFrame {

    private CardLayout cardLayout;
    private java.awt.Container pnlPrincipale;
    private MenuPanel menu;
    private GiocoPanel gioco;

    public Cattura() throws HeadlessException {
        super("Cattura");
        setSize(1900, 880);
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