import java.net.*;
import java.io.*;


/**
 * Diese Klasse ist für die Kommunikation mit dem Server der Immobilienplattform zuständig
 *
 * @author Fabio Kron
 * @version 12.2020
 */
public class Main
{
    private static String serverIP = "127.0.0.1";
    private static int serverPort = 110;
    
    public static void main() {
        System.out.println("\n\n");
        System.err.println("\n\n");
        
        System.out.println("Verbidnungsaufbau...");
        try {
            Socket client = new Socket(serverIP, serverPort);
            
            DataInputStream input;
            input = new DataInputStream(client.getInputStream());
            
            DataOutputStream output;
            output = new DataOutputStream(client.getOutputStream());
            
            System.out.println(input.readUTF());
            output.writeUTF("test erhalten");
            
        } catch(UnknownHostException | ConnectException e) {
            System.out.println("Es konnte keine Verbindung zum Server hergestellt werden!");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
