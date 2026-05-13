package cattura;

/**
 * Contiene le opzioni di configurazione scelte dal giocatore prima
 * di avviare una partita. 
 * Fornisce anche il calcolo del moltiplicatore
 * di punteggio in base alle impostazioni scelte.
 */
public class Opzioni {
    public static final int MODALITA_COMPETITIVA   = 0;
    public static final int MODALITA_CASUAL = 1;
    
    public static final int DEFAULT_VELOCITA = 5;
    public static final int DEFAULT_TEMPO = 30;
    
    private int velocita;
    private int nFigure;
    private int tempoSecondi;
    private int modalita;
    
    /**
     * Definisce i valori delle opzioni in base a quelli scelti.
     * @param velocita velocità delle figure in pixel per frame
     * @param nFigure numero di figure da generare
     * @param tempoSecondi durata della partita in secondi (modalità competitiva)
     * @param modalita modalità di gioco (a tempo o non)
     */
    public Opzioni(int velocita, int nFigure, int tempoSecondi, int modalita) {
        this.velocita = velocita;
        this.nFigure = nFigure;
        this.tempoSecondi = tempoSecondi;
        this.modalita = modalita;
    }
    
    /**
     * Calcola il moltiplicatore del punteggio in base alle opzioni scelte.
     * Il moltiplicatore è la somma di due contributi:
     * <ul>
     *   <li><b>Velocità:</b> ogni unità sopra o sotto la velocità di base (5)
     *       aggiunge o rimuove 0.125.</li>
     *   <li><b>Tempo</b> (solo in modalità competitiva): meno tempo si sceglie,
     *       maggiore è il contributo; ogni unità sotto il tempo di base (30s)
     *       aggiunge 0.125, ogni unità sopra ne rimuove altrettanto.
     *       Il contributo del tempo non scende mai sotto 0.25.</li>
     * </ul>
     * Il moltiplicatore finale non scende mai sotto code 0.125.
     * @return il moltiplicatore del punteggio (valore minimo 0.125)
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
    
    /**
     * @return velocità delle figure.
     */
    public int getVelocita() {
        return velocita;
    }
    
    /**
     * @return il numero di figure.
     */
    public int getNFigure() {
        return nFigure;
    }
    
    /**
     * @return la durata della partita in secondi per la modalità competitiva. 
     */
    public int getTempoSecondi() {
        return tempoSecondi;
    }
    
    /**
     * @return la modalità selezionata. 
     */
    public int getModalita() {
        return modalita;
    }    
}