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
 * Pannello all'interno del quale si svolge il gioco.
 * Gestisce il loop di gioco tramite un thread dedicato, il movimento
 * degli sprite, la rilevazione dell'hover del mouse per la cattura
 * delle figure e la visualizzazione del punteggio.
 */
public class GiocoPanel extends BasePanel implements Runnable, MouseMotionListener, ActionListener {
    private static final int FPS = 60;
    private static final int MS_PER_FRAME = 1000 / FPS;
    
    private ArrayList<Sprite> figure;
    private Opzioni opzioni;
    private Thread threadGioco;
    private boolean inCorso;
    
    private int punteggio;
    private int secondiRimasti;
    private int figureCatturate;
    private long tempoInizio;
    
    private JButton btnMenu;
    
    /**
     * Costruisce il pannello di gioco, imposta il layout e aggiunge
     * il pulsante di ritorno al menù e il listener per il mouse.
     * @param parent il frame principale {@link Cattura}
     */
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
     * Avvia una nuova partita con le opzioni ricevute.
     * Crea gli sprite, azzera le statistiche e avvia il thread di gioco.
     * @param opzioni le opzioni di configurazione della partita
     */
    public void avvia(Opzioni opzioni) {
        this.opzioni = opzioni;
        inCorso = true;
        
        figure = new ArrayList<>();
        for (int i = 0; i < opzioni.getNFigure(); i++) {
            figure.add(new Sprite(this, opzioni.getVelocita(), opzioni.getModalita()));
        }
        
        punteggio = 0;
        figureCatturate = 0;
        secondiRimasti = opzioni.getTempoSecondi();
        tempoInizio = System.currentTimeMillis();
        
        threadGioco = new Thread(this);
        threadGioco.start();
    }
    
    /** 
     * Ferma il loop di gioco 
     */
    public void ferma() {
        inCorso = false;
    }
    
    /**
     * Reimposta il pannello allo stato iniziale fermando il loop di gioco.
     */
    @Override
    public void reset() {
        ferma();
    }
    
    /**
     * Loop principale del gioco eseguito dal thread dedicato.
     * Ad ogni iterazione aggiorna il timer (in modalità competitiva),
     * muove tutti gli sprite e richiede il ridisegno del pannello,
     * rispettando il limite di FPS impostato.
     */
    @Override
    public void run() {
        long ultimoTick = System.currentTimeMillis();
        
        while (inCorso) {
            long ora = System.currentTimeMillis();
            
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
            
            long attesa = MS_PER_FRAME - (System.currentTimeMillis() - ora);
            if (attesa > 0) {
                try { 
                    Thread.sleep(attesa); 
                } catch (InterruptedException ex) { }
            }
        }
    }
    
    /**
     * Ridisegna il pannello: disegna tutti gli sprite e sovrappone
     * le informazioni di gioco (punteggio, figure catturate, timer).
     * @param g il contesto grafico su cui disegnare
     */
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
     * Rileva il movimento del mouse e cattura gli sprite
     * che si trovano sotto il cursore.
     * @param e l'evento di movimento del mouse
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
     * Gestisce il trascinamento del mouse in modo che la cattura funzioni 
     * anche tenendo premuto il tasto del mouse.
     * @param e l'evento di trascinamento del mouse
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }
    
    /**
     * Gestisce la cattura di una singola figura.
     * Rimuove lo sprite dalla lista, incrementa il contatore delle figure
     * catturate e calcola i punti guadagnati in base alla dimensione,
     * al tempo trascorso e al moltiplicatore delle opzioni.
     * Se la lista degli sprite è vuota la partita termina.
     * @param s lo sprite da catturare
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
     * Gestisce la fine della partita: ferma il loop, calcola il tempo totale,
     * aggiorna le statistiche se necessario e mostra una JDialog
     * con il risultato. 
     * Il messaggio varia in base alla modalità e all'esito (vittoria o tempo scaduto).
     */
    private void finePartita() {
        repaint();
        inCorso = false;
        long tempoTotaleMs = System.currentTimeMillis() - tempoInizio;
        Statistiche.salvaRecord(opzioni.getModalita(), punteggio, tempoTotaleMs);
        
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
     * Riporta al menù se viene premuto il bottone.
     * @param e l'evento generato dal pulsante premuto
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnMenu) {
            parent.tornaAlMenu();
        }
    }
}