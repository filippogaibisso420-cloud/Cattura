package cattura;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe di utilità che gestisce il salvataggio e la lettura dei record
 * su file di testo.
 * I record vengono mantenuti in memoria dopo il primo
 * caricamento per evitare letture ripetute dal disco.
 * Tutti i metodi sono statici: non è necessario istanziare la classe.
 */
public class Statistiche {
    
    private static final String FILE_RECORD = "dati/record/record.txt";
    
    private static long recordTempoMs = Long.MAX_VALUE;
    private static int recordPunteggio = 0;
    private static boolean caricato = false;
    
    /**
     * Salva il risultato di una partita se batte il record
     * @param modalita modalità della partita conclusa (casual o competitiva)
     * @param punteggio punteggio ottenuto in modalità competitiva 
     * @param tempoMs tempo trascorso totale in millisecondi
     */
    public static void salvaRecord(int modalita, int punteggio, long tempoMs) {
        caricaSeNecessario();
        
        if (modalita == Opzioni.MODALITA_CASUAL) {
            if (tempoMs < recordTempoMs) {
                recordTempoMs = tempoMs;
                scriviFile();
            }
        } else {
            if (punteggio > recordPunteggio) {
                recordPunteggio = punteggio;
                scriviFile();
            }
        }
    }
    
    /**
     * @return il tempo minore ottenuto in modalità casual (in millisecondi).
     */
    public static long getRecordTempoMs() {
        caricaSeNecessario();
        return recordTempoMs;
    }
    
    /**
     * @return il miglior punteggio ottenuto.
     */
    public static int getRecordPunteggio() {
        caricaSeNecessario();
        return recordPunteggio;
    }
    
    private static void caricaSeNecessario() {
        if (caricato) return;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_RECORD))) {
            recordTempoMs = Long.parseLong(br.readLine().trim());
            recordPunteggio  = Integer.parseInt(br.readLine().trim());
        } catch (IOException | NumberFormatException ex) {
            
        }
        caricato = true;
    }
    
    private static void scriviFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_RECORD))) {
            bw.write(String.valueOf(recordTempoMs));
            bw.newLine();
            bw.write(String.valueOf(recordPunteggio));
            bw.newLine();
        } catch (IOException ex) {
            System.out.println("Errore: impossibile scrivere il file dei record.");
        }
    }
}