package procesor.baza;

import java.io.Serializable;

/**
 * Klasa tworząca obiekt samochodu
 * 
 */
public class Samochód implements Serializable {   
    private final String marka, model, kolor, paliwo;   
    private final int rok;

    /**
     * Konstruktor samochodu
     * @param marka przesłana marka samochodu
     * @param model przesłany model samochodu
     * @param kolor przesłany kolor samochodu
     * @param paliwo przesłany rodzaj paliwa samochodu
     * @param rok przesłany rok produkcji
     */
    public Samochód(String marka, String model, String kolor, String paliwo, int rok) {
        this.marka = marka;
        this.model = model;
        this.kolor = kolor;
        this.paliwo = paliwo;
        this.rok = rok;        
    }
    
    public String pobierzMarkę() {
        return marka;
    }
    
    public String pobierzModel() {
        return model;
    }
    
    public String pobierzKolor() {
        return kolor;
    }  
    
    public String pobierzPaliwo() {
        return paliwo;
    }   
    
    public int pobierzRok() {
        return rok;
    }    
}
