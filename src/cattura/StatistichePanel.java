package cattura;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Pannello che visualizza i record del giocatore.
 * Mostra il miglior tempo registrato in modalità casual e il punteggio
 * massimo ottenuto in modalità competitiva.
 */
public class StatistichePanel extends BasePanel implements ActionListener {
    
    private JLabel lblRecordTempo;
    private JLabel lblRecordPunteggio;
    private JButton btnIndietro;
    
    /**
     * Costruisce il pannello delle statistiche, inizializza l'interfaccia
     * grafica e aggiunge i listener.
     * @param parent il frame principale
     */
    public StatistichePanel(Cattura parent) {
        super(parent);
        setLayout(new BorderLayout(10, 10));
        creaGUI();
        aggiungiListener();
    }
    
    private void creaGUI() {
        JPanel pnlRecord = new JPanel(new GridLayout(2, 2, 5, 5));
        
        pnlRecord.add(new JLabel("Miglior tempo (cattura tutte):"));
        lblRecordTempo = new JLabel("--");
        pnlRecord.add(lblRecordTempo);
        
        pnlRecord.add(new JLabel("Punteggio massimo (modalità competitiva):"));
        lblRecordPunteggio = new JLabel("--");
        pnlRecord.add(lblRecordPunteggio);
        
        btnIndietro = new JButton("← Indietro");
        
        add(new JLabel("Statistiche", JLabel.CENTER), BorderLayout.NORTH);
        add(pnlRecord,   BorderLayout.CENTER);
        add(btnIndietro, BorderLayout.SOUTH);
    }
    
    private void aggiungiListener() {
        btnIndietro.addActionListener(this);
    }
    
    /**
     * Aggiorna le etichette con i record correnti.
     * Se non esiste ancora nessun record mostra il testo "Nessun record". 
     * Il tempo viene formattato come (minuti, secondi, millisecondi).
     */
    @Override
    public void reset() {
        long tempoMs = Statistiche.getRecordTempoMs();
        if (tempoMs == Long.MAX_VALUE) {
            lblRecordTempo.setText("Nessun record");
        } else {
            lblRecordTempo.setText(String.format("%d:%02d.%03d",
                tempoMs / 60000,
                (tempoMs % 60000) / 1000,
                tempoMs % 1000));
        }
        
        int recordPunteggio = Statistiche.getRecordPunteggio();
        lblRecordPunteggio.setText(recordPunteggio == 0 ? "Nessun record" : String.valueOf(recordPunteggio));
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
}