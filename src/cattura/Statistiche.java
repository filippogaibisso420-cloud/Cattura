package cattura;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Gestisce il salvataggio e la lettura dei record su file
 */
public class Statistiche {

    private static final String FILE_RECORD = "dati/record/record.txt";

    // record in memoria
    private static long recordTempoMs    = Long.MAX_VALUE;  // modalità figure: tempo minore
    private static int recordPunteggio     = 0;
    private static boolean caricato      = false;

    /**
     * Salva il risultato di una partita se batte il record
     * @param modalita Opzioni.MODALITA_TEMPO o MODALITA_CASUAL
     * @param punteggio punteggio ottenuto in modalità competitiva 
     * @param tempoMs     tempo totale in millisecondi
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

    public static long getRecordTempoMs() {
        caricaSeNecessario();
        return recordTempoMs;
    }

    public static int getRecordPunteggio() {
        caricaSeNecessario();
        return recordPunteggio;
    }

    // ---------------------------------------------------------- I/O su file
    private static void caricaSeNecessario() {
        if (caricato) return;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_RECORD))) {
            recordTempoMs = Long.parseLong(br.readLine().trim());
            recordPunteggio  = Integer.parseInt(br.readLine().trim());
        } catch (IOException | NumberFormatException ex) {
            // file non ancora esistente: si parte da zero
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