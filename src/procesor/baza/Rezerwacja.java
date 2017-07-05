package procesor.baza;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Klasa tworząca obiekt rezerwacji
 * 
 */
public class Rezerwacja implements Serializable {      
    private final Użytkownik użytkownik;
    private final Samochód samochód; 
    private final LocalDate data_odbioru, data_zwrotu;

    /**
     * Konstruktor rezerwacji
     * @param użytkownik obiekt klasy użytkownika
     * @param samochód obiekt klasy samochód
     * @param odbiór data odbioru rezerwacji
     * @param zwrot data zwrotu rezerwacji
     */ 
    public Rezerwacja(Użytkownik użytkownik, Samochód samochód, LocalDate odbiór, LocalDate zwrot) {
        this.użytkownik = użytkownik;
        this.samochód = samochód;
        this.data_odbioru = odbiór;
        this.data_zwrotu = zwrot;   
    }
    
    public Użytkownik pobierzUżytkownika() {
        return użytkownik;
    }
    
    public Samochód pobierzSamochód() {
        return samochód;
    }   
    
    public LocalDate pobierzDatęOdbioru() {
        return data_odbioru;
    } 
    
    public LocalDate pobierzDatęZwrotu() {
        return data_zwrotu;
    }     
}
