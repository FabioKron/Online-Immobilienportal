import java.util.ArrayList;
import java.util.LinkedList;


/**
 * Diese Klasse speichert alle Benutzer und alle Immobilien.
 *
 * @author Fabio Kron.
 * @version 1.2021.
 */
public class DataCenter
{   
    /** Speichert Referenzen zu allen Benutzern. */
    private static ArrayList<User> users = new ArrayList<User>();
    
    /** Speichert Referenzen zu allen Immobilien. */
    private static LinkedList<RealEstate> realEstates = new LinkedList<RealEstate>();
    
    
    /**
     * Die Methode speichert neue Benutzer.
     * 
     * @param newUser User: der neu erstellte Benutzer.
     */
    public static void addNewUser(User newUser) {
        users.add(newUser);
    }
}
