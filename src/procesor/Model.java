package procesor;

import gui.Widok;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import kontroler.Kontroler;
import procesor.baza.*;

/**
 * Klasa tworząca obiekt modelu we wzorcu mvc (model - view - controller)
 * 
 */
public class Model {
    private Widok widok;
    
    // referencja na wątek obsługujący żądania klientów
    private Połączenie połączenie = null;

    /**
     * Metoda kasująca dane w tabelach bazy i wpisująca dane początkowe
     */    
    public void inicjalizuj() {
        try {
            Baza.pobierzInstancję();
            widok.dodajKomunikat("Pomyślnie połączono z bazą SQL");
        } catch (SQLException ex) {
            widok.dodajKomunikat("Nieudana próba połączenia z bazą SQL");
        }
        
        try {
            Baza.pobierzInstancję().wyczyśćTabele();
            Baza.pobierzInstancję().dodajUżytkownika(new Użytkownik("mati11", "haslo4", "Mateusz", "Kowalski", new Adres("Kościuszki 8/66", "Poznań")));
            Baza.pobierzInstancję().dodajUżytkownika(new Użytkownik("rysio", "123", "Ryszard", "Nowak", new Adres("Wesoła 1", "Warszawa"))); 
            Baza.pobierzInstancję().dodajPracownika(new Pracownik("root", "root", "Michał", "Anioł", new Adres("Wiejska 5", "Kraków"), new Stanowisko("Administrator bazy danych", 5600)));
            Baza.pobierzInstancję().dodajPracownika(new Pracownik("admin", "admin", "Adam", "Nowak", new Adres("Wesoła 1", "Warszawa"), new Stanowisko("Administrator systemów", 5000)));
            Baza.pobierzInstancję().dodajSamochód(new Samochód("Daewoo", "Matiz", "zieleń", "LPG", 2010));
            Baza.pobierzInstancję().dodajSamochód(new Samochód("Toyota", "Yaris", "granat", "Benzyna", 2016));
            Baza.pobierzInstancję().dodajSamochód(new Samochód("Toyota", "Yaris", "złoto", "Benzyna", 2015));
            Baza.pobierzInstancję().dodajSamochód(new Samochód("Renault", "Thalia", "granat", "ON", 2012));
            Baza.pobierzInstancję().dodajSamochód(new Samochód("Citroen", "Saxo", "czerwień", "Benzyna", 2005));
            Baza.pobierzInstancję().dodajSamochód(new Samochód("Fiat", "126p", "biel", "Benzyna", 1980));
            Baza.pobierzInstancję().dodajRezerwację(new Rezerwacja(new Użytkownik("piotrek", "234", "Piotr", "Góra", new Adres("Wesoła 14", "Łódź")), new Samochód("Daewoo", "Tico", "niebieski", "Benzyna", 2010), LocalDate.now(), LocalDate.now().plusDays(3)));
            Baza.pobierzInstancję().dodajRezerwację(new Rezerwacja(new Użytkownik("rysio", "123", "Ryszard", "Nowak", new Adres("Wesoła 1", "Warszawa")), new Samochód("Renault", "Thalia", "granat", "ON", 2012), LocalDate.now().plusDays(2), LocalDate.now().plusDays(7)));      
            widok.dodajKomunikat("Pomyślnie zainicjalizowano dane");
        } catch (SQLException ex) {
            widok.dodajKomunikat("Nieudana próba inicjalizacji bazy");
        }
    }

    /**
     * Metoda uruchamiająca wątek serwera nasłuchującego nowych komunikatów
     */    
    public void startuj() {
        try {
            połączenie = new Połączenie(widok);
            widok.dodajKomunikat("Pomyślnie utworzono serwer");
            połączenie.start(); // uruchamiamy wątek   
        } catch (IOException ex) {
            widok.dodajKomunikat("Wystąpił błąd podczas tworzenia serwera TCP/IP");
        }            
    }

    /**
     * Metoda zatrzymująca wątek serwera nasłuchującego nowych komunikatów
     */     
    public void zatrzymaj() {
        połączenie.interrupt(); // przerywamy       
        połączenie = null; 
        widok.dodajKomunikat("Przerwano działanie serwera"); 
        widok.dodajKomunikat("" + połączenie.interrupted());
    }
    
    /**
     * Metoda przypisująca referencję do obiektu widoku
     * @param widok obiekt do którego ma być referencja
     */ 
    public void setReference(Widok widok){
        this.widok = widok;
    }    
}
