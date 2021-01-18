import java.net.*;
import java.io.*;
import java.util.Scanner;


/**
 * Diese Klasse ist für die Kommunikation mit dem Server der Immobilienplattform zuständig
 *
 * @author Fabio Kron
 * @version 12.2020
 */
public class Main
{   
    ///** IP-Adresse des Servers. */
    //private static String serverIP = "127.0.0.1";
    
    /** Port des Servers. */
    private static int serverPort = 110;
    
    /**
     * Startet den Client
     * Vor dem Start des Clients muss der Server gestartet sein.
     * 
     * @param serverIP String - IP-Adresse des Servers.
     */
    public static void main(String serverIP) {
        
        System.out.println("\n\n");
        System.err.println("\n\n");
        
        System.out.println("Verbidnungsaufbau...");
        try {
            Socket client = new Socket(serverIP, serverPort);
            
            DataInputStream input;
            input = new DataInputStream(client.getInputStream());
            
            DataOutputStream output;
            output = new DataOutputStream(client.getOutputStream());
            
            Scanner scanner;
            scanner = new Scanner(System.in);
            
            if (input.readUTF().equals("test")) {
                    
                output.writeUTF("test erhalten");
                if (input.readUTF().equals("test erhalten erhalten")){
                    System.out.println("Verbindung zum Server aufgebaut.");
                    
                    listen(input, output, scanner);
                }
            }
            
            client.close();
            System.out.println("\nBis zum nächsten mal!");
        } catch(UnknownHostException | ConnectException e) {
            System.out.println("Es konnte keine Verbindung zum Server hergestellt werden!");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Kommunikation mit dem Server
     * Erhalten von Nachrichten des Servers
     * Anzeigen der Benutzerobefläche
     * Senden von Benutzereingaben.
     * 
     * @param input : DataInputStream zum empfangen von Nachrichten des Servers.
     * @param output : DataOutputStream zum senden von Nachrichten an den Server.
     * @param scanner : Scanner zum lesen von Benutzereingaben.
     * 
     * @throws IOException falls Fehler bei der Kommunikation mit dem Server auftreten.
     */
    public static void listen(DataInputStream input, DataOutputStream output, Scanner scanner) throws IOException {
        while(true) { 
            String serverMessage = input.readUTF();
            if (serverMessage.equals("Benutzereingabe")){
                String userInput = scanner.nextLine();
                if (userInput.toLowerCase().equals("beenden")){
                    return;
                } else {
                    output.writeUTF(userInput);
                }
                System.out.print("\n");

            } else {
                System.out.println(serverMessage);
            }
        }
    }
}
