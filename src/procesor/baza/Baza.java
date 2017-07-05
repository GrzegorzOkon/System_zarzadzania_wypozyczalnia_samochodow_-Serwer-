package procesor.baza;

import gui.Widok;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/**
 *
 * 
 */
public class Baza {
    private static Baza baza; 
    private Connection połączenie;

    /**
     * Metoda pozwalająca na pobranie instancji obiektu
     * @return zainicjalizowany obiekt klasy 
     * @throws SQLException
     */     
    public static final Baza pobierzInstancję() throws SQLException {
        if (baza != null) {
            return baza;
        } else {
            baza = new Baza();
        }
        
        return baza;
    }

    /**
     * Konstruktor bazy
     */ 
    private Baza() throws SQLException {
        Properties connectionProperties = new Properties();
        connectionProperties.put("user", "hr");
        connectionProperties.put("password", "hr");
        
        połączenie = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", connectionProperties);
    }    
    
    /**
     * Metoda dodająca adres do bazy danych
     * @param adres adres
     * @return id adresu w bazie danych
     * @throws SQLException 
     */
    public int dodajAdres(Adres adres) throws SQLException {
        ResultSet zapytanie = wykonajZapytanie("SELECT * FROM adresy");
        int id = zapytanie.last() ? zapytanie.getRow() : 0;
        połączenie.createStatement().execute("INSERT INTO adresy VALUES(" + ++id + ", '" + adres.pobierzUlicę() + "', '" + adres.pobierzMiasto() + "')");
        return id;
    }

    /**
     * Metoda pobierająca id adresu
     * @param adres adres
     * @return id adresu lub 0 jeśli nie ma adresu w bazie
     * @throws SQLException 
     */
    public int pobierzAdres(Adres adres) throws SQLException {
        ResultSet zapytanie = wykonajZapytanie("SELECT * FROM adresy WHERE ulica = '" + adres.pobierzUlicę() + "' AND miasto = '" + adres.pobierzMiasto() + "'");
        if (zapytanie.next()) {
            return zapytanie.getInt("id");
        }
        return 0;
    }

    /**
     * Metoda pobierająca z bazy danych adres o podanym id
     * @param id id adresu
     * @return zaincjalizowany obiekt adresu
     * @throws java.sql.SQLException
     */
    public Adres pobierzAdres(int id) throws SQLException {
        ResultSet zapytanie = wykonajZapytanie("SELECT * FROM adresy WHERE id = " + id);
        Adres adres = null;
        if (zapytanie.next()) {
            String ulica = zapytanie.getString("ulica");
            String miasto = zapytanie.getString("miasto");
            adres = new Adres(ulica, miasto);
        }
        return adres;
    }
    
    /**
     * Metoda dodająca użytkownika do bazy danych
     * @param użytkownik dodawany użytkownik
     * @return id użytkownika w bazie danych
     * @throws SQLException 
     */
    public final int dodajUżytkownika(Użytkownik użytkownik) throws SQLException {
        Statement stmt = połączenie.createStatement();
        ResultSet rs = wykonajZapytanie("SELECT * from uzytkownicy");
        int idUżytkownika = rs.last() ? rs.getRow() : 0;
        Adres adres = użytkownik.pobierzAdres();
        int idAdresu = pobierzAdres(adres);
        // jeśli nie znaleziono podanego adresu w bazie
        if (idAdresu == 0)
            idAdresu = dodajAdres(adres);
        stmt.execute("INSERT INTO uzytkownicy VALUES(" + ++idUżytkownika + ", '" + użytkownik.pobierzLogin() + "', '" + użytkownik.pobierzHasło() + "', '" + użytkownik.pobierzImię() + "', '" + użytkownik.pobierzNazwisko() + "', " + idAdresu + ")");
        return idUżytkownika;
    }  

    /**
     * Metoda pobierająca z bazy danych użytkownika o podanym id
     * @param id id użytkownika
     * @return zaincjalizowany obiekt użytkownika
     * @throws java.sql.SQLException
     */
    public Użytkownik pobierzUżytkownika(int id) throws SQLException {
        ResultSet zapytanie = wykonajZapytanie("SELECT * FROM uzytkownicy WHERE id = " + id);
        Użytkownik użytkownik = null;
        if (zapytanie.next()) {
            String login = zapytanie.getString("login");
            String hasło = zapytanie.getString("haslo");
            String imię = zapytanie.getString("imie");
            String nazwisko = zapytanie.getString("nazwisko");
            Adres adres = pobierzAdres(zapytanie.getInt("id_adresu"));
            użytkownik = new Użytkownik(login, hasło, imię, nazwisko, adres);
        }
        return użytkownik;
    }
    
    /**
     * Metoda pobierająca id użytkownika
     * @param użytkownik użytkownik
     * @return id użytkownika lub 0 jeśli nie ma użytkownika w bazie
     * @throws SQLException 
     */
    public int pobierzUżytkownika(Użytkownik użytkownik) throws SQLException {
        ResultSet zapytanie = wykonajZapytanie("SELECT * FROM uzytkownicy WHERE login = '" + użytkownik.pobierzLogin() + "'");
        if (zapytanie.next()) {
            return zapytanie.getInt("id");
        }
        return 0;
    }
    
    /**
     * Metoda dodająca stanowisko do bazy danych
     * @param stanowisko stanowisko
     * @return id stanowiska w bazie danych
     * @throws SQLException 
     */
    public int dodajStanowisko(Stanowisko stanowisko) throws SQLException {
        ResultSet zapytanie = wykonajZapytanie("SELECT * FROM stanowiska");
        int id = zapytanie.last() ? zapytanie.getRow() : 0;
        połączenie.createStatement().execute("INSERT INTO stanowiska VALUES(" + ++id + ", '" + stanowisko.pobierzNazwę() + "', '" + stanowisko.pobierzWynagrodzenie() + "')");
        return id;
    }    
    
     /**
     * Metoda dodająca pracownika do bazy danych
     * @param pracownik dodawany pracownik
     * @return id pracownika w bazie danych
     * @throws SQLException 
     */
    public final int dodajPracownika(Pracownik pracownik) throws SQLException {
        int idUżytkownika = dodajUżytkownika((Użytkownik) pracownik);
        Stanowisko stanowisko = pracownik.pobierzStanowisko();
        int idStanowiska = pobierzStanowisko(stanowisko);
        if (idStanowiska == 0)
            idStanowiska = dodajStanowisko(stanowisko);
        Statement stmt = połączenie.createStatement();
        ResultSet rs = wykonajZapytanie("SELECT * from pracownicy");
        int idPracownika = rs.last() ? rs.getRow() : 0;
        stmt.execute("INSERT INTO pracownicy VALUES(" + ++idPracownika + ", " + idStanowiska + ", " + idUżytkownika + ")");
        return idPracownika;
    }

     /**
     * Metoda pobierająca id pracownika
     * @param użytkownik użytkownik
     * @return id pracownika lub 0 jeśli nie ma adresu w bazie
     * @throws SQLException 
     */
    public int pobierzPracownika(Użytkownik użytkownik) throws SQLException {
        int idUżytkownika = pobierzUżytkownika(użytkownik);
        if (idUżytkownika != 0) {
            ResultSet zapytanie = wykonajZapytanie("SELECT * FROM pracownicy WHERE id_uzytkownika = " + idUżytkownika);
            if (zapytanie.next()) {
                return zapytanie.getInt("id");
            }
        }
        return 0;
    }
    
    /**
     * Metoda pobierająca z bazy danych pracownika o podanym id
     * @param id id stanowiska
     * @return zaincjalizowany obiekt pracownika
     * @throws java.sql.SQLException
     */
    public Pracownik pobierzPracownika(int id) throws SQLException {
        ResultSet zapytanie = wykonajZapytanie("SELECT * FROM pracownicy WHERE id = " + id);
        Pracownik pracownik = null;
        if (zapytanie.next()) {
            int idUżytkownika = zapytanie.getInt("id_uzytkownika");
            int idStanowiska = zapytanie.getInt("id_stanowiska");
            pracownik = new Pracownik(pobierzUżytkownika(idUżytkownika), pobierzStanowisko(idStanowiska));
        }
        return pracownik;
    }
    
    /**
     * Metoda pobierająca z bazy danych stanowisko o podanym id
     * @param id id stanowiska
     * @return zaincjalizowany obiekt stanowiska
     * @throws java.sql.SQLException
     */
    public Stanowisko pobierzStanowisko(int id) throws SQLException {
        ResultSet zapytanie = wykonajZapytanie("SELECT * FROM stanowiska WHERE id = " + id);
        Stanowisko stanowisko = null;
        if (zapytanie.next()) {
            int wynagrodzenie = zapytanie.getInt("wynagrodzenie");
            String nazwa = zapytanie.getString("nazwa");
            stanowisko = new Stanowisko(nazwa, wynagrodzenie);
        }
        return stanowisko;
    }
    
    /**
     * Metoda pobierająca id stanowiska
     * @param stanowisko stanowisko
     * @return id stanowiska lub 0 jeśli nie ma adresu w bazie
     * @throws SQLException 
     */
    public int pobierzStanowisko(Stanowisko stanowisko) throws SQLException {
        ResultSet zapytanie = wykonajZapytanie("SELECT * FROM stanowiska WHERE nazwa = '" + stanowisko.pobierzNazwę() + "' AND wynagrodzenie = '" + stanowisko.pobierzWynagrodzenie() + "'");
        if (zapytanie.next()) {
            return zapytanie.getInt("id");
        }
        return 0;
    }
    
    /**
     * Metoda dodająca samochód do bazy danych
     * @param samochód dodawany samochód
     * @return id samochodu w bazie danych
     * @throws SQLException 
     */
    public final int dodajSamochód(Samochód samochód) throws SQLException {
        Statement stmt = połączenie.createStatement();
        ResultSet rs = wykonajZapytanie("SELECT * from samochody");
        int idSamochodu = rs.last() ? rs.getRow() : 0;
        stmt.execute("INSERT INTO samochody VALUES(" + ++idSamochodu + ", '" + samochód.pobierzMarkę()+ "', '" + samochód.pobierzModel()+ "', '" + samochód.pobierzKolor() + "', '" + samochód.pobierzPaliwo() + "', " + samochód.pobierzRok() + ")");
        return idSamochodu;
    } 

    /**
     * Metoda pobierająca id samochodu
     * @param samochód samochód
     * @return id samochodu lub 0 jeśli nie ma samochodu w bazie
     * @throws SQLException 
     */
    public int pobierzSamochód(Samochód samochód) throws SQLException {
        ResultSet zapytanie = wykonajZapytanie("SELECT * FROM samochody WHERE marka = '" + samochód.pobierzMarkę() + "' AND model = '" + samochód.pobierzModel() + "' AND kolor = '" + samochód.pobierzKolor() + "'");
        if (zapytanie.next()) {
            return zapytanie.getInt("id");
        }
        return 0;
    }    
    
    /**
     * Metoda dodająca rezerwację do bazy danych
     * @param rezerwacja dodawana rezerwacja
     * @return id rezerwacji w bazie danych
     * @throws SQLException 
     */
    public final int dodajRezerwację(Rezerwacja rezerwacja) throws SQLException {
        int idUżytkownika = pobierzUżytkownika(rezerwacja.pobierzUżytkownika());     
        if (idUżytkownika == 0)
            idUżytkownika = dodajUżytkownika(rezerwacja.pobierzUżytkownika());
        int idSamochodu = pobierzSamochód(rezerwacja.pobierzSamochód());
        if (idSamochodu == 0)
            idSamochodu = dodajSamochód(rezerwacja.pobierzSamochód());
        Statement stmt = połączenie.createStatement();
        ResultSet rs = wykonajZapytanie("SELECT * from rezerwacje");
        int idRezerwacji = rs.last() ? rs.getRow() : 0;
        stmt.executeUpdate("INSERT INTO rezerwacje VALUES(" + ++idRezerwacji + ", " + idUżytkownika + ", " + idSamochodu + ", '" + rezerwacja.pobierzDatęOdbioru() + "', '" + rezerwacja.pobierzDatęZwrotu() + "')");
        return idRezerwacji;
    }  
    
    /**
     * Metoda pozwalająca na zalogowanie się do systemu
     * @param login login do konta
     * @param hasło hasło do konta
     * @return obiekt użytkownika lub null w przypadku niepowodzenia
     * @throws java.sql.SQLException
     */
    public Użytkownik zaloguj(String login, String hasło) throws SQLException {
        ResultSet rs = wykonajZapytanie("SELECT * FROM uzytkownicy WHERE login = '" + login + "' AND haslo = '" + hasło + "'");
        if (rs.next()) {
            Adres adres = pobierzAdres(rs.getInt("id_adresu"));
            if (adres != null) {
                Użytkownik użytkownik = new Użytkownik(rs.getString("login"), rs.getString("haslo"), rs.getString("imie"), rs.getString("nazwisko"), adres);
                int idPracownika = pobierzPracownika(użytkownik);
                // jeśli znaleziono podanego pracownika
                if (idPracownika != 0) {
                    użytkownik = pobierzPracownika(idPracownika);
                }
                return użytkownik;
            }
        }
        return null;
    }
    
    /**
     * Metoda pobierająca ofertę dostępnych samochodów
     * @return lista obiektów dostępnych samochodów
     * @throws SQLException 
     */
    public ArrayList<Samochód> pobierzOfertę() throws SQLException {
        ResultSet zapytanie = wykonajZapytanie("SELECT samochody.marka, samochody.model, samochody.kolor, samochody.paliwo, samochody.rok FROM samochody MINUS SELECT samochody.marka, samochody.model, samochody.kolor, samochody.paliwo, samochody.rok FROM samochody, rezerwacje WHERE samochody.id = rezerwacje.id_samochodu");
        ArrayList<Samochód> oferta = new ArrayList<>();
        
        if (zapytanie != null) {
            if (zapytanie.last() == true) {
                zapytanie.beforeFirst();
                while (zapytanie.next()) {
                    oferta.add(new Samochód(zapytanie.getString("marka"), zapytanie.getString("model"), zapytanie.getString("kolor"), zapytanie.getString("paliwo"), zapytanie.getInt("rok")));
                }  
            }
            return oferta;            
        } else {
            return null;
        }
    }

        /**
     * Metoda pobierająca listę rezerwacji zalogowanego użytkownika
     * @param login przesłany użytkownik
     * @return lista rezerwacji lub 0 jeśli nie ma rezerwacji w bazie
     * @throws SQLException 
     */
    public ArrayList<Rezerwacja> pobierzRezerwacje(String login) throws SQLException {        
        ResultSet zapytanie = wykonajZapytanie("SELECT  * FROM samochody, rezerwacje, uzytkownicy WHERE samochody.id = rezerwacje.id_samochodu AND uzytkownicy.id = rezerwacje.id_uzytkownika AND uzytkownicy.login = '" + login + "'");
        ArrayList<Rezerwacja> rezerwacje = new ArrayList<>();
        
        if (zapytanie != null) {
            if (zapytanie.last() == true) {
                zapytanie.beforeFirst();
                while (zapytanie.next()) {
                    rezerwacje.add(new Rezerwacja(null, new Samochód(zapytanie.getString("marka"), zapytanie.getString("model"), zapytanie.getString("kolor"), zapytanie.getString("paliwo"), zapytanie.getInt("rok")), new java.sql.Date(zapytanie.getDate("data_od").getTime()).toLocalDate(), new java.sql.Date(zapytanie.getDate("data_do").getTime()).toLocalDate()));
                }  
            }
            return rezerwacje;            
        } else {
            return null;
        }
    }

     /**
     * Metoda pobierająca wszystkie rezerwacje
     * @return lista rezerwacji lub 0 jeśli nie ma adresu w bazie
     * @throws SQLException 
     */
    public ArrayList<Rezerwacja> pobierzRezerwacje() throws SQLException {        
        ResultSet zapytanie = wykonajZapytanie("SELECT  * FROM samochody, rezerwacje, uzytkownicy WHERE samochody.id = rezerwacje.id_samochodu AND uzytkownicy.id = rezerwacje.id_uzytkownika");
        ArrayList<Rezerwacja> rezerwacje = new ArrayList<>();
        
        if (zapytanie != null) {
            if (zapytanie.last() == true) {
                zapytanie.beforeFirst();
                while (zapytanie.next()) {
                    rezerwacje.add(new Rezerwacja(new Użytkownik(zapytanie.getString("login"), zapytanie.getString("haslo"), zapytanie.getString("imie"), zapytanie.getString("nazwisko"), null), new Samochód(zapytanie.getString("marka"), zapytanie.getString("model"), zapytanie.getString("kolor"), zapytanie.getString("paliwo"), zapytanie.getInt("rok")), new java.sql.Date(zapytanie.getDate("data_od").getTime()).toLocalDate(), new java.sql.Date(zapytanie.getDate("data_do").getTime()).toLocalDate()));
                }  
            }
            return rezerwacje;            
        } else {
            return null;
        }
    }
    
     /**
     * Metoda anulująca wybraną rezerwację zalogowanego użytkownika
     * @param login zalogowany użytkownik
     * @param marka przesłana marka samochodu
     * @param model przesłany model samochodu
     * @param kolor przesłany kolor samochodu
     * @throws SQLException 
     */
    public void anulujRezerwację(String login, String marka, String model, String kolor) throws SQLException {    
        int id_użytkownika = pobierzUżytkownika(new Użytkownik(login, null, null, null, null));
        int id_samochodu = pobierzSamochód(new Samochód(marka, model, kolor, null, 0));
        
        wykonajZmianę("DELETE FROM rezerwacje WHERE id_uzytkownika = " + id_użytkownika + "AND id_samochodu = " + id_samochodu);
    }
    
    /**
     * Metoda wykonująca zapytanie SQL
     * @param zapytanie zapytanie w formie łańcucha znaków
     * @return wynik zapytania
     */
    public ResultSet wykonajZapytanie(String zapytanie) {
        try {
            Statement stmt = połączenie.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(zapytanie);
            return rs;
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }
        return null;
    }  

    /**
     * Metoda wykonująca polecenie zmiany danych w bazie SQL
     * @param polecenie komenda w formie łańcucha znaków
     */
    public void wykonajZmianę(String polecenie) {
        try {
            Statement stmt = połączenie.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.executeUpdate(polecenie);
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }
    }  

    /**
     * Metoda wykonująca czyszczenie tabel danych w bazie SQL
     * @throws SQLException
     */    
    public void wyczyśćTabele() throws SQLException{
        Statement stmt = połączenie.createStatement();
        stmt.executeUpdate("DELETE FROM Rezerwacje");
        stmt.executeUpdate("DELETE FROM Pracownicy");
        stmt.executeUpdate("DELETE FROM Uzytkownicy");
        stmt.executeUpdate("DELETE FROM Adresy");
        stmt.executeUpdate("DELETE FROM Samochody");
        stmt.executeUpdate("DELETE FROM Stanowiska");
    }   
}