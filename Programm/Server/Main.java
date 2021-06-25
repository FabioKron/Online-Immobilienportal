import java.net.*;
import java.io.*;

/**
 * Diese Klasse startet den Server und verbindet neue Clients mit der Anwendung.
 */
public class Main
{   
    /** Port des Servers, mit dem die Clients kommunizieren können. */
    private static int port = 110;
    
    /**
     * Startet den Server.
     */
    public static void main(String[] args) {
        System.out.println("\n\n\n");
        System.err.println("\n\n\n");
        
        try {
            runServer();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Startet den Server auf dem angegebenen Port.
     * 
     * @throws IOException bei Problemen mit dem Server.
     */
    private static void runServer() throws IOException{
        ServerSocket server = new ServerSocket(port);
        System.out.println("Server online");
        
        connectClients(server);
    }
    
    
    /**
     * Verbindet neue Clients mit dem Server.
     * 
     * @param server ServerSocket.
     * @throws IOException bei Fehlern des Verbindungsaufbaus mit Clients.
     */
    private static void connectClients(ServerSocket server) throws IOException {
        while (true) {
            System.out.println("Warten auf Client auf Port " + port );
            Socket client = server.accept();
            
            ClientInterface newClientInterface = new ClientInterface(client);
            newClientInterface.start();
        }
    }
}
