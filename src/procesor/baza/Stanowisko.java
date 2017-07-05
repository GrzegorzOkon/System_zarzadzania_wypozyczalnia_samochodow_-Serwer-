package procesor.baza;

import java.io.Serializable;

/**
 * Klasa tworząca obiekt stanowiska
 * 
 */
public class Stanowisko implements Serializable {    
    private final String nazwa;
    private final int wynagrodzenie;

    /**
     * Konstruktor stanowiska
     * @param nazwa przesłana nazaa stanwiska
     * @param wynagrodzenie przesłana kwota wynagrodzenia
     */    
    public Stanowisko(String nazwa, int wynagrodzenie) {
        this.nazwa = nazwa;
        this.wynagrodzenie = wynagrodzenie;
    }

    public int pobierzWynagrodzenie() {
        return wynagrodzenie;
    }

    public String pobierzNazwę() {
        return nazwa;
    }    
}
