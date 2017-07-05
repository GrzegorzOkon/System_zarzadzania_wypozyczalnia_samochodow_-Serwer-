package gui;

import gui.komunikaty.*;
import kontroler.Kontroler;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Klas widoku tj. głównego okna aplikacji
 * 
 */
public class Widok extends JFrame {
    private Kontroler kontroler;
    private JButton przciskAkceptacji;
    private JButton przyciskInicjalizacji;
    private JFrame ramka;
    private JMenu file;
    private JMenu helpMenu;    
    private JMenuBar pasekMenu;
    private JMenuItem quitMenuItem;
    private JMenuItem aboutMenuItem;
    private JPanel głównyPanel;
    private JPanel górnyPanel;
    private JScrollPane panelKonsoli;
    private JTextArea konsola;
                    
    /**
     * Konstruktor widoku
     */ 
    public Widok() {
        ramka = new JFrame("Wypożyczalnia samochodów");
        przciskAkceptacji = new JButton();
        przyciskInicjalizacji = new JButton();
        file = new JMenu();
        helpMenu = new JMenu();
        pasekMenu = new JMenuBar();    
        quitMenuItem = new JMenuItem();
        aboutMenuItem = new JMenuItem();
        głównyPanel = new JPanel();
        górnyPanel = new JPanel();
        panelKonsoli = new JScrollPane();
        konsola = new JTextArea();        

        //Generuje wygląd okienka
        ramka.setUndecorated(true);
        ramka.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        ramka.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);   
        ramka.setResizable(false);
               
        //Utworzenie menu i podmenu
        ramka.setJMenuBar(pasekMenu);
        
        file.setMnemonic('f');
        file.setText("Plik");        
        pasekMenu.add(file);
        
        quitMenuItem.setMnemonic('x');
        quitMenuItem.setText("Zamknij");
        file.add(quitMenuItem);
        
        helpMenu.setMnemonic('h');
        helpMenu.setText("Pomoc");
        pasekMenu.add(helpMenu);
        
        aboutMenuItem.setMnemonic('a');
        aboutMenuItem.setText("O programie...");
        helpMenu.add(aboutMenuItem);        
        
        głównyPanel.setLayout(new java.awt.BorderLayout());
        
        przyciskInicjalizacji.setText("Inicjalizuj");               
        
        głównyPanel.add(przyciskInicjalizacji, java.awt.BorderLayout.PAGE_START);
        
        konsola.setEditable(false);
        konsola.setColumns(20);
        konsola.setRows(5);
        konsola.setWrapStyleWord(true);
        panelKonsoli.setViewportView(konsola);

        głównyPanel.add(panelKonsoli, java.awt.BorderLayout.CENTER);        

        przciskAkceptacji.setText("Uruchom");
        
        głównyPanel.add(przciskAkceptacji, java.awt.BorderLayout.PAGE_END);
        głównyPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        //Fragment odpowiadający za rozmieszczenie elementów grafiznych według menedzera rozkładu BorderLayout
        ramka.getContentPane().setLayout(new BorderLayout());
        ramka.getContentPane().add(głównyPanel, BorderLayout.CENTER);
 
        quitMenuItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                System.exit(0); 
            }
        });
        
        aboutMenuItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                JOptionPane.showMessageDialog(aboutMenuItem, 
                        "Wypożyczalnia samochodów v.1.0\nAutor:\nProjekt na zaliczenie",
                        "O programie\u2026",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });        

        //Przypisanie akcji do elementów menu wywołanych wybraniem elementu
        przyciskInicjalizacji.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                kontroler.wykonajAkcję(new InicjalizujKomunikat());
            }
        });

        //Przypisanie akcji do elementów menu wywołanych wybraniem elementu
        przciskAkceptacji.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                if (przciskAkceptacji.getText().equals("Uruchom")) {
                    przciskAkceptacji.setText("Stop");
                    kontroler.wykonajAkcję(new StartujKomunikat());
                } else {
                    przciskAkceptacji.setText("Uruchom");
                    kontroler.wykonajAkcję(new ZatrzymajKomunikat());
                }
            }
        });
                
        pack();

        //Określenie rozmiaru okenka oraz jego wyświetlenie
        ramka.setSize(300,300);
        ramka.setLocationRelativeTo(null);   //Ustawienie na środku ekranu
        ramka.setVisible(true);
    }    

    /**
     * Metoda wyswietlająca przesłany komunikat
     * @param komunikat treść przesłanego komunikatu
     */    
    public void dodajKomunikat(String komunikat) {
        System.out.println(komunikat); // na konsolę systemową
        konsola.append(komunikat + "\n"); // do interfejsu graficznego
    }
    
    /**
     * Metoda przypisująca referencję do obiektu kontrolera
     * @param kontroler obiekt do którego ma być referencja
     */ 
    public void setReference(Kontroler kontroler){
        this.kontroler = kontroler;
    }    
}
