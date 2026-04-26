package cattura;

/**
 * Contiene le opzioni di configurazione scelte dal giocatore
 */
public class Opzioni {

    public static final int MODALITA_COMPETITIVA   = 0;
    public static final int MODALITA_CASUAL  = 1;

    private int velocita;
    private int nFigure;
    private int tempoSecondi;
    private int modalita;

    public Opzioni(int velocita, int nFigure, int tempoSecondi, int modalita) {
        this.velocita     = velocita;
        this.nFigure      = nFigure;
        this.tempoSecondi = tempoSecondi;
        this.modalita     = modalita;
    }

    public int getVelocita() {
        return velocita;
    }

    public int getNFigure() {
        return nFigure;
    }

    public int getTempoSecondi() {
        return tempoSecondi;
    }

    public int getModalita() {
        return modalita;
    }    
}