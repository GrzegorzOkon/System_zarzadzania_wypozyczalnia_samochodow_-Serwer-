package procesor.baza;

import java.io.Serializable;

/**
 * Klasa tworząca obiekt adresu
 * 
 */
public class Adres implements Serializable {    
    private final String ulica, miasto;
    
    /**
     * Konstruktor adresu
     * @param ulica pełna nazwa ulicy
     * @param miasto nazwa miasta
     */
    public Adres(String ulica, String miasto) {
        this.ulica = ulica;
        this.miasto = miasto;
    }

    public String pobierzMiasto() {
        return miasto;
    }

    public String pobierzUlicę() {
        return ulica;
    }
    
    @Override
    public String toString() {
        return ulica + " " + " " + miasto;
    }     
}
