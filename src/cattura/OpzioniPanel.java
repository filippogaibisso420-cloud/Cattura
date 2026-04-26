package cattura;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * Pannello che permette all'utente di configurare le opzioni di gioco
 */
public class OpzioniPanel extends BasePanel implements ActionListener {

    private JSpinner spnVelocita;
    private JSpinner spnNFigure;
    private JSpinner spnTempo;
    private JRadioButton rbtnCompetitivo;
    private JRadioButton rbtnCasual;
    private JButton btnIndietro;

    public OpzioniPanel(Cattura parent) {
        super(parent);
        setLayout(new BorderLayout());
        creaGUI();
        aggiungiListener();
    }

    private void creaGUI() {
        JPanel pnlCampi = new JPanel(/*new GridLayout(5, 2, 5, 5)*/);

        spnVelocita = new JSpinner(new SpinnerNumberModel(5, 1, 15, 1));
        spnNFigure  = new JSpinner(new SpinnerNumberModel(10, 1, 30,  1));
        spnTempo    = new JSpinner(new SpinnerNumberModel(30, 10, 120, 5));

        rbtnCompetitivo   = new JRadioButton("Competitiva (a tempo)");
        rbtnCasual  = new JRadioButton("Casual", true);
        ButtonGroup gruppo = new ButtonGroup();
        gruppo.add(rbtnCompetitivo);
        gruppo.add(rbtnCasual);

        pnlCampi.add(new JLabel("Velocità:"));
        pnlCampi.add(spnVelocita);
        pnlCampi.add(new JLabel("Numero figure:"));
        pnlCampi.add(spnNFigure);
        pnlCampi.add(new JLabel("Tempo (secondi):"));
        pnlCampi.add(spnTempo);
        pnlCampi.add(new JLabel("Modalità:"));
        JPanel pnlRadio = new JPanel();
        pnlRadio.add(rbtnCompetitivo);
        pnlRadio.add(rbtnCasual);
        pnlCampi.add(pnlRadio);

        btnIndietro = new JButton("← Indietro");
        JLabel opzioni = new JLabel("Opzioni di gioco", JLabel.CENTER);
        opzioni.setFont(new Font("Geneva", Font.ROMAN_BASELINE, 18));
        
        add(opzioni, BorderLayout.NORTH);
        add(pnlCampi,    BorderLayout.CENTER);
        add(btnIndietro, BorderLayout.SOUTH);
    }

    private void aggiungiListener() {
        btnIndietro.addActionListener(this);
    }

    /**
     * Restituisce le opzioni attualmente configurate
     * @return le opzioni del gioco
     */
    public Opzioni getOpzioni() {
        int vel      = (int) spnVelocita.getValue();
        int nFigure  = (int) spnNFigure.getValue();
        int tempo    = (int) spnTempo.getValue();
        int modalita = rbtnCompetitivo.isSelected()
                        ? Opzioni.MODALITA_COMPETITIVA
                        : Opzioni.MODALITA_CASUAL;
        return new Opzioni(vel, nFigure, tempo, modalita);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnIndietro) {
            parent.tornaAlMenu();
        }
    }

    @Override
    public void reset() {
    }
}