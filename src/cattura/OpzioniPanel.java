package cattura;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * Pannello che permette all'utente di configurare le opzioni di gioco
 * prima di avviare una partita. 
 * Consente di impostare la velocità delle figure, 
 * il numero di figure, il tempo disponibile e la modalità di gioco.
 * Gli spinner non sono modificabili da tastiera per evitare valori non validi.
 * Lo sfondo di questo pannello è un'immagine animata (GIF).
 */
public class OpzioniPanel extends BasePanel implements ActionListener {
    private Image background;
    
    private JSpinner spnVelocita;
    private JSpinner spnNFigure;
    private JSpinner spnTempo;
    private JRadioButton rbtnCompetitivo;
    private JRadioButton rbtnCasual;
    private JButton btnIndietro;
    
    /**
     * Costruisce il pannello delle opzioni, inizializza l'interfaccia grafica,
     * aggiunge i listener e carica l'immagine di sfondo animata.
     * @param parent il frame principale
     */
    public OpzioniPanel(Cattura parent) {
        super(parent);
        setLayout(new BorderLayout());
        creaGUI();
        aggiungiListener();
        background = new ImageIcon("dati/background_animato.gif").getImage();
    }
    
    private void creaGUI() {
        JPanel pnlCampi = new JPanel();
        
        spnVelocita = new JSpinner(new SpinnerNumberModel(Opzioni.DEFAULT_VELOCITA, 1, 10, 1));
        spnNFigure = new JSpinner(new SpinnerNumberModel(10, 1, 30,  1));
        spnTempo = new JSpinner(new SpinnerNumberModel(Opzioni.DEFAULT_TEMPO, 10, 60, 5));
        
        ((JSpinner.DefaultEditor) spnVelocita.getEditor()).getTextField().setEditable(false);
        ((JSpinner.DefaultEditor) spnNFigure.getEditor()).getTextField().setEditable(false);
        ((JSpinner.DefaultEditor) spnTempo.getEditor()).getTextField().setEditable(false);
        
        rbtnCompetitivo = new JRadioButton("Competitiva (a tempo)");
        rbtnCasual = new JRadioButton("Casual", true);
        ButtonGroup gruppo = new ButtonGroup();
        gruppo.add(rbtnCompetitivo);
        gruppo.add(rbtnCasual);
        
        JLabel lblVelocita = new JLabel("Velocità:");
        lblVelocita.setFont(new Font("Verdana", Font.PLAIN, 14));
        lblVelocita.setForeground(Color.WHITE);
        pnlCampi.add(lblVelocita);
        pnlCampi.add(spnVelocita);
        
        JLabel lblFigure = new JLabel("Numero figure:");
        lblFigure.setFont(new Font("Verdana", Font.PLAIN, 14));
        lblFigure.setForeground(Color.WHITE);
        pnlCampi.add(lblFigure);
        pnlCampi.add(spnNFigure);
        
        JLabel lblTempo = new JLabel("Tempo (secondi):");
        lblTempo.setFont(new Font("Verdana", Font.PLAIN, 14));
        lblTempo.setForeground(Color.WHITE);
        pnlCampi.add(lblTempo);
        pnlCampi.add(spnTempo);
        
        JLabel lblModalita = new JLabel("Modalità:");
        lblModalita.setFont(new Font("Verdana", Font.PLAIN, 14));
        lblModalita.setForeground(Color.WHITE);
        pnlCampi.add(lblModalita);
        
        JPanel pnlRadio = new JPanel();
        pnlRadio.add(rbtnCompetitivo);
        pnlRadio.add(rbtnCasual);
        pnlRadio.setOpaque(false);
        pnlCampi.add(pnlRadio);
        
        btnIndietro = new JButton("← Indietro");
        JLabel lblOpzioni = new JLabel("Opzioni di gioco", JLabel.CENTER);
        lblOpzioni.setFont(new Font("Verdana", Font.BOLD, 22));
        lblOpzioni.setForeground(Color.WHITE);
        
        setOpaque(false);
        pnlCampi.setOpaque(false);
        add(lblOpzioni, BorderLayout.NORTH);
        add(pnlCampi,    BorderLayout.CENTER);
        add(btnIndietro, BorderLayout.SOUTH);
    }
    
    private void aggiungiListener() {
        btnIndietro.addActionListener(this);
    }
    
    /**
     * Legge i valori correnti degli spinner e dei radio button
     * e restituisce le opzioni con la configurazione scelta.
     * @return le opzioni di gioco attualmente configurate
     */
    public Opzioni getOpzioni() {
        int vel = (int) spnVelocita.getValue();
        int nFigure = (int) spnNFigure.getValue();
        int tempo = (int) spnTempo.getValue();
        int modalita = rbtnCompetitivo.isSelected()
            ? Opzioni.MODALITA_COMPETITIVA
            : Opzioni.MODALITA_CASUAL;
        return new Opzioni(vel, nFigure, tempo, modalita);
    }
    
    /**
     * Gestisce il click sul pulsante indietro tornando al menù principale.
     * @param e l'evento generato dal pulsante premuto
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnIndietro) {
            parent.tornaAlMenu();
        }
    }
    
    @Override
    void reset() {
    }
    
    /**
     * Disegna l'immagine di sfondo animata prima di tutti gli altri componenti.
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