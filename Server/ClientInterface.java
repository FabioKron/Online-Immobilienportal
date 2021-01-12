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
        
        String name = receiveNameInput();
        String eMail = receiveEMailInput();
        String password = receivePasswordInput();
        
        System.out.println(password);
        
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
            output.writeUTF("\nBitte geben Sie Ihr Passwort ein:");
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
     * Der Benutzer wird aufgefordert seine E-Mail einzugeben;
     * daraufhin wird die Eingabe validiert.
     * 
     * @throws IOException bei Problemen der Kommunikation mit dem Client.
     * 
     * @return eMail validierte E-Mail des Benutzers.
     */
    public String receiveEMailInput() throws IOException {
        while (true) {
            output.writeUTF("\nBitte geben Sie Ihre E-Mail ein:");
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
            output.writeUTF("\nBitte geben Sie Ihren Namen ein:");
            String name = getUserInput();
            
            if (name.matches("[a-z][a-z -]*")) {
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
