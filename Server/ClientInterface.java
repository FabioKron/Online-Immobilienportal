import java.net.*;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Diese Klasse ist für die Kommunikation mit dem Client verantwortlich
 *
 * @author Fabio Kron
 * @version 12.2020
 */
public class ClientInterface extends Thread
{   
    /** Schnittstelle zur Verbindung mit dem Client. */
    private Socket client;
    
    /** Zum Empfangen von Daten des Clients. */
    private DataInputStream input;
    
    /** Zum Senden von Daten an den Client. */
    private DataOutputStream output;
    
    /** IP-Adresse und Port des Clients. */
    private SocketAddress clientAddress;
    
    
    /**
     * Verbindungsaufbau mit dem Client,
     * Initialisieren der dazu benötigten Schnittstellen.
     * 
     * @param client : Socket 
     */
    public ClientInterface(Socket client) {
        this.client = client;
        try {
            this.input = new DataInputStream(client.getInputStream());
            this.output = new DataOutputStream(client.getOutputStream());
            this.clientAddress = client.getRemoteSocketAddress();
            
            this.output.writeUTF("test");
            
            if (this.input.readUTF().equals("test erhalten")) {
                this.output.writeUTF("test erhalten erhalten");
                System.out.println("Verbindung mit " + clientAddress + " aufgebaut!");
            } else {
                System.out.println("Fehler beim Verbindungsaufbau mit " + clientAddress + "!");
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

    }
    
    
    /**
     * Kommunikation mit dem Client: Benutzerobefläche und Benutzereingaben.
     */
    public void run() {
        try {
            output.writeUTF("\n\n\n\n\n\nWillkommen bei unserem Immobilienportal!");
            displayMainMenu();
        }catch(EOFException | SocketException e) {
            System.out.println("Verbindung zu " + clientAddress + " getrennt!");
        }catch(IOException e){
            e.printStackTrace();
        }
            
    }
    
    /**
     * Senden des Hauptmenüs an den Client;
     * Empfangen und Verarbeiten der Benutzereingabe.
     * 
     * @throws IOException bei Fehlern mit der Verbindung.
     */
    public void displayMainMenu() throws IOException {
        while (true){
            output.writeUTF(getMainMenu());
            String userInput = getUserInput();
            
            processMainMenuInput(userInput);
        }
    }
    
    /**
     * Verarbeiten der Benutzereingabe im Hauptmenü
     * mit möglicher Einleitung des nächsten Programmschritts.
     * 
     * @param userInput : String Benutzereingabe im Hauptmenü.
     * 
     * @throws IOException bei Fehlern mit der Verbindung.
     */
    public void processMainMenuInput(String userInput) throws IOException {
        if (userInput.equals("registrieren")){
            displaySignUpMenu();
        } else if(userInput.equals("anmelden")) {
            displaySignInMenu();
        } else{
            output.writeUTF("Ungültige Eingabe!!");
        }
    }
    
    /**
     * Der Benutzer wird dazu aufgefordert, sich anzumelden;
     * die Eingaben werden überprüft und falls diese stimmen, wird der Benutzer in das
     * Benutzermenü weitergeleitet, wo er weitere Aktionen durchführen kann.
     * 
     * @throws IOException bei Fehlern mit der Verbindung.
     */
    public void displaySignInMenu() throws IOException {
        output.writeUTF("Hier können Sie sich anmelden!");
        
        String eMail = receiveEMailInput();
        String password = receivePasswordInput();
        
        User user = DataCenter.authenticateUser(eMail, password);
        if (user != null) { 
            output.writeUTF("Anmeldung erfolgreich!");
            displayUserMenu(user);
        } else {
            output.writeUTF("Benutzername oder Passwort falsch!");
        }
    }
    
    /**
     * Benutzermenü: 
     * Der Nutzer kann zwischen verschiedenen Aktionen auswählen:
     * - Immobilien hinzufügen
     * - Eigene Immobilien ansehen
     * - Eigene Immobilie entfernen
     * - Alle angebotenen Immobilien suchen
     * - Abmelden.
     * 
     * @param user User - Der angemeldete Nutzer.
     * 
     * @throws IOException bei Fehlern der Kommunikation mit dem Client.
     */
    public void displayUserMenu(User user) throws IOException {
        String userInput;
        do {
            output.writeUTF(getUserMenu(user.getName()));
            userInput = getUserInput();
            
            processUserMenuInput(userInput, user);
        } while (!userInput.equals("abmelden"));
        
        output.writeUTF("Erfolgreich abgemeldet!");
    }
    
    /**
     * Die Benutzereingabe im Benutzermenü wird verarbeitet
     * und die nächste Aktion wird gestartet.
     * 
     * @param userInput String - Nutzereingabe des Benutzermenüs.
     * @param user User - angemeldeter Nutzer, der die Eingabe gemacht hat.
     * 
     * @throws IOException bei Fehlern der Kommunikation mit dem Client.
     */
    public void processUserMenuInput(String userInput, User user) throws IOException {
        switch (userInput) {
            case "hinzufügen": 
                displayAddRealEstateMenu(user);
                break;
            case "ansehen":
                displayUsersRealEstates(user);
                break;
            case "entfernen":
                displayRemoveRealEstateMenu(user);
                break;
            case "suchen":
                displayAllRealEstates();
                break;
            case "abmelden":
                break;
            default:
                output.writeUTF("Ungültige Eingabe!");
        }
    }
    
    /**
     * Die Methode gibt alle Immobilien aus, die veröffentlicht wurden.
     * 
     * @throws IOException bei Fehlern mit der Verbindung mit dem Client.
     */
    public void displayAllRealEstates() throws IOException {
        String[] realEstatesInfo = DataCenter.getRealEstatesInformation();
        if (realEstatesInfo.length > 0) {
            for(String infoString: realEstatesInfo) {
                output.writeUTF("\n" + infoString);
            }
        } else {
            output.writeUTF("Keine Immobilien gefunden!");
        }
    }
    
    /**
     * Der Benutzer bekommt seine Immobilien mit zugewiesenen Nummern angezeigt;
     * dann kann er anhand der Nummer entscheiden, welche Immobilie er löschen will.
     * 
     * @param owner User - Benutzer, der eine Immobilie löschen will.
     * 
     * @throws IOException bei Fehlern mit der Verbindung mit dem Client.
     */
    public void displayRemoveRealEstateMenu(User owner) throws IOException {
        String[] realEstatesInformation = owner.getRealEstatesInformation();
        
        if (realEstatesInformation.length > 0) {
            
            for (int i = 0; i < realEstatesInformation.length; i++) {
                output.writeUTF("\nNummer der nächsten Immobilie: " + (i + 1));
                output.writeUTF(realEstatesInformation[i]);
            }
            int realEstateToRemove = receiveRealEstateNumInput(realEstatesInformation.length);
            owner.removeRealEstate(realEstateToRemove - 1);
            
            output.writeUTF("Die Immobilie wurde entfernt!");
            
        } else {
            output.writeUTF("Keine Immobilien gefunden!");
        }
        
    }
    
    /**
     * Der Benutzer wird dazu aufgefordert die Nummer der Immobilie einzugeben, die er löschen will.
     * 
     * @param maxRealEstateNum int - Höchste Eingabe, die der Nutzer tätigen kann.
     * 
     * @return realEstateNum int - Nummer der ausgewählten Immobilie.
     * 
     * @throws IOException bei Fehlern mit der Verbindung mit dem Client.
     */
    public int receiveRealEstateNumInput(int maxRealEstateNum) throws IOException{
        while (true) {
            output.writeUTF("Was ist die Nummer der Immobilie, die Sie löschen möchten?");
            try {
                int realEstateNum = Integer.parseInt(getUserInput());
                
                if (realEstateNum > 0 && realEstateNum <= maxRealEstateNum) {
                    return realEstateNum;
                }
                    output.writeUTF("Ungültige Eingabe!");   
            } catch(NumberFormatException e) {
                output.writeUTF("Ungültige Eingabe!");
            }
            
        }
    }
    
    /**
     * Die Methode gibt beim Benutzer alle Immobilie aus, die dieser veröffentlicht hat.
     * 
     * @param owner User - Der Benutzert, dessen Immobilien ausgegeben werden sollen.
     * 
     * @throws IOException bei Fehlern mit der Verbindung mit dem Client.
     */
    public void displayUsersRealEstates(User owner) throws IOException{
        String[] realEstatesInformation = owner.getRealEstatesInformation();
        
        if (realEstatesInformation.length > 0) {
            
            for (String infoString: realEstatesInformation) {
                output.writeUTF("\n"+infoString);
            }
            
        } else {
            output.writeUTF("Keine Immobilien gefunden!");
        }
    }
    
    /**
     * Durch diese Methode kann der Benutzer neue Immobilien erstellen und speichern.
     * 
     * @param user User - der angemeldete Benutzer.
     * 
     * @throws IOException bei Fehlern mit der Verbindung mit dem Client.
     */
    public void displayAddRealEstateMenu(User user) throws IOException {
        output.writeUTF("Hier können Sie eine neue Immobilien hinzufügen!");
        
        double price = receivePriceInput();
        String address = receiveAddressInput();
        int numOfRooms = receiveNumOfRoomsInput();
        int floorArea = receiveFloorAreaInput();
        
        RealEstate newRealEstate = new RealEstate(price, address, numOfRooms, floorArea,
        user);
        
       
        user.addRealEstate(newRealEstate);
        DataCenter.addRealEstate(newRealEstate);
    }
    
    /**
     * Der Benutzer wird aufgefordert, die Fläche der Immobilie einzugeben.
     * 
     * @return floorArea int - eingegebene Fläche der Immobilie.
     * 
     * @throws IOException bei Fehlern mit der Verbindung mit dem Client.
     */
    public int receiveFloorAreaInput() throws IOException {
        while (true) {
            output.writeUTF("Welche Fläche hat die Immobilie?");
            try {
                int floorArea = Integer.parseInt(getUserInput());
                
                if (floorArea > 0) {
                    return floorArea;
                }
                output.writeUTF("Ungültige Eingabe!");              
            } catch(NumberFormatException e) {
                output.writeUTF("Ungültige Eingabe!");
            }
        }
    }
    
    /**
     * Der Benutzer wird aufgefordert, die Anzahl der Räume der Immobilie einzugeben.
     * 
     * @return numOfRooms int - eingegebene Anzahl der Räume der Immobilie.
     * 
     * @throws IOException bei Fehlern mit der Verbindung mit dem Client.
     */
    public int receiveNumOfRoomsInput() throws IOException {
        while (true) {
            output.writeUTF("Wie viele Räume hat die Immobilie?");
            try {
                int numOfRooms = Integer.parseInt(getUserInput());
                
                if (numOfRooms > 0) {
                    return numOfRooms;
                }
                output.writeUTF("Ungültige Eingabe!");
                
            } catch(NumberFormatException e) {
                output.writeUTF("Ungültige Eingabe!");
            }
        }
    }
    
    /**
     * Der Benutzer wird dazu aufgefordert, die Adresse seiner Immobilie einzugeben.
     * 
     * @return address String - eingegebene Adresse der Immobilie.
     * 
     * @throws IOException bei Fehlern mit der Verbindung mit dem Client.
     */
    public String receiveAddressInput() throws IOException {
        while (true) {
            output.writeUTF("Was ist die Adresse der Immobilie?");
            String address = getUserInput();
            
            if (address.length() > 0) {
                return address;
            }
            
        }
    }
    
    /**
     * Der Benutzer wird dazu aufgefordert, den Preis seiner Immobilie einzugeben.
     * 
     * @return price double - Eingegebener Preis der Immobilie.
     * 
     * @throws IOException bei Fehlern mit der Verbindung mit dem Client.
     */
    public double receivePriceInput() throws IOException{
        while (true) {
            output.writeUTF("Was ist der Preis Ihrer Immobilie?");
            try {
                double price = Double.parseDouble(getUserInput());
            
                if (price > 0) {
                    return price;
                }
                
                output.writeUTF("Ungültige Eingabe!");
                
            } catch(NumberFormatException e) {
                output.writeUTF("Ungültige Eingabe!");
            }
        }
    }
    
    /**
     * Diese Methode gibt das Nutzermenü zurück.
     * 
     * @param name String - Name des Benutzers.
     * 
     * @return userMenu String - Das Benutzermenü.
     */
    public String getUserMenu(String name) {
        String userMenu = "\nHerzlich Willkommen " + name.toUpperCase() + "!" 
            + "\nWas wollen Sie tun?"
            + "\nhinzufügen: Immobilie hinzufügen"
            + "\nansehen: Eigene Immobilien ansehen"
            + "\nentfernen: Immobilie löschen"
            + "\nsuchen: Alle angebotenen Immobilien suchen"
            + "\nabmelden: Abmelden";
        
        return userMenu;
    }
    
    /**
     * Benutzeroberfläche, um einen neuen Nutzer zu registrieren:
     * Nutzer wird zur Eingabe von Name, E-Mail und Passwort aufgefordert
     * Neuer Nutzer wird erstellt und gespeichert.
     * 
     * @throws IOException bei Fehlern mit der Verbindung.
     */
    public void displaySignUpMenu() throws IOException {
        
        output.writeUTF("Hier können sie einen neuen Nutzer registrieren!");
        
        String name = receiveNameInput();
        String eMail = receiveEMailInput();
        String password = receivePasswordInput();
                
        User newUser = new User(name , eMail, password);
        DataCenter.addNewUser(newUser);
        
    }
    /**
     * In der Methode wird der Nutzer dazu aufgefordert, sein Passort einzugeben;
     * dieses wird dann verschlüsselt zurückgegeben.
     * 
     * @throws IOException bei Fehlern der Kommunikation mit dem Client.
     * 
     * @return password verschlüsseltes Passwort des Benutzers.
     */
    public String receivePasswordInput() throws IOException {
        while (true) {
            output.writeUTF("Bitte geben Sie Ihr Passwort ein:");
            String password = getUserInput();
            
            if (password.length() >= 8) {
                try {
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    
                    byte[] bytePassword = md.digest(password.getBytes());
                    
                    BigInteger bIPassword = new BigInteger(1, bytePassword);
                    
                    password = bIPassword.toString(16);
                    return password;
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
            output.writeUTF("Passwort zu kurz!");
        }
    }
    
    /**
     * Der Benutzer wird aufgefordert, seine E-Mail einzugeben;
     * daraufhin wird die Eingabe validiert.
     * 
     * @throws IOException bei Problemen der Kommunikation mit dem Client.
     * 
     * @return eMail validierte E-Mail des Benutzers.
     */
    public String receiveEMailInput() throws IOException {
        while (true) {
            output.writeUTF("Bitte geben Sie Ihre E-Mail ein:");
            String eMail = getUserInput();
            
            if (eMail.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")) {
                return eMail;
            }
            output.writeUTF("Ungültige E-Mail!");
        }
    }
    
    /**
     * Der Benutzer wird aufgefordert, seinen Namen einzugeben;
     * daraufhin wird der Name validiert.
     * 
     * @throws IOException bei Fehlern der Kommunikation mit dem Client.
     * 
     * @return name validierter Name des Benutzers.
     */
    public String receiveNameInput() throws IOException {
        
        while (true) {
            output.writeUTF("Bitte geben Sie Ihren Namen ein:");
            String name = getUserInput();
            
            if (name.matches("[A-Za-z][A-Za-z -]*")) {
                return name;
            }
            output.writeUTF("Ungültiger Name!");
        }
        
    }
        
        
    
    /**
     * Rückgabe des Texts des Hauptmenüs.
     * 
     * @return mainMenu : String; das Hauptmenü als String.
     */
    public String getMainMenu() {
        String mainMenu =
            "\nDas ist das Hauptmenü des Immobilienportals!" +
            "\nWas wollen Sie tun?" + 
            "\nregistrieren: einen neuen Account registrieren" +
            "\nanmelden: sich mit einem Account anmelden" +
            "\nbeenden: Das Programm beenden";
            
        
        return mainMenu;
    }
    
    /**
     * Fordern und Empfangen einer Benutzereingabe beim Client.
     * 
     * @return userInput : String; Benutzereingabe beim Client.
     * 
     * @throws IOException bei Fehlern mit der Verbindung.
     */
    public String getUserInput() throws IOException {
        output.writeUTF("Benutzereingabe");
        String userInput = input.readUTF();
        return userInput;
    }
}
