package procesor.baza.obsługa;

import gui.Widok;
import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import procesor.baza.*;

/**
 *
 * 
 */
public class Klient extends Thread {
    private Widok widok;
    
    // referencja na obiekt umożliwiający połączenie z serwerem
    private final Połączenie połączenie;
    
    // strumienie służące do odbierania oraz wysyłania danych
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    
    /**
     * Konstruktor obsługi klienta
     * @param socket gniazdo dzięki któremu będzie możliwa komunikacja
     * @param połączenie obiekt umożliwiający nawiązywanie połączenia z serwerem
     */
    public Klient(Widok widok, final Socket socket, Połączenie połączenie) {
        this.widok = widok;
        this.połączenie = połączenie;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            System.err.println("Nie udało się pobrać strumieni od klienta");
        }
    }  

    /**
     * Metoda pozwalająca na odczytanie wiadomości ze strumienia wejściowego
     * @return odebrany łańcuch znaków
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    private String odbierzWiadomość() throws IOException, ClassNotFoundException {
        String wiadomość = (String) ois.readObject();
        return wiadomość.trim();
    }

    /**
     * Metoda pozwalająca na wysłanie wiadomości do klienta
     * @param wiadomość łańcuch znaków, który zostanie wysłany
     * @throws IOException 
     */
    private void wyślijWiadomość(String wiadomość) throws IOException {
        oos.writeObject(wiadomość);
        oos.flush();
    }
    
    /**
     * Metoda przetwarzająca żądania klienta
     * @param wiadomość wiadomość od klienta
     */
    private void przetwórzWiadomość(String wiadomość) throws SQLException {          
        widok.dodajKomunikat("Odebrano: " + wiadomość);
        // sprawdzamy jakie żądanie wysłał klient
        // pobierając tylko łańcuch znaków do znaku "<"
        String żądanie = wiadomość.substring(0, wiadomość.indexOf("<"));
        // wyciągamy informacje z łańcucha znaków
        String informacje = wiadomość.substring(wiadomość.indexOf("<")+1);
        
        switch (żądanie) {
            case "Zaloguj":
                {
                    // wydzielamy dane
                    String[] dane = informacje.split("/");
                    String login = dane[0];
                    String hasło = dane[1];
                    Użytkownik użytkownik = Baza.pobierzInstancję().zaloguj(login, hasło);
                    try {
                        if (użytkownik == null) {
                            wyślijWiadomość("Nieudana próba logowania");                           
                        } else {
                            wyślijWiadomość("Dane do konta zostały pomyślnie zweryfikowane");
                            oos.writeObject(użytkownik); // wysyłamy obiekt użytkownika przez strumień
                            oos.flush();
                        }
                    } catch (IOException ex) {
                        widok.dodajKomunikat("Nieudana próba wysłania komunikatu do klienta");
                    }
                }
                break;
            case "Wyloguj":
                {
                    połączenie.wyloguj(this);                    
                }
                break;
            case "Przeglądaj ofertę":
                {
                    ArrayList<Samochód> oferta = Baza.pobierzInstancję().pobierzOfertę();
                    try {
                        if (oferta == null) {
                            widok.dodajKomunikat("Nie udało się pobrać oferty");
                        } else {
                            wyślijWiadomość("Udało się pobrać ofertę");
                            oos.writeObject(oferta); // wysyłamy obiekt użytkownika przez strumień
                            oos.flush();    
                        }
                    }catch (IOException ex) {
                        widok.dodajKomunikat("Nieudana próba wysłania komunikatu do klienta");
                    }                  
                }
                break;
            case "Rezerwuj":
                {
                    // wydzielamy dane
                    String[] dane = informacje.split("/");
                    String login = dane[0];
                    String marka = dane[1];
                    String model = dane[2];
                    String kolor = dane[3]; 
                    String paliwo = dane[4];
                    int rok = Integer.valueOf(dane[5]);     
                    LocalDate dataOdbioru = LocalDate.parse(dane[6]);
                    LocalDate dataZwrotu = LocalDate.parse(dane[7]);
                    int idRezerwacji = Baza.pobierzInstancję().dodajRezerwację(new Rezerwacja(new Użytkownik(login, null, null, null, null), new Samochód(marka, model, kolor, paliwo, rok), dataOdbioru, dataZwrotu));
                    try {
                        if (idRezerwacji == 0) {
                            wyślijWiadomość("Nieudana próba rezerwacji");                           
                        } else {
                            wyślijWiadomość("Zarezerwowano samochód");
                        }
                    } catch (IOException ex) {
                        widok.dodajKomunikat("Nieudana próba wysłania komunikatu do klienta");
                    }
                } 
                break;
            case "Przeglądaj rezerwacje":
                {
                    // wydzielamy dane
                    String[] dane = informacje.split("/");
                    String login = dane[0];
                    ArrayList<Rezerwacja> rezerwacje = Baza.pobierzInstancję().pobierzRezerwacje(login);
                    try {
                        if (rezerwacje == null) {
                            widok.dodajKomunikat("Nie udało się pobrać rezerwacji");
                        } else {
                            wyślijWiadomość("Udało się pobrać rezerwacje");
                            oos.writeObject(rezerwacje); // wysyłamy obiekt użytkownika przez strumień
                            oos.flush();    
                        }
                    }catch (IOException ex) {
                        widok.dodajKomunikat("Nieudana próba wysłania komunikatu do klienta");
                    }   
                    
                }
                break;
            case "Przeglądaj wszystkie rezerwacje":
                {
                    ArrayList<Rezerwacja> rezerwacje = Baza.pobierzInstancję().pobierzRezerwacje();
                    try {
                        if (rezerwacje == null) {
                            widok.dodajKomunikat("Nie udało się pobrać rezerwacji");
                        } else {
                            wyślijWiadomość("Udało się pobrać rezerwacje");
                            oos.writeObject(rezerwacje); // wysyłamy obiekt użytkownika przez strumień
                            oos.flush();    
                        }
                    }catch (IOException ex) {
                        widok.dodajKomunikat("Nieudana próba wysłania komunikatu do klienta");
                    }   
                    
                }
                break;                               
            case "Anuluj rezerwację":
                {
                    // wydzielamy dane
                    String[] dane = informacje.split("/");
                    String login = dane[0];
                    String marka = dane[1];
                    String model = dane[2];
                    String kolor = dane[3]; 
                    String paliwo = dane[4];
                    int rok = Integer.valueOf(dane[5]);     
                    LocalDate dataOdbioru = LocalDate.parse(dane[6]);
                    LocalDate dataZwrotu = LocalDate.parse(dane[7]);
                    Baza.pobierzInstancję().anulujRezerwację(login, marka, model ,kolor);                
                }
                break;                                
        }
    }    
    
    @Override
    public void run() {
        while (true) {
            try {
                String wiadomość = odbierzWiadomość();
                przetwórzWiadomość(wiadomość);
            } catch (IOException | ClassNotFoundException ex) {
                widok.dodajKomunikat("Wystąpił błąd podczas odbierania wiadomości");
                ex.printStackTrace(System.err);
                break;
            } catch (SQLException ex) {
                widok.dodajKomunikat("Wystąpił błąd podczas przetwarzania wiadomości");
                ex.printStackTrace(System.err);
            }
        }
    }    
}
