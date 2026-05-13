package cattura;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Pannello che gestisce il menù principale dell'applicazione.
 * Contiene i pulsanti per avviare il gioco, aprire le opzioni
 * e visualizzare le statistiche.
 */
public class MenuPanel extends BasePanel implements ActionListener {
    private Image background;
    private CardLayout cardLayout;
    private JPanel pnlCentro;
    
    private JButton btnGioca;
    private JButton btnOpzioni;
    private JButton btnStatistiche;
    
    private BasePanel actualPanel;
    private OpzioniPanel opzioni;
    private StatistichePanel statistiche;
    
    /**
     * Costruisce il menù principale, inizializza l'interfaccia grafica,
     * aggiunge i listener e carica l'immagine di sfondo.
     * @param parent il frame principale
     */
    public MenuPanel(Cattura parent) {
        super(parent);
        cardLayout = new CardLayout(10, 10);
        setLayout(new BorderLayout());
        creaGUI();
        aggiungiListener();
        try {
            background = ImageIO.read(new File("dati/background_statico.png"));
        } catch (IOException e) {
            System.out.println("Errore: background non trovato.");
        }
    }
    
    private void creaGUI() {
        pnlCentro = new JPanel(cardLayout);
        
        JPanel pnlHome = new JPanel();
        btnGioca = new JButton("Gioca");
        btnOpzioni = new JButton("Opzioni");
        btnStatistiche = new JButton("Statistiche");
        
        pnlHome.add(btnGioca);
        pnlHome.add(btnOpzioni);
        pnlHome.add(btnStatistiche);
        pnlHome.setOpaque(false);
        
        opzioni = new OpzioniPanel(parent);
        statistiche = new StatistichePanel(parent);
        
        pnlCentro.add(pnlHome, "HOME");
        pnlCentro.add(opzioni, "OPZIONI");
        pnlCentro.add(statistiche, "STATISTICHE");
        
        setOpaque(false);
        pnlCentro.setOpaque(false);
        add(pnlCentro, BorderLayout.CENTER);
        cardLayout.show(pnlCentro, "HOME");
    }
    
    private void aggiungiListener() {
        btnGioca.addActionListener(this);
        btnOpzioni.addActionListener(this);
        btnStatistiche.addActionListener(this);
    }
    
    /**
     * Gestisce i click sui pulsanti del menù:
     * avvia il gioco con le opzioni configurate, oppure
     * mostra il pannello delle opzioni o delle statistiche.
     * @param e l'evento generato dal pulsante premuto
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnGioca) {
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
    
    /**
     * Riporta il menù alla schermata HOME.
     */
    @Override
    public void reset() {
        cardLayout.show(pnlCentro, "HOME");
    }
    
    /**
     * Disegna l'immagine di sfondo prima di tutti gli altri componenti.
     * @param g il contesto grafico su cui disegnare
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }
}