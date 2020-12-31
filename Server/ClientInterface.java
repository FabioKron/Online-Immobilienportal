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
    private Socket client;
    private DataInputStream input;
    private DataOutputStream output;
    private SocketAddress clientAddress;

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
    
    public void displayMainMenu() throws IOException {
        while (true){
            output.writeUTF(getMainMenu());
            String userInput = getUserInput();
            
            processMainMenuInput(userInput);
        }
    }
    
    public void processMainMenuInput(String userInput) throws IOException {
        if (userInput.equals("registrieren")){
            // displaySignUpMenu();
        } else if(userInput.equals("anmelden")) {
            //displaySignInMenu();
        } else{
            output.writeUTF("\n\nUngültige Eingabe!!");
        }
    }
    
    public String getMainMenu() {
        String mainMenu =
            "\n\nDas ist das Hauptmenü des Immobilienportals!" +
            "\nWas wollen Sie tun?" + 
            "\nregistrieren: einen neuen Account registrieren" +
            "\nanmelden: sich mit einem Account anmelden" +
            "\nbeenden: Das Programm beenden";
            
        
        return mainMenu;
    }
    
    public String getUserInput() throws IOException {
        output.writeUTF("Benutzereingabe");
        return input.readUTF();
    }
}
