package cattura;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Pannello che gestisce il menù principale dell'applicazione
 */
public class MenuPanel extends BasePanel implements ActionListener {

    private CardLayout cardLayout;
    private JPanel pnlCentro;

    private JButton btnGioca;
    private JButton btnOpzioni;
    private JButton btnStatistiche;

    private BasePanel actualPanel;
    private OpzioniPanel opzioni;
    private StatistichePanel statistiche;

    public MenuPanel(Cattura parent) {
        super(parent);
        cardLayout = new CardLayout(10, 10);
        setLayout(new BorderLayout());
        creaGUI();
        aggiungiListener();
    }

    private void creaGUI() {
        pnlCentro = new JPanel(cardLayout);

        // --- schermata principale del menu ---
        JPanel pnlHome = new JPanel();
        btnGioca       = new JButton("Gioca");
        btnOpzioni     = new JButton("Opzioni");
        btnStatistiche = new JButton("Statistiche");
        pnlHome.add(btnGioca);
        pnlHome.add(btnOpzioni);
        pnlHome.add(btnStatistiche);

        opzioni     = new OpzioniPanel(parent);
        statistiche = new StatistichePanel(parent);

        pnlCentro.add(pnlHome,      "HOME");
        pnlCentro.add(opzioni,      "OPZIONI");
        pnlCentro.add(statistiche,  "STATISTICHE");

        add(pnlCentro, BorderLayout.CENTER);
        cardLayout.show(pnlCentro, "HOME");
    }

    private void aggiungiListener() {
        btnGioca.addActionListener(this);
        btnOpzioni.addActionListener(this);
        btnStatistiche.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnGioca) {
            // legge le opzioni configurate e avvia il gioco
            parent.avviaGioco(opzioni.getOpzioni());

        } else if (e.getSource() == btnOpzioni) {
            actualPanel = opzioni;
            actualPanel.reset();
            cardLayout.show(pnlCentro, "OPZIONI");

        } else if (e.getSource() == btnStatistiche) {
            actualPanel = statistiche;
            actualPanel.reset();
            cardLayout.show(pnlCentro, "STATISTICHE");
        }
    }

    @Override
    public void reset() {
        cardLayout.show(pnlCentro, "HOME");
    }
}