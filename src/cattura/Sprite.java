package cattura;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * classe che rappresenta la figura generica che verrà visualizzata sulla finestra
 */
public class Sprite {
    private int x, y;
    private int vx, vy;
    private int dimensione;
    private BufferedImage imgAttuale;
    private JPanel parent;

    private static BufferedImage[] immaginiSalvate = new BufferedImage[3]; 
    private static boolean immaginiCaricate = false;
    
    public Sprite(JPanel parent, int velocita, int modalita) {
        this.parent = parent;
        Random rand = new Random();
        
        this.dimensione = rand.nextInt(35, 81);
        
        if (!immaginiCaricate) {
            try {
                immaginiSalvate[0] = ImageIO.read(new File("dati/Arancia.png"));
                immaginiSalvate[1] = ImageIO.read(new File("dati/Mirtillo.png"));
                immaginiSalvate[2] = ImageIO.read(new File("dati/Cocomero.png"));
                immaginiCaricate = true;
            } catch (IOException e) {
                System.out.println("Errore: Impossibile trovare una o piu' immagini .png!");
            }
        }

        if (immaginiCaricate) {
            int indiceCasuale = rand.nextInt(3);
            this.imgAttuale = immaginiSalvate[indiceCasuale];
        }
        
        int larghezzaPannello = parent.getWidth() > 0 ? parent.getWidth() : 1900;
        int altezzaPannello = parent.getHeight() > 0 ? parent.getHeight() : 880;

        this.x = rand.nextInt(larghezzaPannello - dimensione);
        this.y = rand.nextInt(altezzaPannello - dimensione);
        if(modalita == Opzioni.MODALITA_CASUAL) {
            this.vx = (rand.nextBoolean() ? 1 : -1) * (rand.nextInt(velocita) + 1);
            this.vy = (rand.nextBoolean() ? 1 : -1) * (rand.nextInt(velocita) + 1); 
        } else if(modalita == Opzioni.MODALITA_COMPETITIVA) {
            this.vx = (rand.nextBoolean() ? 1 : -1) * velocita;
            this.vy = (rand.nextBoolean() ? 1 : -1) * velocita;
        }
    }
    
    public void muovi() {
        x += vx;
        y += vy;

        int limiteDestro = parent.getWidth();
        int limiteInferiore = parent.getHeight();

        // Rimbalzi
        if (x <= 0 || x + dimensione >= limiteDestro) {
            vx = -vx; 
        }
        if (y <= 0 || y + dimensione >= limiteInferiore) {
            vy = -vy; 
        }
    }

    // Metodo per disegnare
    public void disegna(Graphics g) {
        if (imgAttuale != null) {
//            Graphics2D g2d = (Graphics2D) g;
//            Composite compositeOriginale = g2d.getComposite();
//
//            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
//            g2d.drawImage(imgAttuale, x, y, dimensione, dimensione, null);
//
//            g2d.setComposite(compositeOriginale);
            g.drawImage(imgAttuale, x, y, dimensione, dimensione, parent);
        } else {
            g.setColor(java.awt.Color.GRAY);
            g.fillRect(x, y, dimensione, dimensione);
        }
    }
    
    /**
     * Controlla se il punto (px, py) è contenuto nella figura
     * @param px coordinata x del punto
     * @param py coordinata y del punto
     * @return true se il punto è dentro la figura
     */
    public boolean contienePunto(int px, int py) {
        return px >= x && px <= x + dimensione
            && py >= y && py <= y + dimensione;
    }

    /**
     * @return Dimensione dello sprite 
     */
    public int getDimensione() {
        return dimensione; 
    }
    
    /**
     * @return posizione sul piano orizzontale in pixel
     */
    public int getX() { 
        return x;
    }
    
    /**
     * @return posizione sul piano verticale in pixel
     */
    public int getY() {
        return y; 
    }
}
