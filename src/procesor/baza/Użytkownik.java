package procesor.baza;

import java.io.Serializable;

/**
 * Klasa tworząca obiekt użytkownika
 * 
 */
public class Użytkownik implements Serializable {   
    private final String login, hasło, imię, nazwisko;    
    private final Adres adres;

    /**
     * Konstruktor użytkownika
     * @param login przesłany login użytkownika
     * @param hasło przesłane hasło użytkownika
     * @param imię przesłane imię użytkownika
     * @param nazwisko przesłane nazwisko użytkownika
     * @param adres przeslany adres użytkownika
     */    
    public Użytkownik(String login, String hasło, String imię, String nazwisko, Adres adres) {
        this.login = login;
        this.hasło = hasło;
        this.imię = imię;
        this.nazwisko = nazwisko;
        this.adres = adres;
    }
     
    public String pobierzImię() {
        return imię;
    }
    
    public String pobierzNazwisko() {
        return nazwisko;
    } 
    
    public String pobierzLogin() {
        return login;
    }
    
    public String pobierzHasło() {
        return hasło;
    }
    
    public Adres pobierzAdres() {
        return adres;
    }   
}