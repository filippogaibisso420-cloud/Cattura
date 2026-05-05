package cattura;

import java.awt.BorderLayout;
import java.awt.Font;
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
        JPanel pnlCampi = new JPanel();

        spnVelocita = new JSpinner(new SpinnerNumberModel(Opzioni.DEFAULT_VELOCITA, 1, 10, 1));
        spnNFigure  = new JSpinner(new SpinnerNumberModel(10, 1, 30,  1));
        spnTempo    = new JSpinner(new SpinnerNumberModel(Opzioni.DEFAULT_TEMPO, 10, 60, 5));
        
        ((JSpinner.DefaultEditor) spnVelocita.getEditor()).getTextField().setEditable(false);
        ((JSpinner.DefaultEditor) spnNFigure.getEditor()).getTextField().setEditable(false);
        ((JSpinner.DefaultEditor) spnTempo.getEditor()).getTextField().setEditable(false);
        
        rbtnCompetitivo   = new JRadioButton("Competitiva (a tempo)");
        rbtnCasual  = new JRadioButton("Casual", true);
        ButtonGroup gruppo = new ButtonGroup();
        gruppo.add(rbtnCompetitivo);
        gruppo.add(rbtnCasual);

        JLabel lblVelocita = new JLabel("Velocità:");
        lblVelocita.setFont(new Font("Verdana", Font.PLAIN, 14));
        pnlCampi.add(lblVelocita);
        pnlCampi.add(spnVelocita);
        
        JLabel lblFigure = new JLabel("Numero figure:");
        lblFigure.setFont(new Font("Verdana", Font.PLAIN, 14));
        pnlCampi.add(lblFigure);
        pnlCampi.add(spnNFigure);
        
        JLabel lblTempo = new JLabel("Tempo (secondi):");
        lblTempo.setFont(new Font("Verdana", Font.PLAIN, 14));
        pnlCampi.add(lblTempo);
        pnlCampi.add(spnTempo);
        
        JLabel lblModalita = new JLabel("Modalità:");
        lblModalita.setFont(new Font("Verdana", Font.PLAIN, 14));
        pnlCampi.add(lblModalita);
        JPanel pnlRadio = new JPanel();
        pnlRadio.add(rbtnCompetitivo);
        pnlRadio.add(rbtnCasual);
        pnlCampi.add(pnlRadio);

        btnIndietro = new JButton("← Indietro");
        JLabel lblOpzioni = new JLabel("Opzioni di gioco", JLabel.CENTER);
        lblOpzioni.setFont(new Font("Verdana", Font.BOLD, 22));
        
        add(lblOpzioni, BorderLayout.NORTH);
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