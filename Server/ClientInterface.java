import java.net.*;
import java.io.*;


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
            output.writeUTF("\n\nWillkommen auf unserem Immobilienportal!");
            displayMainMenu();
        }catch(EOFException e) {
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
            //displaySignInMenu();
        } else{
            output.writeUTF("\n\nUngültige Eingabe!!");
        }
    }
    
    /**
     * Benutzeroberfläche, um einen neuen Nutzer zu registrieren:
     * Nutzer wird zur Eingabe von Name, E-Mail und Passwort aufgefordert
     * Neuer Nutzer wird erstellt und gespeichert.
     * 
     * @throws IOException bei Fehlern mit der Verbindung.
     */
    public void displaySignUpMenu() throws IOException {
        
        output.writeUTF("\n\nHier können sie einen neuen Nutzer registrieren!");
        
        
        // String name = receiveNameInput();
        // String eMail = receiveEMailInput();
        // String password = receivePasswordInput();
        
        User newUser = new User("Bob", "bob@bob.bob", "Passwort123");
        DataCenter.addNewUser(newUser);
        
    }
    
    /**
     * Rückgabe des Texts des Hauptmenüs.
     * 
     * @return mainMenu : String; das Hauptmenü als String.
     */
    public String getMainMenu() {
        String mainMenu =
            "\n\nDas ist das Hauptmenü des Immobilienportals!" +
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
