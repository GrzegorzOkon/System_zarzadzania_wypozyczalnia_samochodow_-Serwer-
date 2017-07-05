package procesor.baza;

/**
 * Klasa tworząca obiekt pracownika
 * 
 */
public class Pracownik extends Użytkownik {   
    private final Stanowisko stanowisko;

    /**
     * Konstruktor pracownika
     * @param użytkownik obiekt klasy użytkownika
     * @param obiekt klasy stanowiska
     */ 
    public Pracownik(Użytkownik użytkownik, Stanowisko stanowisko) {
        super(użytkownik.pobierzLogin(), użytkownik.pobierzHasło(), użytkownik.pobierzImię(), użytkownik.pobierzNazwisko(), użytkownik.pobierzAdres());
        this.stanowisko = stanowisko;
    }

    /**
     * Konstruktor pracownika
     * @param login przesłany login
     * @param hasło przesłane hasło
     * @param imię przesłane imię
     * @param nazwisko przesłane nazwisko
     * @param adres obiekt klasy adres
     * @param stanowisko obiekt klasy stanowsiko
     */     
    public Pracownik(String login, String hasło, String imię, String nazwisko, Adres adres, Stanowisko stanowisko) {
        super(login, hasło, imię, nazwisko, adres);
        this.stanowisko = stanowisko;
    }

    public Stanowisko pobierzStanowisko() {
        return stanowisko;
    }    
}