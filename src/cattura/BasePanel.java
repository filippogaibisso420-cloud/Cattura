package cattura;

import javax.swing.JPanel;

/**
 * classe base per l'implementazione delle varie schermate dell'applicazione.
 */
abstract public class BasePanel extends JPanel {
    protected Cattura parent;
    
    /**
     * crea il collegamento tra il pannello e il padre.
     * @param parent il padre del pannello
     */
    public BasePanel(Cattura parent) {
        this.parent = parent;
    }
    
    /**
     * metodo che ripristina il panel allo stato originale.
     */
    abstract void reset();
}
