package cattura;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Pannello all'interno del quale si svolge il gioco
 */
public class GiocoPanel extends BasePanel implements Runnable, MouseMotionListener, ActionListener {
    private static final int FPS = 60;
    private static final int MS_PER_FRAME = 1000 / FPS;

    private ArrayList<Sprite> figure;
    private Opzioni opzioni;
    private Thread threadGioco;
    private boolean inCorso;

    private int punteggio;
    private int secondiRimasti;      // usato in modalità tempo
    private int figureCatturate;
    private long tempoInizio;        // ms, usato in modalità figure

    private JButton btnMenu;

    public GiocoPanel(Cattura parent) {
        super(parent);
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        btnMenu = new JButton("← Menu");
        btnMenu.addActionListener(this);
        add(btnMenu, BorderLayout.SOUTH);

        addMouseMotionListener(this);
    }

    /**
     * Avvia il gioco con le opzioni ricevute
     * @param opzioni opzioni di configurazione
     */
    public void avvia(Opzioni opzioni) {
        this.opzioni = opzioni;
        inCorso = true;

        figure = new ArrayList<>();
        for (int i = 0; i < opzioni.getNFigure(); i++) {
            figure.add(new Sprite(this, opzioni.getVelocita(), opzioni.getModalita()));
        }

        punteggio        = 0;
        figureCatturate  = 0;
        secondiRimasti   = opzioni.getTempoSecondi();
        tempoInizio      = System.currentTimeMillis();

        threadGioco = new Thread(this);
        threadGioco.start();
    }

    /** Ferma il loop di gioco */
    public void ferma() {
        inCorso = false;
    }

    @Override
    public void reset() {
        ferma();
    }

    // ------------------------------------------------------------------ loop
    @Override
    public void run() {
        long ultimoTick = System.currentTimeMillis();

        while (inCorso) {
            long ora = System.currentTimeMillis();

            // aggiorna timer ogni secondo
            if (opzioni.getModalita() == Opzioni.MODALITA_COMPETITIVA) {
                secondiRimasti = opzioni.getTempoSecondi()
                                 - (int)((ora - tempoInizio) / 1000);
                if (secondiRimasti <= 0) {
                    finePartita();
                    break;
                }
            }

            for (Sprite s : figure) {
                s.muovi();
            }

            repaint();

            // pausa per rispettare i FPS
            long attesa = MS_PER_FRAME - (System.currentTimeMillis() - ora);
            if (attesa > 0) {
                try { Thread.sleep(attesa); } catch (InterruptedException ex) { }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Sprite s : figure) {
            s.disegna(g);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Geneva", Font.BOLD, 16));
        g.drawString("Punteggio: " + punteggio, 10, 20);
        g.drawString("Catturate: " + figureCatturate + "/" + opzioni.getNFigure(), 10, 40);

        if (opzioni.getModalita() == Opzioni.MODALITA_COMPETITIVA) {
            g.drawString("Tempo: " + secondiRimasti + "s", 10, 60);
        }
    }

    /**
     * 
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        if (!inCorso) return;

        ArrayList<Sprite> daCatturare = new ArrayList<>();
        for (Sprite s : figure) {
            if (s.contienePunto(e.getX(), e.getY())) {
                daCatturare.add(s);
            }
        }

        for (Sprite s : daCatturare) {
            cattura(s);
        }
    }
    
    /**
     * 
     * @param e 
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    /**
     * Gestisce la cattura di una singola figura
     * @param s la figura catturata
     */
    private void cattura(Sprite s) {
        figure.remove(s);
        figureCatturate++;

        long tempoTrascorso = (System.currentTimeMillis() - tempoInizio) / 1000 + 1;
        int puntiBase = (int)(s.getDimensione() * 10 / tempoTrascorso);
        int puntiGuadagnati = (int)Math.round(puntiBase * opzioni.getMoltiplicatore());
        punteggio += puntiGuadagnati;

        if (figure.isEmpty()) {
            finePartita();
        }
    }

    /**
     * Chiamato quando la partita termina 
     */
    private void finePartita() {
        repaint();
        inCorso = false;
        long tempoTotaleMs = System.currentTimeMillis() - tempoInizio;

        String titolo, messaggio;
        if (opzioni.getModalita() == Opzioni.MODALITA_CASUAL) {
            titolo = "Hai vinto!";
            messaggio = String.format(
                "<html><center>Hai catturato tutte le figure!<br>" +
                "Punteggio: %d<br>" +
                "Tempo impiegato: %d:%02d.%03d</center></html>",
                punteggio,
                tempoTotaleMs / 60000,
                (tempoTotaleMs % 60000) / 1000,
                tempoTotaleMs % 1000
            );
        } else {
            if (figure.isEmpty()) {
                punteggio += opzioni.getMoltiplicatore() * 100 * opzioni.getTempoSecondi();
                titolo = "Hai vinto!";
                messaggio = String.format(
                    "<html><center>Hai catturato tutte le figure in tempo!<br>" +
                    "Punteggio: %d</center></html>",
                    punteggio
                );
            } else {
                titolo = "Tempo scaduto!";
                messaggio = String.format(
                    "<html><center>Figure catturate: %d/%d<br>" +
                    "Punteggio: %d</center></html>",
                    figureCatturate, opzioni.getNFigure(), punteggio
                );
            }
            Statistiche.salvaRecord(opzioni.getModalita(), punteggio, tempoTotaleMs);
        }

        JDialog dialog = new JDialog(parent, titolo, true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setResizable(false);

        JLabel lblMessaggio = new JLabel(messaggio, JLabel.CENTER);
        lblMessaggio.setFont(new Font("Arial", Font.PLAIN, 16));
        lblMessaggio.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        JButton btnTornaMenu = new JButton("Torna al menu");
        btnTornaMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                parent.tornaAlMenu();
            }
        });

        JPanel pnlBtn = new JPanel();
        pnlBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        pnlBtn.add(btnTornaMenu);

        dialog.add(lblMessaggio, BorderLayout.CENTER);
        dialog.add(pnlBtn, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    /**
     * 
     * @param e 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnMenu) {
            parent.tornaAlMenu();
        }
    }
}