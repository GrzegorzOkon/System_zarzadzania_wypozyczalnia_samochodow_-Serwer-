import procesor.Model;
import kontroler.Kontroler;
import gui.*;

/**
 *  Klasa uruchomieniowa. Łączy ze sobą poszcególne klasy modelu mvc (model - view -controller)
 * 
 */
public class Startuj {
    public static void main (String[] args){
        Model model = new Model();
        Widok widok = new Widok();
        Kontroler kontroler = new Kontroler(model, widok);
        //Zwraca referencję zwrotną do kontrolera
        widok.setReference(kontroler);
        model.setReference(widok);
    }
}
