package kontroler;

import gui.Widok;
import gui.komunikaty.*;
import procesor.Model;

/**
 * Klasa tworząca obiekt kontrolera we wzorcu mvc (model - view - controller)
 * 
 */
public class Kontroler {
    private Model model;
    private Widok view;
    
    /**
     * Konstruktor kontrolera
     * @param model obiekt klasy model
     * @param view obiekt klasy view
     */ 
    public Kontroler(Model model, Widok view){
        this.model = model;
        this.view = view;
    }   
    
    /**
     * Metoda korzystająca ze wzorca strategia w celu wybrania odpowiedniego dziąłania
     * @param klik będący komunikatem wygenerowanym po naciśnięciu przycisku
     */
    public void wykonajAkcję(Komunikat klik){
        //Sprawdza czy przesłany komunikat jest odpowiednią instancją
        if (klik instanceof InicjalizujKomunikat){  
            model.inicjalizuj();
        } else if (klik instanceof StartujKomunikat) {
            model.startuj();
        } else if (klik instanceof ZatrzymajKomunikat) {
            model.zatrzymaj();          
        }
    }
}
