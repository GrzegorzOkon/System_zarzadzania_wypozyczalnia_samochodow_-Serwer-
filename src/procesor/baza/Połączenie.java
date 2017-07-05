package procesor.baza;

import gui.Widok;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import procesor.baza.obsługa.Klient;

/**
 *
 * 
 */
public class Połączenie extends Thread {
    private Widok widok;
    
    // gniazdo TCP/IP
    private ServerSocket serverSocket;    

    // stałe statyczne publiczne przechowujące dane serwera
    private final int PORT = 1300;
    
    // lista połączonych klientów
    private final ArrayList<Klient> klienci = new ArrayList<>();

    /**
     * Konstruktor połączenia
     * @param widok obiekt klasy obiekt
     */     
    public Połączenie(Widok widok) throws IOException {
        this.widok = widok;
        serverSocket = new ServerSocket(PORT);       
    }

    /**
     * Metoda umożliwiająca użytkownikowi systemu na wylogowanie się
     * @param klient klient, który wysyła żądanie
     */
    public void wyloguj(Klient klient) {
        klienci.remove(klient);
        widok.dodajKomunikat("Klient zakończył połączenie");
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                // wywołujemy metodę blokującą oczekująca na połączenie ze strony klienta
                Socket socket = serverSocket.accept();
                Klient klient = new Klient(widok, socket, this);
                klienci.add(klient);
                klient.start();
                // stosowny komunikat
                widok.dodajKomunikat("Liczba połączonych klientów: " + klienci.size());
            } catch (IOException ex) {
                widok.dodajKomunikat("Nieudana próba nawiązania połączenia z klientem");
            }
            
        }
    }    
    
}
