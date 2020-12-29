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
                System.out.println("Verbindung mit " + clientAddress + " aufgebaut!");
            } else {
                System.out.println("Fehler beim Verbindungsaufbau mit " + clientAddress + "!");
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

    }
    
    public void run() {
        for (int i = 1; i <= 5; i++) {
            System.out.println(i);
        }
    }
}
