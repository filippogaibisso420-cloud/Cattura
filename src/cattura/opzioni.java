package cattura;

/**
 * Contiene le opzioni di configurazione scelte dal giocatore
 */
public class Opzioni {
    public static final int MODALITA_COMPETITIVA   = 0;
    public static final int MODALITA_CASUAL  = 1;
    
    public static final int DEFAULT_VELOCITA = 5;
    public static final int DEFAULT_TEMPO    = 30;
    
    private int velocita;
    private int nFigure;
    private int tempoSecondi;
    private int modalita;
    
    /**
     * 
     * @param velocita
     * @param nFigure
     * @param tempoSecondi
     * @param modalita 
     */
    public Opzioni(int velocita, int nFigure, int tempoSecondi, int modalita) {
        this.velocita     = velocita;
        this.nFigure      = nFigure;
        this.tempoSecondi = tempoSecondi;
        this.modalita     = modalita;
    }
    
    /**
     * 
     * @return 
     */
    public double getMoltiplicatore() {
        double molVelocita = 1.0 + (velocita - DEFAULT_VELOCITA) * 0.125;

        double molTempo = 1.0;
        if (modalita == MODALITA_COMPETITIVA) {
            molTempo = 1.0 + (DEFAULT_TEMPO - tempoSecondi) * 0.125;

            if (molTempo < 0.25) {
                molTempo = 0.25;
            }
        }
        
        double moltiplicatore = molVelocita + molTempo - 1.0;
        if(moltiplicatore < 0.125) {
            moltiplicatore = 0.125;
        }
        
        return moltiplicatore;
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